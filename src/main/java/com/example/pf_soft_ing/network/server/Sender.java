package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.player.TokenColors;

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

    void sendMatches(Map<Integer, List<String>> matches, int playerID);

    void createMatchResult(int matchID, String hostNickname);

    void selectMatchResult(int matchID, List<String> nicknames);

    void chooseNicknameResult(String nickname);

    void sendGameStart(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                       int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                       int starterCardID);

    void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                          int commonObjectiveCardID1, int commonObjectiveCardID2,
                          int secretObjectiveCardID1, int secretObjectiveCardID2);

    void confirmSecretObjective();

    /**
     * Method to place a card
     * @param placed boolean
     */
    void placeCard(boolean placed);

    void sendNewPlayer(String nickname);

    void sendPlayerTurn(int playerID, String playerNickname);
}
