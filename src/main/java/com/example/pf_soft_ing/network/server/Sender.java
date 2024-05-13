package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.player.TokenColors;

import java.util.List;
import java.util.Map;

public interface Sender {

    void sendError(String errorMsg);

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

    void placeCard();

    void sendFirstPlayer(int playerID, String playerNickname);

    void sendFirstPlayerTurn(int playerID, String playerNickname);

    void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID, String playerNickname,
                               int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);
}
