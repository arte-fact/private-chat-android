package fr.artefact.private_chat.Activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Adapters.MessageAdapter;
import fr.artefact.private_chat.Utilities.DataRequests;

public class MessageActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final String token = AppDatabase.getAppDatabase(getApplicationContext())
                .authResponseDao().getAll().getAccessToken();

        Bundle options = getIntent().getExtras();
        final int conversationId = options.getInt("conversation_id");

        final List<Message> messages =
                AppDatabase.getAppDatabase(getApplicationContext()).messageDao()
                        .getConversationMessages(conversationId);

        mRecyclerView = findViewById(R.id.message_recycler_view);

        mLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);

        Button button = findViewById(R.id.button_chatbox_send);

        button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edittext_chatbox);
                String text = editText.getText().toString();

                Message message = new Message();
                message.setMessage(text);
                message.setConversationId(conversationId);

                DataRequests.sendMessage(token, MessageActivity.this, message, mAdapter);
            }
        });
    }

}
