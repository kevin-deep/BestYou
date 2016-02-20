package com.bestofyou.fm.bestofyou.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by FM on 12/7/2015.
 */
public class SummaryContract {
    public static final String CONTENT_AUTHORITY = "com.bestofyou.fm.bestofyou";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_HISTORY= "history";
    public static final String PATH_RUBRIC= "rubric";

    public static final class UsrHistory implements BaseColumns {
        //table location
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_HISTORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;
        // Table name
        public static final String TABLE_NAME = "mdb";
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
        public static final String N_Total = "nTotal";

        public static Uri buildHistoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }



    public static final class Rubric implements BaseColumns {
        //table location
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RUBRIC).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RUBRIC;
        public static final String CONTENT__ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RUBRIC;
        // Table name
        public static final String TABLE_NAME = "rubric";
        // User name
        public static final String NAME = "name";
        // timeStamp
        public static final String CREATED_AT = "created_at";
        // weight
        public static final String WEIGHT = "weight";

        public static Uri buildRubricUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
