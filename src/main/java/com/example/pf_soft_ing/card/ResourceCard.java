package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class ResourceCard extends PlaceableCard{
    private final int points;
    public ResourceCard(int points, int priority, int id, Side front, Side back) {
        super(priority, id, front, back);

        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}