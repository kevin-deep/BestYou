package com.bestofyou.fm.bestofyou;

/**
 * Created by FM on 2/14/2016.
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public final TextView textView;
    public CardView cardV;


    public ItemViewHolder(View itemView) {
        super(itemView);
        cardV  = (CardView) itemView.findViewById(R.id.card_view);
        textView = (TextView) itemView.findViewById(R.id.text);
    }

}