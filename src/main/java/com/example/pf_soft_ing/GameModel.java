package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;

import java.util.ArrayList;
import java.util.HashMap;
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

    public GameModel() {
        this.playerIDList = new ArrayList<>();
        this.board = new HashMap<>();

        IDToPlayerMap = new HashMap<>();
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
    public void setVisibleCards(){
        resourceCardsDeck.restoreVisibleCards();
        goldenCardsDeck.restoreVisibleCards();
    }

    /**
     * Sets the visible cards list in the golden and resource card decks
     * @param resourceCardID1 ID of the first resource card to set visible
     * @param resourceCardID2 ID of the second resource card to set visible
     * @param goldenCardID1 ID of the first golden card to set visible
     * @param goldenCardID2 ID of the second golden card to set visible
     */
    public void setVisibleCards(int resourceCardID1, int resourceCardID2, int goldenCardID1, int goldenCardID2){
        resourceCardsDeck.restoreVisibleCard(resourceCardID1);
        resourceCardsDeck.restoreVisibleCard(resourceCardID2);
        goldenCardsDeck.restoreVisibleCard(goldenCardID1);
        goldenCardsDeck.restoreVisibleCard(goldenCardID2);
    }

    /**
     * Sets the visible resource card in the resource card deck
     * @param resourceCardID ID of the resource card to set visible
     */
    public void setResourceVisibleCard(int resourceCardID){
        resourceCardsDeck.restoreVisibleCard(resourceCardID);
    }

    /**
     * Sets the visible golden card in the golden card deck
     * @param goldenCardID ID of the golden card to set visible
     */
    public void setGoldenVisibleCard(int goldenCardID){
        goldenCardsDeck.restoreVisibleCard(goldenCardID);
    }

    /**
     * Method to draw the first resource card of the deck
     * @return First resource card of the deck
     */
    public PlaceableCard drawResourceCard(){
        return resourceCardsDeck.drawCard();
    }

    /**
     * Method to draw a specific resource card from the deck
     * @param cardID ID of the card to draw
     * @return The card that has been drawn
     */
    public PlaceableCard drawResourceCard(int cardID){
        return resourceCardsDeck.drawCard(cardID);
    }

    /**
     * Method to draw the first golden card of the deck
     * @return First golden card of the deck
     */
    public PlaceableCard drawGoldenCard(){
        return goldenCardsDeck.drawCard();
    }

    /**
     * Method to draw a specific golden card from the deck
     * @param cardID ID of the card to draw
     * @return The card that has been drawn
     */
    public PlaceableCard drawGoldenCard(int cardID){
        return goldenCardsDeck.drawCard(cardID);
    }

    /**
     * Method to draw the first starter card of the deck
     * @return First starter card of the deck
     */
    public PlaceableCard drawStarterCard(){
        return starterCardsDeck.drawCard();
    }

    /**
     * Method to draw a specific starter card from the deck
     * @param cardID ID of the card to draw
     * @return The card that has been drawn
     */
    public PlaceableCard drawStarterCard(int cardID){
        return starterCardsDeck.drawCard(cardID);
    }

    /**
     * Method to draw the first objective card of the deck
     * @return First objective card of the deck
     */
    public ObjectiveCard drawObjectiveCard(){
        return objectiveCardsDeck.drawCard();
    }

    /**
     * Method to draw a specific objective card from the deck
     * @param cardID ID of the card to draw
     * @return The card that has been drawn
     */
    public ObjectiveCard drawObjectiveCard(int cardID){
        return objectiveCardsDeck.drawCard(cardID);
    }

    /**
     * Method to draw the first visible resource card of the deck
     * @param index Index of the card to draw
     * @return Visible resource card of the deck at the given index
     */
    public PlaceableCard drawVisibleResourceCard(int index){
        if (0 <= index && index <= 1){
            return resourceCardsDeck.drawVisibleCard(index);
        }
        else {
            return null;
        }
    }

    /**
     * Method to draw the first visible golden card of the deck
     * @param index Index of the card to draw
     * @return Visible golden card of the deck at the given index
     */
    public PlaceableCard drawVisibleGoldenCard(int index){
        if (0 <= index && index <= 1){
            return goldenCardsDeck.drawVisibleCard(index);
        }
        else {
            return null;
        }
    }

    /**
     * Method to randomly choose the first player and give him the black token
     */
    public void setRandomFirstPlayer(){
        for (Integer i : playerIDList){
            IDToPlayerMap.get(i).setAsFirstPlayer(false);
        }

        firstPlayerID = playerIDList.get((int) Math.round(Math.random() * (playerIDList.size() - 1)));
        currPlayerID = firstPlayerID;

        PlayerModel firstPlayer = IDToPlayerMap.get(firstPlayerID);
        firstPlayer.setAsFirstPlayer(true);
    }

    /**
     * Method to set the first player
     * @param playerID ID of the first player
     */
    public void setFirstPlayer(int playerID){
        if (!playerIDList.contains(playerID)){
            return;
        }

        for (Integer i : playerIDList){
            IDToPlayerMap.get(i).setAsFirstPlayer(false);
        }

        firstPlayerID = playerID;
        currPlayerID = firstPlayerID;

        PlayerModel firstPlayer = IDToPlayerMap.get(firstPlayerID);
        firstPlayer.setAsFirstPlayer(true);
    }

    /**
     * Method to set the order of the players based on the first player
     */
    public void setOrderOfPlayers() {
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

    public void endTurn(){
        int currPlayerIndex = 0;
        for (int i = 0; i < orderOfPlayersIDs.length; i++){
            if (orderOfPlayersIDs[i] == currPlayerID){
                currPlayerIndex = i;
                break;
            }
        }

        if (currPlayerIndex == orderOfPlayersIDs.length - 1){
            currPlayerID = orderOfPlayersIDs[0];
        }
        else {
            currPlayerID = orderOfPlayersIDs[currPlayerIndex + 1];
        }
    }
}
