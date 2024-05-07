package com.example.pf_soft_ing.game;

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
     * Getter
     * @return Map of match IDs with the corresponding nicknames
     */
    public Map<Integer, List<String>> getMatches() {
        return gameModel.getMatches();
    }

    /**
     * Creates a new match in the game model
     * @param playerID ID of the host player
     * @param numberOfPlayers Maximum number of players in the match
     * @param nickname Nickname of the host player
     * @throws InvalidNumOfPlayersException If the number of players is not between 2 and 4
     */
    public MatchController createMatch(int playerID, int numberOfPlayers, String nickname) throws InvalidNumOfPlayersException, InvalidPlayerStateException, GameIsFullException {
        // Creates a new match in the game model
        return gameModel.createGame(playerID, numberOfPlayers, nickname);
    }

    /**
     * Allows a player to occupy a slot in a match without a nickname
     * The player will be marked as "ready" only after choosing a nickname
     * @param playerID ID of the player
     * @param matchID ID of the match
     * @throws InvalidMatchIDException If the match ID is invalid
     */
    public MatchController selectMatch(int playerID, int matchID) throws InvalidMatchIDException, GameIsFullException {
        // The player occupies a slot in the match (without a nickname)
        return gameModel.selectMatch(playerID, matchID);
    }

    /**
     * Allows a player to choose a nickname for a match and marks the player as "ready"
     * @param playerID ID of the player
     * @param nickname Nickname of the player
     * @throws InvalidMatchIDException If the match ID doesn't exists
     * @throws NicknameAlreadyExistsException If the nickname is already taken by another player in the same match
     */
    public void chooseNickname(int playerID, String nickname) throws InvalidMatchIDException, NicknameAlreadyExistsException {
        // The player chooses a nickname for the match and is completely added to the match
        gameModel.chooseNickname(playerID, nickname);
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
