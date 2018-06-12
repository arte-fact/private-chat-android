package fr.artefact.private_chat.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.artefact.private_chat.Auth.AuthResponse;
import fr.artefact.private_chat.Core.AppDatabase;
import fr.artefact.private_chat.Core.DataRequests;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final AuthResponse authResponse =
                AppDatabase.getAppDatabase(getApplicationContext()).authResponseDao().getAll();

        DataRequests.fetchUsers(authResponse.getAccessToken(), getApplicationContext());
        DataRequests.fetchConversations(authResponse.getAccessToken(), getApplicationContext());

        Intent home = new Intent(this, HomeActivity.class);
        super.onCreate(savedInstanceState);
        startActivity(home);
    }
}
