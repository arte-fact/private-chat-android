package fr.artefact.private_chat.Activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Adapters.MessageAdapter;

public class MessageActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle options = getIntent().getExtras();

        final List<Message> messages =
                AppDatabase.getAppDatabase(getApplicationContext()).messageDao()
                        .getConversationMessages(options.getInt("conversation_id"));

        mRecyclerView = findViewById(R.id.message_recycler_view);

        mLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);
    }
}

