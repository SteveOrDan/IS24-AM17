package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class GoldenCard extends PlaceableCard{
    private boolean isPointPerResource;
    private ResourceType pointPerResourceRes;

    private boolean isPointPerCoveredCorner;

    public GoldenCard(int points, int priority, int id, Side front, Side back, HashMap<ResourceType, Integer> requiredResources) {
        super(points, priority, id, front, back, requiredResources);
    }

    @Override
    public boolean isGolden(){
        return true;
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
}
