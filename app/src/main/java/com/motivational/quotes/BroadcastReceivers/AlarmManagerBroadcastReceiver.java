package com.motivational.quotes.BroadcastReceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        Log.d("ALARM FIRED", "FIRED ");
        wl.release();
    }

    public void setAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.motivational.quotes.START_ALARM");
        intent.putExtra("ONE_TIME", Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),60, pi);
    }



}
