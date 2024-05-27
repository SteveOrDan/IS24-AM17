package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.player.PlayerModel;

import java.util.List;

public class GameController {

    private final GameModel gameModel = new GameModel();

    public MatchController getMatchByID(int matchID) throws InvalidMatchIDException {
        return gameModel.getMatchByID(matchID);
    }

    /**
     * Sender
     * @send Map of match IDs with the corresponding nicknames to client
     */
    public void getMatches(int playerID) {
        getPlayerSender(playerID).sendMatches(gameModel.getMatches(), playerID);
    }

    /**
     * Creates a new match in the game model
     * @param playerID ID of the host player
     * @param numberOfPlayers Maximum number of players in the match
     * @param nickname Nickname of the host player
     */
    public MatchController createMatch(int playerID, int numberOfPlayers, String nickname) {
        // Creates a new match in the game model
        try {
            MatchController matchController = gameModel.createGame(playerID, numberOfPlayers, nickname);
            getPlayerSender(playerID).createMatchResult(matchController.getMatchID(), nickname);
            return matchController;
        }
        catch (InvalidNumOfPlayersException | InvalidPlayerStateException e) {
            getPlayerSender(playerID).sendError(e.getMessage());
            return null; // It's OK -> the match is supposed to stay null if create match fails
        }
    }

    /**
     * Allows a player to occupy a slot in a match without a nickname
     * The player will be marked as "ready" only after choosing a nickname
     * @param playerID ID of the player
     * @param matchID ID of the match
     */
    public MatchController selectMatch(int playerID, int matchID) {
        // The player occupies a slot in the match (without a nickname)
        try {
        MatchController matchController = gameModel.selectMatch(playerID, matchID);
        getPlayerSender(playerID).selectMatchResult(matchController.getMatchID(), matchController.getNicknames());
        return matchController;
        }
        catch (InvalidMatchIDException | GameIsFullException e) {
            getPlayerSender(playerID).sendError(e.getMessage());
            return null; // It's OK -> the match is supposed to stay null if select match fails
        }
    }

    /**
     * Allows a player to choose a nickname for a match and marks the player as "ready"
     * @param playerID ID of the player
     * @param nickname Nickname of the player
     */
    public void chooseNickname(int playerID, String nickname, MatchController matchController) {
        // The player chooses a nickname for the match and is completely added to the match
        try {
            if (nickname == null || nickname.isEmpty() || nickname.isBlank() ||
                    nickname.equalsIgnoreCase("all") || nickname.equalsIgnoreCase("(you)")) {
                throw new InvalidNicknameException();
            }

            gameModel.chooseNickname(playerID, nickname);

            // if match reached max players, start the game ; else send the nickname to the player
            if (matchController.checkForGameStart()) {
                gameModel.startGame(matchController);

                List<PlaceableCard> visibleResCards = matchController.getVisibleResourceCards();
                List<PlaceableCard> visibleGoldCards = matchController.getVisibleGoldenCards();

                PlaceableCard resDeckCardID = matchController.checkFirstResDeckCard();
                PlaceableCard goldDeckCardID = matchController.checkFirstGoldDeckCard();

                matchController.setCommonObjectives();

                for (Integer ID : matchController.getIDToPlayerMap().keySet()) {
                    String thisNickname = matchController.getIDToPlayerMap().get(ID).getNickname();

                    PlaceableCard starterCard = matchController.drawStarterCard();

                    matchController.getIDToPlayerMap().get(ID).setStarterCard(starterCard);

                    getPlayerSender(ID).sendGameStart(thisNickname, matchController.getIDToNicknameMap(),
                            resDeckCardID.getID(), visibleResCards.getFirst().getID(), visibleResCards.get(1).getID(),
                            goldDeckCardID.getID(), visibleGoldCards.getFirst().getID(), visibleGoldCards.get(1).getID(),
                            starterCard.getID());
                }
            }
            else {
                getPlayerSender(playerID).chooseNicknameResult(nickname);
            }
        }
        catch (InvalidMatchIDException | NicknameAlreadyExistsException | InvalidNicknameException e) {
            getPlayerSender(playerID).sendError(e.getMessage());
        }
    }

    /**
     * Creates a new player in the game model
     * @param sender Sender of the player
     * @return PlayerModel created with a give sender
     */
    public PlayerModel createPlayer(Sender sender){
        return gameModel.createPlayer(sender);
    }

    /**
     * Getter
     * @param playerID ID of the player
     * @return Match ID of the player with the given ID
     */
    public int getMatchIDWithPlayer(int playerID) {
        return gameModel.getIDToPlayers().get(playerID).getMatchID();
    }

    /**
     * Getter
     * @param playerID ID of the player
     * @return Sender of the player with the given ID
     */
    public Sender getPlayerSender(int playerID) {
        return gameModel.getIDToPlayers().get(playerID).getSender();
    }

    public void checkMatchState(MatchController matchController) {
        if (matchController.hasNoPlayers() || matchController.hasNoPlayersOnline()) {
            gameModel.removeMatch(matchController.getMatchID());
        }
    }

    public boolean matchNotPresent(MatchController matchController) {
        return !gameModel.containsMatch(matchController);
    }
}
