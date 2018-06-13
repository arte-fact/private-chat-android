package fr.artefact.private_chat.Message;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message")
    List<Message> getAll();

    @Query("SELECT * FROM message WHERE conversationId = :conversationId")
    List<Message> getConversationMessages(int conversationId);

    @Insert (onConflict = REPLACE)
    void insertAll(List<Message> messages);
}
