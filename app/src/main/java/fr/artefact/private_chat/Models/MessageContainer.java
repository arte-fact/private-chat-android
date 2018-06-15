package fr.artefact.private_chat.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageContainer {
    @SerializedName("message")
    @Expose
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
