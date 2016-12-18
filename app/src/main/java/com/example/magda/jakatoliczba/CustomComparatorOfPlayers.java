package com.example.magda.jakatoliczba;

import java.util.Comparator;

/**
 * Created by Madzia on 2016-12-17.
 */

public class CustomComparatorOfPlayers implements Comparator<Player> {
    @Override
    public int compare(Player player1, Player player2) {
        Integer amount1=player1.getNumberOfTrials();
        Integer amount2=player2.getNumberOfTrials();

        return amount1.compareTo(amount2);
    }
}
