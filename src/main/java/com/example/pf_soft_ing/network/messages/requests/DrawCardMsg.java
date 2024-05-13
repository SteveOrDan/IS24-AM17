package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class DrawCardMsg extends Message {

    private final int playerID;
    private final boolean isGolden;
    private final boolean isVisible;
    private final int index;

    public DrawCardMsg(int playerID, boolean isGolden, boolean isVisible, int index) {
        this.playerID = playerID;
        this.isGolden = isGolden;
        this.isVisible = isVisible;
        this.index = index;
    }

    public int getPlayerID() {
        return playerID;
    }

    public boolean isGolden() {
        return isGolden;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "";
    }
}
