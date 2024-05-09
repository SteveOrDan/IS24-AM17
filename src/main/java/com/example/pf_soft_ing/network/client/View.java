package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.player.TokenColors;

import java.util.List;
import java.util.Map;

public interface View {

    void setSender(ClientSender sender);

    void showMatches(Map<Integer, List<String>> matches);

    void createMatch(int matchID, String hostNickname);

    void selectMatch(int matchID, List<String> nicknames);

    void chooseNickname(String nickname);

    void startGame(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                   int starterCardID);

    void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID,
                         TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2,
                         int secretObjectiveCardID1, int secretObjectiveCardID2);

    void confirmSecretObjective(int secretObjectiveCardID);

    void errorMessage(String errorMessage);
    void showNewPlayer(String nicknames);

    void showPlayerTurn(int playerID);

    void setID(int playerID);
}
