package com.example.magda.jakatoliczba;

import java.io.Serializable;

/**
 * Created by Magda on 28.11.2016.
 */
public class Player implements Serializable {
    private String nick;
    private int score;
    private int numberOfTrials;
    private int number;
    private int tempNumber;
    private boolean numberIsGuessed;

    public Player(){
        nick="";
        numberOfTrials=0;
        number=0;
        tempNumber=0;
        score=0;
        setNumberIsGuessed(false);

    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    public void setNumberOfTrials(int numberOfTrials) {
        this.numberOfTrials = numberOfTrials;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTempNumber() {
        return tempNumber;
    }

    public void setTempNumber(int tempNumber) {
        this.tempNumber = tempNumber;
    }

    public boolean isNumberIsGuessed() {
        return numberIsGuessed;
    }

    public void setNumberIsGuessed(boolean numberIsGuessed) {
        this.numberIsGuessed = numberIsGuessed;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
