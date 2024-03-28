package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.ResourceType;

public class HiddenCorner extends CardCorner{

    public HiddenCorner() {
        this.cornerType = "HiddenCorner";
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public ResourceType getResource() {
        return null;
    }
}
