package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;

import java.util.ArrayList;
import java.util.List;

public abstract class Side {

    public String sideType;
    public final CardCorner BLCorner;
    public final CardCorner BRCorner;
    public final CardCorner TLCorner;
    public final CardCorner TRCorner;

    public Side(CardCorner BLCorner, CardCorner BRCorner, CardCorner TLCorner, CardCorner TRCorner) {
        this.BLCorner = BLCorner;
        this.BRCorner = BRCorner;
        this.TLCorner = TLCorner;
        this.TRCorner = TRCorner;
    }

    /**
     * Getter
     * @return Bottom left corner of the card
     */
    public CardCorner getBLCorner() {
        return BLCorner;
    }

    /**
     * Getter
     * @return Bottom right corner of the card
     */
    public CardCorner getBRCorner() {
        return BRCorner;
    }

    /**
     * Getter
     * @return Top left corner of the card
     */
    public CardCorner getTLCorner() {
        return TLCorner;
    }

    /**
     * Getter
     * @return Top right corner of the card
     */
    public CardCorner getTRCorner() {
        return TRCorner;
    }

    /**
     * Checks all corners of the card and returns the resources on the side
     * @return List of resources on the side
     */
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
