package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class FirstPlayerTurnMsg extends Message {

    private final int playerID;

    private final String playerNickname;

    public FirstPlayerTurnMsg(int playerID, String playerNickname){
        this.playerID = playerID;
        this.playerNickname = playerNickname;
    }

    public int getPlayerID(){
        return playerID;
    }

    public String getPlayerNickname(){
        return playerNickname;
    }

    @Override
    public String toString() {
        return "";
    }
}
