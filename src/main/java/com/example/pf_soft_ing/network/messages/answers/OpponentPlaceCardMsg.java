package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.network.messages.Message;

public class OpponentPlaceCardMsg extends Message {

    private final int playerID;
    private final int cardID;
    private final Position pos;
    private final CardSideType choosenSide;

    public OpponentPlaceCardMsg(int playerID, int cardID, Position pos, CardSideType choosenSide) {
        this.playerID = playerID;
        this.cardID = cardID;
        this.pos = pos;
        this.choosenSide = choosenSide;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getCardID() {
        return cardID;
    }

    public Position getPos() {
        return pos;
    }

    public CardSideType getChoosenSide() {
        return choosenSide;
    }

    @Override
    public String toString() {
        return null;
    }
}
