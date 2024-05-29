package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.network.messages.*;
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
    private static final long CLEANUP_DELAY = 300000;

    public static void decode(Message message, int playerID) {
        System.out.println("Received msg from player " + playerID);

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
                synchronized (playerIDToMatch) {
                    playerIDToMatch.put(playerID, mc);
                }
                DisconnectionManager discMan = new DisconnectionManager(gameController, mc, mc.getPlayerSender(playerID), playerID);
                synchronized (playerIDToDiscMan) {
                    playerIDToDiscMan.put(playerID, discMan);
                }
                discMan.startConnectionCheck();
            }

            case ChooseNicknameMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    gameController.chooseNickname(playerID, castedMsg.getNickname(), playerIDToMatch.get(playerID));
                }
            }

            case PlaceStarterCardMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(playerID).placeStarterCardForPlayer(playerID, castedMsg.getSide());
                }
            }

            case ChooseSecretObjMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(playerID).setSecretObjectiveForPlayer(playerID, castedMsg.getCardID());
                }
            }

            case PlaceCardMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(playerID).placeCard(playerID, castedMsg.getCardID(), castedMsg.getPos(), castedMsg.getSide());
                }
            }

            case DrawCardMsg castedMsg -> {
                if (castedMsg.isGolden()) {
                    if (castedMsg.isVisible()) {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(playerID).drawVisibleGoldenCard(playerID, castedMsg.getIndex());
                        }
                    }
                    else {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(playerID).drawGoldenCard(playerID);
                        }
                    }
                }
                else {
                    if (castedMsg.isVisible()) {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(playerID).drawVisibleResourceCard(playerID, castedMsg.getIndex());
                        }
                    }
                    else {
                        synchronized (playerIDToMatch) {
                            playerIDToMatch.get(playerID).drawResourceCard(playerID);
                        }
                    }
                }
            }

            case ChatMessageMsg castedMsg -> {
                synchronized (playerIDToMatch) {
                    playerIDToMatch.get(playerID).chatMessage(playerID, castedMsg.getRecipient(), castedMsg.getMessage());
                }
            }

            case PongMsg ignored -> {
                synchronized (playerIDToMatch) {
                    playerIDToDiscMan.get(playerID).resetPacketLoss();
                }
            }

            case null, default -> System.out.println("Invalid message type");
        }
    }

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

        timer.scheduleAtFixedRate(timerTaskCleanup, CLEANUP_DELAY, CLEANUP_PERIOD);
    }
}
