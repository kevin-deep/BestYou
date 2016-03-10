package com.bestofyou.fm.bestofyou;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by FM on 3/9/2016.
 */

    public class McontentObserver extends ContentObserver {
    public Context contexTo = null; //This is for displaying Toasts

    public McontentObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        // do s.th.
        // depending on the handler you might be on the UI
        // thread, so be cautious!
        ((Callback) contexTo ).update_main_header();
       //ShowToast("Settings change detected");
    }

    /*private void ShowToast(String strMensaje) {
        Toast toast1 = Toast.makeText(contexTo, strMensaje, Toast.LENGTH_SHORT);
        toast1.show();
    }
*/
    interface Callback{
        void update_main_header();
    }

}
