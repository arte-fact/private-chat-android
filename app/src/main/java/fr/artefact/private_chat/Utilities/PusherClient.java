package fr.artefact.private_chat.Utilities;

import android.util.Log;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import fr.artefact.private_chat.Adapters.MessageAdapter;
import fr.artefact.private_chat.Models.Conversation;

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
                Log.d("pusher","There was a problem connecting!");
            }
        }, ConnectionState.ALL);
    }

    public Pusher getPusher() {
        return pusher;
    }

    public void subscribeChannel(int conversationId) {
        Channel channel = this.getPusher().subscribe("conversation-channel." + conversationId);

        channel.bind("App\\Events\\MessageCreatedEvent", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {

            }
        });
    }

    public void unSubscribeChannel(int conversationId) {
        this.getPusher().unsubscribe("conversation-channel." + conversationId);
    }
}
