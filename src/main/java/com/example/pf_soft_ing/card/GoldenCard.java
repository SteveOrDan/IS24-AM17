package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class GoldenCard extends PlaceableCard{

    private static final int pointsPerResource = 1;
    private static final int pointsPerCoveredCorner = 2;

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

        setCardType("GoldenCard");
    }

    /**
     * Getter
     * @return Number of points given when the card has no conditions to earn points (either 0, 3 or 5)
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter
     * @return Resource that gives points when placed
     */
    public ResourceType getPointPerResourceRes() {
        return pointPerResourceRes;
    }

    /**
     * Getter
     * @return Map of required resources to place the card
     */
    public HashMap<ResourceType, Integer> getRequiredResources() {
        return requiredResources;
    }

    /**
     * Getter
     * @return True if the card gives points per resource
     */
    public boolean isPointPerResource() {
        return isPointPerResource;
    }

    /**
     * Checks if the player has enough resources to place the card
     * @param numOfResourcesArr Array containing the number of resources of the player per type
     * @return True if the player has enough resources to place the card
     */
    @Override
    public boolean hasEnoughRequiredResources(int[] numOfResourcesArr, CardSideType chosenSide){
        if (chosenSide.equals(CardSideType.FRONT)) {
            for (ResourceType res : requiredResources.keySet()) {
                if (numOfResourcesArr[res.getValue()] < requiredResources.get(res)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calculates the points given by the card when placed
     * @param numOfCoveredCorners Number of corners covered by the card
     * @param numOfResourcesArr Array containing the number of resources of the player per type
     * @return Points given by the card placement
     */
    @Override
    public int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr, CardSideType currSide){
        if (currSide.equals(CardSideType.FRONT)) {
            if (points == 0) {
                if (isPointPerResource) {
                    return numOfResourcesArr[pointPerResourceRes.getValue()] * pointsPerResource;
                } else {
                    return numOfCoveredCorners * pointsPerCoveredCorner;
                }
            }
            return points;
        }
        return 0;
    }
}
