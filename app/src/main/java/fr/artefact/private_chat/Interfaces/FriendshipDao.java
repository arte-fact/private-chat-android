package fr.artefact.private_chat.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Models.Friendship;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface FriendshipDao {
    @Query("SELECT * FROM friendship")
    List<Friendship> getAll();

    @Insert (onConflict = IGNORE)
    void insertAll(List<Friendship> friendships);

    @Insert (onConflict = IGNORE)
    void insert(Friendship friendship);
}
