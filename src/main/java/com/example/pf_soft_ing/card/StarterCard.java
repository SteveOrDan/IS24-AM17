package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.exceptions.StarterCardAlreadyPlacedException;

public class StarterCard extends PlaceableCard{
    public StarterCard(int id, Side front, Side back) {
        super(CardElementType.STARTER, id, front, back);

        this.cardType = "StarterCard";
    }

    @Override
    public boolean hasEnoughRequiredResources(int[] numOfResourcesArr) {
        return false;
    }

    @Override
    public int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr) {
        return 0;
    }
}
