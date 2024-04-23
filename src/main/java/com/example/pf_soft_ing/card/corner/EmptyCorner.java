package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.card.ResourceType;

public class EmptyCorner extends VisibleCorner {

    public EmptyCorner() {
        this.cornerType = "EmptyCorner";
    }

    @Override
    public ResourceType getResource() {
        return null;
    }
}
