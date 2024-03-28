package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;

public class ResourceCard extends PlaceableCard{
    public final int points;
    public ResourceCard(int points, CardElementType elementType, int id, Side front, Side back) {
        super(elementType, id, front, back);

        this.points = points;
        this.cardType = "ResourceCard";
    }

    /**
     * Getter
     * @return Points given when placing the card
     */
    public int getPoints() {
        return points;
    }
}