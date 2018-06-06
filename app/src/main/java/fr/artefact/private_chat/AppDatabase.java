package fr.artefact.private_chat;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static void fetchUsers (String token, Context context) {

        final AppDatabase db = AppDatabase.getAppDatabase(context);
        Boolean status = false;

        final Call<List<User>> usersCall =
                HttpClientHolder.getClient().getUsers("Bearer " + token);

        // Execute the call asynchronously. Get a positive or negative callback.
        usersCall.enqueue(new retrofit2.Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                db.userDao().insertAll((List<User>) response.body());
                Log.d("db:", db.userDao().getAll().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("response", t.toString());
            }
        });
    }
}

