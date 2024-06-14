package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.exceptions.InvalidMatchIDException;
import com.example.pf_soft_ing.network.messages.*;
import com.example.pf_soft_ing.network.messages.answers.PongMsg;
import com.example.pf_soft_ing.network.messages.requests.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Decoder {

    private static GameController gameController;
    private static final Map<Integer, MatchController> playerIDToMatch = new HashMap<>();
    private static final Map<Integer, DisconnectionManager> playerIDToDiscMan = new HashMap<>();

    private static final long CLEANUP_PERIOD = 10000;

    /**
     * Stops all threads related to a player in a match when the match is over
     * @param playerID ID of the player
     */
    public static void finishedMatch(int playerID) {
        if (!playerIDToDiscMan.containsKey(playerID)) return;

        synchronized (playerIDToDiscMan) {
            playerIDToDiscMan.get(playerID).stopConnectionCheck();
        }
    }

    /**
     * Decodes the message and calls the appropriate method in the GameController
     * @param message Message to decode
     * @param playerID ID of the player
     */
    public static void decode(Message message, int playerID) {
        switch (message) {
            case GetMatchesMsg ignored -> gameController.getMatches(playerID);

            case CreateMatchMsg castedMsg -> {
                MatchController mc = gameController.createMatch(playerID, castedMsg.getNumberOfPlayers(), castedMsg.getNickname());
                synchronized (playerIDToMatch) {
                    playerIDToMatch.put(playerID, mc);
                }
                DisconnectionManager discMan = new DisconnectionManager(gameController, mc, mc.getPlayerSender(playerID), playerID);
                synchronized (playerIDToDiscMan) {
                    playerIDToDiscMan.put(playerID, discMan);
                }
                discMan.startConnectionCheck();
            }

            case SelectMatchMsg castedMsg -> {
                MatchController mc = gameController.selectMatch(playerID, castedMsg.getMatchID());
                if (mc != null) {
                    synchronized (playerIDToMatch) {
                        playerIDToMatch.put(playerID, mc);
                    }
                    DisconnectionManager discMan = new DisconnectionManager(gameController, mc, mc.getPlayerSender(playerID), playerID);
                    synchronized (playerIDToDiscMan) {
                        playerIDToDiscMan.put(playerID, discMan);
                    }
                    discMan.startConnectionCheck();
                }
            }

            case ChooseNicknameMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    gameController.chooseNickname(playerID, castedMsg.getNickname(), playerIDToMatch.get(playerID));
                }
            }

            case ReconnectToMatchMsg castedMsg -> {
                int originalPlayerID = gameController.reconnectToMatch(playerID, castedMsg.getNickname(), castedMsg.getMatchID());

                if (originalPlayerID != -1) {
                    synchronized (playerIDToMatch) {
                        Sender newSender = gameController.getPlayerSender(playerID);
                        try {
                            playerIDToMatch.put(originalPlayerID, gameController.getMatchByID(castedMsg.getMatchID()));
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

            case PlaceStarterCardMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(castedMsg.getPlayerID()).placeStarterCardForPlayer(castedMsg.getPlayerID(), castedMsg.getSide());
                }
            }

            case ChooseSecretObjMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(castedMsg.getPlayerID()).setSecretObjectiveForPlayer(castedMsg.getPlayerID(), castedMsg.getCardID());
                }
            }

            case PlaceCardMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(castedMsg.getPlayerID()).placeCard(castedMsg.getPlayerID(), castedMsg.getCardID(), castedMsg.getPos(), castedMsg.getSide());
                }
            }

            case DrawCardMsg castedMsg -> {
                if (castedMsg.isGolden()) {
                    if (castedMsg.isVisible()) {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(castedMsg.getPlayerID()).drawVisibleGoldenCard(castedMsg.getPlayerID(), castedMsg.getIndex());
                        }
                    }
                    else {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(castedMsg.getPlayerID()).drawGoldenCard(castedMsg.getPlayerID());
                        }
                    }
                }
                else {
                    if (castedMsg.isVisible()) {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(castedMsg.getPlayerID()).drawVisibleResourceCard(castedMsg.getPlayerID(), castedMsg.getIndex());
                        }
                    }
                    else {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(castedMsg.getPlayerID()).drawResourceCard(castedMsg.getPlayerID());
                        }
                    }
                }
            }

            case ChatMessageMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(castedMsg.getPlayerID()).chatMessage(castedMsg.getPlayerID(), castedMsg.getRecipient(), castedMsg.getMessage());
                }
            }

            case PongMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToDiscMan.get(castedMsg.getPlayerID()).resetPacketLoss();
                }
            }

            case null, default -> System.out.println("Invalid message type");
        }
    }

    /**
     * Setter
     * @param gameController GameController object
     */
    public static void setGameController(GameController gameController) {
        Decoder.gameController = gameController;
        startPeriodicCleanup();
    }

    /**
     * Starts the periodic cleanup of the playerIDToMatch and playerIDToDiscMan maps
     */
    private static void startPeriodicCleanup() {
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

        timer.scheduleAtFixedRate(timerTaskCleanup, 300000, CLEANUP_PERIOD);
    }
}
