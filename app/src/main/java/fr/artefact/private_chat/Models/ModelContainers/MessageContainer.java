package fr.artefact.private_chat.Models.ModelContainers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fr.artefact.private_chat.Models.Message;

public class MessageContainer {
    @SerializedName("message")
    @Expose
    private Message message;

    public Message getMessage() {
        return message;
    }

}
