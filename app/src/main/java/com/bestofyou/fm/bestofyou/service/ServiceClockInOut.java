package com.bestofyou.fm.bestofyou.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.bestofyou.fm.bestofyou.MainActivity;
import com.bestofyou.fm.bestofyou.R;
import com.bestofyou.fm.bestofyou.Utility;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * Created by FM on 3/23/2016.
 */
public class ServiceClockInOut extends Service {
    public static final String LOG_TAG = ServiceClockInOut.class.getSimpleName();
    // constant
    public static final long NOTIFY_INTERVAL_10SECONDS = 5 * 1000; // 10 seconds
    public static final long NOTIFY_INTERVAL_MINUTES = 5 * 1000 * 6;

    //amount of milliseconds in a day
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final long HALF_DAY_IN_MILLIS = 1000 * 60 * 60 * 12;
    //unique ID for the notification
    private static final int BESTOFYOU_NOTIFICATION_ID = 3004;

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
        notification();
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        registerReceiver(mMessageReceiver, new IntentFilter("end_time"));
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL_MINUTES);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast

                    //SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
                    //String currentDateTime = dateTimeFormat.format(new Date()).toString();
                    //String[] startSplit = currentDateTime.split(" ");
                    //String CurrentTime = startSplit[1] + " " + startSplit[2].toUpperCase();
                    notification();

                    //System.out.println("service start-======   " + CurrentTime + " == " + " == " );

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

    private void notification() {
        Context context = this.getBaseContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if (displayNotifications) {


            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= NOTIFY_INTERVAL_MINUTES) {
                // Last sync was more than 1 day ago, let's send a notification.
                Log.v(">>>enter notification", LOG_TAG);
                // Define the text of the notification.
                String contentText = String.format(context.getString(R.string.notification_message),
                        "Today",
                        "",
                        "");

                /*// NotificationCompatBuilder is to  build backward-compatible notifications.
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this.getBaseContext()).setContentText(contentText);

                // Make something interesting happen when the user clicks on the notification.
                // In this case, opening the app is sufficient.
                Intent resultIntent = new Intent(context, MainActivity.class);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) this.getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                // BESTOFYOU_NOTIFICATION_ID allows you to update the notification later on.
                mNotificationManager.notify(BESTOFYOU_NOTIFICATION_ID, mBuilder.build());*/

                NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent dest = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, dest, 0);
                //TODO: remove the ico background

                int pIco = R.drawable.ic_sentiment_very_satisfied_tab;
                int nIco = R.drawable.ic_sentiment_very_dissatisfied_tab;
                int ico = (SummaryProvider.getPtotalMonth() > SummaryProvider.getNtotalMonth()) ? pIco : nIco;
                //int color = 0xff123456;

                Notification notification = new Notification.Builder(this)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(contentText)
                        .setSmallIcon(ico)

                        .setContentIntent(pendingIntent)
                        .build();

                notifManager.notify(1, notification);

                //refreshing last sync
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(lastNotificationKey, System.currentTimeMillis());
                editor.commit();
            }
        }
    }

}