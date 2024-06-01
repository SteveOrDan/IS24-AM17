package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

import java.util.Map;

public class ReOnStarterPlacementMsg extends Message {
    int playerID;
    Map<Integer, String> IDToOpponentNickname;
    int[] gameSetupCards;

    public ReOnStarterPlacementMsg(int playerID, Map<Integer, String> IDToOpponentNickname, int[] gameSetupCards) {
        this.playerID = playerID;
        this.IDToOpponentNickname = IDToOpponentNickname;
        this.gameSetupCards = gameSetupCards;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Map<Integer, String> getIDToOpponentNickname() {
        return IDToOpponentNickname;
    }

    public int[] getGameSetupCards() {
        return gameSetupCards;
    }

    @Override
    public String toString() {
        return "ReGameStartMsg";
    }
}
