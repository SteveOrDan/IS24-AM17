package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;

public class RankingMsg extends Message {

    private final int lastPlayerID;
    private final int cardID;
    private final Position pos;
    private final CardSideType side;
    private final String[] nicknames;
    private final int[] scores;
    private final int[] numOfSecretObjectives;

    public RankingMsg(int lastPlayerID, int cardID, Position pos, CardSideType side, String[] nicknames, int[] scores, int[] numOfSecretObjectives) {
        this.lastPlayerID = lastPlayerID;
        this.cardID = cardID;
        this.pos = pos;
        this.side = side;
        this.nicknames = nicknames;
        this.scores = scores;
        this.numOfSecretObjectives = numOfSecretObjectives;
    }

    public String[] getNicknames() {
        return nicknames;
    }

    public int[] getScores() {
        return scores;
    }

    public int[] getNumOfSecretObjectives() {
        return numOfSecretObjectives;
    }

    public int getLastPlayerID() {
        return lastPlayerID;
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
        return "This is a ranking message.";
    }
}
