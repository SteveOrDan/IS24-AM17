package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.exceptions.StarterCardAlreadyPlacedException;

public class StarterCard extends PlaceableCard {
    public StarterCard(int id, Side front, Side back) {
        super(CardElementType.STARTER, id, front, back);

        setCardType("StarterCard");
    }

    @Override
    public boolean hasEnoughRequiredResources(int[] numOfResourcesArr, CardSideType chosenSide) {
        return true;
    }

    @Override
    public int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr, CardSideType chosenSide) {
        return 0;
    }
}
