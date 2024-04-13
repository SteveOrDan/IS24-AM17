package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public class TRBLDiagonalObjectiveCard extends DiagonalObjectiveCard{

    private static final int direction = 1;

    public TRBLDiagonalObjectiveCard(int id, CardElementType elementType) {
        super(id, elementType);

        this.objectiveType = "TRBLDiagonalObjectiveCard";
    }

    @Override
    public int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int[] numOfResourcesArr) {
        return super.calculateObjectivePoints(playArea, direction);
    }
}
