package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.player.TokenColors;

public class FirstPlayerTurnMsg extends Message {

    private final int lastPlayerID;
    private final int playerID;

    private final int[] playerIDs;
    private final int[] starterCardIDs;
    private final CardSideType[] starterCardSides;
    private final TokenColors[] tokenColors;
    private final int[][] playerHands;

    private final int resDeckCardID;
    private final int visibleResCardID1;
    private final int visibleResCardID2;

    private final int goldDeckCardID;
    private final int visibleGoldCardID1;
    private final int visibleGoldCardID2;

    public FirstPlayerTurnMsg(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs,
                              CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                              int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                              int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2){
        this.lastPlayerID = lastPlayerID;
        this.playerID = playerID;

        this.playerIDs = playerIDs;
        this.starterCardIDs = starterCardIDs;
        this.starterCardSides = starterCardSides;
        this.tokenColors = tokenColors;
        this.playerHands = playerHands;

        this.resDeckCardID = resDeckCardID;
        this.visibleResCardID1 = visibleResCardID1;
        this.visibleResCardID2 = visibleResCardID2;

        this.goldDeckCardID = goldDeckCardID;
        this.visibleGoldCardID1 = visibleGoldCardID1;
        this.visibleGoldCardID2 = visibleGoldCardID2;
    }

    public int getLastPlayerID(){
        return lastPlayerID;
    }

    public int getPlayerID(){
        return playerID;
    }

    public int[] getPlayerIDs() {
        return playerIDs;
    }

    public int[] getStarterCardIDs() {
        return starterCardIDs;
    }

    public CardSideType[] getStarterCardSides() {
        return starterCardSides;
    }

    public TokenColors[] getTokenColors() {
        return tokenColors;
    }

    public int[][] getPlayerHands() {
        return playerHands;
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
