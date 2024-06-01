package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;

public class PlaceCardMsg extends Message {

    private final int playerID;
    private final int cardID;
    private final CardSideType side;
    private final Position pos;

    public PlaceCardMsg(int playerID, int cardID, CardSideType side, Position pos) {
        this.playerID = playerID;
        this.cardID = cardID;
        this.side = side;
        this.pos = pos;
    }

    /**
     * @return the playerID
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * @return the cardID
     */
    public int getCardID() {
        return cardID;
    }

    /**
     * @return the side
     */
    public CardSideType getSide() {
        return side;
    }

    /**
     * @return the pos
     */
    public Position getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return "";
    }
}
