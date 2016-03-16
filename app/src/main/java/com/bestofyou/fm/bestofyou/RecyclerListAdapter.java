package com.bestofyou.fm.bestofyou;

/**
 * Created by FM on 2/14/2016.
 */
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.helper.ItemTouchHelperAdapter;
import com.bestofyou.fm.bestofyou.helper.ItemTouchHelperViewHolder;
import com.bestofyou.fm.bestofyou.data.SummaryHelper;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter
{
    public static final int COL_TOTAL_NAME = 1;
    public static final int COL_TOTAL_P_TOTAL = 2;
    public static final int COL_TOTAL_N_TOTAL = 3;

    public static final int COL_RUBRIC_ID = 0;
    public static final int COL_RUBRIC_NAME = 1;
    public static final int COL_RUBRIC_WEIGHT = 2;
    public static final int COL_RUBRIC_POPULARITY = 3;

    public static final String[] TOTAL_COLUMNS = {
            SummaryContract.Total.TABLE_NAME + "." + SummaryContract.Total._ID,
            SummaryContract.Total.NAME,
            SummaryContract.Total.P_IN_Total,
            SummaryContract.Total.N_IN_Total
    };

    public static final String[] TOTAL_COLUMNS_RUBRIC = {
            SummaryContract.Rubric.TABLE_NAME + "." + SummaryContract.Rubric._ID,
            SummaryContract.Rubric.NAME,
            SummaryContract.Rubric.WEIGHT,
            SummaryContract.Rubric.POPULARITY
    };
    public static final int PAGE_TYPE_POSITIVE = 1;
    public static final int PAGE_TYPE_NEGATIVE = 2;
    private Cursor mCursor;
    //final private ItemChoiceManager mICM;
    private Context mContext;
   // final private ForecastAdapterOnClickHandler mClickHandler;
    private View rateView;
    private int pageType;
    public static interface ForecastAdapterOnClickHandler {
        void onClick(Long date, ItemViewHolder vh);}
    View rootView;
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
    public RecyclerListAdapter(Context context, int pageType) {
        this.pageType = pageType;
        mContext = context;
        this.rateView = rateView;
        rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);
    }


    /**
     * create a new RecyclerView.ViewHolder and initializes some private fields to be used by RecyclerView.
     * @param viewGroup parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     *
     * The new ViewHolder will be used to display items of the adapter using onBindViewHolder(ViewHolder, int, List).
     * Since it will be re-used to display different items in the data set,
     * it is a good idea to cache references to sub views of the View to avoid unnecessary findViewById(int) calls.
     */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, viewGroup, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final int  thisPosition = position;
        mCursor.moveToPosition(position);
       /* int dateColumnIndex = mCursor.getColumnIndex(SummaryContract.Rubric.WEIGHT);
        //!!!!!Important This going to send the date into mClickHandler then it will be deliver to constructor
        mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);*/
        // Read name from cursor
        String name = mCursor.getString(PositiveFragment.COL_RUBRIC_NAME);
        holder.name.setText(name);
        // Read weight from cursor
        final float weight = mCursor.getFloat(PositiveFragment.COL_RUBRIC_WEIGHT);
        if (Math.abs(weight) == 1F ){
            holder.itemWeightColor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.priority_low));
        }else if(Math.abs(weight) == 2F){
            holder.itemWeightColor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.priority_medium));
        }else {
            holder.itemWeightColor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.priority_high));
        }
        holder.rateHour.setVisibility(holder.tick ? View.GONE : View.VISIBLE);
        //holder.textView.setText(mItems.get(position));

        holder.cardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tickCross.callOnClick();
            }
        });

        //add bottom
        holder.tickCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AnimatedVectorDrawable drawable = holder.tick ? holder.tickToCross : holder.crossToTick;
                AnimatedVectorDrawable drawable = holder.tick ? holder.tickToCross : holder.crossToTick;
                holder.tickCross.setImageDrawable(drawable);
                drawable.start();
                holder.tick=!holder.tick;
                holder.rateHour.setVisibility(holder.tick ?  View.GONE:View.VISIBLE);
            }
        });

        holder.oneHour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContentValues value = SummaryProvider.getRubric(mCursor,thisPosition);
                String habitName = value.getAsString(SummaryContract.Rubric.NAME);
                SummaryProvider.insertHistory(mContext, 1 * weight, habitName);
                SummaryProvider.insertTotal(mContext, 1 * weight);
                holder.tickCross.callOnClick();
                snakeDisply(rootView, 1 * weight, pageType);
            }
        });

        holder.twoHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("add two hour", "onClick");
                ContentValues value = SummaryProvider.getRubric(mCursor,thisPosition);
                String habitName = value.getAsString(SummaryContract.Rubric.NAME);
                SummaryProvider.insertHistory(mContext, 2 * weight, habitName);
                SummaryProvider.insertTotal(mContext, 2 * weight);
                int rowId = SummaryProvider.getRubricId(mCursor,position);
                SummaryProvider.updatePopularityRubric(mContext,rowId);
                holder.tickCross.callOnClick();
                snakeDisply(rootView,2*weight, pageType);

            }
        });
        holder.halfHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("add two hour", "onClick");
                ContentValues value = SummaryProvider.getRubric(mCursor,thisPosition);
                String habitName = value.getAsString(SummaryContract.Rubric.NAME);
                SummaryProvider.insertHistory(mContext,  weight/2, habitName);
                SummaryProvider.insertTotal(mContext,  weight/2);
                holder.tickCross.callOnClick();
                snakeDisply(rootView,weight/2, pageType);

            }
        });
        holder.oneHalfHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("add two hour", "onClick");
                ContentValues value = SummaryProvider.getRubric(mCursor, thisPosition);
                String habitName = value.getAsString(SummaryContract.Rubric.NAME);
                SummaryProvider.insertHistory(mContext, 1.5F * weight, habitName);
                SummaryProvider.insertTotal(mContext, 1.5F * weight);
                holder.tickCross.callOnClick();
                snakeDisply(rootView, 1.5F * weight, pageType);

            }
        });
    }

    private void snakeDisply(View view, Float weight, int habitType){
        String toPlace;
        if (habitType == PAGE_TYPE_POSITIVE) toPlace = "Positive Point";
        else toPlace = "Negative Point";
        String message = Float.toString(Math.abs(weight)) + " points has been added as " + toPlace;
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        snackbar.show();
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
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        /*Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);*/
        return true;
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
    @Override
    public void onItemDismiss(int position) {
        /*mCursor.moveToPosition(position);

        String name = mCursor.getString(PositiveFragment.COL_RUBRIC_NAME);

        // Read weight from cursor
        Long weight = mCursor.getLong(PositiveFragment.COL_RUBRIC_WEIGHT);

        mItems.remove(position);
        Toast.makeText(this.mContext, name+ "   "+weight , Toast.LENGTH_SHORT).show();
        notifyItemRemoved(position);
        int lastIndex = getItemCount();*/
        //notifyItemMoved(position, lastIndex-1);



    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        public int pageType;

        public final TextView name;
        //public final TextView weight;
        public CardView cardV;
        private ImageView tickCross;
        private AnimatedVectorDrawable tickToCross;
        private AnimatedVectorDrawable crossToTick;
        private View rateHour;
        private Button oneHour,oneHalfHour,twoHour,halfHour;
        private ImageView itemWeightColor;


        private boolean tick = true;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardV  = (CardView) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.name);
            //weight = (TextView) itemView.findViewById(R.id.weight);
            rateHour = (View) itemView.findViewById(R.id.rates_in_hour);
            oneHour = (Button)itemView.findViewById(R.id.btn_one_hour);
            oneHalfHour = (Button)itemView.findViewById(R.id.btn_one_half_hour);
            twoHour = (Button)itemView.findViewById(R.id.btn_two_hour);
            halfHour = (Button)itemView.findViewById(R.id.btn_half_hour);

            //tickCross = (ImageView) itemView.findViewById(R.id.tick_cross);
            tickCross = (ImageView) itemView.findViewById(R.id.tick_cross);
            //tickToCross = (AnimatedVectorDrawable)itemView.getContext() .getDrawable(R.drawable.avd_tick_to_cross);
            tickToCross = (AnimatedVectorDrawable)itemView.getContext() .getDrawable(R.drawable.avd_click_to_show_red);
            crossToTick = (AnimatedVectorDrawable) itemView.getContext().getDrawable(R.drawable.avd_click_to_show_black);
            itemWeightColor = (ImageView)itemView.findViewById(R.id.item_weight_color);


        }
        //update the negative rubric ico showing on the negative view


        @Override
        public void onItemSelected() {
            final int position = this.getLayoutPosition();
            itemView.setBackgroundColor(Color.GRAY);
            CharSequence colors[] = new CharSequence[] {"Delete", "Update"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            //builder.setTitle("CRUD");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //TODO switch clause
                    switch (which) {
                        case 0:
                            Log.v("delete", "delete");
                            String mSelectionClause = SummaryContract.Rubric._ID + " =?" ;
                            int rowId = SummaryProvider.getRubricId(mCursor,position);
                            String[] mSelectionArgs = {Integer.toString(rowId)};
                            mContext.getContentResolver().delete(SummaryContract.Rubric.CONTENT_URI,
                                    mSelectionClause,
                                    mSelectionArgs
                                    );
                            break;
                        case 1:
                            Log.v("update", "update");
                            ContentValues value = SummaryProvider.getRubric(mCursor,position);
                            int rowIdupdate = SummaryProvider.getRubricId(mCursor, position);
                            Bundle b=new Bundle();
                            b.putFloat(SummaryContract.Rubric.WEIGHT, value.getAsFloat(SummaryContract.Rubric.WEIGHT));
                            b.putString(SummaryContract.Rubric.NAME, value.getAsString(SummaryContract.Rubric.NAME));
                            b.putFloat(SummaryContract.Rubric.POPULARITY, value.getAsFloat(SummaryContract.Rubric.POPULARITY));
                            b.putInt(SummaryContract.Rubric._ID, rowIdupdate);
                            Intent intent=new Intent(mContext,AddNewtypeActivity.class);
                            intent.putExtras(b);
                            mContext.startActivity(intent);
                            break;
                        default:
                            Log.v("error", "CRUD error");
                    }
                }
            });
            builder.show();
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }

}