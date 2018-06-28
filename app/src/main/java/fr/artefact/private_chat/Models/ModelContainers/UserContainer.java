package fr.artefact.private_chat.Models.ModelContainers;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fr.artefact.private_chat.Models.User;


public class UserContainer {
    @SerializedName("user")
    @Expose
    private User user;

    public User getUser() {
        return this.user;
    }
}
