package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.card.PlaceableCard;
import javafx.geometry.Pos;

import java.util.HashMap;

public abstract class ObjectiveCard {
    private final int id;

    public ObjectiveCard(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract int calculateObjectivePoints(HashMap<PlaceableCard, Position> playArea, int[] numOfResourcesArr);
}