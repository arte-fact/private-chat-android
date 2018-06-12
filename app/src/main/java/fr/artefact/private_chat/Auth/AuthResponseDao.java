package fr.artefact.private_chat.Auth;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface AuthResponseDao {
    @Query("SELECT * FROM auth_response")
    AuthResponse getAll();

    @Insert (onConflict = REPLACE)
    void insert(AuthResponse authResponse);
}