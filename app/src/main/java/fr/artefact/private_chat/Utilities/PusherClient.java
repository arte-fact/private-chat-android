package fr.artefact.private_chat.Utilities;

import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import fr.artefact.private_chat.Adapters.MessageAdapter;

public class PusherClient {

    private Pusher pusher;
    private PusherOptions options;

    private String appCluster = "eu";
    private String appKey= "a7460dcc066019b90756";


    public PusherClient() {
        options = new PusherOptions().setCluster(appCluster);
        pusher = new Pusher(appKey, options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                String text = "State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState();
                Log.d("pusher", text);
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
            }
        }, ConnectionState.ALL);
    }

    public Pusher getPusher() {
        return pusher;
    }
}
