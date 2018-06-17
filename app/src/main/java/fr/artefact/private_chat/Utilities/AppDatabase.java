package fr.artefact.private_chat.Utilities;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import fr.artefact.private_chat.Interfaces.SettingsDao;
import fr.artefact.private_chat.Models.AuthResponse;
import fr.artefact.private_chat.Interfaces.AuthResponseDao;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Interfaces.ConversationDao;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Interfaces.MessageDao;
import fr.artefact.private_chat.Models.Settings;
import fr.artefact.private_chat.Models.User;
import fr.artefact.private_chat.Interfaces.UserDao;

@Database(entities = {User.class, AuthResponse.class, Conversation.class, Message.class, Settings.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract MessageDao messageDao();

    public abstract ConversationDao conversationDao();

    public abstract AuthResponseDao authResponseDao();

    public abstract SettingsDao settingsDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "private-chat-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }
}

