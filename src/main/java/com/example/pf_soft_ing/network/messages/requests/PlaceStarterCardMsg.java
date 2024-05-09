package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;

public class PlaceStarterCardMsg extends Message {

    private final CardSideType side;

    public PlaceStarterCardMsg(CardSideType side) {
        this.side = side;
    }

    /**
     * @return the side
     */
    public CardSideType getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "";
    }
}
