package com.bestofyou.fm.bestofyou;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by FM on 3/16/2016.
 */
public class Utility {


    /***
     * Checks that application runs first time and write flag at SharedPreferences
     * @return true if 1st time
     */
    public static boolean isFirstTime(Activity ac, String key)
    {
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

    public static String getMonth(){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH)+1;//month is zero-based
        if (month>=10){
            return Integer.toString(month);
        }else{
            return "0" + Integer.toString(month);
        }
    }

    public static void overshootInterpolator(View v, Context mContext){
        Interpolator interpolator;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        v.setTranslationY(metrics.heightPixels);
        try {
            interpolator = OvershootInterpolator.class.newInstance();
            v.animate().setInterpolator(interpolator)
                    .setDuration(100)
                    .setStartDelay(500)
                    .translationXBy(-metrics.heightPixels)
                    .start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
