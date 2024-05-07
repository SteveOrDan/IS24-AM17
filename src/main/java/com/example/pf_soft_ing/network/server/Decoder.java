package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.network.messages.*;
import com.example.pf_soft_ing.network.messages.requests.*;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

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
                Map<Integer, List<String>> matches = gameController.getMatches();

                sender.sendMatches(matches);
            }
            case CreateMatchMsg castedMsg -> {
                try {
                    matchController = gameController.createMatch(playerID, castedMsg.getNumberOfPlayers(), castedMsg.getNickname());

                    sender.createMatchResult(matchController.getMatchModel().getMatchID(), castedMsg.getNickname());
                }
                catch (InvalidNumOfPlayers | InvalidPlayerStateException | GameIsFullException e) {
                    sender.sendError(e.getMessage());
                }
            }
            case SelectMatchMsg castedMsg -> {
                try {
                    matchController = gameController.selectMatch(playerID, castedMsg.getMatchID());

                    sender.selectMatchResult(matchController.getMatchModel().getMatchID(), matchController.getMatchModel().getNicknames());
                }
                catch (InvalidMatchID | GameIsFullException e) {
                    sender.sendError(e.getMessage());
                }
            }
            case ChooseNicknameMsg castedMsg -> {
                try {
                    gameController.chooseNickname(playerID, castedMsg.getNickname());

                    // if match reached max players, start the game ; else send the nickname to the player
//                    MatchController matchController = gameController.getMatchWithPlayer(playerID);

                    if (gameController.checkForGameStart(matchController)) {
                        gameController.getGameModel().startGame(matchController);

                        List<PlaceableCard> visibleResCards = matchController.getVisibleResourceCards();
                        List<PlaceableCard> visibleGoldCards = matchController.getVisibleGoldenCards();

                        PlaceableCard resDeckCardID = matchController.getMatchModel().getResourceCardsDeck().getDeck().getFirst();
                        PlaceableCard goldDeckCardID = matchController.getMatchModel().getGoldenCardsDeck().getDeck().getFirst();

                        for (Integer ID : matchController.getIDToPlayerMap().keySet()) {
                            PlaceableCard starterCard = matchController.getMatchModel().drawStarterCard();

                            matchController.getIDToPlayerMap().get(ID).setStarterCard(starterCard);

                            gameController.getPlayerSender(ID).sendGameStart(resDeckCardID.getID(), visibleResCards.getFirst().getID(), visibleResCards.get(1).getID(),
                                    goldDeckCardID.getID(), visibleGoldCards.getFirst().getID(), visibleGoldCards.get(1).getID(),
                                    starterCard.getID());
                        }
                    }
                    else {
                        sender.chooseNicknameResult(castedMsg.getNickname());
                    }
                }
                catch (InvalidMatchID | NicknameAlreadyExistsException e) {
                    sender.sendError(e.getMessage());
                }
            }
            case PlaceStarterCardMsg castedMsg -> {
                // TODO: Check if the way of implementing current card side usage is correct
                matchController.getIDToPlayerMap().get(playerID).placeStarterCard(castedMsg.getSide());
            }
            case null, default -> System.out.println("Invalid message type");
        }
    }

    public static void setGameController(GameController gameController) {
        Decoder.gameController = gameController;
    }
}
