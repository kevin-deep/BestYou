package com.bestofyou.fm.bestofyou.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by FM on 12/7/2015.
 */
public class SummaryContract {
    public static final String CONTENT_AUTHORITY = "com.bestofyou.fm.bestofyou";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH= "summary";
    public static final class UsrSummary implements BaseColumns {
        //table location
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        // Table name
        public static final String TABLE_NAME = "summaryList";
        // User name
        public static final String USER_NAME = "userName";
        // timeStamp
        public static final String CREATED_AT = "created_at";
        // Positive history
        public static final String P_History = "pHistory";

        // Negative history
        public static final String N_History = "nHistory";

        //Positive in total
        public static final String P_Total = "pTotal";

        //Negative in total
        public static final String N_Total = "nHistory";
    }
}
