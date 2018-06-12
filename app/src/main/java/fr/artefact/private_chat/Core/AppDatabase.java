package fr.artefact.private_chat.Core;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import fr.artefact.private_chat.Auth.AuthResponse;
import fr.artefact.private_chat.Auth.AuthResponseDao;
import fr.artefact.private_chat.Conversation.Conversation;
import fr.artefact.private_chat.Conversation.ConversationDao;
import fr.artefact.private_chat.Message.Message;
import fr.artefact.private_chat.Message.MessageDao;
import fr.artefact.private_chat.User.User;
import fr.artefact.private_chat.User.UserDao;

@Database(entities = {User.class, AuthResponse.class, Conversation.class, Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract MessageDao messageDao();

    public abstract ConversationDao conversationDao();

    public abstract AuthResponseDao authResponseDao();

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

