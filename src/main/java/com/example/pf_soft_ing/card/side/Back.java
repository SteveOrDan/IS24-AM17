package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;

import java.util.List;

public class Back extends Side{
    public List<ResourceType> permanentResources;

    public Back(CardCorner BLCorner, CardCorner BRCorner, CardCorner TLCorner, CardCorner TRCorner, List<ResourceType> permanentResources) {
        super(BLCorner, BRCorner, TLCorner, TRCorner);
        this.permanentResources = permanentResources;
        this.sideType = "Back";
    }

    @Override
    public List<ResourceType> getResources() {
        List<ResourceType> res = super.getResources();
        res.addAll(permanentResources);
        return res;
    }
}
