package com.bestofyou.fm.bestofyou.widget;

import android.appwidget.AppWidgetProvider;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.bestofyou.fm.bestofyou.MainActivity;
import com.bestofyou.fm.bestofyou.McontentObserver;
import com.bestofyou.fm.bestofyou.R;
import com.bestofyou.fm.bestofyou.service.MyBestSyncAdapter;


/**
 * Created by FM on 3/11/2016.
 */
public class MyBestWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, MyBestWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, MyBestWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (McontentObserver.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, MyBestWidgetIntentService.class));
        }
    }

}
