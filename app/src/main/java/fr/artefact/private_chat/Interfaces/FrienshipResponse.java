package fr.artefact.private_chat.Interfaces;

import android.support.annotation.Nullable;

import java.util.List;

import fr.artefact.private_chat.Models.Friendship;

public interface FrienshipResponse {
    void onResponse (@Nullable List<Friendship> friendship, @Nullable String error);
}
