package fr.artefact.private_chat.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "settings")
public class Settings {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("email")
    @Expose
    private String password;
    @SerializedName("password")
    @Expose
    private String email;
    @SerializedName("server_url")
    @Expose
    private String serverUrl;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("client_secret")
    @Expose
    private String clientSecret;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) { this.password = password; }

    public String getClientId() { return clientId; }

    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getServerUrl() { return serverUrl; }

    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }

    public String getClientSecret() { return clientSecret; }

    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
}