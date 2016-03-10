package com.bestofyou.fm.bestofyou;

/**
 * Created by FM on 2/14/2016.
 */
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    public static final String[] TOTAL_COLUMNS = {
            SummaryContract.Total.TABLE_NAME + "." + SummaryContract.Total._ID,
            SummaryContract.Total.NAME,
            SummaryContract.Total.P_IN_Total,
            SummaryContract.Total.N_IN_Total
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
    public RecyclerListAdapter(Context context, int pageType) {
        this.pageType = pageType;
        mContext = context;
        this.rateView = rateView;
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
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        mCursor.moveToPosition(position);
       /* int dateColumnIndex = mCursor.getColumnIndex(SummaryContract.Rubric.WEIGHT);
        //!!!!!Important This going to send the date into mClickHandler then it will be deliver to constructor
        mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);*/
        // Read name from cursor


        hiddenBars(holder);
        if (pageType ==PAGE_TYPE_NEGATIVE) holder.updateNegativeIcons();
        String name = mCursor.getString(PositiveFragment.COL_RUBRIC_NAME);
        holder.name.setText(name);
        // Read weight from cursor
        final float weight = mCursor.getFloat(PositiveFragment.COL_RUBRIC_WEIGHT);
        barsVisibility(weight, holder);
        //holder.weight.setText(weight.toString());

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
                SummaryProvider.insertTotal(mContext, 1 * weight);
                holder.tickCross.callOnClick();

            }
        });

        holder.twoHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("add two hour", "onClick");
                SummaryProvider.insertTotal(mContext,2 * weight);
                holder.tickCross.callOnClick();

            }
        });
    }

    public void barsVisibility(float weight ,ItemViewHolder holder){

        if (Math.abs(weight)==1){
            holder.bar1.setVisibility(View.VISIBLE);
        }
        else if(Math.abs(weight)==2){
            holder.bar1.setVisibility(View.VISIBLE);
            holder.bar2.setVisibility(View.VISIBLE);
        }else if (Math.abs(weight)==3){
            holder.bar1.setVisibility(View.VISIBLE);
            holder.bar2.setVisibility(View.VISIBLE);
            holder.bar3.setVisibility(View.VISIBLE);
        }else if (Math.abs(weight)==4){
            holder.bar1.setVisibility(View.VISIBLE);
            holder.bar2.setVisibility(View.VISIBLE);
            holder.bar3.setVisibility(View.VISIBLE);
            holder.bar4.setVisibility(View.VISIBLE);
        }else if (Math.abs(weight)==5){
            holder.bar1.setVisibility(View.VISIBLE);
            holder.bar2.setVisibility(View.VISIBLE);
            holder.bar3.setVisibility(View.VISIBLE);
            holder.bar4.setVisibility(View.VISIBLE);
            holder.bar5.setVisibility(View.VISIBLE);
        }
    }
    private void hiddenBars(ItemViewHolder holder){
        holder.bar1.setVisibility(View.GONE);
        holder.bar2.setVisibility(View.GONE);
        holder.bar3.setVisibility(View.GONE);
        holder.bar4.setVisibility(View.GONE);
        holder.bar5.setVisibility(View.GONE);
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
        private ImageView oneHour;
        private ImageView twoHour;
        private  ImageView bar1;
        private  ImageView bar2;
        private  ImageView bar3;
        private  ImageView bar4;
        private  ImageView bar5;
        private boolean tick = true;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardV  = (CardView) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.name);
            //weight = (TextView) itemView.findViewById(R.id.weight);
            rateHour = (View) itemView.findViewById(R.id.rates_in_hour);
            oneHour = (ImageView)itemView.findViewById(R.id.ic_add_one_hour);
            twoHour = (ImageView)itemView.findViewById(R.id.ic_add_two_hour);
            //tickCross = (ImageView) itemView.findViewById(R.id.tick_cross);
            tickCross = (ImageView) itemView.findViewById(R.id.tick_cross);
            //tickToCross = (AnimatedVectorDrawable)itemView.getContext() .getDrawable(R.drawable.avd_tick_to_cross);
            tickToCross = (AnimatedVectorDrawable)itemView.getContext() .getDrawable(R.drawable.avd_click_to_show_red);
            crossToTick = (AnimatedVectorDrawable) itemView.getContext().getDrawable(R.drawable.avd_click_to_show_black);

            bar1 = (ImageView)itemView.findViewById(R.id.bar1);
            bar2 = (ImageView)itemView.findViewById(R.id.bar2);
            bar3 = (ImageView)itemView.findViewById(R.id.bar3);
            bar4 = (ImageView)itemView.findViewById(R.id.bar4);
            bar5 = (ImageView)itemView.findViewById(R.id.bar5);
        }
        //update the negative rubric ico showing on the negative view
        public void updateNegativeIcons(){
            bar1.setImageResource(R.drawable.ic_star_outline_24dp);
            bar2.setImageResource(R.drawable.ic_star_outline_24dp);
            bar3.setImageResource(R.drawable.ic_star_outline_24dp);
            bar4.setImageResource(R.drawable.ic_star_outline_24dp);
            bar5.setImageResource(R.drawable.ic_star_outline_24dp);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.GRAY);
            Toast.makeText(mContext, "long Press detected", Toast.LENGTH_LONG);
            Log.v("longpress detected", "longpress detected");


            CharSequence colors[] = new CharSequence[] {"Delete", "Update"};

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            //builder.setTitle("CRUD");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v("longpress detected", "longpress detected");
                    //TODO swtich clause
                }
            });
            builder.show();
            //cardV.setCardBackgroundColor(Color.GREEN);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
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