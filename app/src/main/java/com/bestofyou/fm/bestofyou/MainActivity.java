package com.bestofyou.fm.bestofyou;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bestofyou.fm.bestofyou.CustomizedView.CircleProgressBar;
import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;
import com.bestofyou.fm.bestofyou.service.ServiceClockInOut;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class MainActivity extends AppCompatActivity
        implements McontentObserver.Callback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public CollapsingToolbarLayout mCollapsingToobar;
    public Toolbar mToolbar;
    private ImageButton menu, menuMonth;
    private RelativeLayout  btn_detail_header_month, btn_detail_header_day;
    public TextView pPoint, nPoint, nPointM, pPointM, usrNameDay, usrNameMonth;
    private ViewFlipper vf;
    private CircleProgressBar circleProgressBarDay, circleProgressBarMonth;
    private GoogleApiClient mGoogleApiClient;
    public Person currentUser;
    //Content observer handler
    private McontentObserver observer = new McontentObserver(new Handler());
    private Activity activity;
    boolean header = true; //determine which header shoot on the screen
    private float initialX;

    private BroadcastReceiver mMessageReceiver;


    //public String loginUsr = authInstance.currentUser.getDisplayName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;


        setContentView(R.layout.activity_main);
       /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        vf = (ViewFlipper) findViewById(R.id.view_flipper);
        mCollapsingToobar = (CollapsingToolbarLayout) findViewById(R.id.Collapse_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //profile = (FloatingActionButton) findViewById(R.id.profile);
        menu = (ImageButton) findViewById(R.id.menu_main);
        menuMonth = (ImageButton) findViewById(R.id.menu_main_month);
        btn_detail_header_month = (RelativeLayout) findViewById(R.id.detail_header_month);
        btn_detail_header_day = (RelativeLayout) findViewById(R.id.detail_header_day);
        pPoint = (TextView) findViewById(R.id.p_point_header_day);
        nPoint = (TextView) findViewById(R.id.n_point_header_day);
        pPointM = (TextView) findViewById(R.id.p_point_header_month);
        nPointM = (TextView) findViewById(R.id.n_point_header_month);
        usrNameDay = (TextView) findViewById(R.id.usr_Name_day);
        usrNameMonth = (TextView) findViewById(R.id.usr_Name_month);
        circleProgressBarDay = (CircleProgressBar) findViewById(R.id.custom_progressBar_day);
        circleProgressBarMonth = (CircleProgressBar) findViewById(R.id.custom_progressBar_month);
        //build google ID
        mGoogleApiClient = buildApiClient();
        //build google analytics
        ((MyApplication) getApplication()).startTracking();


        vf = (ViewFlipper) findViewById(R.id.view_flipper);
        vf.setInAnimation(this, android.R.anim.fade_in);
        vf.setOutAnimation(this, android.R.anim.fade_out);

        updateHeader();
        //updateUserName();
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //draw the ico on table layout
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_sentiment_very_satisfied_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_sentiment_very_dissatisfied_tab);

        //register mMessageReceiver and start the service
        registerReceiver(mMessageReceiver, new IntentFilter("time"));
        startService(new Intent(getApplicationContext(), ServiceClockInOut.class));

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String timer = intent.getStringExtra("timer");
                //Utils.showNotification(mContext, 1);
                Log.v(timer, "timer in Mainactivity");

                /*NotificationManager notifManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Intent dest = new Intent(activity, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, dest, 0);

                Notification notification = new Notification.Builder(activity)
                        .setContentTitle("My Application")
                        .setContentText("Go to application now!")
                        .setSmallIcon(R.drawable.ic_sentiment_very_satisfied_tab)
                        .setContentIntent(pendingIntent)
                        .build();

                notifManager.notify(1, notification);*/


            }
        };


        FloatingActionButton addNewType = (FloatingActionButton) findViewById(R.id.addNewType);
        addNewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), AddNewtypeActivity.class));
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AuthenticationActivity.class));
            }
        });
        menuMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AuthenticationActivity.class));
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSummary();
            }
        });

        vf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (header) {
                    vf.setDisplayedChild(1);
                    header = !header;
                } else {
                    vf.setDisplayedChild(0);
                    header = !header;
                }

            }
        });

        btn_detail_header_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), History.class));

            }
        });
        btn_detail_header_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), History.class));

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initalObserver();
    }

    public GoogleApiClient buildApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope(Scopes.PROFILE))
                .build();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /***
     * also unregister the Content Observer, otherwise you would create a memory leak
     * and the Activity would never be garbage collected.
     */
    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().
                unregisterContentObserver(observer);
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
        Log.i(LOG_TAG, "onConnectionSuspended:" + cause);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(LOG_TAG, "onConnected");

        // Update the user interface to reflect that the user is signed in.

        // Indicate that the sign in process is complete.
        // We are signed in!
        // Retrieve some profile information to personalize our app for the user.
        currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        String name = currentUser.getDisplayName();
        if (name != null && name != "") {
            usrNameDay.setText("Hey " + Utility.subUpToSpace(name));
            usrNameMonth.setText("Hey " + Utility.subUpToSpace(name));
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.i(LOG_TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        ;
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
    /*public void updateUserName(){
        userName.setText(loginUsr);
    }*/

    public void updateHeader() {

        Float pPointMonth = SummaryProvider.getPtotalMonth();
        Float nPointMonth = SummaryProvider.getNtotalMonth();
        Float pPointPercentMonth = pPointMonth / (pPointMonth + Math.abs(nPointMonth)) * 100;

        Float pPointDay = SummaryProvider.getPTotalToday();
        Float nPointDay = SummaryProvider.getNtotalToday();
        Float pPointPercentDay = pPointDay / (pPointDay + Math.abs(nPointDay)) * 100;

        Resources res = getResources();
        //int color = res.getColor(R.color.ag_blue);
        int color = res.getColor(R.color.colorAccent);
        circleProgressBarDay.setColor(color);
        circleProgressBarDay.setProgressWithAnimation(50);
        circleProgressBarDay.setProgressWithAnimation(pPointPercentDay);
        circleProgressBarDay.setStrokeWidth(50);

        int colorMonth = res.getColor(R.color.colorAccent);
        circleProgressBarMonth.setColor(colorMonth);
        circleProgressBarMonth.setProgressWithAnimation(50);
        circleProgressBarMonth.setProgressWithAnimation(pPointPercentMonth);
        circleProgressBarMonth.setStrokeWidth(50);

        pPointM.setText(Integer.toString(Math.round(pPointMonth)));
        nPointM.setText(Integer.toString(Math.round(Math.abs(nPointMonth))));
        pPoint.setText(Utility.floatToString(pPointDay));
        nPoint.setText(Utility.floatToString(nPointDay));
    }

    //register the Content Observer
    public void initalObserver() {
        observer.contexTo = this;
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

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                if (initialX > finalX) {
                    if (vf.getDisplayedChild() == 1)
                        break;

                    vf.setInAnimation(this, R.anim.in_right);
                    vf.setOutAnimation(this, R.anim.out_left);

                    vf.showNext();
                } else {
                    if (vf.getDisplayedChild() == 0)
                        break;

                    vf.setInAnimation(this, R.anim.in_left);
                    vf.setOutAnimation(this, R.anim.out_right);
                    vf.showPrevious();
                }
                break;
        }
        return false;
    }





}
