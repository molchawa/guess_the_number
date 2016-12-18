package com.example.magda.jakatoliczba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button playButton, optionsButton, rulesButton,exitButton;

    int rangeValue;
    int mode;

    public static final String MY_PREFERENCES = "MyPrefs" ;

    SharedPreferences sharedOptionsPreferences;

    /** Called when the activity is first created. */
    @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedOptionsPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        rangeValue = sharedOptionsPreferences.getInt("range", 50);
        mode = sharedOptionsPreferences.getInt("mode", 2);

        playButton = (Button) findViewById(R.id.playButton);
        optionsButton = (Button) findViewById(R.id.optionsButton);
        rulesButton = (Button) findViewById(R.id.rulesButton);
        exitButton = (Button) findViewById(R.id.exitButton);

        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AGameActivity.class);
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
                Intent i = new Intent(MainActivity.this,AboutActivity.class);
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
    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();

    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();

    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();

    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}





