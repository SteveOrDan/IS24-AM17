package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.ResourceType;

public class ResourceCorner extends VisibleCorner {

    public final ResourceType resourceType;

    public ResourceCorner(ResourceType resourceType){
        this.resourceType = resourceType;
        this.cornerType = "ResourceCorner";
    }

    @Override
    public ResourceType getResource() {
        return resourceType;
    }
}
