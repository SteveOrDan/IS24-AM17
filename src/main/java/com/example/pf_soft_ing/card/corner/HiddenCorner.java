package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.ResourceType;

public class HiddenCorner extends CardCorner{

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public ResourceType getResource() {
        return null;
    }
}
