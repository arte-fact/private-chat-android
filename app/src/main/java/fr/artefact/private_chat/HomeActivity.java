package fr.artefact.private_chat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        java.util.List<User> contacts = AppDatabase.getAppDatabase(getApplicationContext()).userDao().getAll();
        RecyclerView homeRecyclerView = findViewById(R.id.home_recycler_view);
        homeRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new UserAdapter(contacts);
        mRecyclerView.setAdapter(mAdapter);

        }
    }

