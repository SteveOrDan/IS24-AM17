package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.exceptions.*;

import java.util.List;
import java.util.Map;

public class GameController {

    private final GameModel gameModel = new GameModel();

    /**
     * Getter
     * @return GameModel
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Sender
     * @send Map of match IDs with the corresponding nicknames to client
     */
    public void getMatches(int playerID) {
        gameModel.getIDToPlayers().get(playerID).getSender().sendMatches(gameModel.getMatches(), playerID);
    }

    /**
     * Creates a new match in the game model
     * @param playerID ID of the host player
     * @param numberOfPlayers Maximum number of players in the match
     * @param nickname Nickname of the host player
     * @throws InvalidNumOfPlayersException If the number of players is not between 2 and 4
     */
    public void createMatch(int playerID, int numberOfPlayers, String nickname) {
        // Creates a new match in the game model
        try {
            MatchController matchController = gameModel.createGame(playerID, numberOfPlayers, nickname);
            gameModel.getIDToPlayers().get(playerID).getSender().createMatchResult(matchController.getMatchModel().getMatchID(), nickname);
            //TODO synchronize on matchController
        }
        catch (InvalidNumOfPlayersException | InvalidPlayerStateException | GameIsFullException e) {
            gameModel.getIDToPlayers().get(playerID).getSender().sendError(e.getMessage());
        }
    }

    /**
     * Allows a player to occupy a slot in a match without a nickname
     * The player will be marked as "ready" only after choosing a nickname
     * @param playerID ID of the player
     * @param matchID ID of the match
     * @throws InvalidMatchIDException If the match ID is invalid
     */
    public void selectMatch(int playerID, int matchID) {
        // The player occupies a slot in the match (without a nickname)
        try {
        MatchController matchController = gameModel.selectMatch(playerID, matchID);
        gameModel.getIDToPlayers().get(playerID).getSender().selectMatchResult(matchController.getMatchModel().getMatchID(), matchController.getMatchModel().getNicknames());
        //TODO synchronize on matchController
        }
        catch (InvalidMatchIDException | GameIsFullException e) {
            gameModel.getIDToPlayers().get(playerID).getSender().sendError(e.getMessage());
        }
    }

    /**
     * Allows a player to choose a nickname for a match and marks the player as "ready"
     * @param playerID ID of the player
     * @param nickname Nickname of the player
     * @throws InvalidMatchIDException If the match ID doesn't exists
     * @throws NicknameAlreadyExistsException If the nickname is already taken by another player in the same match
     */
    public void chooseNickname(int playerID, String nickname, MatchController matchController) {
        // The player chooses a nickname for the match and is completely added to the match
        try {
            gameModel.chooseNickname(playerID, nickname);

            // if match reached max players, start the game ; else send the nickname to the player
//                    MatchController matchController = gameController.getMatchWithPlayer(playerID);

            if (checkForGameStart(matchController)) {
                getGameModel().startGame(matchController);

                List<PlaceableCard> visibleResCards = matchController.getVisibleResourceCards();
                List<PlaceableCard> visibleGoldCards = matchController.getVisibleGoldenCards();

                PlaceableCard resDeckCardID = matchController.getMatchModel().getResourceCardsDeck().getDeck().getFirst();
                PlaceableCard goldDeckCardID = matchController.getMatchModel().getGoldenCardsDeck().getDeck().getFirst();

                matchController.setCommonObjectives();

                for (Integer ID : matchController.getIDToPlayerMap().keySet()) {
                    PlaceableCard starterCard = matchController.getMatchModel().drawStarterCard();

                    matchController.getIDToPlayerMap().get(ID).setStarterCard(starterCard);

                    getPlayerSender(ID).sendGameStart(resDeckCardID.getID(), visibleResCards.getFirst().getID(), visibleResCards.get(1).getID(),
                            goldDeckCardID.getID(), visibleGoldCards.getFirst().getID(), visibleGoldCards.get(1).getID(),
                            starterCard.getID());
                }
            } else {
                gameModel.getIDToPlayers().get(playerID).getSender().chooseNicknameResult(nickname);
            }
        }catch (InvalidMatchIDException | NicknameAlreadyExistsException e) {
            gameModel.getIDToPlayers().get(playerID).getSender().sendError(e.getMessage());
        }
    }

    /**
     * Starts the game if the match has reached the maximum number of players
     * @param matchController MatchController of the match
     */
    public boolean checkForGameStart(MatchController matchController) {
        MatchModel match = matchController.getMatchModel();

        if (match.getPlayersReady() == match.getMaxPlayers()){
            gameModel.startGame(matchController);
            return true;
        }

        return false;
    }

    public MatchController getMatchWithPlayer(int playerID) throws InvalidPlayerIDException {
        for (MatchController match : gameModel.getMatchesList()){
            if (match.getIDToPlayerMap().containsKey(playerID)){
                return match;
            }
        }

        throw new InvalidPlayerIDException();
    }

    /**
     * Getter
     * @param playerID ID of the player
     * @return Sender of the player with the given ID
     */
    public Sender getPlayerSender(int playerID) {
        return gameModel.getIDToPlayers().get(playerID).getSender();
    }

// Broadcast Area === KEEP OUT!!!! ===
//    public List<Sender> getAllOtherSenders(int playerID){
//        for (PlayerModel player : gameModel.getIDToPlayers().values()){
//
//        }
//    }
//
//    public List<Sender> getOpponentSenders(int matchID, int playerID){
//
//    }
}
