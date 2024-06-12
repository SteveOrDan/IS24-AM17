package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.Map;

public class ReAfterSetupMsg extends Message {

    private final int playerID;
    private final Map<Integer, String> idToNicknameMap;
    private final int[] gameSetupCards;
    private final CardSideType starterSide;
    private final TokenColors tokenColor;

    public ReAfterSetupMsg(int playerID, Map<Integer, String> idToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {
        this.playerID = playerID;
        this.idToNicknameMap = idToNicknameMap;
        this.gameSetupCards = gameSetupCards;
        this.starterSide = starterSide;
        this.tokenColor = tokenColor;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Map<Integer, String> getIdToNicknameMap() {
        return idToNicknameMap;
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
        return "ReAfterSetupMsg";
    }
}
