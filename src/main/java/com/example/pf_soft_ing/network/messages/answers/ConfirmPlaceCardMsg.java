package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;

public class ConfirmPlaceCardMsg extends Message {

    private final int playerID;
    private final int cardID;
    private final Position pos;
    private final CardSideType side;

    public ConfirmPlaceCardMsg(int playerID, int cardID, Position pos, CardSideType side) {
        this.playerID = playerID;
        this.cardID = cardID;
        this.pos = pos;
        this.side = side;
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

    public CardSideType getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "";
    }
}
