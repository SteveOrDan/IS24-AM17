package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public class LShapeObjectiveCard extends ObjectiveCard {
    public LShapeObjectiveCard(int id) {
        super(id);
    }

    @Override
    public int calculateObjectivePoints(HashMap<PlaceableCard, Position> playArea, int[] numOfResourcesArr) {
        return 0;
    }
}
