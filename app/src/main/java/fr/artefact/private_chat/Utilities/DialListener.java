package fr.artefact.private_chat.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.artefact.private_chat.Activities.MainActivity;

public class DialListener extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {

        String pwd = intent.getData().getHost();
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("data", pwd);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}