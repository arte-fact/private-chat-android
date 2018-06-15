package fr.artefact.private_chat.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
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
import fr.artefact.private_chat.R;
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



    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AppDatabase db = AppDatabase.getAppDatabase(HomeActivity.this);


        pusherClient = new PusherClient();

        conversations = db.conversationDao().getAll();
        setRecyclerView(db, conversations);

        DataRequests.fetchConversations(
                db.authResponseDao().getAll().getAccessToken(),
                HomeActivity.this,
                mAdapter
        );

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

    private void setRecyclerView(AppDatabase db, List<Conversation> conversations) {
        mRecyclerView = new RecyclerView(HomeActivity.this);

        mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ConversationAdapter(conversations);
        mRecyclerView.setAdapter(mAdapter);

        ConstraintLayout layout = new ConstraintLayout(getBaseContext());

        layout.addView(mRecyclerView);
        setClickListener(db);
        setContentView(layout);
    }

    private void setClickListener(final AppDatabase db) {
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
                        Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_SHORT).show();

                    }
                });

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(HomeActivity.this)
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setContentTitle("Private Chat")
                                .setContentText("Nouveau message!");


                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // When you issue multiple notifications about the same type of event,
                // it’s best practice for your app to try to update an existing notification
                // with this new information, rather than immediately creating a new notification.
                // If you want to update this notification at a later date, you need to assign it an ID.
                // You can then use this ID whenever you issue a subsequent notification.
                // If the previous notification is still visible, the system will update this existing notification,
                // rather than create a new one. In this example, the notification’s ID is 001//

                mNotificationManager.notify(001, mBuilder.build());
            }
        });
    }

    private void unSubscribeChannel(int conversationId) {
        pusherClient.getPusher().unsubscribe("conversation-channel." + conversationId);
    }
}

