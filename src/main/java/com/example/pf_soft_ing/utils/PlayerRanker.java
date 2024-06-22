package com.example.pf_soft_ing.utils;

import com.example.pf_soft_ing.mvc.model.player.PlayerModel;

import java.util.Comparator;

public class PlayerRanker implements Comparator<PlayerModel> {
    @Override
    public int compare(PlayerModel p1, PlayerModel p2) {
        if (p1.getCurrScore() > p2.getCurrScore()) {
            return -1;
        }
        else if (p1.getCurrScore() < p2.getCurrScore()) {
            return 1;
        }
        else {
            return -Integer.compare(p1.getNumOfCompletedObjectives(), p2.getNumOfCompletedObjectives());
        }
    }
}
