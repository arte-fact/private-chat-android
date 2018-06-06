package fr.artefact.private_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent login = new Intent(this, LoginActivity.class);
        super.onCreate(savedInstanceState);
        startActivity(login);
    }

    public Context getActualApplicationContext () {
        return this.getApplicationContext();
    }
}
