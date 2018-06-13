package fr.artefact.private_chat.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Conversation.Conversation;
import fr.artefact.private_chat.Conversation.ConversationAdapter;
import fr.artefact.private_chat.Core.AppDatabase;

public class HomeActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<Conversation> conversations =
                AppDatabase.getAppDatabase(getApplicationContext()).conversationDao().getAll();

        mRecyclerView = new RecyclerView(HomeActivity.this);

        mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ConversationAdapter(conversations);
        mRecyclerView.setAdapter(mAdapter);

        ConstraintLayout layout = new ConstraintLayout(getBaseContext());
        layout.addView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemOnClickListener(
                        HomeActivity.this,
                        mRecyclerView,
                        new RecyclerItemOnClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                Intent message = new Intent(HomeActivity.this, MessageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("conversation_id", position);
                                message.putExtras(bundle);
                                startActivity(message, bundle);
                            }

                            @Override public void onLongItemClick(View view, int position) {
                                Toast.makeText(HomeActivity.this, "click long", Toast.LENGTH_LONG).show();
                            }
                        })
        );
        setContentView(layout);
    }
}

