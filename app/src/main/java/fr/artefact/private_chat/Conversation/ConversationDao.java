package fr.artefact.private_chat.Conversation;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ConversationDao {
    @Query("SELECT * FROM conversation")
    List<Conversation> getAll();

    @Insert (onConflict = REPLACE)
    void insertAll(List<Conversation> conversations);
}
