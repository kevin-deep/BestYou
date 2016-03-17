package com.bestofyou.fm.bestofyou;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by FM on 3/3/2016.
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {


    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Favored", "Hateful" };
    private int[] imageResId = {
            R.drawable.ic_sentiment_very_dissatisfied_active,
            R.drawable.ic_sentiment_very_satisfied_active

    };
    private Context context;

    public FragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        //return BlankFragment.newInstance(position + 1);

        switch (position) {
            case 0:
                PositiveFragment tab1 = new PositiveFragment();
                return tab1;
            case 1:
                NegativeFragment tab2 = new NegativeFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //return tabTitles[position];

       Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;


    }
}
