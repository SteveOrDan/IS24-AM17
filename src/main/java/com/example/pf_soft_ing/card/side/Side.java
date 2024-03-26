package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;

import java.util.ArrayList;
import java.util.List;

public abstract class Side {
    private final CardCorner BLCorner;
    private final CardCorner BRCorner;
    private final CardCorner TLCorner;
    private final CardCorner TRCorner;

    public Side(CardCorner BLCorner, CardCorner BRCorner, CardCorner TLCorner, CardCorner TRCorner) {
        this.BLCorner = BLCorner;
        this.BRCorner = BRCorner;
        this.TLCorner = TLCorner;
        this.TRCorner = TRCorner;
    }

    public CardCorner getBLCorner() {
        return BLCorner;
    }

    public CardCorner getBRCorner() {
        return BRCorner;
    }

    public CardCorner getTLCorner() {
        return TLCorner;
    }

    public CardCorner getTRCorner() {
        return TRCorner;
    }

    public List<ResourceType> getResources(){
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
