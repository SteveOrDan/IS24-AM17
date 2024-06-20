package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.network.messages.Message;

public class UndoPlaceWithOnePlayerLeftMsg extends Message {
    private final int playerID;
    private final Position pos;
    private final int score;

    public UndoPlaceWithOnePlayerLeftMsg(int playerID, Position pos, int score) {
        this.playerID = playerID;
        this.pos = pos;
        this.score = score;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Position getPos() {
        return pos;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "UndoPlaceWithOnePlayerLeftMsg";
    }
}
