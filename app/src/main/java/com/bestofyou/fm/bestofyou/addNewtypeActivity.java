package com.bestofyou.fm.bestofyou;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;

import java.util.Scanner;

public class AddNewtypeActivity extends AppCompatActivity{
    public static final String LOG_TAG = AddNewtypeActivity.class.getSimpleName();

    TextView text_add_new_type;
    EditText newTypeName;
    Button low, netural, high;
    ImageButton favor, hateful;
    LinearLayout priority,habit_type;
    FloatingActionButton fab;

    public static ContentValues values= new ContentValues();
    float popularity = 20F;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newtype);
        final Bundle b = getIntent().getExtras();
        newTypeName = (EditText)findViewById((R.id.habit_name));
        text_add_new_type = (TextView) findViewById(R.id.text_add_new_type);
        priority = (LinearLayout) findViewById(R.id.priority);
        habit_type = (LinearLayout) findViewById(R.id.habit_type);
        low = (Button)findViewById(R.id.low);
        high = (Button)findViewById(R.id.high);
        netural = (Button)findViewById(R.id.netural);
        favor = (ImageButton)findViewById(R.id.favor);
        hateful = (ImageButton)findViewById(R.id.hateful);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_new_habit);

        newTypeName.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
               if (s.length()>0) {
                   text_add_new_type.setVisibility(View.VISIBLE);
                   priority.setVisibility(View.VISIBLE);
                   if (updateHabitType()&&updateFAB()) fab.setVisibility(View.VISIBLE);
               }
                else {
                   fab.setVisibility(View.GONE);
               }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        newTypeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        low.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                low.setPressed(true);
                high.setPressed(false);
                netural.setPressed(false);
                updateFAB();
                updateHabitType();
                Utility.snakeDisplay(v, "+1 point each hour");
                hideKeyboard(v);
                return true;
            }
        });
        high.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                high.setPressed(true);
                low.setPressed(false);
                netural.setPressed(false);
                updateHabitType();
                hideKeyboard(v);
                updateFAB();
                Utility.snakeDisplay(v, "+3 points each hour");
                return true;
            }
        });
        netural.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                netural.setPressed(true);
                low.setPressed(false);
                high.setPressed(false);
                updateHabitType();
                updateFAB();
                hideKeyboard(v);
                Utility.snakeDisplay(v, "+2 points each hour");
                return true;
            }
        });

        favor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                favor.setPressed(true);
                hateful.setPressed(false);
                Utility.snakeDisplay(v, "I like this habit");
                updateFAB();
                hideKeyboard(v);
                return true;
            }
        });
        hateful.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hateful.setPressed(true);
                favor.setPressed(false);
                Utility.snakeDisplay(v, "I dislike this habit");
                updateFAB();
                hideKeyboard(v);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b!=null) {
                    int rowId = b.getInt(SummaryContract.Rubric._ID);
                    String[] mSelectionArgs = {Integer.toString(rowId)};
                    String mSelectionClause = SummaryContract.Rubric._ID + " =?" ;
                    getContentResolver().delete(SummaryContract.Rubric.CONTENT_URI,
                            mSelectionClause,
                            mSelectionArgs
                    );
                }
                addToDatabase();
            }
        });

        //if this is a update event, shoot it on the screen
        if (b != null) {
            newTypeName.setText(b.getString(SummaryContract.Rubric.NAME));
            int passedWeight =Math.abs(Math.round(b.getFloat(SummaryContract.Rubric.WEIGHT)));
            if (passedWeight == 3F) {
                high.setPressed(true);
            }else if(passedWeight == 2F){
                netural.setPressed(true);
            }else {
                low.setPressed(true);
            }
            if (b.getFloat(SummaryContract.Rubric.WEIGHT)>0){
                favor.setPressed(true);
                updateHabitType();
            }else {
                hateful.setPressed(true);
                updateHabitType();
            }
            //new_weight.setProgress(Math.abs(Math.round(b.getFloat(SummaryContract.Rubric.WEIGHT))));
            popularity = b.getFloat(SummaryContract.Rubric.POPULARITY);
            updateFAB();
        }

    }
    private boolean updateHabitType(){
        if (low.isPressed()||high.isPressed()||netural.isPressed()){
            habit_type.setVisibility(View.VISIBLE);
            return true;
        }
        else {
            return false;
        }
    }

    private  boolean updateFAB(){
        if (updateHabitType()&&(favor.isPressed()||hateful.isPressed())) {
            fab.setVisibility(View.VISIBLE);
            return true;
        }
        else {
            return false;
        }
    }

    private void addToDatabase(){
        float convertToN = 1;
        float weight;
        if (hateful.isPressed()) convertToN = -1;

        if (low.isPressed()) weight = 1;
        else if (netural.isPressed()) weight = 2;
        else weight =3;

        SummaryProvider.insertRubric(this.getBaseContext(),newTypeName.getText().toString(),weight*convertToN, popularity);
        this.finish();
    }

    public boolean isInteger(String s){
        Scanner sc = new Scanner((s.trim()));
        return sc.hasNextInt();
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }





}
