package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;

public class PlaceStarterCardMsg extends Message {

    private final int playerID;
    private final CardSideType side;

    public PlaceStarterCardMsg(int playerID, CardSideType side) {
        this.playerID = playerID;
        this.side = side;
    }

    /**
     * @return the playerID
     */
    public int getPlayerID() {
        return playerID;
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
