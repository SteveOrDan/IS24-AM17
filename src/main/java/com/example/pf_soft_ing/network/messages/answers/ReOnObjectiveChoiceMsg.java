package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.Map;

public class ReOnObjectiveChoiceMsg extends Message {
    int playerID;
    Map<Integer, String> IDToOpponentNickname;
    int[] gameSetupCards;
    CardSideType starterSide;
    TokenColors tokenColor;

    public ReOnObjectiveChoiceMsg(int playerID, Map<Integer, String> idToOpponentNickname, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {
        this.playerID = playerID;
        this.IDToOpponentNickname = idToOpponentNickname;
        this.gameSetupCards = gameSetupCards;
        this.starterSide = starterSide;
        this.tokenColor = tokenColor;
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

    public CardSideType getStarterSide() {
        return starterSide;
    }

    public TokenColors getTokenColor() {
        return tokenColor;
    }

    @Override
    public String toString() {
        return "ReOnObjectiveChoiceMsg";
    }
}
