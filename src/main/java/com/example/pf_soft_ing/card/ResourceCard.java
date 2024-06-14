package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;

public class ResourceCard extends PlaceableCard{

    private final int points;

    public ResourceCard(int points, CardElementType elementType, int id, Side front, Side back) {
        super(elementType, id, front, back);

        this.points = points;
        setCardType("ResourceCard");
    }

    /**
     * Getter
     * @return Standard points given when placing the card
     */
    public int getPoints() {
        return points;
    }

    /**
     * Checks if the player has enough resources to place the card
     * @param numOfResourcesArr Array containing the number of resources of the player ordered by type
     * @return Always true, since a resource card doesn't require any resources to be placed
     */
    @Override
    public boolean hasEnoughRequiredResources(int[] numOfResourcesArr, CardSideType chosenSide){
        return true;
    }

    /**
     * Calculates the points given by the card when placed
     * @param numOfCoveredCorners Number of corners covered when placing the card
     * @param numOfResourcesArr Array containing the number of resources of the player ordered by type
     * @return Points given by the card
     */
    @Override
    public int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr, CardSideType chosenSide){
        if (chosenSide.equals(CardSideType.FRONT)) {
            return points;
        }
        return 0;
    }
}
