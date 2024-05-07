package com.example.pf_soft_ing.network.server;

import java.util.List;
import java.util.Map;

public interface Sender {

    /**
     * Method to requestError to the Player
     */
    void sendError(String errorMsg);

    /**
     * Method to return all the matches
     */
    void sendMatches(Map<Integer, List<String>> matches);

    void createMatchResult(int matchID, String hostNickname);

    void selectMatchResult(int matchID, List<String> nicknames);

    void chooseNicknameResult(String nickname);

    void sendGameStart(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                       int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                       int starterCardID);

    /**
     * Method to place starter card
     * @param placed boolean
     */
    void placeStarterCard(boolean placed);

    /**
     * Method to place a card
     * @param placed boolean
     */
    void placeCard(boolean placed);

    void sendNewPlayer(String nickname);
}
