package com.bestofyou.fm.bestofyou.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by FM on 3/23/2016.
 */
public class ServiceClockInOut extends Service {
    // constant
    public static final long NOTIFY_INTERVAL_10SECONDS = 5 * 1000 ; // 10 seconds
    public static final long NOTIFY_INTERVAL_DAY = NOTIFY_INTERVAL_10SECONDS * 6 * 60 *24 ; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        registerReceiver(mMessageReceiver, new IntentFilter("end_time"));
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL_10SECONDS);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast

                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                    String currentDateTime = dateTimeFormat.format(new Date()).toString();
                    String[] startSplit = currentDateTime.split(" ");
                    String CurrentTime = startSplit[1] + " " + startSplit[2].toUpperCase();


                    System.out.println("service start-======   " + CurrentTime + " == " +
                             " == " );

                    Intent intent = new Intent();
                    intent.putExtra("timer", "1");
                    sendBroadcast(intent);



                }

            });
        }

    }
    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            if (mTimer != null)
                mTimer.cancel();
            unregisterReceiver(mMessageReceiver);
        }
    };
}