package fr.artefact.private_chat.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.List;

import fr.artefact.private_chat.Adapters.MessageAdapter;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Models.MessageContainer;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;
import fr.artefact.private_chat.Utilities.PusherClient;

public class MessageActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MessageAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    int conversationId;
    PusherClient pusherClient;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final String token = AppDatabase.getAppDatabase(getApplicationContext())
                .authResponseDao().getAll().getAccessToken();

        Bundle options = getIntent().getExtras();
        conversationId = options.getInt("conversation_id") + 1;

        final List<Message> messages =
                AppDatabase.getAppDatabase(getApplicationContext()).messageDao()
                        .getConversationMessages(conversationId);

        mRecyclerView = findViewById(R.id.message_recycler_view);

        mLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);
        pusherClient = new PusherClient( mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.mRecyclerView.scrollToPosition(mAdapter.getItemCount() -1);

        DataRequests.fetchMessages(getApplicationContext(), mAdapter);


        subscribeChannel(conversationId);


        Button button = findViewById(R.id.button_chatbox_send);

        button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edittext_chatbox);
                String text = editText.getText().toString();

                Message message = new Message();
                message.setText(text);
                message.setConversationId(conversationId);

                DataRequests.sendMessage(token, MessageActivity.this, message, mAdapter);
            }
        });
    }

    private void subscribeChannel(int conversationId) {
        Channel channel = pusherClient.getPusher().subscribe("conversation-channel." + conversationId);

        channel.bind("App\\Events\\MessageCreatedEvent", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {

                MessageActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MessageContainer messageContainer = gson.fromJson(data, MessageContainer.class);
                        Message message = messageContainer.getMessage();
                        Log.d("data",data);
                        mAdapter.addItem(message);
                        Toast.makeText(MessageActivity.this, "message", Toast.LENGTH_SHORT).show();
                        mAdapter.mRecyclerView.scrollToPosition(mAdapter.getItemCount());
                    }
                });
            }
        });
    }

    private void unSubscribeChannel(int conversationId) {
        pusherClient.getPusher().unsubscribe("conversation-channel." + conversationId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribeChannel(conversationId);
        Log.d("MessageActivity:","destroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MessageActivity:","pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MessageActivity:","resume");
    }
}

