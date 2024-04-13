package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.InvalidVisibleCardIndexException;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameModel {
    private final HashMap<Integer, PlayerModel> IDToPlayerMap;
    private final HashMap<Integer, Integer> board;
    private UsableCardsDeck resourceCardsDeck;
    private UsableCardsDeck goldenCardsDeck;
    private ObjectiveCardsDeck objectiveCardsDeck;
    private StarterCardsDeck starterCardsDeck;
    private final List<Integer> playerIDList;
    private int currPlayerID;
    private int firstPlayerID;
    private int[] orderOfPlayersIDs;
    private GameState gameState;

    public GameModel() {
        this.playerIDList = new ArrayList<>();
        this.board = new HashMap<>();

        IDToPlayerMap = new HashMap<>();
        gameState = GameState.PREGAME;
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
     * Getter
     * @return IDToPlayerMap with keys as player IDs and values as PlayerModel objects
     */
    public HashMap<Integer, PlayerModel> getIDToPlayerMap() {
        return IDToPlayerMap;
    }

    /**
     * Getter
     * @return Board map with keys as player IDs and values as player score
     */
    public HashMap<Integer, Integer> getBoard() {
        return board;
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
     * @return List of players' IDs
     */
    public List<Integer> getPlayerIDList() {
        return playerIDList;
    }

    /**
     * Getter
     * @return ID of the player that is currently playing his turn
     */
    public int getCurrPlayerID() {
        return currPlayerID;
    }

    /**
     * Setter
     * @param currPlayerID ID of the player that is currently playing his turn
     */
    public void setCurrPlayerID(int currPlayerID) {
        this.currPlayerID = currPlayerID;
    }

    /**
     * Getter
     * @return ID of the first player to play his turn
     */
    public int getFirstPlayerID() {
        return firstPlayerID;
    }

    /**
     * Setter
     * @param firstPlayerID ID of the first player to play his turn
     */
    public void setFirstPlayerID(int firstPlayerID) {
        this.firstPlayerID = firstPlayerID;
    }

    public void addPlayer(PlayerModel player){
        IDToPlayerMap.put(player.getId(), player);
        playerIDList.add(player.getId());
        board.put(player.getId(), 0);
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

    public void restoreVisibleResourceCard (){
        if (!resourceCardsDeck.isDeckEmpty()){
            resourceCardsDeck.restoreVisibleCard();
        }
        else if (!goldenCardsDeck.isDeckEmpty()){
            try {
                resourceCardsDeck.restoreVisibleCardWithOtherDeck(goldenCardsDeck.drawCard());
            } catch (NotEnoughCardsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void restoreVisibleGoldenCard (){
        if (!goldenCardsDeck.isDeckEmpty()){
            goldenCardsDeck.restoreVisibleCard();
        }
        else if (!resourceCardsDeck.isDeckEmpty()){
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
        for (Integer i : playerIDList){
            IDToPlayerMap.get(i).setAsFirstPlayer(false);
            IDToPlayerMap.get(i).setState(PlayerState.WAITING);
        }

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
        orderOfPlayersIDs = new int[playerIDList.size()];

        orderOfPlayersIDs[0] = firstPlayerID;

        int pos = 1;
        for (Integer i : playerIDList){
            if (i != firstPlayerID){
                orderOfPlayersIDs[pos] = i;
                pos++;
            }
        }
    }

    /**
     * Getter
     * @return Order of the players' IDs
     */
    public int[] getOrderOfPlayersIDs(){
        return orderOfPlayersIDs;
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

        IDToPlayerMap.get(currPlayerID).setState(PlayerState.PLACING);
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

    public void determineRanking() {
        List<PlayerModel> players = new LinkedList<>(IDToPlayerMap.values());
        List<String> playerNicknames = new ArrayList<>();
        for (PlayerModel player : IDToPlayerMap.values()){
            player.calculateObjectivePoints(player.getSecretObjective());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().getFirst());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().get(1));
        }
        players.sort(new PlayerRanker());
        players.forEach(player -> playerNicknames.add(player.getNickname()));
        int i = 1;
        for (String s : playerNicknames){
            System.out.println("Number " + i + " Player is " + s);
            i++;
        }
    }
}
