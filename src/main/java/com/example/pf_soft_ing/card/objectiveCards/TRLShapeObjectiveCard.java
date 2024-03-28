package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public class TRLShapeObjectiveCard extends LShapeObjectiveCard {
    private static final int yDirection = 1;
    private static final int xDirection = 1;
    public TRLShapeObjectiveCard(int id, CardElementType mainElementType, CardElementType secondaryElementType) {
        super(id, mainElementType, secondaryElementType);

        this.objectiveType = "TRLShapeObjectiveCard";
    }

    @Override
    public int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int[] numOfResourcesArr) {
        return super.calculateObjectivePoints(playArea, xDirection, yDirection);
    }
}
