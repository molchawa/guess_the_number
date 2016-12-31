package com.example.magda.jakatoliczba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Madzia on 2016-12-05.
 */
public class ResultActivity extends AppCompatActivity {
    private int noOfPlayers;
    private ArrayList<Player> listOfPlayers;
    private static final String TAG = "ResultActivityLogi";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        noOfPlayers = intent.getIntExtra("numberOfPlayers", 2);
        listOfPlayers = new ArrayList<Player>();
        listOfPlayers = (ArrayList<Player>) intent.getSerializableExtra("listOfPlayers");
        //sorting list of players taking into consideration amount of trials
        Collections.sort(listOfPlayers, (player1, player2) -> {
            Integer amount1 = player1.getNumberOfTrials();
            Integer amount2 = player2.getNumberOfTrials();
            return amount1.compareTo(amount2);
        });
        for (int i = 0; i < noOfPlayers; i++) {
            Log.d(TAG, i + ". miejsce: " + listOfPlayers.get(i).getNick() + " " + listOfPlayers.get(i).getNumberOfTrials());
        }
        //dynamically created table containing players and their scores
        TableLayout result = (TableLayout) findViewById(R.id.resultTable);
        for (int i = 0; i < noOfPlayers; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView place = new TextView(this);
            place.setText("" + (i + 1) + ".");
            row.addView(place);
            TextView nick = new TextView(this);
            nick.setText(listOfPlayers.get(i).getNick());
            row.addView(nick);
            TextView trials = new TextView(this);
            trials.setText("" + listOfPlayers.get(i).getNumberOfTrials());
            row.addView(trials);
            result.addView(row, i + 1);
        }
    }
}
