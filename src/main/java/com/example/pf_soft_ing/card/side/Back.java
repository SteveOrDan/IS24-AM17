package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.ResourceType;

import java.util.List;

public class Back extends Side{
    public List<ResourceType> permanentResources;

    public Back(List<ResourceType> permanentResources){
        this.permanentResources = permanentResources;
    }
}
