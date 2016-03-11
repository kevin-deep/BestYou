package com.bestofyou.fm.bestofyou;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bestofyou.fm.bestofyou.data.SummaryContract;
import com.bestofyou.fm.bestofyou.data.SummaryProvider;

import java.util.Scanner;

public class AddNewtypeActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    public static final String LOG_TAG = AddNewtypeActivity.class.getSimpleName();
    Switch switchButton, switchButton2;
    TextView text_new_weight, textView, textView2;
    EditText newTypeName;
    SeekBar new_weight;
    public static ContentValues values= new ContentValues();
    float popularity = 20F;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newtype);
        Bundle b = getIntent().getExtras();

        // For first switch button
        switchButton = (Switch) findViewById(R.id.switchButton);
        textView = (TextView) findViewById(R.id.textView);

        // for second switch button
        switchButton2 = (Switch) findViewById(R.id.switchButton2);


        new_weight =(SeekBar) findViewById(R.id.new_weight);
        new_weight.setOnSeekBarChangeListener(this);
        text_new_weight = (TextView)findViewById(R.id.text_new_weight);
        newTypeName = (EditText)findViewById((R.id.habit_name));

        //if this is a update event, shoot it on the screen
        if (b != null) {
            newTypeName.setText(b.getString(SummaryContract.Rubric.NAME));
            if (b.getFloat(SummaryContract.Rubric.WEIGHT)>0){
                switchButton.setChecked(true);
            }else {
                switchButton2.setChecked(true);
            }
            new_weight.setProgress(Math.abs(Math.round(b.getFloat(SummaryContract.Rubric.WEIGHT))));
            popularity = b.getFloat(SummaryContract.Rubric.POPULARITY);
        }


        switchButton.setChecked(true);
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
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToDatabase();

            }
        });

    }



    private void addToDatabase(){
        float convertToN = 1;
        if (switchButton2.isChecked()) convertToN = -1;
        float weight = Float.parseFloat(text_new_weight.getText().toString())*convertToN;
        SummaryProvider.insertRubric(this.getBaseContext(),newTypeName.getText().toString(),weight, popularity);
        this.finish();
    }
    //check seeker value
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {


        text_new_weight.setText(Integer.toString(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        text_new_weight.setText("");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        seekBar.setSecondaryProgress(seekBar.getProgress()); // set the shade of the previous value.

    }

    public boolean isInteger(String s){
        Scanner sc = new Scanner((s.trim()));
        return sc.hasNextInt();
    }





}
