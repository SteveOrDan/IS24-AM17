package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.network.messages.*;
import com.example.pf_soft_ing.network.messages.requests.*;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.List;
import java.util.Map;

public class Decoder {

    private static GameController gameController;

    private static MatchController matchController;

    public static void decode(Message message, int playerID) {
        Sender sender = gameController.getPlayerSender(playerID);
        System.out.println("Received msg from player " + playerID);

        switch (message) {
            case GetMatchesMsg ignored -> {
                gameController.getMatches(playerID);
            }
            case CreateMatchMsg castedMsg -> {
                gameController.createMatch(playerID, castedMsg.getNumberOfPlayers(), castedMsg.getNickname());
                try {
                    matchController = gameController.getGameModel().getMatchByID(gameController.getGameModel().getIDToPlayers().get(playerID).getMatchID());
                } catch (InvalidMatchIDException e) {
                    System.out.println("Already notified client, failed match creation");
                }
            }
            case SelectMatchMsg castedMsg -> {
                gameController.selectMatch(playerID, castedMsg.getMatchID());
                try {
                    matchController = gameController.getGameModel().getMatchByID(gameController.getGameModel().getIDToPlayers().get(playerID).getMatchID());
                } catch (InvalidMatchIDException e) {
                    System.out.println("Already notified client, failed match selection");
                }
            }
            case ChooseNicknameMsg castedMsg -> {
                gameController.chooseNickname(playerID, castedMsg.getNickname(), matchController);
            }

            case PlaceStarterCardMsg castedMsg -> {
                matchController.placeStarterCardForPlayer(playerID, castedMsg.getSide());
            }

            case ChooseSecretObjMsg castedMsg -> {
                matchController.setSecretObjectiveForPlayer(playerID, castedMsg.getCardID());
            }


            case null, default -> System.out.println("Invalid message type");
        }
    }

    public static void setGameController(GameController gameController) {
        Decoder.gameController = gameController;
    }
}
