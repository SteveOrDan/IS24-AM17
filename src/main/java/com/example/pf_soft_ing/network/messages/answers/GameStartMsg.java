package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

import java.util.Map;

public class GameStartMsg extends Message {

    private final Map<Integer, String> IDToNicknameMap;

    private final int resDeckCardID;
    private final int visibleResCardID1;
    private final int visibleResCardID2;

    private final int goldDeckCardID;
    private final int visibleGoldCardID1;
    private final int visibleGoldCardID2;

    private final int starterCardID;

    public GameStartMsg(Map<Integer, String> IDToNicknameMap,
                        int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                        int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                        int starterCardID) {
        this.IDToNicknameMap = IDToNicknameMap;

        this.resDeckCardID = resDeckCardID;
        this.visibleResCardID1 = visibleResCardID1;
        this.visibleResCardID2 = visibleResCardID2;

        this.goldDeckCardID = goldDeckCardID;
        this.visibleGoldCardID1 = visibleGoldCardID1;
        this.visibleGoldCardID2 = visibleGoldCardID2;

        this.starterCardID = starterCardID;
    }

    public Map<Integer, String> getIDToNicknameMap() {
        return IDToNicknameMap;
    }

    public int getVisibleResCardID1() {
        return visibleResCardID1;
    }

    public int getVisibleResCardID2() {
        return visibleResCardID2;
    }

    public int getVisibleGoldCardID1() {
        return visibleGoldCardID1;
    }

    public int getVisibleGoldCardID2() {
        return visibleGoldCardID2;
    }

    public int getStarterCardID() {
        return starterCardID;
    }

    public int getResDeckCardID() {
        return resDeckCardID;
    }

    public int getGoldDeckCardID() {
        return goldDeckCardID;
    }

    @Override
    public String toString() {
        return "";
    }
}
