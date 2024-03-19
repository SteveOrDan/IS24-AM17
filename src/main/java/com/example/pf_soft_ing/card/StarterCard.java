package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.Side;

public class StarterCard extends PlaceableCard{
    public StarterCard(int id, Side front, Side back) {
        super(CardElementType.STARTER, id, front, back);
    }
}