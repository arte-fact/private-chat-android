package fr.artefact.private_chat.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Activities.MainActivity;
import fr.artefact.private_chat.Adapters.FriendshipAdapter;
import fr.artefact.private_chat.Models.Friendship;
import fr.artefact.private_chat.R;
import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;
import fr.artefact.private_chat.Utilities.RecyclerItemOnClickListener;

public class ContactsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public FriendshipAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Friendship> friendships;
    private AppDatabase db;
    private TextInputEditText numberInput;
    private TextInputEditText nameInput;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contacts, container, false);
        mRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        numberInput = view.findViewById(R.id.add_friend_number_input);
        nameInput = view.findViewById(R.id.add_friend_name_input);
        db = AppDatabase.getAppDatabase(view.getContext());
        try {
            friendships = db.friendshipDao().getAll();
        } catch (Exception e) {
            friendships = null;
        }
        setRecyclerView(friendships);
        Button button = view.findViewById(R.id.button_add_friend_send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = numberInput.getText().toString();
                String name = nameInput.getText().toString();

                DataRequests.createFriendship(db.authResponseDao().getAll().getAccessToken(),
                        getContext(), number, name, (MainActivity) getActivity());
            }
        });
        return view;
    }


    private void setRecyclerView(List<Friendship> friendships) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FriendshipAdapter(friendships);
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
                                int id = db.friendshipDao().getAll().get(position).getId();
                                DataRequests.createConversation(token, context, id, (MainActivity) getContext());
                            }

                            @Override public void onLongItemClick(View view, int position) {

                            }
                        })
        );
    }
}

