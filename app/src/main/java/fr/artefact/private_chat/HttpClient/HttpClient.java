package fr.artefact.private_chat.HttpClient;

import java.util.List;

import fr.artefact.private_chat.Auth.AuthResponse;
import fr.artefact.private_chat.Conversation.Conversation;
import fr.artefact.private_chat.Message.Message;
import fr.artefact.private_chat.User.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface HttpClient {

    @FormUrlEncoded
    @POST("oauth/token")
    Call<AuthResponse> getAccessToken(
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("scope") String scope,
            @Field("grant_type") String grantType);

    @GET("api/users")
    Call<List<User>> getUsers(@Header("authorization") String token);

    @GET("api/messages")
    Call<List<Message>> getMessages(@Header("authorization") String token);

    @GET("api/conversations")
    Call<List<Conversation>> getConversations(@Header("authorization") String token);
}
