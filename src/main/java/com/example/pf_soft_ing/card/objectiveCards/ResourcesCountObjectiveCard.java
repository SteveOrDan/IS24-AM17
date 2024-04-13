package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public class ResourcesCountObjectiveCard extends ObjectiveCard {

    private static final int points = 2;

    public final ResourceType resourceType;

    public final int requiredResourceCount;

    public ResourcesCountObjectiveCard(int id, ResourceType resourceType, int requiredResourceCount) {
        super(id, points);

        this.resourceType = resourceType;
        this.requiredResourceCount = requiredResourceCount;

        this.objectiveType = "ResourcesCountObjectiveCard";
    }

    @Override
    public int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int[] numOfResourcesArr) {
        return points * (numOfResourcesArr[resourceType.getValue()] / requiredResourceCount);
    }
}
