package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.network.messages.Message;

public class NewPlayerTurnNewStateMsg extends Message {

    private final int drawnCardID;
    private final int lastPlayerID;

    private final int newPlayerID;
    private final String newPlayerNickname;

    private final int resDeckCardID;
    private final int visibleResCardID1;
    private final int visibleResCardID2;

    private final int goldDeckCardID;
    private final int visibleGoldCardID1;
    private final int visibleGoldCardID2;

    private final GameState gameState;

    public NewPlayerTurnNewStateMsg(int drawnCardID, int lastPlayerID, int newPlayerID, String newPlayerNickname,
                                    int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                    int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                    GameState gameState) {
        this.drawnCardID = drawnCardID;
        this.lastPlayerID = lastPlayerID;

        this.newPlayerID = newPlayerID;
        this.newPlayerNickname = newPlayerNickname;

        this.resDeckCardID = resDeckCardID;
        this.visibleResCardID1 = visibleResCardID1;
        this.visibleResCardID2 = visibleResCardID2;

        this.goldDeckCardID = goldDeckCardID;
        this.visibleGoldCardID1 = visibleGoldCardID1;
        this.visibleGoldCardID2 = visibleGoldCardID2;

        this.gameState = gameState;
    }

    public int getDrawnCardID() {
        return drawnCardID;
    }

    public int getLastPlayerID() {
        return lastPlayerID;
    }

    public int getNewPlayerID() {
        return newPlayerID;
    }

    public String getNewPlayerNickname() {
        return newPlayerNickname;
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

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public String toString() {
        return "";
    }
}
