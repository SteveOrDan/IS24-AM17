package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.card.corner.CardCorner;

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
}
