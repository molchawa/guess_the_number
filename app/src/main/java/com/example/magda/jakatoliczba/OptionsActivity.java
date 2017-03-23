package com.example.magda.jakatoliczba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created by Magda on 12.11.2016.
 */
public class OptionsActivity extends AppCompatActivity implements OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {
    private SeekBar rangeSeekBar;
    private int rangeValue;
    private TextView result;
    private RadioGroup radioGroupModes;
    private int mode;
    public static final String MY_PREFERENCES = "MyPrefs";
    private SharedPreferences sharedOptionsPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sharedOptionsPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        rangeValue = sharedOptionsPreferences.getInt("range", 50);
        mode = sharedOptionsPreferences.getInt("mode", 2);

        //seekBar to choose the range (which percentage of a number will be a difference, so +/- this amount prompts like too small or too big wil be displeyed)
        rangeSeekBar = (SeekBar) findViewById(R.id.rangeSeekBar);
        result = (TextView) findViewById(R.id.resultTextView);
        rangeSeekBar.setOnSeekBarChangeListener(this);
        rangeSeekBar.setProgress(rangeValue);

        radioGroupModes = (RadioGroup) findViewById(R.id.radioGroupModes);
        radioGroupModes.setOnCheckedChangeListener(this);

        if (mode == 2) {
            radioGroupModes.check(R.id.radio_common);
        } else if (mode == 1) {
            radioGroupModes.check(R.id.radio_individual);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // change progress text label with current seekbar value
        rangeValue = progress;
        result.setText("Wybrany zakres: " + progress);
        // change action text label to changing
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    //pressing back means it is time to save shared Options preferences
    @Override
    public void onBackPressed() {

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE).edit();
        editor.putInt("range", rangeValue);
        editor.putInt("mode", mode);
        editor.commit();
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radio_individual:
                mode = 1;
                break;

            case R.id.radio_common:
                mode = 2;
                break;
        }
    }
}
