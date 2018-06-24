package fr.artefact.private_chat.Utilities.HttpClient;

import java.util.List;

import fr.artefact.private_chat.Models.AuthResponse;
import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.Models.ModelContainers.ConversationContainer;
import fr.artefact.private_chat.Models.User;
import fr.artefact.private_chat.Models.ModelContainers.UserContainer;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @FormUrlEncoded
    @POST("api/user-number")
    Call<UserContainer> getUserNumber(
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
    Call<ConversationContainer> getConversations(@Header("authorization") String token);

    @POST("api/messages")
    Call<Message> postMessage(
            @Header("authorization") String token,
            @Body Message message
            );

    @POST("api/conversations")
    Call<Conversation> postConversation(
            @Header("authorization") String token,
            @Body int user_id
            );
}
