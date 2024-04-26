package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.Side;

public class ResourceCard extends PlaceableCard{

    public final int points;

    public ResourceCard(int points, CardElementType elementType, int id, Side front, Side back) {
        super(elementType, id, front, back);

        this.points = points;
        this.cardType = "ResourceCard";
    }

    /**
     * Getter
     * @return Points given when placing the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * Checks if the player has enough resources to place the card
     * @param numOfResourcesArr Number of resources in the player area
     * @return True since a resource card doesn't require any resources to be placed
     */
    @Override
    public boolean hasEnoughRequiredResources(int[] numOfResourcesArr, Side chosenSide){
        return true;
    }

    /**
     * Calculates the points given by the card when placed
     * @param numOfCoveredCorners Number of corners covered by the card
     * @param numOfResourcesArr Array containing the number of resources of the player per type
     * @return Points given by the card
     */
    @Override
    public int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr, Side chosenSide){
        if (chosenSide.equals(front)) {
            return points;
        }
        return 0;
    }
}
