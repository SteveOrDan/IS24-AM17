package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class RMISender implements Sender {

    private final ClientRMIInterface client;

    public RMISender(ClientRMIInterface client){
        this.client = client;
    }


    @Override
    public void sendError(String errorMsg) {
        new Thread(() -> {
            try {
                client.sendError(errorMsg);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
            }
        }).start();
    }

    @Override
    public void sendMatches(Map<Integer, List<String>> matches, int playerID) {
        new Thread(() -> {
            try {
                System.out.println("Sever: Showing matches");
                client.showMatches(matches, playerID);
                System.out.println("Server: Matches shown");
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
            }
        }).start();
    }

    @Override
    public void createMatchResult(int matchID, String hostNickname) {
        new Thread(() -> {
            try {
                client.createMatchResult(matchID, hostNickname);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
            }
        }).start();
    }

    @Override
    public void selectMatchResult(int matchID, List<String> nicknames) {
        new Thread(() -> {
            try {
                client.selectMatchResult(matchID, nicknames);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
            }
        }).start();
    }

    @Override
    public void chooseNicknameResult(String nickname) {
        new Thread(() -> {
            try {
                client.chooseNicknameResult(nickname);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Nickname is not available");
            }
        }).start();
    }

    @Override
    public void sendGameStart(String nickname, Map<Integer, String> IDtoOpponentNickname,
                              int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                              int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                              int starterCardID) {
        new Thread(() -> {
            try {
                client.startGame(nickname, IDtoOpponentNickname,
                        resDeckCardID, visibleResCardID1, visibleResCardID2,
                        goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2,
                        starterCardID);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Game not started");
            }
        }).start();
    }

    @Override
    public void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                                 int commonObjectiveCardID1, int commonObjectiveCardID2,
                                 int secretObjectiveCardID1, int secretObjectiveCardID2) {
        new Thread(() -> {
            try {
                client.setMissingSetup(resourceCardID1, resourceCardID2, goldenCardID, tokenColor,
                        commonObjectiveCardID1, commonObjectiveCardID2,
                        secretObjectiveCardID1, secretObjectiveCardID2);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Missing setup");
            }
        }).start();
    }

    @Override
    public void confirmSecretObjective() {
        new Thread(() -> {
            try {
                client.confirmSecretObjective();
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Secret objective not confirmed");
            }
        }).start();
    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos, CardSideType chosenSide, int score) {
        new Thread(() -> {
            try {
                client.placeCardResult(playerID, cardID, pos, chosenSide, score);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Place card not shown");
            }
        }).start();
    }

    @Override
    public void sendFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs, CardSideType[] starterCardSides,
                                    int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                    int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        new Thread(() -> {
            try {
                client.sendFirstPlayerTurn(lastPlayerID, playerID, playerIDs, starterCardIDs, starterCardSides,
                        resDeckCardID, visibleResCardID1, visibleResCardID2,
                        goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". New first player turn not shown");
            }
        }).start();
    }

    @Override
    public void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                                  int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                  int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        new Thread(() -> {
            try {
                client.setNewPlayerTurn(drawnCardID, lastPlayerID, newPlayerID,
                        resDeckCardID, visibleResCardID1, visibleResCardID2,
                        goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
            }
            catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". New player's turn not shown");
            }
        }).start();
    }

    @Override
    public void sendNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int score) {
        new Thread(() ->{
            try {
                client.setNewPlayerExtraTurn(cardID, lastPlayerID, pos, side, newPlayerID, score);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". New player's extra turn not shown");
            }
        }).start();
    }

    @Override
    public void sendNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID,
                                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, GameState gameState) {
        new Thread(() -> {
            try {
                client.setNewPlayerTurnNewState(drawnCardID, lastPlayerID, newPlayerID,
                        resDeckCardID, visibleResCardID1, visibleResCardID2,
                        goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, gameState);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". New player's turn state not shown");
            }
        }).start();
    }

    @Override
    public void sendChatMessage(String sender, String recipient, String message) {
        new Thread(() ->{
            try {
                client.sendChatMessage(sender, recipient, message);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Chat message not sent");
            }
        }).start();
    }

    @Override
    public void sendRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives) {
        new Thread(() ->{
            try {
                client.showRanking(lastPlayerID, cardID, pos, side, deltaScore, nicknames, scores, numOfSecretObjectives);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Ranking not shown");
            }
        }).start();
    }

    @Override
    public void sendPlayerDisconnection(int playerID) {
        new Thread(() ->{
            try {
                client.sendPlayerDisconnection(playerID);
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Player disconnection not shown");
            }
        }).start();
    }

    @Override
    public void startHeartbeat() {
        new Thread(() ->{
            try {
                client.startHeartbeat();
            } catch (RemoteException e) {
                System.out.println("Error: " + e.getMessage() + ". Start heartbeat request not sent");
            }
        }).start();
    }
}
