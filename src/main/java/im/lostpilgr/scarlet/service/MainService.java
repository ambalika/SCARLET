package im.lostpilgr.scarlet.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pilgrim on 5/17/13.
 */
public class MainService extends Service {
    private final IBinder mBinder = new ScarletBinder();
    private ArrayList<String> scarletLog = new ArrayList<String>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Randomly generates demo log entries
        Random random = new Random();
        if(random.nextBoolean()) {
            scarletLog.add("Demo log entry 1");
        }
        if(random.nextBoolean()) {
            scarletLog.add("Demo log entry 2");
        }
        if(random.nextBoolean()) {
            scarletLog.add("Demo log entry 3");
        }
        if(scarletLog.size() >= 20) {
            scarletLog.remove(0);
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class ScarletBinder extends Binder {
        MainService getService() {
            return MainService.this;
        }
    }

    public List<String> getLog() {
        return scarletLog;
    }
}