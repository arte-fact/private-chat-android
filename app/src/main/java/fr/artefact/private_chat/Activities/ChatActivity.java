package fr.artefact.private_chat.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.List;

import fr.artefact.private_chat.Adapters.MessageAdapter;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Models.ModelContainers.MessageContainer;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;
import fr.artefact.private_chat.Utilities.PusherClient;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int conversationId;
    private PusherClient pusherClient;
    private AppDatabase db;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle options = getIntent().getExtras();
        setContentView(R.layout.activity_message);
        conversationId = options.getInt("conversation_id");

        db = AppDatabase.getAppDatabase(getBaseContext());
        final String token =db.authResponseDao().getAll().getAccessToken();

        final List<Message> messages = db.messageDao().getConversationMessages(conversationId);

        setRecyclerView(messages);
        setButton(token);
        setKeyboardObserver();
        DataRequests.fetchMessages(db.authResponseDao().getAll().getAccessToken(), getApplicationContext());
        subscribeChannel(conversationId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribeChannel(conversationId);
    }

    private void setKeyboardObserver() {
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }

    private void setRecyclerView(List<Message> messages) {
        mRecyclerView = findViewById(R.id.message_recycler_view);

        mLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);
        pusherClient = new PusherClient();
        mAdapter.notifyDataSetChanged();
        mAdapter.mRecyclerView.scrollToPosition(mAdapter.getItemCount() -1);
    }

    private void setButton(final String token) {
        Button button = findViewById(R.id.button_chatbox_send);

        button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edittext_chatbox);
                String text = editText.getText().toString();

                Message message = new Message();
                message.setText(text);
                message.setConversationId(conversationId);

                DataRequests.sendMessage(token, ChatActivity.this, message);

                editText.setText("");
            }
        });
    }

    private void subscribeChannel(int conversationId) {
        Channel channel = pusherClient.getPusher().subscribe("conversation-channel." + conversationId);

        channel.bind("App\\Events\\MessageCreatedEvent", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {

                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MessageContainer messageContainer = gson.fromJson(data, MessageContainer.class);
                        Message message = messageContainer.getMessage();
                        mAdapter.addItem(message);
                    }
                });
            }
        });
    }

    private void unSubscribeChannel(int conversationId) {
        pusherClient.getPusher().unsubscribe("conversation-channel." + conversationId);
    }
}

