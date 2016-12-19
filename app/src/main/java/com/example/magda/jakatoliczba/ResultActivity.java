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
import java.util.Comparator;


/**
 * Created by Madzia on 2016-12-05.
 */

public class ResultActivity extends AppCompatActivity {

    int noOfPlayers;
    ArrayList<Player> listOfPlayers;


    private static final String TAG = "ResultActivityLogi";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        noOfPlayers = intent.getIntExtra("numberOfPlayers", 2);
        listOfPlayers = new ArrayList<Player>();
        listOfPlayers = (ArrayList<Player>) intent.getSerializableExtra("listOfPlayers");

        Collections.sort(listOfPlayers,(player1, player2) -> {
            Integer amount1=player1.getNumberOfTrials();
            Integer amount2=player2.getNumberOfTrials();

            return amount1.compareTo(amount2);} );

        for(int i=0;i<noOfPlayers;i++){
            Log.d(TAG,i+". miejsce: "+listOfPlayers.get(i).getNick()+" "+listOfPlayers.get(i).getNumberOfTrials());

        }

        TableLayout result = (TableLayout) findViewById(R.id.resultTable);



    }


}
