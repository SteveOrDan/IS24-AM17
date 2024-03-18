package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class ResourceCard extends PlaceableCard{
    public ResourceCard(int points, int priority, int id, Side front, Side back, HashMap<ResourceType, Integer> requiredResources) {
        super(points, priority, id, front, back, requiredResources);
    }

    @Override
    public boolean isGolden(){
        return false;
    }
}
