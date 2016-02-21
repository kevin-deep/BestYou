package com.bestofyou.fm.bestofyou;

/**
 * Created by FM on 2/14/2016.
 */
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bestofyou.fm.bestofyou.data.SummaryContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder> {
    private Cursor mCursor;
    //final private ItemChoiceManager mICM;
    final private Context mContext;
   // final private ForecastAdapterOnClickHandler mClickHandler;

    public static interface ForecastAdapterOnClickHandler {
        void onClick(Long date, ItemViewHolder vh);}
    private static final String[] STRINGS = new String[]{
            "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten"
    };

    private final List<String> mItems = new ArrayList<>();

    /*public RecyclerListAdapter() {
        mItems.addAll(Arrays.asList(STRINGS));
    }*/

    /*public RecyclerListAdapter(Context context, ForecastAdapterOnClickHandler dh, View emptyView, int choiceMode) {
        mItems.addAll(Arrays.asList(STRINGS));
        mContext = context;
        mClickHandler = dh;
        //mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }
*/
    public RecyclerListAdapter(Context context) {
        mItems.addAll(Arrays.asList(STRINGS));
        mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ItemViewHolder(view);
    }

    /*
        * Returns this view's tag.
        *Returns
        *the Object stored in this view as a tag, or null if not set
        */
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        mCursor.moveToPosition(position);
       /* int dateColumnIndex = mCursor.getColumnIndex(SummaryContract.Rubric.WEIGHT);
        //!!!!!Important This going to send the date into mClickHandler then it will be deliver to constructor
        mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);*/

        // Read name from cursor
        String name = mCursor.getString(MainActivity.COL_RUBRIC_NAME);

        //holder.textView.setText(mItems.get(position));
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
        //return mItems.size();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
       // mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }



  /*  public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }*/
    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if ( viewHolder instanceof ItemViewHolder ) {
            ItemViewHolder vfh = (ItemViewHolder)viewHolder;
            //// TODO: 2/20/2016 remove onClick
            //vfh.onClick(vfh.itemView);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;
        public CardView cardV;


        public ItemViewHolder(View itemView) {
            super(itemView);
            cardV  = (CardView) itemView.findViewById(R.id.card_view);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

       /* @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            //get the index of this column
            int dateColumnIndex = mCursor.getColumnIndex(SummaryContract.Rubric.WEIGHT);
            //!!!!!Important This going to send the date into mClickHandler then it will be deliver to constructor
            mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);
            mICM.onClick(this);
        }*/

    }

}