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

    private int minimum = 1;
    private int maximum = 1000000;
    private int noOfPlayers;
    private int rangeValue;
    private int mode;
    private double difference;
    private ArrayList<Player> listOfPlayers;
    private Button startPlayButton;
    public static final String MY_PREFERENCES = "MyPrefs";
    private static final String TAG = "BGameActivityLogi";
    private SharedPreferences sharedOptionsPreferences;
    //variables to control gettin numbers from users
    private int currentPlayer;
    private int currentRound;
    private int finish;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgame);

        sharedOptionsPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        rangeValue = sharedOptionsPreferences.getInt("range", 50);
        mode = sharedOptionsPreferences.getInt("mode", 2);
        //difference to describe in which surround of number clues are being shown
        difference = ((double)maximum) * (((double)rangeValue) / (double) 100);
        Intent intent = getIntent();
        noOfPlayers = intent.getIntExtra("numberOfPlayers", 2);
        listOfPlayers = new ArrayList<Player>();
        listOfPlayers = (ArrayList<Player>) intent.getSerializableExtra("listOfPlayers");

        //it's time to start the game
        startPlayButton = (Button) findViewById(R.id.startPlayButton);
        startPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting range from which the number will be generated
                EditText minimumEditText = (EditText) findViewById(R.id.minEditText);
                minimum = Integer.parseInt(minimumEditText.getText().toString());
                EditText maximumEditText = (EditText) findViewById(R.id.maxEditText);
                maximum = Integer.parseInt(maximumEditText.getText().toString());

                //sorting list of players in generated order
                listOfPlayers = sortingPlayers(listOfPlayers, noOfPlayers);
                //generating a number to be guessed
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
        ArrayList<Integer> numbersForOrder = new ArrayList<Integer>();
        //generating new order
        Random generator = new Random();
        for (int i = 0; i < amountOfPlayers; i++) {
            numbersForOrder.add(i);
        }
        //sorting list of players
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
                //there is one number for all players
                for (int i = 0; i < noOfPlayers; i++) {
                    Random generator = new Random();
                    list.get(i).setNumber(generator.nextInt((max - min) + 1) + min);
                }
                break;
            case 2:
                //each player has his own, generated number
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

        roundTextView.setVisibility(View.VISIBLE);
        getNumberEditText.setVisibility(View.VISIBLE);
        //showing an information whose whose turn it is
        roundTextView.setText("Runda: "+1+", Gracz: "+list.get(0).getNick());
        Button checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setVisibility(View.VISIBLE);
        //button clicked after typing in a number
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    list.get(currentPlayer).setTempNumber(Integer.parseInt(getNumberEditText.getText().toString()));
                    //if number is guessed a special parameter is incremented (this parameter is sth like counter of players, who guessed a number)
                    if(checkIsNumberCorrect(list.get(currentPlayer),currentRound)){
                        finish++;
                    }
                //here is time to increment a current player
                do{
                    currentPlayer++;

                    //but if it is equal to amount of players we need to make it 0 again and increment a round counter
                    if(currentPlayer==a) {
                        currentRound++;
                        currentPlayer=0;
                    }
                    //if finish=a it means that everybody guessed their numbers
                    if(finish==a){
                        break;
                    }
                //current player should ommit players who have guessed their numbers (there is no need to show request for them again)
                }while(list.get(currentPlayer).isNumberIsGuessed());
                //if all players have guessed their numbers it is time to start new activity
                if(finish==a){
                    Intent i = new Intent(BGameActivity.this,ResultActivity.class);
                    i.putExtra("listOfPlayers",listOfPlayers);
                    i.putExtra("numberOfPlayers", noOfPlayers);
                    startActivity(i);
                }
                //if not - we have to change request
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
