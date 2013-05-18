package im.lostpilgr.scarlet.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Pilgrim on 5/17/13.
 */
public class StartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MainService.class);
        context.startService(service);
    }
}