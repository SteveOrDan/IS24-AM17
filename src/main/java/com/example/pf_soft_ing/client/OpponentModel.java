package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;

import java.util.HashMap;

public class OpponentModel {
    private String nickname;
    private int id;
    private final HashMap<Position, PlaceableCard> playArea = new HashMap<>();
    private int currMaxPriority = 0;

    public OpponentModel(String nickname, int id) {
        this.nickname = nickname;
        this.id = id;
    }

    public void setCurrMaxPriority(int currMaxPriority) {
        this.currMaxPriority = currMaxPriority;
    }

    public void addCard(Position pos, PlaceableCard card){
        playArea.put(pos, card);
    }
}
