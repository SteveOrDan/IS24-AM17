package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.network.messages.*;
import com.example.pf_soft_ing.network.messages.requests.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

public class Decoder {

    private static GameController gameController;

    private static MatchController matchController;

    public static void decode(Message message, int playerID) {
        System.out.println("Received msg from player " + playerID);

        switch (message) {
            case GetMatchesMsg ignored -> gameController.getMatches(playerID);

            case CreateMatchMsg castedMsg -> matchController = gameController.createMatch(playerID, castedMsg.getNumberOfPlayers(), castedMsg.getNickname());

            case SelectMatchMsg castedMsg -> matchController = gameController.selectMatch(playerID, castedMsg.getMatchID());

            case ChooseNicknameMsg castedMsg -> gameController.chooseNickname(playerID, castedMsg.getNickname(), matchController);

            case PlaceStarterCardMsg castedMsg -> matchController.placeStarterCardForPlayer(playerID, castedMsg.getSide());

            case ChooseSecretObjMsg castedMsg -> matchController.setSecretObjectiveForPlayer(playerID, castedMsg.getCardID());

            case PlaceCardMsg castedMsg -> matchController.placeCard(playerID, castedMsg.getCardID(), castedMsg.getPos(), castedMsg.getSide());

            case DrawCardMsg castedMsg -> {
                if (castedMsg.isGolden()) {
                    if (castedMsg.isVisible()) {
                        matchController.drawVisibleGoldenCard(playerID, castedMsg.getIndex());
                    }
                    else {
                        matchController.drawGoldenCard(playerID);
                    }
                }
                else {
                    if (castedMsg.isVisible()) {
                        matchController.drawVisibleResourceCard(playerID, castedMsg.getIndex());
                    }
                    else {
                        matchController.drawResourceCard(playerID);
                    }
                }
            }

            case null, default -> System.out.println("Invalid message type");
        }
    }

    public static void setGameController(GameController gameController) {
        Decoder.gameController = gameController;
    }
}
