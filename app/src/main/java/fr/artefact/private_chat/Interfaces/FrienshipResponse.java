package fr.artefact.private_chat.Interfaces;

import android.support.annotation.Nullable;

import fr.artefact.private_chat.Models.Friendship;

public interface FrienshipResponse {
    void onResponse (@Nullable Friendship friendship, @Nullable String error);
}
