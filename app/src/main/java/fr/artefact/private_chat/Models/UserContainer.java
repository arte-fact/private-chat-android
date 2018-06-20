package fr.artefact.private_chat.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserContainer {
    @SerializedName("user")
    @Expose
    private User user;

    public User getUser() {
        return this.user;
    }
}
