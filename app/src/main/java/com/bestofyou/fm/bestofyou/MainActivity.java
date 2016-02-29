package com.bestofyou.fm.bestofyou;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.helper.SimpleItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    RecyclerListAdapter mRecyclerAdapter;
    RecyclerView mRecyclerView;
    View rateView;
    private static final int BEST_LOADER = 0; // have to be unique for every loader using in activity
    private int mPosition = RecyclerView.NO_POSITION;

    static final int COL_RUBRIC_NAME = 1;
    static final int COL_RUBRIC_WEIGHT = 2;

    private static final String[] RUBRIC_COLUMNS = {
            SummaryContract.Rubric.TABLE_NAME + "." + SummaryContract.Rubric._ID,
            SummaryContract.Rubric.NAME,
            SummaryContract.Rubric.WEIGHT
    };

    private ItemTouchHelper mItemTouchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rateView = findViewById(R.id.rates_in_hour);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext()));
        //RecyclerListAdapter adapter = new RecyclerListAdapter();

        // The ForecastAdapter will take data from a source and use it to populate the RecyclerView  it's attached to.
        mRecyclerAdapter = new RecyclerListAdapter(this,rateView);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        getSupportLoaderManager().initLoader(BEST_LOADER, null, this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecyclerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                callSummary();


            }
        });
    }

    public void callSummary(){
        startActivity(new Intent(this, Summary.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //String locationSetting = Utility.getPreferredLocation(getActivity());
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.
        //initial the loader cursor
        Uri rubricUri = SummaryContract.Rubric.CONTENT_URI;


        return new CursorLoader(this,
                rubricUri,
                // null,
                RUBRIC_COLUMNS,
                null,
                null,
                null);
    }
    //build the new loader
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecyclerAdapter.swapCursor(data);
        if (mPosition != RecyclerView.NO_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mRecyclerView.smoothScrollToPosition(mPosition);
        }
        if ( data.getCount() == 0 ) {
            this.supportStartPostponedEnterTransition();}else{

            /*mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    // Since we know we're going to get items, we keep the listener around until
                    // we see Children.
                    if (mRecyclerView.getChildCount() > 0) {
                        mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        int itemPosition = mRecyclerAdapter.getSelectedItemPosition();
                        if (RecyclerView.NO_POSITION == itemPosition) itemPosition = 0;
                        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(itemPosition);
                        if (null != vh) {
                            //// TODO: 2/20/2016
                            mRecyclerAdapter.selectView(vh);
                        }

                        //if ( mHoldForTransition )  getActivity().supportStartPostponedEnterTransition();
                       *//* this.postponeEnterTransition ();*//*
                        return true;
                    }
                    return false;
                }
            });*/
        }
    }
    //release any resource
    @Override
    public void onLoaderReset(Loader<Cursor> Loader) {
        mRecyclerAdapter.swapCursor(null);
    }

    /*
      Updates the empty hours rate view
   */
    private void updateEmptyView() {

    }

}
