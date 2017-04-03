package com.example.magda.jakatoliczba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Magda on 02.04.2017.
 */

public class HighscoresActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES_2 = "MyPrefs2";
    private SharedPreferences sharedHighscoresPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        sharedHighscoresPreferences = getSharedPreferences(MY_PREFERENCES_2, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedHighscoresPreferences.getString("highscores", null);
        if(json!=null){
            Type type = new TypeToken<ArrayList<Player>>() {}.getType();
            ArrayList<Player> listOfHighscores = gson.fromJson(json, type);

            int noOfPlayers=listOfHighscores.size();

            TableLayout result = (TableLayout) findViewById(R.id.resultTable);

            for (int i = 0; i < noOfPlayers; i++) {
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                TextView place = new TextView(this);
                place.setGravity(Gravity.CENTER);
                place.setText("" + (i + 1) + ".");
                row.addView(place);
                TextView nick = new TextView(this);
                nick.setText(listOfHighscores.get(i).getNick());
                nick.setGravity(Gravity.CENTER);
                row.addView(nick);
                TextView trials = new TextView(this);
                trials.setText("" + listOfHighscores.get(i).getNumberOfTrials());
                trials.setGravity(Gravity.CENTER);
                row.addView(trials);
                result.addView(row, i + 1);
            }
        }

    }





}
