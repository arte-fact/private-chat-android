package fr.artefact.private_chat.UI;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Core.AppDatabase;
import fr.artefact.private_chat.Message.Message;
import fr.artefact.private_chat.Message.MessageAdapter;

public class MessageActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<Message> messages =
                AppDatabase.getAppDatabase(getApplicationContext()).messageDao().getAll();

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

