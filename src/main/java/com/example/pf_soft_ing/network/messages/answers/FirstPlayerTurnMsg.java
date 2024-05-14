package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.network.messages.Message;

import java.util.Map;

public class FirstPlayerTurnMsg extends Message {

    private final int playerID;

    private final String playerNickname;

    private final Map<Integer, String> IDtoOpponentNickname;

    private final Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea;

    public FirstPlayerTurnMsg(int playerID, String playerNickname, Map<Integer, String> IDtoOpponentNickname, Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea){
        this.playerID = playerID;
        this.playerNickname = playerNickname;
        this.IDtoOpponentNickname = IDtoOpponentNickname;
        this.IDtoOpponentPlayArea = IDtoOpponentPlayArea;
    }

    public int getPlayerID(){
        return playerID;
    }

    public String getPlayerNickname(){
        return playerNickname;
    }

    public Map<Integer, String> getIDtoOpponentNickname() {
        return IDtoOpponentNickname;
    }

    public Map<Integer, Map<Position, Integer>> getIDtoOpponentPlayArea() {
        return IDtoOpponentPlayArea;
    }

    @Override
    public String toString() {
        return "";
    }
}
