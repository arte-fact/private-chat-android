package fr.artefact.private_chat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Adapters.ConversationAdapter;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.RecyclerItemOnClickListener;

public class HomeActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ConversationAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AppDatabase db = AppDatabase.getAppDatabase(HomeActivity.this);

        final List<Conversation> conversations = db.conversationDao().getAll();

//        final String token = db.authResponseDao().getAll().getAccessToken();

        mRecyclerView = new RecyclerView(HomeActivity.this);

        mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ConversationAdapter(conversations);
        mRecyclerView.setAdapter(mAdapter);

//        DataRequests.updateConversations(token, getApplicationContext(), mAdapter);

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

