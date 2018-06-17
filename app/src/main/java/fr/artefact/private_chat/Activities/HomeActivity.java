package fr.artefact.private_chat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.List;

import fr.artefact.private_chat.Adapters.ConversationAdapter;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;
import fr.artefact.private_chat.Utilities.PusherClient;
import fr.artefact.private_chat.Utilities.RecyclerItemOnClickListener;

public class HomeActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ConversationAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    PusherClient pusherClient;
    List<Conversation> conversations;
    AppDatabase db;
    String token;


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getAppDatabase(HomeActivity.this);
        pusherClient = new PusherClient();
        try {
            token = db.authResponseDao().getAll().getAccessToken();
        } catch (Exception e) {
            token = null;
            Intent loginActivity = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginActivity);
        }

        try {
            conversations = db.conversationDao().getAll();
        } catch (Exception e) {
            conversations = null;
        }
        setRecyclerView(conversations);

        if (token != null) {
            DataRequests.fetchConversations(
                    token,
                    getApplicationContext(),
                    mAdapter
            );
            DataRequests.fetchMessages(getApplicationContext());
        }

        for (Conversation conversation: conversations) {
            subscribeChannel(conversation.getId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Conversation conversation: conversations) {
            unSubscribeChannel(conversation.getId());
        }
    }

    private void setRecyclerView(List<Conversation> conversations) {
        mRecyclerView = new RecyclerView(HomeActivity.this);

        mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ConversationAdapter(conversations);
        mRecyclerView.setAdapter(mAdapter);

        ConstraintLayout layout = new ConstraintLayout(getBaseContext());

        layout.addView(mRecyclerView);
        setClickListener();
        setContentView(layout);
    }

    private void setClickListener() {
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemOnClickListener(
                        HomeActivity.this,
                        mRecyclerView,
                        new RecyclerItemOnClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                Intent message = new Intent(HomeActivity.this, MessageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("conversation_id", db.conversationDao().getAll().get(position).getId());
                                message.putExtras(bundle);
                                startActivity(message, bundle);
                            }

                            @Override public void onLongItemClick(View view, int position) {
                                Toast.makeText(HomeActivity.this, "click long", Toast.LENGTH_LONG).show();
                            }
                        })
        );
    }

    private void subscribeChannel(int conversationId) {
        Channel channel = pusherClient.getPusher().subscribe("conversation-channel." + conversationId);

        channel.bind("App\\Events\\MessageCreatedEvent", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                HomeActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(HomeActivity.this,
                                "Nouveau message! :)", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void unSubscribeChannel(int conversationId) {
        pusherClient.getPusher().unsubscribe("conversation-channel." + conversationId);
    }
}

