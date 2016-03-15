package com.bestofyou.fm.bestofyou;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
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
        Bundle b = getIntent().getExtras();
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


        low.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                low.setPressed(true);
                high.setPressed(false);
                netural.setPressed(false);
                updateHabitType();
                snakeDisply(v,"+1 each hour");
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
                snakeDisply(v, "+3 each hour");
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
                snakeDisply(v, "+2 each hour");
                return true;
            }
        });

        favor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                favor.setPressed(true);
                hateful.setPressed(false);
                snakeDisply(v, "I like this habit");
                updateFAB();
                return true;
            }
        });
        hateful.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hateful.setPressed(true);
                favor.setPressed(false);
                snakeDisply(v, "I dislike this habit");
                updateFAB();
                return true;
            }
        });


        /*switchButton.setChecked(true);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    switchButton2.setChecked(false);
                } else {
                    switchButton2.setChecked(true);
                }
            }
        });

        textView2 = (TextView) findViewById(R.id.textView2);

        if (switchButton.isChecked()) {
            switchButton2.setChecked(false);
        } else {
            switchButton2.setChecked(true);
        }
        switchButton2.setChecked(false);
        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    switchButton.setChecked(false);
                } else {
                    switchButton.setChecked(true);
                }
            }
        });

        if (switchButton2.isChecked()) {
            switchButton.setChecked(false);

        } else {
            switchButton.setChecked(true);
        }*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void snakeDisply(View view, String message){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }





}
