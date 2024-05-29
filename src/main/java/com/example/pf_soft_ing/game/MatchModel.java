package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.decks.ObjectiveCardsDeck;
import com.example.pf_soft_ing.card.decks.StarterCardsDeck;
import com.example.pf_soft_ing.card.decks.UsableCardsDeck;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.InvalidVisibleCardIndexException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerRanker;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.*;

public class MatchModel {

    private final int maxPlayers;
    private int currPlayers = 0;
    private int playersReady = 0;
    private final int matchID;

    private final HashMap<Integer, PlayerModel> IDToPlayerMap = new HashMap<>();

    private UsableCardsDeck resourceCardsDeck;
    private UsableCardsDeck goldenCardsDeck;
    private ObjectiveCardsDeck objectiveCardsDeck;
    private StarterCardsDeck starterCardsDeck;

    private int currPlayerID = -1;
    private int firstPlayerID;
    private int[] orderOfPlayersIDs;

    private GameState gameState = GameState.PREGAME;

    private final List<TokenColors> tokenColors = new ArrayList<>(List.of(TokenColors.values()));

    private String[] nicknamesRanked;
    private int[] scoresRanked;
    private int[] numOfCompletedObjectivesRanked;

    public MatchModel(int maxPlayers, int matchID){
        this.maxPlayers = maxPlayers;

        this.matchID = matchID;
    }

    /**
     * Method to get a random token color
     * @return Token color
     */
    public TokenColors getTokenColor(){
        Random rand = new Random();
        int n = rand.nextInt(tokenColors.size());

        while (tokenColors.get(n) == TokenColors.BLACK){
            n = rand.nextInt(tokenColors.size());
        }

        TokenColors tokenColor = tokenColors.get(n);
        tokenColors.remove(n);
        return tokenColor;
    }

    // TODO: get rid of getIDToPlayerMap() and create the necessary methods
    /**
     * Getter
     * @return Map of player IDs to player models
     */
    public HashMap<Integer, PlayerModel> getIDToPlayerMap() {
        return IDToPlayerMap;
    }

    /**
     * Getter
     * @return All players' nicknames
     */
    public List<String> getNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (PlayerModel playerModel : IDToPlayerMap.values()){
            nicknames.add(playerModel.getNickname());
        }
        return nicknames;
    }

    /**
     * Getter
     * @return Number of maximum players in the match
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Getter
     * @return Number of players ready in the match
     */
    public int getPlayersReady() {
        return playersReady;
    }

    /**
     * Getter
     * @return ID of the match
     */
    public int getMatchID() {
        return matchID;
    }

    /**
     * Getter
     * @return Resource cards deck
     */
    public UsableCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * Getter
     * @return Golden cards deck
     */
    public UsableCardsDeck getGoldenCardsDeck() {
        return goldenCardsDeck;
    }

    /**
     * Getter
     * @return Objective cards deck
     */
    public ObjectiveCardsDeck getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    /**
     * Getter
     * @return Starter cards deck
     */
    public StarterCardsDeck getStarterCardsDeck() {
        return starterCardsDeck;
    }

    /**
     * Getter
     * @return ID of the player that is currently playing his turn
     */
    public int getCurrPlayerID() {
        return currPlayerID;
    }

    /**
     * Getter
     * @return Current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Setter
     * @param gameState New game state
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void addHost(PlayerModel playerModel){
        IDToPlayerMap.put(playerModel.getID(), playerModel);

        currPlayers++;
        playersReady++;
    }

    /**
     * Increase by one the number of players ready in the match
     */
    public void addReadyPlayer(){
        playersReady++;
    }

    /**
     * Increase by one the number of current players in the match
     */
    public void addCurrPlayer(PlayerModel playerModel) throws GameIsFullException {
        if (currPlayers >= maxPlayers){
            // If there are no players disconnected, the match is full
            if (allPlayersOnline()) {
                throw new GameIsFullException();
            }
            // If there are players disconnected
            else {
                // TODO: implement reconnection
                playersReady++;
            }
        }
        IDToPlayerMap.put(playerModel.getID(), playerModel);

        currPlayers++;
    }

    /**
     * Copies the deck from the game resources
     */
    public void initializeDecks(){
        GameResources.initializeAllDecks();

        resourceCardsDeck = new UsableCardsDeck(GameResources.getResourcesDeck());
        goldenCardsDeck = new UsableCardsDeck(GameResources.getGoldenDeck());
        objectiveCardsDeck = new ObjectiveCardsDeck(GameResources.getObjectiveDeck());
        starterCardsDeck = new StarterCardsDeck(GameResources.getStarterDeck());
    }

    /**
     * Method to shuffle all decks
     */
    public void shuffleAllDecks(){
        resourceCardsDeck.shuffleDeck();
        goldenCardsDeck.shuffleDeck();
        objectiveCardsDeck.shuffleDeck();
        starterCardsDeck.shuffleDeck();
    }

    /**
     * Sets the visible cards list in the golden and resource card decks
     */
    public void setVisibleCards () {
        resourceCardsDeck.restoreInitialVisibleCards();
        goldenCardsDeck.restoreInitialVisibleCards();
    }

    /**
     * Getter
     * @return Visible resource cards
     */
    public List<PlaceableCard> getVisibleResourceCards(){
        return resourceCardsDeck.getVisibleCards();
    }

    /**
     * Getter
     * @return Visible golden cards
     */
    public List<PlaceableCard> getVisibleGoldenCards(){
        return goldenCardsDeck.getVisibleCards();
    }

    /**
     * Getter
     * @return Nicknames of the players ranked by their final scores
     */
    public String[] getNicknamesRanked() {
        return nicknamesRanked;
    }

    /**
     * Getter
     * @return Final scores of the players ranked
     */
    public int[] getScoresRanked() {
        return scoresRanked;
    }

    /**
     * Getter
     * @return Array that contains the number of completed objectives of the players ranked
     */
    public int[] getNumOfCompletedObjectivesRanked() {
        return numOfCompletedObjectivesRanked;
    }

    /**
     * Method to draw the first resource card of the deck
     * @return First resource card of the deck
     */
    public PlaceableCard drawResourceCard() throws NotEnoughCardsException {
        return resourceCardsDeck.drawCard();
    }

    /**
     * Method to draw the first golden card of the deck
     * @return First golden card of the deck
     */
    public PlaceableCard drawGoldenCard() throws NotEnoughCardsException {
        return goldenCardsDeck.drawCard();
    }

    /**
     * Method to draw the first starter card of the deck
     * @return First starter card of the deck
     */
    public PlaceableCard drawStarterCard(){
        return starterCardsDeck.drawCard();
    }

    /**
     * Method to draw the first objective card of the deck
     * @return First objective card of the deck
     */
    public ObjectiveCard drawObjectiveCard(){
        return objectiveCardsDeck.drawCard();
    }

    /**
     * Method to draw the visible resource card of the deck with the given ID
     * @param index index of the card to draw (either 0 or 1)
     * @return Visible resource card of the deck with the given ID
     */
    public PlaceableCard drawVisibleResourceCard(int index) throws InvalidVisibleCardIndexException {
        return resourceCardsDeck.drawVisibleCard(index);
    }

    /**
     * Method to draw the visible golden card of the deck with the given ID
     * @param index index of the card to draw (either 0 or 1)
     * @return Visible golden card of the deck with the given ID
     */
    public PlaceableCard drawVisibleGoldenCard(int index) throws InvalidVisibleCardIndexException {
        return goldenCardsDeck.drawVisibleCard(index);
    }

    /**
     * Method to restore the visible resource cards
     */
    public void restoreVisibleResourceCard (){
        if (!resourceCardsDeck.isDeckEmpty()){
            resourceCardsDeck.restoreVisibleCard();
        }
        else { // if (!goldenCardsDeck.isDeckEmpty())
            try {
                resourceCardsDeck.restoreVisibleCardWithOtherDeck(goldenCardsDeck.drawCard());
            } catch (NotEnoughCardsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method to restore the visible golden cards
     */
    public void restoreVisibleGoldenCard (){
        if (!goldenCardsDeck.isDeckEmpty()){
            goldenCardsDeck.restoreVisibleCard();
        }
        else { // if (!resourceCardsDeck.isDeckEmpty())
            try {
                goldenCardsDeck.restoreVisibleCardWithOtherDeck(resourceCardsDeck.drawCard());
            } catch (NotEnoughCardsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method to randomly choose the first player and give him the black token
     */
    public void setRandomFirstPlayer(){
        for (Integer i : IDToPlayerMap.keySet()){
            IDToPlayerMap.get(i).setAsFirstPlayer(false);
            IDToPlayerMap.get(i).setState(PlayerState.WAITING);
        }

        List<Integer> playerIDList = new ArrayList<>(IDToPlayerMap.keySet());

        firstPlayerID = playerIDList.get((int) Math.round(Math.random() * (playerIDList.size() - 1)));
        currPlayerID = firstPlayerID;

        PlayerModel firstPlayer = IDToPlayerMap.get(firstPlayerID);
        firstPlayer.setAsFirstPlayer(true);
        firstPlayer.setState(PlayerState.PLACING);
    }

    /**
     * Method to calculate the order of the players based on the first player
     */
    public void calculateOrderOfPlayers() {
        orderOfPlayersIDs = new int[IDToPlayerMap.size()];

        orderOfPlayersIDs[0] = firstPlayerID;

        int pos = 1;
        for (Integer i : IDToPlayerMap.keySet()){
            if (i != firstPlayerID){
                orderOfPlayersIDs[pos] = i;
                pos++;
            }
        }
    }

    /**
     * Method to end the turn of the current player and pass the turn to
     * the next player in the order
     */
    public void endTurn(){
        IDToPlayerMap.get(currPlayerID).setState(PlayerState.WAITING);

        int currPlayerIndex = 0;
        for (int i = 0; i < orderOfPlayersIDs.length; i++){
            if (orderOfPlayersIDs[i] == currPlayerID){
                currPlayerIndex = i;
                break;
            }
        }

        if (gameState == GameState.PLAYING){
            checkForEndGame();
        }

        boolean hasLooped = false;
        boolean foundNextPlayer = false;
        while (!foundNextPlayer){
            if (currPlayerIndex == orderOfPlayersIDs.length - 1){
                currPlayerIndex = 0;
                hasLooped = true;
            }
            else {
                currPlayerIndex++;
            }

            if (IDToPlayerMap.get(orderOfPlayersIDs[currPlayerIndex]).getState() != PlayerState.DISCONNECTED){
                foundNextPlayer = true;
            }
        }

        if (hasLooped){
            manageEndGame();
        }
        currPlayerID = orderOfPlayersIDs[currPlayerIndex];

        if (gameState != GameState.END_GAME){
            IDToPlayerMap.get(currPlayerID).setState(PlayerState.PLACING);
        }
    }

    /**
     * Method to manage game state transitions
     */
    public void checkForEndGame(){
        if (IDToPlayerMap.get(currPlayerID).getCurrScore() >= 20 || (resourceCardsDeck.isDeckEmpty() && goldenCardsDeck.isDeckEmpty())){
            gameState = GameState.FINAL_ROUND;
        }
    }

    /**
     * Method to manage the final rounds of the game
     */
    public void manageEndGame() {
        if (gameState == GameState.FINAL_ROUND){
            gameState = GameState.EXTRA_ROUND;
        }
        else if (gameState == GameState.EXTRA_ROUND){
            gameState = GameState.END_GAME;
            determineRanking();
        }
    }

    /**
     * Method to determine the ranking of the players based on their final scores
     */
    public void determineRanking() {
        List<PlayerModel> players = new LinkedList<>(IDToPlayerMap.values());
        nicknamesRanked = new String[players.size()];
        scoresRanked = new int[players.size()];
        numOfCompletedObjectivesRanked = new int[players.size()];

        for (PlayerModel player : IDToPlayerMap.values()) {
            player.calculateObjectivePoints(player.getSecretObjective());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().getFirst());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().get(1));
        }

        players.sort(new PlayerRanker());

        for (PlayerModel player : players) {
            nicknamesRanked[players.indexOf(player)] = player.getNickname();
            scoresRanked[players.indexOf(player)] = player.getCurrScore();
            numOfCompletedObjectivesRanked[players.indexOf(player)] = player.getNumOfCompletedObjectives();
        }
    }

    public boolean checkForTurnOrderPhase() {
        for (PlayerModel player : IDToPlayerMap.values()){
            if (player.getState() != PlayerState.COMPLETED_SETUP){
                return false;
            }
        }
        return true;
    }

    public void startTurnOrderPhase() {
        for (PlayerModel player : IDToPlayerMap.values()){
            player.setState(PlayerState.WAITING);
        }

        setGameState(GameState.PLAYING);

        setRandomFirstPlayer();

        calculateOrderOfPlayers();
    }

    /**
     * Method to check if the nickname is already taken by another player
     * @param nickname Nickname to check
     * @throws NicknameAlreadyExistsException if the nickname is already taken or "all"
     */
    public void checkNickname(String nickname) throws NicknameAlreadyExistsException {
        if (nickname.equals("all")){
            throw new NicknameAlreadyExistsException();
        }

        for (String otherNickname : getNicknames()) {
            if (otherNickname.equals(nickname)) {
                throw new NicknameAlreadyExistsException();
            }
        }
    }

    /**
     * Getter
     * @return Map of player IDs to their nicknames
     */
    public Map<Integer, String> getIDtoNicknameMap() {
        Map<Integer, String> IDToNickname = new HashMap<>();

        for (PlayerModel player : IDToPlayerMap.values()){
            IDToNickname.put(player.getID(), player.getNickname());
        }
        return IDToNickname;
    }

    /**
     * Removes the player with the given ID from the match
     * @param playerID ID of the player to remove
     */
    public void removePlayer(int playerID) {
        IDToPlayerMap.remove(playerID);
        currPlayers--;
        playersReady--;
    }

    /**
     * Method to check if the match has no players
     * @return true if the match has no players, false otherwise
     */
    public boolean hasNoPlayers() {
        return currPlayers == 0;
    }

    /**
     * Method to check if all players are disconnected
     * @return true if the match has no players online, false otherwise
     */
    public boolean hasNoPlayersOnline() {
        for (PlayerModel player : IDToPlayerMap.values()){
            if (player.getState() != PlayerState.DISCONNECTED){
                return false;
            }
        }
        return true;
    }

    /**
     * Method to check if all players are online
     * @return true if all players are online, false otherwise
     */
    private boolean allPlayersOnline() {
        for (PlayerModel player : IDToPlayerMap.values()){
            if (player.getState() == PlayerState.DISCONNECTED){
                return false;
            }
        }
        return true;
    }

    public void reducePlayersOnline(int playerID) {
        // TODO: implement timer before announcing the last connected player as winner
    }

    public void undoCardPlacement(int playerID) {
        IDToPlayerMap.get(playerID).undoCardPlacement();
    }
}
