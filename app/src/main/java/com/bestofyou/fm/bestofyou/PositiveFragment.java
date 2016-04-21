package com.bestofyou.fm.bestofyou;


import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.helper.SimpleItemTouchHelperCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class PositiveFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = PositiveFragment.class.getSimpleName();
    private RecyclerListAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private View rateView;
    private static final int BEST_LOADER = 2; // have to be unique for every loader using in activity
    private int mPosition = RecyclerView.NO_POSITION;

    static final int COL_RUBRIC_NAME = 1;
    static final int COL_RUBRIC_WEIGHT = 2;
    static final int COL_RUBRIC_COMMITMENT = 4;
    private View root;
    private static final String[] RUBRIC_COLUMNS = {
            SummaryContract.Rubric.TABLE_NAME + "." + SummaryContract.Rubric._ID,
            SummaryContract.Rubric.NAME,
            SummaryContract.Rubric.WEIGHT,
            SummaryContract.Rubric.POPULARITY + " AS p",
            SummaryContract.Rubric.COMMITMENT
    };
    public CollapsingToolbarLayout mCollapsingToobar;
    public Toolbar mToolbar;


    private ItemTouchHelper mItemTouchHelper;


    public PositiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_positive, container, false);
        rateView =  root.findViewById(R.id.rates_in_hour);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //RecyclerListAdapter adapter = new RecyclerListAdapter();

        // The mRecyclerAdapter will take data from a source and use it to populate the RecyclerView  it's attached to.
        mRecyclerAdapter = new RecyclerListAdapter(this.getContext(),RecyclerListAdapter.PAGE_TYPE_POSITIVE);
        mRecyclerView.setAdapter(mRecyclerAdapter);


        if (Utility.isFirstTime(getActivity(), getString(R.string.first_time_login))) {
            CharSequence dList[] = new CharSequence[]{"Yes", "No"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Do you want to import some popular habits? ");
            builder.setItems(dList, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //TODO switch clause
                    switch (which) {
                        case 0:
                            Utility.insertDefaultHabits(getActivity().getBaseContext());
                            break;
                        case 1:
                            Utility.snakeDisplay(getActivity().getWindow().getDecorView().getRootView(), "You can import habits from menu later.");
                            break;
                        default:
                            Log.v("error", "CRUD error");
                    }
                }
            });
            builder.show();
        }

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecyclerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return root;
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
    public void onStart() {
        super.onStart();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //String locationSetting = Utility.getPreferredLocation(getActivity());
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.
        //initial the loader cursor
        Uri rubricUri = SummaryContract.Rubric.CONTENT_URI;

        final String  select = "((" + SummaryContract.Rubric.NAME + " NOTNULL) AND ("
                + SummaryContract.Rubric.WEIGHT + " >0 ))";
        final String SORT = " p DESC";
        return new CursorLoader(this.getContext(),
                rubricUri,
                RUBRIC_COLUMNS,
                select,
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

        }
    }
    //release any resource
    @Override
    public void onLoaderReset(Loader<Cursor> Loader) {
        mRecyclerAdapter.swapCursor(null);
    }


}
