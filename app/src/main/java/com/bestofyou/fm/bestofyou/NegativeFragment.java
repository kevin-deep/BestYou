package com.bestofyou.fm.bestofyou;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.helper.SimpleItemTouchHelperCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class NegativeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = NegativeFragment.class.getSimpleName();
    private RecyclerListAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private View rateView;
    private static final int BEST_LOADER = 3; // have to be unique for every loader using in activity
    private int mPosition = RecyclerView.NO_POSITION;
    private ItemTouchHelper mItemTouchHelper;
    static final int COL_RUBRIC_NAME = 1;
    static final int COL_RUBRIC_WEIGHT = 2;

    private static final String[] RUBRIC_COLUMNS = {
            SummaryContract.Rubric.TABLE_NAME + "." + SummaryContract.Rubric._ID,
            SummaryContract.Rubric.NAME,
            SummaryContract.Rubric.WEIGHT,
            SummaryContract.Rubric.POPULARITY + " AS p",
            SummaryContract.Rubric.COMMITMENT
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_negative, container, false);
        rateView =  root.findViewById(R.id.rates_in_hour);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        //mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //RecyclerListAdapter adapter = new RecyclerListAdapter();

        // The mRecyclerAdapter will take data from a source and use it to populate the RecyclerView  it's attached to.
        mRecyclerAdapter = new RecyclerListAdapter(this.getContext(),RecyclerListAdapter.PAGE_TYPE_NEGATIVE);
        mRecyclerView.setAdapter(mRecyclerAdapter);



       ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecyclerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return root;
    }

    public NegativeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // hold for transition here just in-case the activity
        // needs to be re-created. In a standard return transition,
        // this doesn't actually make a difference.
        getActivity().supportPostponeEnterTransition();
        getLoaderManager().initLoader(BEST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //String locationSetting = Utility.getPreferredLocation(getActivity());
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.
        //initial the loader cursor
        final String SELECT = "((" + SummaryContract.Rubric.NAME + " NOTNULL) AND ("
                + SummaryContract.Rubric.WEIGHT + " <0 ) AND (" + SummaryContract.Rubric.POPULARITY + " NOTNULL " + "))";
        //final String SORT =SummaryContract.Rubric.POPULARITY + " DESC";
        final String SORT = " p DESC";

        Uri rubricUri = SummaryContract.Rubric.CONTENT_URI;
        return new CursorLoader(this.getContext(),
                rubricUri,
                // null,
                RUBRIC_COLUMNS,
                SELECT,
                null,
                SORT);
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
            getActivity().supportStartPostponedEnterTransition();}else{

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

}
