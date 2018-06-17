package fr.artefact.private_chat.Interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import fr.artefact.private_chat.Models.Settings;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SettingsDao {
    @Insert (onConflict = REPLACE)
    void insert(Settings settings);

    @Query("SELECT * FROM settings WHERE id = :id")
    Settings getWithId(int id);
}
