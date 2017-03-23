package com.example.magda.jakatoliczba;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Magda on 18.11.2016.
 */
public class AGameActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    private final Context context = this;
    private int noOfPlayers = 2;
    private RadioGroup radioGroupNoOfPlayers;
    private Button playersOkButton;
    private ArrayList<Player> listOfPlayers;
    private int currentPlayer;
    private static final String TAG = "AGameActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agame);
        currentPlayer = 1;//sth like counter for players

        radioGroupNoOfPlayers = (RadioGroup) findViewById(R.id.radioGroupNoOfPlayers);
        radioGroupNoOfPlayers.setOnCheckedChangeListener(this);//to choose from 1 to 4 players; this function is overridden above

        //confirming after choosing amount of players
        playersOkButton = (Button) findViewById(R.id.playersOkButton);
        playersOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //custom dialog window for getting nicks
                final Dialog dialogForNicks = new Dialog(context);
                listOfPlayers = new ArrayList<Player>();
                dialogForNicks.setContentView(R.layout.custom_dialog_nicks);
                //dialogForNicks.setTitle("Title...");  - it doesn't work
                final TextView textForNicks = (TextView) dialogForNicks.findViewById(R.id.dialogNicksTextView);
                final EditText editTextToGetNick = (EditText) dialogForNicks.findViewById(R.id.nickEditText);
                final Button dialogButtonNicksAdd = (Button) dialogForNicks.findViewById(R.id.dialogButtonNicksADD);

                textForNicks.setText("Nick " + currentPlayer + ".z graczy");
                dialogForNicks.show();

                //confirming each nick
                dialogButtonNicksAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Player tempPlayer = new Player();//temporary item to check whether nick is repeated
                        boolean nickIsRepeated = false;
                        tempPlayer.setNick(editTextToGetNick.getText().toString());

                        //checking whether nick is repeated
                        for (int k = 0; k < listOfPlayers.size(); k++) {
                            if (tempPlayer.getNick().equals(listOfPlayers.get(k).getNick())) {
                                MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.nickRepetaedToast)).show();
                                nickIsRepeated = true;
                            }
                        }
                        if (tempPlayer.getNick().isEmpty()) {
                            MessagesManager.getToast(getApplicationContext(), 1, getString(R.string.emptyNickToast)).show();
                            nickIsRepeated = true;
                        }
                        //if not we can add it to list of players
                        if (!nickIsRepeated) {
                            listOfPlayers.add(tempPlayer);
                            currentPlayer++;
                        }

                        //while adding last nick we can display different button
                        if (currentPlayer == noOfPlayers) {
                            dialogButtonNicksAdd.setText("Zakończ");
                        }


                        //if we gather all nicks we can close  the dialog window and start new activity
                        if (currentPlayer > noOfPlayers) {
                            dialogForNicks.dismiss();
                            Intent i = new Intent(AGameActivity.this, BGameActivity.class);
                            i.putExtra("listOfPlayers", listOfPlayers);
                            i.putExtra("numberOfPlayers", noOfPlayers);
                            startActivity(i);
                        }

                        //here is setting new text for dialog window (according to player whose nick is picking up
                        textForNicks.setText("Nick " + currentPlayer + ".z graczy");
                        editTextToGetNick.setText("");
                    }
                });
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radio_onePlayer:
                noOfPlayers = 1;
                break;
            case R.id.radio_twoPlayers:
                noOfPlayers = 2;
                break;
            case R.id.radio_threePlayers:
                noOfPlayers = 3;
                break;
            case R.id.radio_fourPlayers:
                noOfPlayers = 4;
                break;
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
                                Intent intent = new Intent(AGameActivity.this, MainActivity.class);
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