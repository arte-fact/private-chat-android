package fr.artefact.private_chat.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Activities.LoginActivity;
import fr.artefact.private_chat.Activities.MessageActivity;
import fr.artefact.private_chat.Adapters.ConversationAdapter;
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
                        "AuYzVbvVAbrRSZnTFUAzz5ZEZPmP7xEGivD9UrsS",
                        "*",
                        "password"
                );

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                try {
                    String token = response.body().getAccessToken();
                    db.authResponseDao().insert(response.body());
                    Log.d("auth:", response.body().getAccessToken());
                } catch (Exception e) {
                    Toast.makeText(context, "Echec de l'authentification... :'(", Toast.LENGTH_SHORT).show();
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

    public static void fetchConversations (String token, final Context context, final ConversationAdapter mAdapter) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        final Call<List<Conversation>> messagesCall =
                HttpClientHolder.getClient().getConversations("Bearer " + token);

        messagesCall.enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Conversation>> call,@NonNull Response<List<Conversation>> response) {
                if(mAdapter == null) {
                    db.conversationDao().insertAll(response.body());
                } else {
                    mAdapter.addItems(response.body());
                }
                Toast.makeText(context, "MAJ des conversations :)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void updateConversations (String token, final Context context, final ConversationAdapter conversationAdapter) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<Conversation>> messagesCall =
                HttpClientHolder.getClient().getConversations("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        messagesCall.enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Conversation>> call,@NonNull Response<List<Conversation>> response) {
                conversationAdapter.addItems(db.conversationDao().getAll());
                Toast.makeText(context, "MAJ des conversations :)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void fetchMessages (final Context context, final MessageAdapter messageAdapter) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<Message>> messagesCall =
                HttpClientHolder.getClient().getMessages("Bearer " + db.authResponseDao().getAll().getAccessToken());

        // Execute the call asynchronously. Get a positive or negative callback.
        messagesCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call,@NonNull Response<List<Message>> response) {
                try {
                    messageAdapter.addItems(response.body());
                } catch (Exception e) {
                    //
                }
                Toast.makeText(context, "MAJ des messages :)", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMessage (String token, final Context context, Message message, final MessageAdapter messageAdapter) {
        final AppDatabase db = AppDatabase.getAppDatabase(context);


        final Call<Message> messageCall =
                HttpClientHolder.getClient().postMessage("Bearer " + token, message);

        // Execute the call asynchronously. Get a positive or negative callback.
        messageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call,@NonNull Response<Message> response) {
                try {
//                    messageAdapter.addItem(response.body());
//                    messageAdapter.mRecyclerView.scrollToPosition(messageAdapter.getItemCount() -1);
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