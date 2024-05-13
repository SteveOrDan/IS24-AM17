package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.HashMap;

public class ResourcesCountObjectiveCard extends ObjectiveCard {

    private static final int points = 2;

    private final ResourceType resourceType;

    private final int requiredResourceCount;

    public ResourcesCountObjectiveCard(int id, ResourceType resourceType, int requiredResourceCount) {
        super(id, points);

        this.resourceType = resourceType;
        this.requiredResourceCount = requiredResourceCount;

        setObjectiveType("ResourcesCountObjectiveCard");
    }

    /**
     * Getter
     * @return Resource type of the objective card
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int[] numOfResourcesArr) {
        return points * (numOfResourcesArr[resourceType.getValue()] / requiredResourceCount);
    }
}
