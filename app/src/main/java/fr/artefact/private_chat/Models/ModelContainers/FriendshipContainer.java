package fr.artefact.private_chat.Models.ModelContainers;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.artefact.private_chat.Models.Friendship;


public class FriendshipContainer {
    @SerializedName("friendship")
    @Expose
    private List<Friendship> friendship;

    public List<Friendship> getFriendship() {
        return this.friendship;
    }
}
