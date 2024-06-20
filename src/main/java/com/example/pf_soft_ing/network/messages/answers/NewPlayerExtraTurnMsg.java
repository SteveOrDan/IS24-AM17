package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;

public class NewPlayerExtraTurnMsg extends Message {

    private final int cardID;
    private final int lastPlayerID;
    private final Position pos;
    private final CardSideType side;
    private final int newPlayerID;
    private final int score;

    public NewPlayerExtraTurnMsg(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int score) {
        this.cardID = cardID;
        this.lastPlayerID = lastPlayerID;
        this.pos = pos;
        this.side = side;
        this.newPlayerID = newPlayerID;
        this.score = score;
    }

    public int getCardID() {
        return cardID;
    }

    public int getLastPlayerID() {
        return lastPlayerID;
    }

    public Position getPos() {
        return pos;
    }

    public CardSideType getSide() {
        return side;
    }

    public int getNewPlayerID() {
        return newPlayerID;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "";
    }
}
