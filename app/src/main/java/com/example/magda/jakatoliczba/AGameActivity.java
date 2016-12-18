package com.example.magda.jakatoliczba;

import android.app.Dialog;
import android.content.Context;
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

    final Context context = this;

    int noOfPlayers = 2;
    RadioGroup radioGroupNoOfPlayers;
    Button playersOkButton;
    ArrayList<Player> listOfPlayers;
    int currentPlayer;


    private static final String TAG = "AGameActivity";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agame);

        currentPlayer = 1;
        radioGroupNoOfPlayers = (RadioGroup) findViewById(R.id.radioGroupNoOfPlayers);
        radioGroupNoOfPlayers.setOnCheckedChangeListener(this);

        playersOkButton = (Button) findViewById(R.id.playersOkButton);

        playersOkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialogForNicks = new Dialog(context);
                listOfPlayers = new ArrayList<Player>();
                dialogForNicks.setContentView(R.layout.custom_dialog_nicks);
                //dialogForNicks.setTitle("Title...");

                // set the custom dialog components - text, image and button
                final TextView textForNicks = (TextView) dialogForNicks.findViewById(R.id.dialogNicksTextView);
                final EditText editTextToGetNick = (EditText) dialogForNicks.findViewById(R.id.nickEditText);
                //text.setText("Android custom dialog example!");

                final Button dialogButtonNicksAdd = (Button) dialogForNicks.findViewById(R.id.dialogButtonNicksADD);
                // if button is clicked, close the custom dialog

                textForNicks.setText("Nick " + currentPlayer + ".z graczy");
                dialogForNicks.show();
                dialogButtonNicksAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Player tempPlayer=new Player();
                        boolean nickIsRepeated=false;

                        tempPlayer.setNick(editTextToGetNick.getText().toString());


                                for(int k=0;k<listOfPlayers.size();k++){

                                    if(tempPlayer.getNick().equals(listOfPlayers.get(k).getNick())){
                                        Log.d(TAG,"niestety powtórzono nicka");
                                        nickIsRepeated=true;

                                    }
                                }

                        if(!nickIsRepeated){
                            listOfPlayers.add(tempPlayer);
                            currentPlayer++;
                        }


                        if(currentPlayer==noOfPlayers){
                            dialogButtonNicksAdd.setText("Zakończ");
                        }
                        if(currentPlayer > noOfPlayers) {

                            dialogForNicks.dismiss();


                            Intent i = new Intent(AGameActivity.this,BGameActivity.class);
                            i.putExtra("listOfPlayers",listOfPlayers);
                            i.putExtra("numberOfPlayers", noOfPlayers);
                            startActivity(i);
                        }
                        textForNicks.setText("Nick " + currentPlayer + ".z graczy");
                        editTextToGetNick.setText("");



                    }
                });


            }

        });


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();


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
}
