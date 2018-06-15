package fr.artefact.private_chat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import fr.artefact.private_chat.Utilities.AppDatabase;
import fr.artefact.private_chat.Utilities.DataRequests;
import fr.artefact.private_chat.Models.Message;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (AppDatabase.getAppDatabase(getApplicationContext()).authResponseDao().getAll() == null){
            Intent login = new Intent(this, LoginActivity.class);
            super.onCreate(savedInstanceState);
            startActivity(login);
        } else {
            String token = AppDatabase.getAppDatabase(getApplicationContext()).authResponseDao().getAll().getAccessToken();

            DataRequests.fetchUsers(token, getApplicationContext());
            DataRequests.fetchConversations(token, getApplicationContext());

            Intent home = new Intent(this, HomeActivity.class);
            super.onCreate(savedInstanceState);
            startActivity(home);
        }
    }
}
