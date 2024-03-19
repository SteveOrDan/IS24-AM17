package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class GoldenCard extends PlaceableCard{
    private final boolean isPointPerResource;
    private final ResourceType pointPerResourceRes;

    private final HashMap<ResourceType, Integer> requiredResources;

    private final int points;

    public GoldenCard(CardElementType elementType, int id, Side front, Side back, int points, HashMap<ResourceType, Integer> requiredResources, boolean isPointPerResource, ResourceType pointPerResourceRes) {
        super(elementType, id, front, back);

        this.requiredResources = requiredResources;
        this.points = points;
        this.isPointPerResource = isPointPerResource;
        this.pointPerResourceRes = pointPerResourceRes;
    }

    public boolean isPointPerResource() {
        return isPointPerResource;
    }

    public ResourceType getPointPerResourceRes() {
        return pointPerResourceRes;
    }

    public HashMap<ResourceType, Integer> getRequiredResources(){
        return requiredResources;
    }

    public int getPoints() {
        return points;
    }
}