package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class GoldenCard extends PlaceableCard{
    private final boolean isPointPerResource;
    private final ResourceType pointPerResourceRes;

    private final HashMap<ResourceType, Integer> requiredResources;

    private final int points;

    public GoldenCard(CardElementType elementType, int id, Side front, Side back, int points, HashMap<ResourceType, Integer> requiredResources, boolean isPointPerResource, ResourceType pointPerResourceRes) {
        super(elementType, id, front, back);

        this.requiredResources = requiredResources;
        this.points = points;
        this.isPointPerResource = isPointPerResource;
        this.pointPerResourceRes = pointPerResourceRes;
    }

    /**
     * Getter
     * @return Boolean that says if the points given by the card are based on the number of resources
     */
    public boolean isPointPerResource() {
        return isPointPerResource;
    }

    /**
     * Getter
     * @return Number of points given for each resource the player has
     */
    public ResourceType getPointPerResourceRes() {
        return pointPerResourceRes;
    }

    /**
     * Getter
     * @return Map of resources needed to place the card
     */
    public HashMap<ResourceType, Integer> getRequiredResources(){
        return requiredResources;
    }

    /**
     * Getter
     * @return Number of points given when the card has no conditions to earn points (either 0, 3 or 5)
     */
    public int getPoints() {
        return points;
    }
}