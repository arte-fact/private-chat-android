package fr.artefact.private_chat;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fr.artefact.private_chat.User.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppDatabaseHolder {
    public static void fetchUsers (String token, Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<User>> usersCall =
                HttpClientHolder.getClient().getUsers("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                db.userDao().insertAll((List<User>) response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("response", t.toString());
            }
        });
    }
}