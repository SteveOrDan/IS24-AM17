package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public abstract class ObjectiveCard {

    public final int id;
    public final int points;
    public String objectiveType;

    public ObjectiveCard(int id, int points) {
        this.id = id;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    /**
     * Function to calculate the number of points given to a player based on the type of objective card
     * @param playArea Player's play area to get placed cards from
     * @param numOfResourcesArr Player's array with the number of each resource
     * @return Number of points given by the objective card
     */
    public abstract int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int[] numOfResourcesArr);
}
