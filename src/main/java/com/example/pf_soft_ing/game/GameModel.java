package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.ServerConnection.Sender;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;

import java.util.*;

public class GameModel {

    private final List<MatchController> matches = new ArrayList<>();
    private final Map<Integer, PlayerModel> IDToPlayers = new HashMap<>();

    /**
     * Getter
     * @return List of matches
     */
    public List<MatchController> getMatches(){
        return matches;
    }

    /**
     * Getter
     * @return Map of player IDs with the corresponding player models
     */
    public Map<Integer, PlayerModel> getIDToPlayers() {
        return IDToPlayers;
    }

    /**
     * Getter
     * @param matchID ID of the match
     * @return MatchController with the given ID
     */
    public MatchController getMatchByID(int matchID) throws InvalidMatchID {
        for (MatchController match : matches) {
            if (match.getMatchModel().getMatchID() == matchID) {
                return match;
            }
        }
        throw new InvalidMatchID();
    }

    /**
     * Creates a new match in the game model
     * @param playerID ID of the host player
     * @param numberOfPlayers Maximum number of players in the match
     * @param nickname Nickname of the host player
     * @throws InvalidNumOfPlayers If the number of players is not between 2 and 4
     */
    public MatchController createGame(int playerID, int numberOfPlayers, String nickname) throws InvalidNumOfPlayers, InvalidPlayerStateException {
        if (numberOfPlayers > 4 || numberOfPlayers < 2){
            throw new InvalidNumOfPlayers();
        }
        if (IDToPlayers.get(playerID).getMatchID() != 0){
            throw new InvalidPlayerStateException(IDToPlayers.get(playerID).getState().name(), "MAIN_MENU");
        }
        List<Integer> matchIDs = new ArrayList<>();
        matches.forEach(match -> matchIDs.add(match.getMatchModel().getMatchID()));

        Random rng = new Random();
        int newID = rng.nextInt(1000);

        while(matchIDs.contains(newID)){
            newID = rng.nextInt(1000);
        }

        MatchController match = new MatchController(numberOfPlayers, newID);
        IDToPlayers.get(playerID).setNickname(nickname);
        IDToPlayers.get(playerID).setState(PlayerState.MATCH_LOBBY);

        matches.add(match);

        return match;
    }

    /**
     * Allows a player to occupy a slot in a match without a nickname
     * The player will be marked as "ready" only after choosing a nickname
     * @param playerID ID of the player
     * @param matchID ID of the match
     * @throws InvalidMatchID If the match ID is invalid
     */
    public MatchController selectMatch(int playerID, int matchID) throws InvalidMatchID, GameIsFullException {
        getMatchByID(matchID).getMatchModel().addCurrPlayer(IDToPlayers.get(playerID));

        return getMatchByID(matchID);
    }

    /**
     * Allows a player to choose a nickname for a match and marks the player as "ready"
     * @param playerID ID of the player
     * @param nickname Nickname of the player
     * @throws InvalidMatchID If the match ID doesn't exists
     * @throws NicknameAlreadyExistsException If the nickname is already taken by another player in the same match
     */
    public void chooseNickname(int playerID, String nickname) throws InvalidMatchID, NicknameAlreadyExistsException {
        int matchID = IDToPlayers.get(playerID).getMatchID();

        MatchModel matchModel = getMatchByID(matchID).getMatchModel();

        for (String otherNickname : matchModel.getNicknames()) {
            if (otherNickname.equals(nickname)) {
                throw new NicknameAlreadyExistsException();
            }
        }

        IDToPlayers.get(playerID).setNickname(nickname);

        matchModel.addReadyPlayer();
    }

    /**
     * Creates a new player
     * @param sender Sender of the player
     * @return ID of the new player
     */
    public int createPlayer(Sender sender){
        Random rng = new Random();

        int newID = rng.nextInt(1000);

        for (Integer ID : IDToPlayers.keySet()) {
            if (ID == newID) {
                newID = rng.nextInt(1000);
            }
        }

        PlayerModel player = new PlayerModel(newID, sender);
        IDToPlayers.put(newID, player);

        return newID;
    }

    /**
     * Starts the game if the match has reached the maximum number of players
     * @param matchController MatchController of the match
     */
    public void startGame(MatchController matchController){
        new Thread(matchController::setUpGame).start();
    }
}
