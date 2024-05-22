package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.network.messages.*;
import com.example.pf_soft_ing.network.messages.answers.PongMsg;
import com.example.pf_soft_ing.network.messages.requests.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

import java.util.HashMap;
import java.util.Map;

public class Decoder {

    private static GameController gameController;
    private static final Map<Integer, MatchController> playerIDToMatch = new HashMap<>();
    private static final Map<Integer, DisconnectionManager> playerIDToDiscMan = new HashMap<>();

    public static void decode(Message message, int playerID) {
        System.out.println("Received msg from player " + playerID);

        switch (message) {
            case GetMatchesMsg ignored -> gameController.getMatches(playerID);

            case CreateMatchMsg castedMsg -> {
                MatchController mc = gameController.createMatch(playerID, castedMsg.getNumberOfPlayers(), castedMsg.getNickname());
                playerIDToMatch.put(playerID, mc);
                DisconnectionManager discMan = new DisconnectionManager(mc, mc.getPlayerSender(playerID), playerID);
                playerIDToDiscMan.put(playerID, discMan);
                discMan.startPing();
            }

            case SelectMatchMsg castedMsg -> {
                MatchController mc = gameController.selectMatch(playerID, castedMsg.getMatchID());
                playerIDToMatch.put(playerID, mc);
                DisconnectionManager discMan = new DisconnectionManager(mc, mc.getPlayerSender(playerID), playerID);
                playerIDToDiscMan.put(playerID, discMan);
                discMan.startPing();
            }

            case ChooseNicknameMsg castedMsg -> gameController.chooseNickname(playerID, castedMsg.getNickname(), playerIDToMatch.get(playerID));

            case PlaceStarterCardMsg castedMsg -> playerIDToMatch.get(playerID).placeStarterCardForPlayer(playerID, castedMsg.getSide());

            case ChooseSecretObjMsg castedMsg -> playerIDToMatch.get(playerID).setSecretObjectiveForPlayer(playerID, castedMsg.getCardID());

            case PlaceCardMsg castedMsg -> playerIDToMatch.get(playerID).placeCard(playerID, castedMsg.getCardID(), castedMsg.getPos(), castedMsg.getSide());

            case DrawCardMsg castedMsg -> {
                if (castedMsg.isGolden()) {
                    if (castedMsg.isVisible()) {
                        playerIDToMatch.get(playerID).drawVisibleGoldenCard(playerID, castedMsg.getIndex());
                    }
                    else {
                        playerIDToMatch.get(playerID).drawGoldenCard(playerID);
                    }
                }
                else {
                    if (castedMsg.isVisible()) {
                        playerIDToMatch.get(playerID).drawVisibleResourceCard(playerID, castedMsg.getIndex());
                    }
                    else {
                        playerIDToMatch.get(playerID).drawResourceCard(playerID);
                    }
                }
            }

            case ChatMessageMsg castedMsg -> playerIDToMatch.get(playerID).chatMessage(playerID, castedMsg.getRecipient(), castedMsg.getMessage());

            case PongMsg ignored -> playerIDToDiscMan.get(playerID).resetPacketLoss();

            case null, default -> System.out.println("Invalid message type");
        }
    }

    public static void setGameController(GameController gameController) {
        Decoder.gameController = gameController;
    }
}
