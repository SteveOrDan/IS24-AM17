package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.exceptions.MissingResourcesException;
import com.example.pf_soft_ing.exceptions.NoAdjacentCardsException;
import com.example.pf_soft_ing.exceptions.PlacingOnInvalidCornerException;
import com.example.pf_soft_ing.exceptions.PositionAlreadyTakenException;

import java.util.ArrayList;
import java.util.HashMap;

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
    public boolean hasEnoughRequiredResources(int[] numOfResourcesArr){
        return true;
    }

    /**
     * Calculates the points given by the card when placed
     * @param numOfCoveredCorners Number of corners covered by the card
     * @param numOfResourcesArr Array containing the number of resources of the player per type
     * @return Points given by the card
     */
    @Override
    public int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr){
        return points;
    }
}