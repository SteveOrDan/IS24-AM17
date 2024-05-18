package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.*;
import java.util.List;
import java.util.Map;

public interface ClientRMIInterface extends Remote {
    // server su client
    void showMatches(Map<Integer, List<String>> matchesNicknames, int playerID) throws RemoteException;

    void placeStarterCard() throws RemoteException;

    void placeCardResult(int playerID, int cardID, Position pos, CardSideType chosenSide) throws RemoteException;

    void sendError(String errorMsg) throws RemoteException;

    void selectMatchResult(int matchID, List<String> nicknames) throws RemoteException;

    void createMatchResult(int matchID, String hostNickname) throws RemoteException;

    void chooseNicknameResult(String hostNickname) throws RemoteException;

    void startGame(String nickname, Map<Integer, String> IDtoNicknameMap,
                   int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                   int starterCardID) throws RemoteException;

    void sendFirstPlayerTurn(int playerID, Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea) throws RemoteException;

    void setMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                          int commonObjectiveCardID1, int commonObjectiveCardID2,
                          int secretObjectiveCardID1, int secretObjectiveCardID2) throws RemoteException;

    void setNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) throws RemoteException;

    void opponentPlaceCard(int playerID, int cardID, Position pos, CardSideType chosenSide) throws RemoteException;

    void showRanking(List<String> rankings) throws RemoteException;

    void confirmSecretObjective() throws RemoteException;

    void sendChatMessage(String senderNickname, String recipientNickname, String message) throws RemoteException;
    void setNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID,
                                  int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                  int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, GameState gameState) throws RemoteException;

    void setNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID) throws RemoteException;
}
