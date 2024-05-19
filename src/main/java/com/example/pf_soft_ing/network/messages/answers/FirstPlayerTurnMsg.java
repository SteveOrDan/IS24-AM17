package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.network.messages.Message;

import java.util.Map;

public class FirstPlayerTurnMsg extends Message {

    private final int playerID;

    private final Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea;

    private final int resDeckCardID;
    private final int visibleResCardID1;
    private final int visibleResCardID2;

    private final int goldDeckCardID;
    private final int visibleGoldCardID1;
    private final int visibleGoldCardID2;

    public FirstPlayerTurnMsg(int playerID, Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea,
                              int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                              int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2){
        this.playerID = playerID;
        this.IDtoOpponentPlayArea = IDtoOpponentPlayArea;

        this.resDeckCardID = resDeckCardID;
        this.visibleResCardID1 = visibleResCardID1;
        this.visibleResCardID2 = visibleResCardID2;

        this.goldDeckCardID = goldDeckCardID;
        this.visibleGoldCardID1 = visibleGoldCardID1;
        this.visibleGoldCardID2 = visibleGoldCardID2;
    }

    public int getPlayerID(){
        return playerID;
    }

    public Map<Integer, Map<Position, Integer>> getIDtoOpponentPlayArea() {
        return IDtoOpponentPlayArea;
    }

    public int getResDeckCardID() {
        return resDeckCardID;
    }

    public int getVisibleResCardID1() {
        return visibleResCardID1;
    }

    public int getVisibleResCardID2() {
        return visibleResCardID2;
    }

    public int getGoldDeckCardID() {
        return goldDeckCardID;
    }

    public int getVisibleGoldCardID1() {
        return visibleGoldCardID1;
    }

    public int getVisibleGoldCardID2() {
        return visibleGoldCardID2;
    }

    @Override
    public String toString() {
        return "";
    }
}
