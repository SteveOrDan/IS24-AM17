package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class PlayerTurnMsg extends Message {

    private final int playerID;

    public PlayerTurnMsg(int playerID){
        this.playerID = playerID;
    }

    public int getPlayerID(){
        return playerID;
    }

    @Override
    public String toString() {
        return "";
    }
}
