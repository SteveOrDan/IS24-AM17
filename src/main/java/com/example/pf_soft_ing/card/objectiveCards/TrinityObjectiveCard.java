package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public class TrinityObjectiveCard extends ObjectiveCard{
    private static final int points = 3;
    public TrinityObjectiveCard(int id) {
        super(id);
    }

    @Override
    public int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int[] numOfResourcesArr) {
        int quillCount = numOfResourcesArr[ResourceType.QUILL.getValue()];
        int manuscriptCount = numOfResourcesArr[ResourceType.MANUSCRIPT.getValue()];
        int inkWellCount = numOfResourcesArr[ResourceType.INKWELL.getValue()];

        int min = quillCount;

        if (manuscriptCount < min){
            min = manuscriptCount;
        }
        else if (inkWellCount < min){
            min = inkWellCount;
        }

        return points * min;
    }
}
