package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;

import java.util.ArrayList;
import java.util.List;

public class Front extends Side{
    public Front(CardCorner BLCorner, CardCorner BRCorner, CardCorner TLCorner, CardCorner TRCorner) {
        super(BLCorner, BRCorner, TLCorner, TRCorner);
    }

    @Override
    public List<ResourceType> getResources() {
        List<ResourceType> res = new ArrayList<>();

        ResourceType BLResource = getBLCorner().getResource();
        ResourceType BRResource = getBRCorner().getResource();
        ResourceType TLResource = getTLCorner().getResource();
        ResourceType TRResource = getTRCorner().getResource();

        if (BLResource != null){
            res.add(BLResource);
        }
        if (BRResource != null){
            res.add(BRResource);
        }
        if (TLResource != null){
            res.add(TLResource);
        }
        if (TRResource != null){
            res.add(TRResource);
        }

        return res;
    }
}
