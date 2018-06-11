package fr.artefact.private_chat;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import fr.artefact.private_chat.User.User;
import fr.artefact.private_chat.User.UserAdapter;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final java.util.List<User> contacts = AppDatabase.getAppDatabase(getApplicationContext()).userDao().getAll();

        mRecyclerView = new RecyclerView(HomeActivity.this);

        mLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new UserAdapter(contacts);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.getAdapter().notifyDataSetChanged();

        ConstraintLayout layout = new ConstraintLayout(getBaseContext());
        layout.addView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemOnClickListener(
                        HomeActivity.this,
                        mRecyclerView,
                        new RecyclerItemOnClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                String message = contacts.get(position).getEmail();
                                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
                            }

                            @Override public void onLongItemClick(View view, int position) {
                                Toast.makeText(HomeActivity.this, "click long", Toast.LENGTH_LONG).show();
                            }
                        })
        );

        setContentView(layout);
    }
}

