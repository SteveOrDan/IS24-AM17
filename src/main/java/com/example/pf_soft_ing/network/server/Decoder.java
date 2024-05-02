package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.network.messages.*;
import com.example.pf_soft_ing.network.messages.requests.ChooseNicknameMsg;
import com.example.pf_soft_ing.network.messages.requests.CreateMatchMsg;
import com.example.pf_soft_ing.network.messages.requests.GetMatchesMsg;
import com.example.pf_soft_ing.network.messages.requests.SelectMatchMsg;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

import java.util.List;
import java.util.Map;

public class Decoder {

    private static GameController gameController;

    public static void decode(Message message, int playerID) {
        Sender sender = gameController.getPlayerSender(playerID);
        System.out.println("Received msg from player " + playerID);

        switch (message) {
            case GetMatchesMsg ignored -> {
                Map<Integer, List<String>> matches = gameController.getMatches();

                sender.sendMatches(matches);
            }
            case CreateMatchMsg castedMsg -> {
                try {
                    MatchController match = gameController.createMatch(playerID, castedMsg.getNumberOfPlayers(), castedMsg.getNickname());

                    sender.createMatchResult(match);
                }
                catch (InvalidNumOfPlayers | InvalidPlayerStateException | GameIsFullException e) {
                    sender.sendError(e.getMessage());
                }
            }
            case SelectMatchMsg castedMsg -> {
                try {
                    MatchController match = gameController.selectMatch(playerID, castedMsg.getMatchID());

                    sender.selectMatchResult(match);
                }
                catch (InvalidMatchID | GameIsFullException e) {
                    sender.sendError(e.getMessage());
                }
            }
            case ChooseNicknameMsg castedMsg -> {
                try {
                    gameController.chooseNickname(playerID, castedMsg.getNickname());

                    sender.chooseNicknameResult(castedMsg.getNickname());
                }
                catch (InvalidMatchID | NicknameAlreadyExistsException e) {
                    sender.sendError(e.getMessage());
                }
            }
            case null, default -> System.out.println("Invalid message type");
        }
    }

    public static void setGameController(GameController gameController) {
        Decoder.gameController = gameController;
    }
}
