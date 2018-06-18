package fr.artefact.private_chat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeActivity extends Fragment {

    RecyclerView mRecyclerView;
    ConversationAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    PusherClient pusherClient;
    List<Conversation> conversations;
    AppDatabase db;
    String token;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        return view;
    }


    protected void subscribeChannels() {

        db = AppDatabase.getAppDatabase(this.getContext());
        pusherClient = new PusherClient();
        try {
            token = db.authResponseDao().getAll().getAccessToken();
        } catch (Exception e) {
            token = null;
            return;
        }

        try {
            conversations = db.conversationDao().getAll();
        } catch (Exception e) {
            conversations = null;
            return;
        }
        setRecyclerView(conversations);

        DataRequests.fetchConversations(
                token,
                this.getContext(),
                mAdapter
        );
        DataRequests.fetchMessages(this.getContext());

        for (Conversation conversation: conversations) {
            subscribeChannel(conversation.getId());
        }
    }

    void unSubscribeChannels() {
        for (Conversation conversation: conversations) {
            unSubscribeChannel(conversation.getId());
        }
    }

    private void setRecyclerView(List<Conversation> conversations) {
        mRecyclerView = getActivity().findViewById(R.id.home_recycler_view);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ConversationAdapter(conversations);
        mRecyclerView.setAdapter(mAdapter);

        setClickListener();
    }

    private void setClickListener() {
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemOnClickListener(
                        getContext(),
                        mRecyclerView,
                        new RecyclerItemOnClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                Intent message = new Intent(view.getContext(), MessageActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("conversation_id", db.conversationDao().getAll().get(position).getId());
                                message.putExtras(bundle);
                                startActivity(message, bundle);
                            }

                            @Override public void onLongItemClick(View view, int position) {
                                Toast.makeText(view.getContext(), "click long", Toast.LENGTH_LONG).show();
                            }
                        })
        );
    }

    private void subscribeChannel(int conversationId) {
        Channel channel = pusherClient.getPusher().subscribe("conversation-channel." + conversationId);

        channel.bind("App\\Events\\MessageCreatedEvent", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(),
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

