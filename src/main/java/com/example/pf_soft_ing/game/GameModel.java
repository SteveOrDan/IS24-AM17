package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;

import java.util.*;

public class GameModel {

    private final List<MatchController> matches = new ArrayList<>();
    private final Map<Integer, PlayerModel> IDToPlayers = new HashMap<>();

    /**
     * Getter
     * @return Map of match IDs with the corresponding nicknames
     */
    public Map<Integer, List<String>> getMatches(){
        Map<Integer, List<String>> allMatches = new LinkedHashMap<>();
        List<MatchController> matchesCopy;

        synchronized (matches){
            matchesCopy = new ArrayList<>(matches);
        }

        for (MatchController match : matchesCopy){
            List<String> nicknames = new ArrayList<>(match.getNicknames());
            allMatches.put(match.getMatchID(), nicknames);
        }
        return allMatches;
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
    public MatchController getMatchByID(int matchID) throws InvalidMatchIDException {
        List<MatchController> matchesCopy;

        synchronized (matches){
            matchesCopy = new ArrayList<>(matches);
        }

        for (MatchController match : matchesCopy) {
            if (match.getMatchID() == matchID) {
                return match;
            }
        }
        throw new InvalidMatchIDException();
    }

    /**
     * Creates a new match in the game model
     * @param playerID ID of the host player
     * @param numberOfPlayers Maximum number of players in the match
     * @param nickname Nickname of the host player
     * @throws InvalidNumOfPlayersException If the number of players is not between 2 and 4
     */
    public MatchController createGame(int playerID, int numberOfPlayers, String nickname) throws InvalidNumOfPlayersException, InvalidPlayerStateException {
        if (numberOfPlayers > 4 || numberOfPlayers < 2){
            throw new InvalidNumOfPlayersException();
        }

        PlayerModel player = IDToPlayers.get(playerID);

        if (player.getState() != PlayerState.MAIN_MENU){
            throw new InvalidPlayerStateException(player.getState().name(), PlayerState.MAIN_MENU.name());
        }

        List<Integer> matchIDs = new ArrayList<>();

        synchronized (matches) {
            matches.forEach(match -> matchIDs.add(match.getMatchID()));
            Random rng = new Random();
            int newID = rng.nextInt(1000);

            while(matchIDs.contains(newID)){
                newID = rng.nextInt(1000);
            }

            MatchController match = new MatchController(numberOfPlayers, newID);

            match.addHost(player);

            player.setNickname(nickname);
            player.setState(PlayerState.MATCH_LOBBY);
            player.setMatchID(newID);

            matches.addFirst(match);

            return match;
        }
    }

    /**
     * Allows a player to occupy a slot in a match without a nickname
     * The player will be marked as "ready" only after choosing a nickname
     * @param playerID ID of the player
     * @param matchID ID of the match
     * @throws InvalidMatchIDException If the match ID is invalid
     */
    public MatchController selectMatch(int playerID, int matchID) throws InvalidMatchIDException, GameIsFullException {
        PlayerModel player = IDToPlayers.get(playerID);

        getMatchByID(matchID).addCurrPlayer(player);

        player.setState(PlayerState.MATCH_LOBBY);
        player.setMatchID(matchID);

        return getMatchByID(matchID);
    }

    /**
     * Allows a player to choose a nickname for a match and marks the player as "ready"
     * @param playerID ID of the player
     * @param nickname Nickname of the player
     * @throws InvalidMatchIDException If the match ID doesn't exists
     * @throws NicknameAlreadyExistsException If the nickname is already taken by another player in the same match
     */
    public void chooseNickname(int playerID, String nickname) throws InvalidMatchIDException, NicknameAlreadyExistsException {
        int matchID = IDToPlayers.get(playerID).getMatchID();

        MatchController matchController = getMatchByID(matchID);

        matchController.checkNickname(nickname);

        IDToPlayers.get(playerID).setNickname(nickname);

        matchController.addReadyPlayer();
    }

    /**
     * Creates a new player
     * @param sender Sender of the player
     * @return ID of the new player
     */
    public PlayerModel createPlayer(Sender sender){
        Random rng = new Random();

        int newID = rng.nextInt(1000);

        for (Integer ID : IDToPlayers.keySet()) {
            if (ID == newID) {
                newID = rng.nextInt(1000);
            }
        }

        PlayerModel player = new PlayerModel(newID, sender);
        IDToPlayers.put(newID, player);

        return player;
    }

    /**
     * Starts the game if the match has reached the maximum number of players
     * @param matchController MatchController of the match
     */
    public void startGame(MatchController matchController){
        matchController.setUpGame();
    }
}
