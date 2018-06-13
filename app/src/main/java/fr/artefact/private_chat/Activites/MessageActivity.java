package fr.artefact.private_chat.Activites;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

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

        Bundle options = getIntent().getExtras();

        final List<Message> messages =
                AppDatabase.getAppDatabase(getApplicationContext()).messageDao().getConversationMessages(options.getInt("conversation_id"));

        mRecyclerView = new RecyclerView(MessageActivity.this);

        mLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);

        ConstraintLayout layout = new ConstraintLayout(getBaseContext());
        layout.addView(mRecyclerView);

        setContentView(layout);
    }
}

