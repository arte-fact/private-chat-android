package fr.artefact.private_chat.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import fr.artefact.private_chat.Models.AuthResponse;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Utilities.HttpClient.HttpClientHolder;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Activities.LoginActivity;
import fr.artefact.private_chat.Models.User;
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
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        db.authResponseDao().insert(response.body());
                    } catch (Exception e) {
                        //
                    }
                } else {
                    Intent login = new Intent(context, LoginActivity.class);
                    context.startActivity(login);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
            }
        });

        return true;
    }

    public static void fetchUsers (String token, Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<User>> usersCall =
                HttpClientHolder.getClient().getUsers("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                db.userDao().insertAll(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("response", t.toString());
            }
        });
    }

    public static void fetchConversations (String token, Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<Conversation>> messagesCall =
                HttpClientHolder.getClient().getConversations("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        messagesCall.enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Conversation>> call,@NonNull Response<List<Conversation>> response) {
                db.conversationDao().insertAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                //
            }
        });
    }

    public static void fetchMessages (String token, Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<Message>> messagesCall =
                HttpClientHolder.getClient().getMessages("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        messagesCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call,@NonNull Response<List<Message>> response) {
                db.messageDao().insertAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                //
            }
        });
    }
}