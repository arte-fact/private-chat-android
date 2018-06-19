package fr.artefact.private_chat.Utilities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import fr.artefact.private_chat.Activities.MainActivity;
import fr.artefact.private_chat.Models.AuthResponse;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Models.Settings;
import fr.artefact.private_chat.Models.User;
import fr.artefact.private_chat.Utilities.HttpClient.HttpClientHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: 16/06/18 trop répétitif, factoriser les methodes

public class DataRequests {

    public static void fetchAuthResponse (final Context context) {
        final AppDatabase db = AppDatabase.getAppDatabase(context);

        final Settings settings = db.settingsDao().getWithId(1);

        Call<AuthResponse> call =
                HttpClientHolder.getClient().getAccessToken(
                        settings.getEmail(),
                        settings.getPassword(),
                        settings.getClientId(),
                        settings.getClientSecret(),
                        "*",
                        "password"
                );

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                try {
                    db.authResponseDao().insert(response.body());
                    Toast.makeText(context, "Authentification:" + response.isSuccessful(), Toast.LENGTH_SHORT).show();
                    fetchAll(db.authResponseDao().getAll().getAccessToken(), context);
                } catch (Exception e) {
                    Toast.makeText(context, "Echec de l'authentification... :'(", Toast.LENGTH_SHORT).show();
//                    Intent login = new Intent(context, SettingsFragment.class);
//                    context.startActivity(login);
                }
//                Intent home = new Intent(context, HomeFragment.class);
//                context.startActivity(home);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void fetchAll (String token, final Context context) {

        fetchUsers(token, context);
        fetchConversations(token, context);
        fetchMessages(token, context);
    }

    private static void fetchUsers (String token, final Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        final Call<List<User>> usersCall =
                HttpClientHolder.getClient().getUsers("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call,@NonNull Response<List<User>> response) {
                db.userDao().insertAll(response.body());
                Toast.makeText(context, "MAJ des utilisateurs :)", Toast.LENGTH_SHORT).show();
                try {
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.contactsFragment.mAdapter.addUsers(response.body());
                    mainActivity.contactsFragment.mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call,@NonNull Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void fetchConversations (String token, final Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        final Call<List<Conversation>> messagesCall =
                HttpClientHolder.getClient().getConversations("Bearer " + token);

        messagesCall.enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Conversation>> call,
                    @NonNull Response<List<Conversation>> response
            ) {
                try {
                    db.conversationDao().insertAll(response.body());
                    try {
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.homeFragment.mAdapter.addItems(response.body());
                        mainActivity.homeFragment.mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        //
                    }
                    Toast.makeText(context, "MAJ des conversations :)", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(context, "Erreur serveur :'(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Conversation>> call,  @NonNull Throwable t) {
                Toast.makeText(
                        context,
                        "Pas de réponse du serveur... :'(",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private static void fetchMessages (String token, final Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        final Call<List<Message>> messagesCall =
                HttpClientHolder.getClient().getMessages("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        messagesCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Message>> call,
                    @NonNull Response<List<Message>> response) {
                try {
                    db.messageDao().insertAll(response.body());
                    Toast.makeText(context, "MAJ des messages :)", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, "Erreur serveur :'(", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMessage (String token, final Context context, Message message) {
        final Call<Message> messageCall =
                HttpClientHolder.getClient().postMessage("Bearer " + token, message);

        messageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call,@NonNull Response<Message> response) {
                //
            }
            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Toast.makeText(
                        context,
                        "Pas de réponse du serveur... :'(",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}