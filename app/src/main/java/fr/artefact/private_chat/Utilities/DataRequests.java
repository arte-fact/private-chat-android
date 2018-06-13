package fr.artefact.private_chat.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Activities.LoginActivity;
import fr.artefact.private_chat.Activities.MessageActivity;
import fr.artefact.private_chat.Adapters.MessageAdapter;
import fr.artefact.private_chat.Models.AuthResponse;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Models.User;
import fr.artefact.private_chat.Utilities.HttpClient.HttpClientHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRequests {
    public static boolean fetchAuthResponse (String mEmail, String mPassword, final Context context) {
        final AppDatabase db = AppDatabase.getAppDatabase(context);

        Call<AuthResponse> call =
                HttpClientHolder.getClient().getAccessToken(
                        mEmail,
                        mPassword,
                        "2",
                        "aoVIjU2Y8Q5KMNVy0ikGdaCbt1TJWwlfSQTwfwy0",
                        "*",
                        "password"
                );

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call,@NonNull Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        db.authResponseDao().insert(response.body());
                    } catch (Exception e) {
                        Toast.makeText(context, "Echec de l'authentification... :'(", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(context, LoginActivity.class);
                        context.startActivity(login);
                    }
                } else {
                    Intent login = new Intent(context, LoginActivity.class);
                    context.startActivity(login);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    public static void fetchUsers (String token, final Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<User>> usersCall =
                HttpClientHolder.getClient().getUsers("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                db.userDao().insertAll(response.body());
                Toast.makeText(context, "MAJ des utilisateurs :)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void fetchConversations (String token, final Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<Conversation>> messagesCall =
                HttpClientHolder.getClient().getConversations("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        messagesCall.enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Conversation>> call,@NonNull Response<List<Conversation>> response) {
                db.conversationDao().insertAll(response.body());
                Toast.makeText(context, "MAJ des conversations :)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void fetchMessages (String token, final Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<Message>> messagesCall =
                HttpClientHolder.getClient().getMessages("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        messagesCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call,@NonNull Response<List<Message>> response) {
                db.messageDao().insertAll(response.body());
                Toast.makeText(context, "MAJ des messages :)", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMessage (String token, final Context context, Message message, final RecyclerView.Adapter messageAdapter) {
        final AppDatabase db = AppDatabase.getAppDatabase(context);


        final Call<Message> messageCall =
                HttpClientHolder.getClient().postMessage("Bearer " + token, message);

        // Execute the call asynchronously. Get a positive or negative callback.
        messageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call,@NonNull Response<Message> response) {
                try {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    db.messageDao().insert(response.body());
                    messageAdapter.notifyItemInserted(messageAdapter.getItemCount() + 1);
                } catch (Exception e) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}