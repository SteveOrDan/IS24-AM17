package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.decks.ObjectiveCardsDeck;
import com.example.pf_soft_ing.card.decks.StarterCardsDeck;
import com.example.pf_soft_ing.card.decks.UsableCardsDeck;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.InvalidVisibleCardIndexException;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerRanker;
import com.example.pf_soft_ing.player.PlayerState;

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

    private int currPlayerID;
    private int firstPlayerID;
    private int[] orderOfPlayersIDs;

    private GameState gameState = GameState.PREGAME;

    public MatchModel(int maxPlayers, int matchID){
        this.maxPlayers = maxPlayers;

        this.matchID = matchID;
    }

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
     * @return A map with other players ID (as key) and nickname (as value)
     */
    public Map<Integer, String> getNicknamesMap(int currPlayerID) {
        Map<Integer, String> nicknamesMap = new HashMap<>();
        for (PlayerModel playerModel : IDToPlayerMap.values()){
            if (playerModel.getID() != currPlayerID) {
                nicknamesMap.put(playerModel.getID(), playerModel.getNickname());
            }
        }
        return nicknamesMap;
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
     * @return Number of current players in the match
     */
    public int getCurrPlayers() {
        return currPlayers;
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
            throw new GameIsFullException();
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
        if (currPlayerIndex == orderOfPlayersIDs.length - 1){
            currPlayerID = orderOfPlayersIDs[0];
            manageEndGame();
        }
        else {
            currPlayerID = orderOfPlayersIDs[currPlayerIndex + 1];
        }

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

        for (PlayerModel player : IDToPlayerMap.values()){
            player.calculateObjectivePoints(player.getSecretObjective());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().getFirst());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().get(1));
        }

        players.sort(new PlayerRanker());

        int pos = 1;
        int i = 0;

        for (PlayerModel p : players){
            System.out.println("Number " + pos + " Player is " + p.getNickname());

            if (i != players.size() - 1 && (p.getCurrScore() != players.get(i + 1).getCurrScore() ||
                    p.getNumOfCompletedObjectives() != players.get(i + 1).getNumOfCompletedObjectives())) {
                pos++;
            }

            i++;
        }
    }
}
