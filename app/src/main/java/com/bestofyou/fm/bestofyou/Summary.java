package com.bestofyou.fm.bestofyou;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bestofyou.fm.bestofyou.CustomizedView.CircleProgressBar;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.data.SummaryHelper;

public class Summary extends AppCompatActivity implements View.OnClickListener{
    SummaryHelper mDbHelper;
    int x;
    private Button addP;
    private Button addN;
    public Context mContext;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        addP = (Button)findViewById(R.id.addP);
        addN = (Button)findViewById(R.id.addN);
        mContext =  getBaseContext();
        t = (TextView)findViewById(R.id.testId);

        addP.setText("add positive list");
        addP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    SummaryProvider.insertRubric(mContext, "Sports", 2);
                    SummaryProvider.insertRubric(mContext,"Reading", 4);
                    SummaryProvider.insertRubric(mContext,"Coding", 2);
                    SummaryProvider.insertRubric(mContext,"Networking", 4);
                    SummaryProvider.insertRubric(mContext,"Family Time", 4);
                    SummaryProvider.insertRubric(mContext,"Early Sleeping", 4);

            }
        });

        addN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SummaryProvider.insertRubric(mContext, "Sloth", -2);
                SummaryProvider.insertRubric(mContext, "Smoking", -4);
                SummaryProvider.insertRubric(mContext, "Watching TV", -2);
                SummaryProvider.insertRubric(mContext, "Drinking", -4);
                SummaryProvider.insertRubric(mContext, "Gaming", -4);
                SummaryProvider.insertRubric(mContext, "Day dreaming", -4);
                SummaryProvider.insertRubric(mContext, "Gluttony", -4);
                SummaryProvider.insertRubric(mContext, "Wrath", -4);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == addP){
            mDbHelper.insert("positive", 1);
            Toast.makeText(this, "data"+x, Toast.LENGTH_SHORT).show();
        }

    }


}
