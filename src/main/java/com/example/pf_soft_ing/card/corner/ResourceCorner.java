package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.ResourceType;

public class ResourceCorner extends VisibleCorner {
    private final ResourceType resourceType;

    public ResourceCorner(ResourceType resourceType){
        this.resourceType = resourceType;
    }

    @Override
    public ResourceType getResource() {
        return resourceType;
    }
}
