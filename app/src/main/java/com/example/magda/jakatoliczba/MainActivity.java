package com.example.magda.jakatoliczba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button playButton, optionsButton, rulesButton, exitButton;
    private int rangeValue;
    private int mode;
    public static final String MY_PREFERENCES = "MyPrefs";
    private SharedPreferences sharedOptionsPreferences;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared options preferences to keep rangeValue and mode, because it is the same for all activities
        sharedOptionsPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        rangeValue = sharedOptionsPreferences.getInt("range", 50);
        mode = sharedOptionsPreferences.getInt("mode", 2);

        playButton = (Button) findViewById(R.id.playButton);
        optionsButton = (Button) findViewById(R.id.optionsButton);
        rulesButton = (Button) findViewById(R.id.rulesButton);
        exitButton = (Button) findViewById(R.id.exitButton);

        //starting various activities (in main activity - first screen-there is only a menu
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AGameActivity.class);
                startActivity(i);
            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(intent);
            }
        });
        rulesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}





