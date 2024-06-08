package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class PlayerDisconnectionWithOnePlayerLeftMsg extends Message {

    private final int playerID;

    public PlayerDisconnectionWithOnePlayerLeftMsg(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    @Override
    public String toString() {
        return "PlayerDisconnectionWithOnePlayerLeftMsg{}";
    }
}
