package com.bestofyou.fm.bestofyou;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.bestofyou.fm.bestofyou.data.SummaryProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by FM on 3/16/2016.
 */
public class Utility {


    /***
     * Checks that application runs first time and write flag at SharedPreferences
     *
     * @return true if 1st time
     */
    public static boolean isFirstTime(Activity ac, String key) {
        SharedPreferences preferences = ac.getPreferences(ac.MODE_PRIVATE);
        boolean mFirst = preferences.getBoolean(key, false);
        if (!mFirst) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, true);
            editor.commit();
        }
        return !mFirst;
    }

    public static String getMonth() {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;//month is zero-based
        if (month >= 10) {
            return Integer.toString(month);
        } else {
            return "0" + Integer.toString(month);
        }
    }

    public static void overshootInterpolator(Activity activity, View v, int duration, int delay) {
        Interpolator interpolator;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        v.setTranslationX(metrics.widthPixels);
        try {
            String path = "OvershootInterpolator";
            if (path == null)
                return;

            interpolator = (Interpolator) OvershootInterpolator.class.newInstance();
            v.animate().setInterpolator(interpolator)
                    .setDuration(duration)
                    .setStartDelay(delay)
                    .translationXBy(-metrics.widthPixels)
                    .start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void zoomIn(Context mContext, View v) {

        Animation zoomOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out_animation);
        v.startAnimation(zoomOutAnimation);
    }


    public static void snakeDisplay(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static String subUpToSpace(String string) {
        if (string.contains(" ")) {
            return string.substring(0, string.indexOf(" "));
        } else {
            return string;
        }
    }

    public static void insertDefaultHabits(Context mContext) {


        SummaryProvider.insertRubric(mContext, "Reading", 1);
        SummaryProvider.insertRubric(mContext, "Coding", 2);
        SummaryProvider.insertRubric(mContext, "Networking", 3);
        SummaryProvider.insertRubric(mContext, "Family Time", 3);
        SummaryProvider.insertRubric(mContext, "Early Sleeping", 2);
        SummaryProvider.insertRubric(mContext, "Exercise", 2);
        SummaryProvider.insertRubric(mContext, "Fruit", 1);
        SummaryProvider.insertRubric(mContext, "Call family", 1);


        SummaryProvider.insertRubric(mContext, "To much Sweet", -2);
        SummaryProvider.insertRubric(mContext, "To much Smoking", -3);
        SummaryProvider.insertRubric(mContext, "Watching TV", -2);
        SummaryProvider.insertRubric(mContext, "Drinking", -1);
        SummaryProvider.insertRubric(mContext, "Gaming", -1);
        SummaryProvider.insertRubric(mContext, "Day dreaming", -2);
        SummaryProvider.insertRubric(mContext, "Nail Biting", -3);
        SummaryProvider.insertRubric(mContext, "Night Owl", -1);

    }

    public static String cutTimeFromDate(String time) {
        String reformattedStr = time;
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            reformattedStr = myFormat.format(fromUser.parse(time));
            System.out.println(reformattedStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    public static String floatToString(Float number){
        return Integer.toString(Math.round(Math.abs(number)));
    }


}
