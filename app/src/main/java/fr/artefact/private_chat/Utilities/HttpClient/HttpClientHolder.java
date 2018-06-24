package fr.artefact.private_chat.Utilities.HttpClient;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClientHolder {

    private static final String API_BASE_URL = "http://10.0.2.2:8000/";

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.LONG)
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory
                    .create(gson));

    private static final Retrofit retrofit = builder.client(httpClient.build()).build();

    private static final HttpClient client = retrofit.create(HttpClient.class);

    public static HttpClient getClient () {
        return client;
    }
}
