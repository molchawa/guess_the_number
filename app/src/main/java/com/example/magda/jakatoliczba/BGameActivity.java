package com.example.magda.jakatoliczba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Magda on 20.11.2016.
 */
public class BGameActivity extends AppCompatActivity {

    int minimum = 1;
    int maximum = 1000000;
    int noOfPlayers;
    int rangeValue;
    int mode;

    double difference;

    ArrayList<Player> listOfPlayers;

    Button startPlayButton;

    public static final String MY_PREFERENCES = "MyPrefs";
    private static final String TAG = "BGameActivityLogi";

    SharedPreferences sharedOptionsPreferences;

    int currentPlayer;
    int currentRound;
    int finish;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgame);

        sharedOptionsPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        rangeValue = sharedOptionsPreferences.getInt("range", 50);
        mode = sharedOptionsPreferences.getInt("mode", 2);

        difference = ((double)maximum) * (((double)rangeValue) / (double) 100);

        Intent intent = getIntent();
        noOfPlayers = intent.getIntExtra("numberOfPlayers", 2);
        listOfPlayers = new ArrayList<Player>();
        listOfPlayers = (ArrayList<Player>) intent.getSerializableExtra("listOfPlayers");


        startPlayButton = (Button) findViewById(R.id.startPlayButton);

        startPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText minimumEditText = (EditText) findViewById(R.id.minEditText);
                minimum = Integer.parseInt(minimumEditText.getText().toString());

                EditText maximumEditText = (EditText) findViewById(R.id.maxEditText);
                maximum = Integer.parseInt(maximumEditText.getText().toString());

                listOfPlayers = sortingPlayers(listOfPlayers, noOfPlayers);
                numberRandomization(listOfPlayers, noOfPlayers, mode, minimum, maximum);

                for (int i = 0; i < noOfPlayers; i++) {
                    Log.d(TAG, "Gracz " + (i + 1) + " " + listOfPlayers.get(i).getNumber());
                }
                play(noOfPlayers,mode,listOfPlayers);


            }
        });
    }


    public ArrayList<Player> sortingPlayers(ArrayList<Player> list, int amountOfPlayers) {
        ArrayList<Player> templistOfPlayers = new ArrayList<Player>();
        Player player;

        ArrayList<Integer> numbersForOrder = new ArrayList<Integer>();
        Random generator = new Random();

        for (int i = 0; i < amountOfPlayers; i++) {
            numbersForOrder.add(i);
        }
        for (int i = 0; i < amountOfPlayers; i++) {
            int order = generator.nextInt(numbersForOrder.size());
            templistOfPlayers.add(i, list.get(numbersForOrder.get(order)));
            numbersForOrder.remove(order);
        }

        return templistOfPlayers;

    }

    public void numberRandomization(ArrayList<Player> list, int noOfPlayers, int m, int min, int max) {
        switch (m) {
            case 1:
                for (int i = 0; i < noOfPlayers; i++) {
                    Random generator = new Random();
                    list.get(i).setNumber(generator.nextInt((max - min) + 1) + min);
                }
                break;
            case 2:
                Random generator = new Random();
                int tempNumber = generator.nextInt((max - min) + 1) + min;
                for (int i = 0; i < noOfPlayers; i++) {

                    list.get(i).setNumber(tempNumber);
                }
                break;
        }
    }


    public void play(int amount, int m, final ArrayList<Player> list) {

        final TextView roundTextView = (TextView) findViewById(R.id.roundTextView);
        final EditText getNumberEditText = (EditText) findViewById(R.id.getNumberEditText);
        final int a=amount;

        currentPlayer=0;
        currentRound=1;
        finish=0;

        Log.d(TAG,"jestem w play");

        roundTextView.setText("Runda: "+1+", Gracz: "+list.get(0).getNick());

        Button checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                    list.get(currentPlayer).setTempNumber(Integer.parseInt(getNumberEditText.getText().toString()));

                    if(checkIsNumberCorrect(list.get(currentPlayer),currentRound)){
                        finish++;
                    }



                do{
                    currentPlayer++;
                    if(currentPlayer==a) {
                        currentRound++;
                        currentPlayer=0;
                    }
                    if(finish==a){
                        break;
                    }
                }while(list.get(currentPlayer).isNumberIsGuessed());
                if(finish==a){
                    Intent i = new Intent(BGameActivity.this,ResultActivity.class);
                    i.putExtra("listOfPlayers",listOfPlayers);
                    i.putExtra("numberOfPlayers", noOfPlayers);
                    startActivity(i);
                }

                if(finish<a){
                    roundTextView.setText("Runda: " + currentRound + " , gracz: " + listOfPlayers.get(currentPlayer).getNick());
                }





            }

        });

    }

    public boolean checkIsNumberCorrect(Player player,int r) {

        player.setNumberOfTrials(r);
        if (player.getNumber() == player.getTempNumber()) {
            player.setNumberIsGuessed(true);
            Log.d(TAG, "Zgadłeś :)");
            return true;

        }
        else {


            if ((player.getTempNumber() >= (player.getNumber() - difference)) && (player.getTempNumber() < player.getNumber())) {
                Log.d(TAG, "Liczba mniejsza od wylosowanej");
            } else if ((player.getTempNumber() <= (player.getNumber() + difference)) && (player.getTempNumber() > player.getNumber())) {
                Log.d(TAG, "Liczba większa od wylosowanej");
            } else {

                Log.d(TAG, "Niestety, nie udało się");
            }

            return false;
        }

    }
}
