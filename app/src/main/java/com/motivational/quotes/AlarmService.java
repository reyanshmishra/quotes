package com.motivational.quotes;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AlarmService extends Service {
    public static final int NOTIFICATION_ID = 1056;

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(NOTIFICATION_ID, mediaStyleNotification());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification mediaStyleNotification() {

        return new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("Content")
                .build();
    }

}
