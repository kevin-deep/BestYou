package com.bestofyou.fm.bestofyou.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;

import com.bestofyou.fm.bestofyou.CustomizedView.CircleProgressBar;
import com.bestofyou.fm.bestofyou.MainActivity;
import com.bestofyou.fm.bestofyou.R;
import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;

/**
 * Created by FM on 3/12/2016.
 */
/**
 * IntentService which handles updating all  widgets with the latest data
 */
public class MyBestWidgetIntentService extends IntentService {
    public static final String[] TOTAL_COLUMNS = {
            SummaryContract.Total.TABLE_NAME + "." + SummaryContract.Total._ID,
            SummaryContract.Total.NAME,
            SummaryContract.Total.P_IN_Total,
            SummaryContract.Total.N_IN_Total
    };
    // these indices must match the projection
    public static final int COL_TOTAL_ID = 0;
    public static final int COL_TOTAL_NAME = 1;
    public static final int COL_TOTAL_P_TOTAL = 2;
    public static final int COL_TOTAL_N_TOTAL = 3;

    public MyBestWidgetIntentService() {
        super("MyBestWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the  widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                MyBestWidgetProvider.class));
        CircleProgressBar circleProgressBarDay;

        // Get Total data from the ContentProvider
        Float pPointMonth = SummaryProvider.getPtotalMonth();
        Float nPointMonth = SummaryProvider.getNtotalMonth();
        Float pPointPercentMonth = pPointMonth/(pPointMonth+Math.abs(nPointMonth))*100;



        String description = getString(R.string.title_widget_mybest);


        if (pPointMonth == null|| nPointMonth ==null) {
            return;
        }
        /**
         * The first loop says, for each int in the array numbers, print the int.
         The second for loops says, for each String in the list, print the String.
         */
        for (int appWidgetId : appWidgetIds) {
            // int layoutId = R.layout.widget_today_small;
            // Find the correct layout based on the widget's width
            int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_best_default_width);
            int largeWidth = getResources().getDimensionPixelSize(R.dimen.widget_best_large_width);
            int layoutId;
            if (widgetWidth >= largeWidth) {
                //layoutId = R.layout.widget_mybest_large;
                layoutId = R.layout.widget_mybest;
            } else if (widgetWidth >= defaultWidth) {
                layoutId = R.layout.widget_mybest;
            } else {
                //layoutId = R.layout.widget_mybest_small;
                layoutId = R.layout.widget_mybest;
            }
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.widget_icon, R.drawable.ic_sentiment_very_satisfied_black_18px);
            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, description);
            }

            //views.setTextViewText(R.id.widget_description, description);
            views.setTextViewText(R.id.n_point_widget, Integer.toString(Math.round(Math.abs(nPointMonth))));
            views.setTextViewText(R.id.p_point_widget, Integer.toString(Math.round(Math.abs(pPointMonth))));
            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_best_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_best_default_width);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget_icon, description);
    }
}
