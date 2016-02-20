package com.bestofyou.fm.bestofyou.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.bestofyou.fm.bestofyou.Summary;

/**
 * Created by FM on 12/7/2015.
 */
public class SummaryProvider extends ContentProvider {

    static final int SUMMARY = 1;
    static final int RUBRIC = 2;
    static final int HISTORY = 3;
    private SummaryHelper mOpenHelper;

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mOpenHelper = new SummaryHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SummaryContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, SummaryContract.PATH_HISTORY, HISTORY);
        matcher.addURI(authority, SummaryContract.PATH_RUBRIC, RUBRIC);
        //matcher.addURI(authority, SummaryContract.PATH_RUBRIC + "/*/#", SUMMARY);
        //"PATH" - matches "PATH" exactly
        //"PATH/#" matches "PATH" Followed by a number
        //"PATH/*" - MATCHES "PATH" followed by any string
        //"Path/*/other/#" matches "PATH" followed by a string followed by "other" followed by a number
        return matcher;
    }


    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case HISTORY:
                return SummaryContract.UsrHistory.CONTENT_TYPE;
            case RUBRIC:
                return SummaryContract.Rubric.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        /*Implement this to handle requests to update one or more rows.
        The implementation should update all rows matching the selection to set the columns according to the provided values map.
         As a courtesy, call notifyChange() after updating. This method can be called from multiple threads, as described in Processes and Threads.

            Parameters
            uri	The URI to query. This can potentially have a record ID if this is an update request for a specific record.
            values	A set of column_name/value pairs to update in the database. This must not be null.
            selection	An optional filter to match rows to update.
            Returns
            the number of rows affected.*/
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case HISTORY:
               // normalizeDate(values);
                rowsUpdated = db.update(SummaryContract.UsrHistory.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case RUBRIC:
                rowsUpdated = db.update(SummaryContract.Rubric.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    public Uri insert(Uri uri, ContentValues values) {
        /*Implement this to handle requests to insert a new row. As a courtesy, call notifyChange() after inserting.
        This method can be called from multiple threads, as described in Processes and Threads.

        Parameters
        uri	The content:// URI of the insertion request. This must not be null.
        values	A set of column_name/value pairs to add to the database. This must not be null.
        Returns
        The URI for the newly inserted item.*/

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case HISTORY: {
                //normalizeDate(values); //call methods of normalizeDate and WeatherContract.normalizeDate to set the Date in the database
                long _id = db.insert(SummaryContract.UsrHistory.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = SummaryContract.UsrHistory.buildHistoryUri(_id);//build the content Uri follow by ID
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RUBRIC:{
                long _id=db.insert(SummaryContract.Rubric.TABLE_NAME,null,values);
                if(_id>0)
                    returnUri=SummaryContract.Rubric.buildRubricUri(_id);//build the content Uri follow by ID
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        /*Implement this to handle requests to delete one or more rows.
        The implementation should apply the selection clause when performing deletion, allowing the operation to affect multiple rows in a directory.
        As a courtesy, call notifyChange() after deleting. This method can be called from multiple threads, as described in Processes and Threads.

        The implementation is responsible for parsing out a row ID at the end of the URI, if a specific row is being deleted.
        That is, the client would pass in content://contacts/people/22 and the implementation is responsible for parsing the record number (22) when creating a SQL statement.

        Parameters
        uri	The full URI to query, including a row ID (if a specific record is requested).
        selection	An optional restriction to apply to rows when deleting.
        Returns
        The number of rows affected.*/
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case HISTORY:
                rowsDeleted = db.delete(
                        SummaryContract.UsrHistory.TABLE_NAME, selection, selectionArgs);
                break;
            case RUBRIC:
                rowsDeleted = db.delete(
                        SummaryContract.Rubric.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
           /* case WEATHER_WITH_LOCATION_AND_DATE:
            {
                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                break;
            }*/
            // "weather/*"
            /*case WEATHER_WITH_LOCATION: {
                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
                break;
            }*/
            // "weather"
            case HISTORY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SummaryContract.UsrHistory.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"
            case RUBRIC: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SummaryContract.Rubric.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }




}
