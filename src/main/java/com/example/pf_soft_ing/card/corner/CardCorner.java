package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.ResourceType;

public abstract class CardCorner {
    /**
     * Function check if on the corner can be placed another card
     * @return Boolean based on the dynamic type
     */
    public abstract boolean isAvailable();

    /**
     * Function that returns the resource stored on a corner.
     * Returns a resource only in ResourceCorner class, other subclasses return null
     * @return Resource stored in the card's corner
     */
    public abstract ResourceType getResource();
}
