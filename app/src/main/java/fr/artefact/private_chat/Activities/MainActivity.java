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
        super.onCreate(savedInstanceState);
        try {
            String token = AppDatabase.getAppDatabase(getApplicationContext()).authResponseDao().getAll().getAccessToken();
            DataRequests.fetchUsers(token, getApplicationContext());
            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);

        }catch (Exception e) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
    }
}

// TODO: 17/06/18 Organiser systeme de login
// TODO: 17/06/18 deconnection, register 
// TODO: 17/06/18 activité parametre de connection telecharge les clés d'api en attaquant une route url mdp 
// TODO: 17/06/18 barre d'action en bas de l'appli 
// TODO: 17/06/18 Sauvegarder données utilisateur
// TODO: 17/06/18 Creer activité utilisateur pour pouvoir creer des conversation et des amitiés
// TODO: 17/06/18 styliser les vues de message 
// TODO: 17/06/18 mode consultation hors connection 
// TODO: 17/06/18 encryptage des messages avec clé partagée entre utilisateurs seulement  
