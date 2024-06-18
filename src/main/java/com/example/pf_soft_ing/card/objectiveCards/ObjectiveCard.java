package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public abstract class ObjectiveCard {

    private final int id;
    private final int points;
    private String objectiveType;

    public ObjectiveCard(int id, int points) {
        this.id = id;
        this.points = points;
    }

    /**
     * Getter
     * @return ID of the objective card
     */
    public int getID() {
        return id;
    }

    /**
     * Getter
     * @return Points given by the objective card
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setter
     * @param objectiveType New objective type
     */
    public void setObjectiveType(String objectiveType) {
        this.objectiveType = objectiveType;
    }

    /**
     * Calculates the number of points given to a player based on the type of objective card
     * @param playArea Player's play area to get placed cards from
     * @param numOfResourcesArr Player's array with the number of each resource
     * @return Number of points given by the objective card
     */
    public abstract int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int[] numOfResourcesArr);
}
