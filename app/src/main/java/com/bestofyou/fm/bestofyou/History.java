package com.bestofyou.fm.bestofyou;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bestofyou.fm.bestofyou.data.SummaryContract;

public class History extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView mRecyclerView;
    private RecyclerListAdapter mRecyclerAdapter;
    private static final int BEST_LOADER = 3;
    private static final String[] HISTORY_COLUMNS = {
            SummaryContract.UsrHistory.TABLE_NAME + "." + SummaryContract.UsrHistory._ID,
            SummaryContract.UsrHistory.HABIT_NAME,
            SummaryContract.UsrHistory.N_History,
            SummaryContract.UsrHistory.P_History,
            SummaryContract.UsrHistory.CREATED_AT  +" AS t"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //RecyclerListAdapter adapter = new RecyclerListAdapter();
        getSupportLoaderManager().initLoader(BEST_LOADER, null, this);
        // The mRecyclerAdapter will take data from a source and use it to populate the RecyclerView  it's attached to.
        mRecyclerAdapter = new RecyclerListAdapter(this,RecyclerListAdapter.PAGE_TYPE_History);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //String locationSetting = Utility.getPreferredLocation(getActivity());
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.
        //initial the loader cursor
        Uri historyUri = SummaryContract.UsrHistory.CONTENT_URI;

        final String  select = "((" + SummaryContract.UsrHistory.HABIT_NAME + " NOTNULL) AND ("
                + SummaryContract.UsrHistory.P_History + " )" +SummaryContract.UsrHistory.N_History +SummaryContract.UsrHistory.CREATED_AT+ ")";
        final String SORT = " t DESC";
        return new CursorLoader(this,
                historyUri,
                HISTORY_COLUMNS,
                select,
                null,
                SORT);
    }
    //build the new loader
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecyclerAdapter.swapCursor(data);

    }
    //release any resource
    @Override
    public void onLoaderReset(Loader<Cursor> Loader) {
        mRecyclerAdapter.swapCursor(null);
    }

}
