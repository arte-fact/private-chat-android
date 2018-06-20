package fr.artefact.private_chat.Utilities.HttpClient;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;

import fr.artefact.private_chat.Utilities.AppDatabase;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClientHolder {

    static String API_BASE_URL = "http://10.0.2.2:8000/";

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();

    static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory
                    .create(gson));

    static Retrofit retrofit = builder.client(httpClient.build()).build();

    static HttpClient client = retrofit.create(HttpClient.class);

    public static HttpClient getClient () {
        return client;
    }
}
