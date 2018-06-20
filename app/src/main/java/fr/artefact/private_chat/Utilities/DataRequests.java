package fr.artefact.private_chat.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.artefact.private_chat.Activities.MainActivity;
import fr.artefact.private_chat.Models.AuthResponse;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Models.Settings;
import fr.artefact.private_chat.Models.User;
import fr.artefact.private_chat.Models.UserContainer;
import fr.artefact.private_chat.Utilities.HttpClient.HttpClientHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: 16/06/18 trop répétitif, factoriser les methodes

public class DataRequests {

    public static void getUserNumber (final Context context, String android_id) {
        final AppDatabase db = AppDatabase.getAppDatabase(context);

        final Settings settings = db.settingsDao().getWithId(1);

        Call<UserContainer> call =
                HttpClientHolder.getClient().getUserNumber(
                        android_id + "@yyy.zzz",
                        "secret",
                        "4",
                        "6KdGPLinH0kVzfPofh15iYSSFiUyPsw793pF0gSK",
                        "*",
                        "password"
                );

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<UserContainer>() {
            @Override
            public void onResponse(@NonNull Call<UserContainer> call, @NonNull Response<UserContainer> response) {
                try {
                    List<User> users = new ArrayList<>();
                    Log.d("user", response.body().getUser().toString());
                    User user = response.body().getUser();
                    users.add(user);
                    db.userDao().insert(user);
                    Toast.makeText(context, "GetUserNumber:" + response.isSuccessful(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(
                            context,
                            "Echec de récuperation du numero d'utilisateur :'(" + response.toString(),
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.d("echec user number: ", response.toString());
                    Log.d("echec user number: ", e.toString());

                }
            }

            @Override
            public void onFailure(Call<UserContainer> call, Throwable t) {
                Toast.makeText(context, "Pas de réponse du serveur... :'(", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                Log.d("user number: ", "echec" + t.toString());

            }
        });
    }

    public static void fetchAuthResponse (final Context context, String android_id, final MainActivity mainActivity) {
        final AppDatabase db = AppDatabase.getAppDatabase(context);

        Call<AuthResponse> call =
                HttpClientHolder.getClient().getAccessToken(
                        (String) android_id + "@yyy.zzz",
                        "secret",
                        "4",
                        "6KdGPLinH0kVzfPofh15iYSSFiUyPsw793pF0gSK",
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
                    fetchAll(db.authResponseDao().getAll().getAccessToken(), context, (MainActivity) mainActivity);
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

    public static void fetchAll (String token, final Context context, MainActivity mainActivity) {

        fetchUsers(token, context, mainActivity);
        fetchConversations(token, context, mainActivity);
        fetchMessages(token, context);
    }

    private static void fetchUsers (String token, final Context context, final MainActivity mainActivity) {

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

    private static void fetchConversations (String token, final Context context, final MainActivity mainActivity) {

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
                    mainActivity.homeFragment.mAdapter.addItems(response.body());
                    mainActivity.homeFragment.mAdapter.notifyDataSetChanged();
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