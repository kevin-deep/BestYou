package com.bestofyou.fm.bestofyou.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by FM on 12/7/2015.
 */
public class SummaryHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "best.db";
    public SummaryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table
        final String SQL_CREATE_SUMMARY_TABLE = "CREATE TABLE " + SummaryContract.UsrHistory.TABLE_NAME + " (" +
        SummaryContract.UsrHistory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SummaryContract.UsrHistory.USER_NAME + " TEXT, " +
                SummaryContract.UsrHistory.P_History + " REAL, " +
                SummaryContract.UsrHistory.N_History + " REAL, " +
                SummaryContract.UsrHistory.P_Total + " REAL, " +
                SummaryContract.UsrHistory.N_Total + " REAL, " +
                SummaryContract.UsrHistory.CREATED_AT +" DATETIME DEFAULT CURRENT_TIMESTAMP "+
                " );";

        final String SQL_CREATE_RUBRIC_TABLE = "CREATE TABLE " + SummaryContract.Rubric.TABLE_NAME + " (" +
                SummaryContract.Rubric._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SummaryContract.Rubric.NAME + " TEXT, " +
                SummaryContract.Rubric.WEIGHT + " REAL NOT NULL, " +
                SummaryContract.Rubric.CREATED_AT +" DATETIME DEFAULT CURRENT_TIMESTAMP "+
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_SUMMARY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RUBRIC_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SummaryContract.UsrHistory.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SummaryContract.Rubric.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //

    public void insert(String col,float content ){
        String colKey = null;
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        switch (col){
            case "positive":
                colKey = SummaryContract.UsrHistory.P_History;
                break;
            case "negative":
                colKey = SummaryContract.UsrHistory.N_History;
                break;
            default:
                Log.d("unattached switch","info");
        }
        //values.put(UsrSummary._ID, id);
        //values.put(SummaryContract.UsrSummary.COLUMN_PLANT, content);
        values.put(colKey, content);
        values.put(SummaryContract.UsrHistory.CREATED_AT, getDateTime());
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                SummaryContract.UsrHistory.TABLE_NAME,
                null,
                values);
        db.close();
    }
    public  float  getPositive( String key){
        SQLiteDatabase db = this.getReadableDatabase();
        final  String[] COLUMNS = {SummaryContract.UsrHistory._ID, SummaryContract.UsrHistory.P_History};
        // query
        Cursor cursor =
                db.query(SummaryContract.UsrHistory.TABLE_NAME, // a. table
                        COLUMNS, // b. column names
                        SummaryContract.UsrHistory.P_History + " = ?", // c. selections
                        new String[]{String.valueOf(key)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        /*if(cursor.moveToFirst())
        {


        }*/
        Cursor c = db.rawQuery("SELECT Sum(pHistory) AS myTotal FROM "
                + SummaryContract.UsrHistory.TABLE_NAME, null);
        float total = c.getFloat(c.getColumnIndex("myTotal"));
       /* float sum;
        while (cursor.moveToNext()) {
            sum = cursor.
        }*/
        return  total;
    }
    public int getCountAll(){
        String countQuery = "SELECT * FROM " + SummaryContract.UsrHistory.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        return cursor.getCount();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
