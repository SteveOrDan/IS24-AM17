package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.List;
import java.util.Map;

public interface Sender {

    void sendError(String errorMsg);

    void sendMatches(Map<Integer, List<String>> matches, int playerID);

    void createMatchResult(int matchID, String hostNickname);

    void selectMatchResult(int matchID, List<String> nicknames);

    void chooseNicknameResult(String nickname);

    void sendGameStart(String nickname, Map<Integer, String> IDtoOpponentNickname,
                       int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                       int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                       int starterCardID);

    void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                          int commonObjectiveCardID1, int commonObjectiveCardID2,
                          int secretObjectiveCardID1, int secretObjectiveCardID2);

    void confirmSecretObjective();

    void placeCard(int playerID, int cardID, Position pos, CardSideType chosenSide);

    void sendFirstPlayerTurn(int playerID, Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea,
                             int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                             int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);

    void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                           int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                           int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);

    void sendNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side,
                                int newPlayerID);

    void sendNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID,
                                   int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                   GameState gameState);

    void sendChatMessage(String sender, String recipient, String message);

    void sendRanking(List<String> rankings);
}
