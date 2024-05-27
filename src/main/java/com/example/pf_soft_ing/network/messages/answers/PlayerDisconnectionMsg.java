package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class PlayerDisconnectionMsg extends Message {

    private final int playerID;

    public PlayerDisconnectionMsg(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return "Player " + playerID + " disconnected";
    }
}
