package com.example.magda.jakatoliczba;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collections;
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
    public static final String MY_PREFERENCES = "com.example.magda.jakatoliczba.PREFERENCES";
    private static final String TAG = "BGameActivityLogi";
    private SharedPreferences sharedOptionsPreferences;
    //variables to control getting numbers from users
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
        // difference = ((double) maximum) * (((double) rangeValue) / (double) 100);
        Intent intent = getIntent();
        noOfPlayers = intent.getIntExtra("numberOfPlayers", 2);
        listOfPlayers = new ArrayList<Player>();
        listOfPlayers = (ArrayList<Player>) intent.getSerializableExtra("listOfPlayers");

        //it's time to start the game
        startPlayButton = (Button) findViewById(R.id.startPlayButton);

        startPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //getting range from which the number will be generated
                EditText minimumEditText = (EditText) findViewById(R.id.minEditText);

                boolean editTextProblem = false;

                if (minimumEditText.getText().toString().isEmpty()) {
                    MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.emptyEditTextToast)).show();
                    editTextProblem = true;
                } else {
                    minimum = Integer.parseInt(minimumEditText.getText().toString());
                    if (minimum < 0) {
                        MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.tooLowMinimumToast)).show();
                        editTextProblem = true;
                    }
                }

                EditText maximumEditText = (EditText) findViewById(R.id.maxEditText);

                if (maximumEditText.getText().toString().isEmpty()) {
                    MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.emptyEditTextToast)).show();
                    editTextProblem = true;
                } else {
                    maximum = Integer.parseInt(maximumEditText.getText().toString());
                    //difference to describe in which surround of number clues are being shown
                    difference = ((double) maximum) * (((double) rangeValue) / (double) 100);
                    if (maximum > 10000) {
                        MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.tooHighMaximumToast)).show();
                        editTextProblem = true;
                    }

                    if (maximum <= minimum) {
                        MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.wrongDifferenceToast)).show();
                        editTextProblem = true;
                    }

                    if (maximum - minimum < 100) {
                        MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.tooSmallDifferenceToast)).show();
                        editTextProblem = true;
                    }
                }

                if (editTextProblem == false) {
                    //sorting list of players in generated order
                    Collections.shuffle(listOfPlayers);
                    //generating a number to be guessed
                    numberRandomization(listOfPlayers, noOfPlayers, mode, minimum, maximum);

                    play(noOfPlayers, mode, listOfPlayers);
                }

            }
        });
    }


    public void numberRandomization(ArrayList<Player> list, int noOfPlayers, int m, int min, int max) {
        switch (m) {
            case 1:
                //there is one number for all players
                Random generator = new Random();
                Stream.of(listOfPlayers).forEach(player -> player.setNumber(generator.nextInt((max - min) + 1) + min));
                break;
            case 2:
                //each player has his own, generated number
                Random generator2 = new Random();
                int tempNumber = generator2.nextInt((max - min) + 1) + min;
                Stream.of(listOfPlayers).forEach(player -> player.setNumber(tempNumber));
                break;
        }
    }

    public void play(int amount, int m, final ArrayList<Player> list) {
        final TextView roundTextView = (TextView) findViewById(R.id.roundTextView);
        EditText tempGetNumberEditText = null;
        if (m == 1) {
            tempGetNumberEditText = (EditText) findViewById(R.id.getNumberEditText);
        } else if (m == 2) {
            tempGetNumberEditText = (EditText) findViewById(R.id.getNumberPasswordEditText);
        }
        final EditText getNumberEditText = tempGetNumberEditText;

        final int a = amount;
        currentPlayer = 0;
        currentRound = 1;
        finish = 0;

        roundTextView.setVisibility(View.VISIBLE);
        getNumberEditText.setVisibility(View.VISIBLE);
        //showing an information whose whose turn it is
        roundTextView.setText("Runda: " + 1 + ", Gracz: " + list.get(0).getNick());
        Button checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setVisibility(View.VISIBLE);
        //button clicked after typing in a number
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNumberEditText.getText().toString().isEmpty()) {
                    MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.emptyEditTextToast)).show();
                } else {
                    list.get(currentPlayer).setTempNumber(Integer.parseInt(getNumberEditText.getText().toString()));
                    //if number is guessed a special parameter is incremented (this parameter is sth like counter of players, who guessed a number)
                    if (checkIsNumberCorrect(list.get(currentPlayer), currentRound)) {
                        finish++;
                    }
                    //here is time to increment a current player
                    do {
                        currentPlayer++;

                        //but if it is equal to amount of players we need to make it 0 again and increment a round counter
                        if (currentPlayer == a) {
                            currentRound++;
                            currentPlayer = 0;
                        }
                        //if finish=a it means that everybody guessed their numbers
                        if (finish == a) {
                            break;
                        }
                        //current player should ommit players who have guessed their numbers (there is no need to show request for them again)
                    } while (list.get(currentPlayer).isNumberIsGuessed());
                    //if all players have guessed their numbers it is time to start new activity
                    if (finish == a) {
                        Intent i = new Intent(BGameActivity.this, ResultActivity.class);
                        i.putExtra("listOfPlayers", listOfPlayers);
                        i.putExtra("numberOfPlayers", noOfPlayers);
                        startActivity(i);
                    }
                    //if not - we have to change request
                    if (finish < a) {
                        roundTextView.setText("Runda: " + currentRound + " , gracz: " + listOfPlayers.get(currentPlayer).getNick());
                        getNumberEditText.setText("");
                    }
                }

            }
        });
    }

    public boolean checkIsNumberCorrect(Player player, int r) {
        player.setNumberOfTrials(r);
        if (player.getNumber() == player.getTempNumber()) {
            player.setNumberIsGuessed(true);

            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pat = {250, 100, 250, 100, 250, 100};
            vib.vibrate(pat, -1);

            MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.numberGuessedToast)).show();

            return true;
        } else {
            if (player.getTempNumber() < minimum) {
                MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.numberSmallerThanMinimumToast)).show();
            } else if ((player.getTempNumber() >= minimum) && (player.getTempNumber() >= (player.getNumber() - difference)) && (player.getTempNumber() < player.getNumber())) {
                MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.numberTooSmallToast)).show();
            } else if ((player.getTempNumber() <= maximum) && (player.getTempNumber() <= (player.getNumber() + difference)) && (player.getTempNumber() > player.getNumber())) {
                MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.numberTooBigToast)).show();
            } else if (player.getTempNumber() > maximum) {
                MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.numberBiggerThanMaximumToast)).show();
            } else {
                MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.numberWrongToast)).show();
            }
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //to ask whether user wants to exit the game or not
        MessagesManager.getSimplyAlertDialog(getString(R.string.questionAboutEndingText),
                getString(R.string.exitMenu), getString(R.string.yesText),
                getString(R.string.cancellingText), this,
                new DialogInterface.OnClickListener() {

                    public static final int BUTTON_POSITIVE = -1;
                    public static final int BUTTON_NEGATIVE = -2;

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case BUTTON_POSITIVE:
                                Intent intent = new Intent(BGameActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                }).show();
    }

}