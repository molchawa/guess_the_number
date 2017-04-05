package com.example.magda.jakatoliczba;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Madzia on 2016-12-05.
 */
public class ResultActivity extends AppCompatActivity {
    private int noOfPlayers;
    private ArrayList<Player> listOfPlayers;
    public static final String MY_PREFERENCES = "com.example.magda.jakatoliczba.PREFERENCES";
    private SharedPreferences sharedHighscoresPreferences;
    private ArrayList<Player> listOfHighscores;

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

        //dynamically created table containing players and their scores
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
            nick.setText(listOfPlayers.get(i).getNick());
            nick.setGravity(Gravity.CENTER);
            row.addView(nick);
            TextView trials = new TextView(this);
            trials.setText("" + listOfPlayers.get(i).getNumberOfTrials());
            trials.setGravity(Gravity.CENTER);
            row.addView(trials);
            result.addView(row, i + 1);
        }


        sharedHighscoresPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedHighscoresPreferences.getString("highscores", null);
        if (json == null) {
            listOfHighscores = new ArrayList<Player>();
        } else {
            Type type = new TypeToken<ArrayList<Player>>() {
            }.getType();
            listOfHighscores = gson.fromJson(json, type);
        }

        //highscores(noOfPlayers,listOfHighscores,listOfPlayers);
        Stream.of(listOfPlayers).forEach(player -> highscores(player, listOfHighscores, listOfPlayers));
        Button exitToMainMenuButton = (Button) findViewById(R.id.exitToMainMenuButton);

        exitToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedHighscoresPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedHighscoresPreferences.edit();
                Gson gson = new Gson();

                String json = gson.toJson(listOfHighscores);

                editor.putString("highscores", json);
                editor.commit();
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    public ArrayList<Player> highscores(Player player, ArrayList<Player> listh, ArrayList<Player> listp) {
        if (listh.size() < 10) {
            listh.add(player);
            Collections.sort(listh, (player1, player2) -> {
                Integer amount1 = player1.getNumberOfTrials();
                Integer amount2 = player2.getNumberOfTrials();
                return amount1.compareTo(amount2);
            });

        } else if (listh.size() >= 10) {
            if (player.getNumberOfTrials() <= listh.get(listh.size() - 1).getNumberOfTrials()) {
                listh.remove(listh.size() - 1);
                listh.add(player);
                Collections.sort(listh, (player1, player2) -> {
                    Integer amount1 = player1.getNumberOfTrials();
                    Integer amount2 = player2.getNumberOfTrials();
                    return amount1.compareTo(amount2);
                });
            }
        }


        return listh;


    };

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
                                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
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
