package com.bestofyou.fm.bestofyou;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by FM on 3/9/2016.
 */

    public class McontentObserver extends ContentObserver {
    public Context contexTo = null;
    public static final String ACTION_DATA_UPDATED ="com.bestofyou.fm.bestofyou.ACTION_DATA_UPDATED";

    public McontentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }
    @Override
    public void onChange(boolean selfChange, Uri uri) {
       ((Callback) contexTo ).update_main_header();
        updateWidgets();
    }
    interface Callback{
        void update_main_header();
    }

    //send broadcast
    private void updateWidgets() {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(contexTo.getPackageName());
        contexTo.sendBroadcast(dataUpdatedIntent);
    }


}
