package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.InvalidMatchIDException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private final GameController gameController;
    private static final Map<Integer, MatchController> playerIDToMatch = new HashMap<>();
    private static final Map<Integer, DisconnectionManager> playerIDToDiscMan = new HashMap<>();

    public RMIReceiver(GameController gameController) throws RemoteException {
        this.gameController = gameController;
        startPeriodicCleanup();
    }

    /**
     * Stops all threads related to a player in a match when the match is over
     * @param playerID ID of the player
     */
    public static void finishedMatch(int playerID) {
        if (! playerIDToDiscMan.containsKey(playerID)) return;
        synchronized (playerIDToDiscMan) {
            playerIDToDiscMan.get(playerID).stopConnectionCheck();
        }
    }

    @Override
    public void connect(ClientRMIInterface client) throws RemoteException {
        // Create player and add to IDToPlayerMap
        PlayerModel playerModel = gameController.createPlayer(new RMISender(client));

        // Send matches to client
        getMatches(playerModel.getID());
    }

    @Override
    public void getMatches(int playerID) throws RemoteException {
        gameController.getMatches(playerID);
    }

    @Override
    public void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException{
        MatchController mc = gameController.createMatch(playerID, numberOfPlayers, nickname);
        synchronized (playerIDToMatch) {
            playerIDToMatch.put(playerID, mc);
        }
        DisconnectionManager discMan = new DisconnectionManager(gameController, playerIDToMatch.get(playerID), playerIDToMatch.get(playerID).getPlayerSender(playerID), playerID);
        synchronized (playerIDToDiscMan) {
            playerIDToDiscMan.put(playerID, discMan);
        }
        discMan.startConnectionCheck();
    }

    @Override
    public void selectMatch(int playerID, int matchID) throws RemoteException {
        MatchController mc = gameController.selectMatch(playerID, matchID);

        if (mc != null) {
            synchronized (playerIDToMatch) {
                playerIDToMatch.put(playerID, mc);
            }
            DisconnectionManager discMan = new DisconnectionManager(gameController, playerIDToMatch.get(playerID), playerIDToMatch.get(playerID).getPlayerSender(playerID), playerID);
            synchronized (playerIDToDiscMan) {
                playerIDToDiscMan.put(playerID, discMan);
            }
            discMan.startConnectionCheck();
        }
    }

    @Override
    public void reconnectToMatch(int playerID, String nickname, int matchID) throws RemoteException {
        int originalPlayerID = gameController.reconnectToMatch(playerID, nickname, matchID);

        if (originalPlayerID != -1) {
            synchronized (playerIDToMatch) {
                Sender newSender = gameController.getPlayerSender(playerID);
                try {
                    playerIDToMatch.put(originalPlayerID, gameController.getMatchByID(matchID));
                } // This exception should never be thrown
                catch (InvalidMatchIDException e) {
                    throw new RuntimeException(e);
                }
                synchronized (playerIDToDiscMan) {
                    DisconnectionManager discMan = new DisconnectionManager(gameController, playerIDToMatch.get(originalPlayerID), newSender, originalPlayerID);
                    playerIDToDiscMan.put(originalPlayerID, discMan);
                    discMan.startConnectionCheck();
                }
            }
        }
    }

    @Override
    public void chooseNickname(int playerID, String nickname) throws RemoteException {
        gameController.chooseNickname(playerID, nickname, playerIDToMatch.get(playerID));
    }

    @Override
    public void placeStarterCard(int playerID, CardSideType side) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).placeStarterCardForPlayer(playerID, side);
        }
    }

    @Override
    public void chooseSecretObj(int playerID, int cardID) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).setSecretObjectiveForPlayer(playerID, cardID);
        }
    }

    @Override
    public void placeCard(int playerID, int cardID, CardSideType side, Position pos) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).placeCard(playerID, cardID, pos, side);
        }
    }

    @Override
    public void drawResourceCard(int playerID) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).drawResourceCard(playerID);
        }
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).drawVisibleResourceCard(playerID, index);
        }
    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).drawGoldenCard(playerID);
        }
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).drawVisibleGoldenCard(playerID, index);
        }
    }

    @Override
    public void sendChatMessage(int playerID, String recipientNickname, String message) throws RemoteException {
        synchronized (playerIDToMatch) {
            playerIDToMatch.get(playerID).chatMessage(playerID, recipientNickname, message);
        }
    }

    @Override
    public void sendPong(int playerID) throws RemoteException {
        synchronized (playerIDToMatch) {
            if (!playerIDToDiscMan.containsKey(playerID)) {
                System.out.println("PlayerID not found in playerIDToDiscMan: " + playerID);
                return;
            }
            playerIDToDiscMan.get(playerID).resetPacketLoss();
        }
    }

    /**
     * Starts the periodic cleanup of the playerIDToMatch and playerIDToDiscMan maps
     */
    private void startPeriodicCleanup() {
        Timer timer = new Timer();
        TimerTask timerTaskCleanup = new TimerTask() {
            @Override
            public void run() {
                synchronized (playerIDToMatch) {
                    for (int playerID : playerIDToMatch.keySet()) {
                        if (gameController.matchNotPresent(playerIDToMatch.get(playerID))) {
                            playerIDToMatch.remove(playerID);
                            synchronized (playerIDToDiscMan) {
                                playerIDToDiscMan.remove(playerID);
                            }
                        }
                    }
                }
            }
        };

        long CLEANUP_PERIOD = 10000;
        timer.scheduleAtFixedRate(timerTaskCleanup, 300000, CLEANUP_PERIOD);
    }
}