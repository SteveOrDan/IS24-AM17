package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class GoldenCard extends PlaceableCard{
    private boolean isPointPerResource;
    private ResourceType pointPerResourceRes;

    private boolean isPointPerCoveredCorner;

    private final HashMap<ResourceType, Integer> requiredResources;

    private final int points;

    public GoldenCard(int points, int priority, int id, Side front, Side back, HashMap<ResourceType, Integer> requiredResources) {
        super(priority, id, front, back);

        this.requiredResources = requiredResources;
        this.points = points;
    }

    public boolean isPointPerResource() {
        return isPointPerResource;
    }

    public ResourceType getPointPerResourceRes() {
        return pointPerResourceRes;
    }

    public boolean isPointPerCoveredCorner() {
        return isPointPerCoveredCorner;
    }

    public HashMap<ResourceType, Integer> getRequiredResources(){
        return requiredResources;
    }

    public int getPoints() {
        return points;
    }
}