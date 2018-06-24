package fr.artefact.private_chat.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Activities.MainActivity;
import fr.artefact.private_chat.Adapters.UserAdapter;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Models.User;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;
import fr.artefact.private_chat.Utilities.RecyclerItemOnClickListener;

public class ContactsFragment extends Fragment {

    RecyclerView mRecyclerView;
    public UserAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    List<User> users;
    AppDatabase db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contacts, container, false);
        mRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        db = AppDatabase.getAppDatabase(view.getContext());
        try {
            users = db.userDao().getAll();
        } catch (Exception e) {
            users = null;
        }
        setRecyclerView(users);
        return view;
    }

    private void setRecyclerView(List<User> users) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserAdapter(users);
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
                                Toast.makeText(view.getContext(), "click court", Toast.LENGTH_LONG).show();
                                String token = db.authResponseDao().getAll().getAccessToken();
                                Context context = view.getContext();
                                int id = db.userDao().getAll().get(position).getId();
                                DataRequests.createConversation(token, context, id, (MainActivity) getContext());

                            }

                            @Override public void onLongItemClick(View view, int position) {
                                Toast.makeText(view.getContext(), "click long", Toast.LENGTH_LONG).show();
                            }
                        })
        );
    }
}

