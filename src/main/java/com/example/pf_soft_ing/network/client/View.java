package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.List;
import java.util.Map;

public interface View {

    void showMatches(Map<Integer, List<String>> matches);

    void createMatch(int matchID, String hostNickname);

    void selectMatch(int matchID, List<String> nicknames);

    void chooseNickname(String nickname);

    void startGame(String nickname, Map<Integer, String> IDToNicknameMap,
                   int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                   int starterCardID);

    void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID,
                         TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2,
                         int secretObjectiveCardID1, int secretObjectiveCardID2);

    void confirmSecretObjective();

    void errorMessage(String errorMessage);

    void showNewPlayer(String nicknames);

    void showFirstPlayerTurn(int playerID, Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea);

    void placeCard();

    void showNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID);

    void updateDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                        int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);

    void setID(int playerID);

    void opponentPlaceCard(int playerId, int cardID, Position pos, CardSideType side);

    void receiveChatMessage(String senderNickname, String recipientNickname, String message);

    void showRanking(List<String> rankings);

    void showNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID, GameState gameState);

    void showNewPlayerExtraTurn(int cardID, int lastPlayerID, int newPlayerID);
}
