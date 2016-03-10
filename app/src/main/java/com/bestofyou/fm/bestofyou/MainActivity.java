package com.bestofyou.fm.bestofyou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;

public class MainActivity extends AppCompatActivity implements McontentObserver.Callback {
    public CollapsingToolbarLayout mCollapsingToobar;
    public Toolbar mToolbar;
    FloatingActionButton profile;
    public TextView pPoint, nPoint;
    //Content observer handler
    McontentObserver observer = new McontentObserver(new Handler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
       /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        mCollapsingToobar = (CollapsingToolbarLayout) findViewById(R.id.Collapse_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        profile = (FloatingActionButton) findViewById(R.id.profile);
        pPoint = (TextView) findViewById(R.id.p_point);
        nPoint = (TextView) findViewById(R.id.n_point);

      /*  mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        updateHeader();
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton addNewType = (FloatingActionButton) findViewById(R.id.addNewType);
        addNewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddNewtypeActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AuthenticationActivity.class));
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                callSummary();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initalObserver();
    }

    /***
     *  also unregister the Content Observer, therwise you would create a memory leak
     *  and the Activity would never be garbage collected.
     */
    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().
                unregisterContentObserver(observer);
    }

    public void callSummary() {
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

    public  void updateHeader() {
        pPoint.setText(SummaryProvider.getPPoint(this.getBaseContext()));
        nPoint.setText(SummaryProvider.getNPoint(this.getBaseContext()));
    }
    //register the Content Observer
    public void initalObserver(){
        observer.contexTo=this;
        getApplicationContext().getContentResolver().registerContentObserver(
                SummaryContract.Total.CONTENT_URI,
                true,
                observer
        );
    }

    @Override
    public void update_main_header() {
        updateHeader();
    }
}
