package im.lostpilgr.scarlet.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Pilgrim on 5/17/13.
 */
public class ScheduledUpdater extends BroadcastReceiver {

    // Restart service every 5 minutes
    private static final long REPEAT_TIME = 1000 * 300;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, StartServiceReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        // Start 30 seconds after boot completed
        cal.add(Calendar.SECOND, 30);
        // Then re-run roughly every 5 minutes
        service.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);
    }
}