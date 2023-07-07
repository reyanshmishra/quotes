package com.motivational.quotes.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.motivational.quotes.BroadcastReceivers.AlarmManagerBroadcastReceiver;
import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;

public class SplashActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.insta_post);
        Common.applyFontForToolbarTitle(this);

        findViewById(R.id.quote).setOnClickListener(v -> startActivity(new Intent(SplashActivity.this, EditQuoteActivity.class)));
        findViewById(R.id.story).setOnClickListener(v -> startActivity(new Intent(SplashActivity.this, StoryActivity.class)));
        findViewById(R.id.share_video).setOnClickListener(v -> setAlarm(getApplicationContext()));
        //.setOnClickListener(v -> startActivity(new Intent(SplashActivity.this, VideoEditActivity.class)));


    }

    public void setAlarm(Context context) {
        AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver =new AlarmManagerBroadcastReceiver();
        alarmManagerBroadcastReceiver.setAlarm(this);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
