package fr.artefact.private_chat.Fragments;

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

import java.util.List;

import fr.artefact.private_chat.Activities.ChatActivity;
import fr.artefact.private_chat.Adapters.ConversationAdapter;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.RecyclerItemOnClickListener;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public ConversationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Conversation> conversations;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        db = AppDatabase.getAppDatabase(getContext());
        conversations = db.conversationDao().getAll();
        mRecyclerView = view.findViewById(R.id.home_recycler_view);
        setRecyclerView(conversations);
        return view;
    }

    private void setRecyclerView(List<Conversation> conversations) {

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
                                Intent message = new Intent(view.getContext(), ChatActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("conversation_id", db.conversationDao().getAll().get(position).getId());
                                message.putExtras(bundle);
                                getContext().startActivity(message, bundle);
                            }

                            @Override public void onLongItemClick(View view, int position) {
                                //
                            }
                        })
        );
    }
}

