package fr.artefact.private_chat.Models.ModelContainers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import fr.artefact.private_chat.Models.Conversation;

public class ConversationContainer {
    @SerializedName("conversation")
    @Expose
    private List<Conversation> conversations;

    public List<Conversation> getConversation() {
        return conversations;
    }

    public void setConversation(List<Conversation> conversation) {
        this.conversations = conversation;
    }

    public int getCount() {
        return conversations.size();
    }
}
