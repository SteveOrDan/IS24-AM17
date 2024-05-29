package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.network.messages.Message;

public class UndoCardPlacementMsg extends Message {
    private final int playerID;
    private final Position position;
    private final int score;

    public UndoCardPlacementMsg(int playerID, Position position, int score) {
        this.playerID = playerID;
        this.position = position;
        this.score = score;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Position getPosition() {
        return position;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "UndoCardPlacementMsg";
    }
}
