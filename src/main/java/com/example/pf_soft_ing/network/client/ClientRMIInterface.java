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

    void placeCardResult(int playerID, int cardID, Position pos, CardSideType chosenSide, int score) throws RemoteException;

    void sendError(String errorMsg) throws RemoteException;

    void selectMatchResult(int matchID, List<String> nicknames) throws RemoteException;

    void createMatchResult(int matchID, String hostNickname) throws RemoteException;

    void chooseNicknameResult(String hostNickname) throws RemoteException;

    void startGame(String nickname, Map<Integer, String> IDtoNicknameMap,
                   int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                   int starterCardID) throws RemoteException;

    void sendFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs,
                             CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                             int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                             int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) throws RemoteException;

    void setMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                          int commonObjectiveCardID1, int commonObjectiveCardID2,
                          int secretObjectiveCardID1, int secretObjectiveCardID2) throws RemoteException;

    void setNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) throws RemoteException;

    void showRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives) throws RemoteException;

    void confirmSecretObjective() throws RemoteException;

    void sendChatMessage(String senderNickname, String recipientNickname, String message) throws RemoteException;

    void setNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID,
                                  int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                  int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, GameState gameState) throws RemoteException;

    void setNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int score) throws RemoteException;

    void sendPlayerDisconnection(int playerID) throws RemoteException;

    void sendPlayerDisconnectionWithOnePlayerLeft(int playerID) throws RemoteException;

    void sendReOnStarterPlacement(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards) throws RemoteException;

    void sendReOnObjectiveChoice(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) throws RemoteException;

    void sendNormalReconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands,
                             List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                             int[] playersScores, int[] gameSetupCards, int currPlayerID) throws RemoteException;

    void sendUndoCardPlacement(int playerID, Position pos, int score, int nextPlayerID) throws RemoteException;

    void sendUndoPlaceWithOnePlayerLeft(int playerID, Position pos, int score) throws RemoteException;

    void sendPlayerReconnection(int playerID) throws RemoteException;

    void sendSoleWinnerMessage() throws RemoteException;

    void sendPing() throws RemoteException;
}
