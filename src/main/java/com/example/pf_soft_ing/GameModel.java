package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;

import java.util.HashMap;
import java.util.List;

public class GameModel {
    private HashMap<Integer, PlayerModel> IDToPlayerMap;
    private HashMap<Integer, Integer> board;
    private UsableCardsDeck resourceCardsDeck;
    private UsableCardsDeck goldenCardsDeck;
    private ObjectiveCardsDeck objectiveCardsDeck;
    private StarterCardsDeck starterCardsDeck;
    private final List<Integer> playerIDList;
    private int currPlayerID;
    private int firstPlayerID;

    public GameModel(List<Integer> playerIDList) {
        this.playerIDList = playerIDList;

        IDToPlayerMap = new HashMap<>();

        for (Integer i : playerIDList){
            IDToPlayerMap.put(i, new PlayerModel(STR."Player\{i.toString()}", i));
        }
    }

    public HashMap<Integer, PlayerModel> getIDToPlayerMap() {
        return IDToPlayerMap;
    }

    public HashMap<Integer, Integer> getBoard() {
        return board;
    }

    public UsableCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    public UsableCardsDeck getGoldenCardsDeck() {
        return goldenCardsDeck;
    }

    public ObjectiveCardsDeck getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

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
     * Getter
     * @return ID of the first player to play his turn
     */
    public int getFirstPlayerID() {
        return firstPlayerID;
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
     * Method to draw the first resource card of the deck
     * @return First resource card of the deck
     */
    public PlaceableCard drawResourceCard(){
        return resourceCardsDeck.drawCard();
    }

    /**
     * Method to draw the first golden card of the deck
     * @return First golden card of the deck
     */
    public PlaceableCard drawGoldenCard(){
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

    public PlaceableCard drawVisibleResourceCard(int index){
        if (0 <= index && index <= 1){
            return resourceCardsDeck.drawVisibleCard(index);
        }
        else {
            return null;
        }
    }

    public PlaceableCard drawVisibleGoldenCard(int index){
        if (0 <= index && index <= 1){
            return goldenCardsDeck.drawVisibleCard(index);
        }
        else {
            return null;
        }
    }

    public void setRandomFirstPlayer(){
        firstPlayerID = playerIDList.get((int) Math.round(Math.random() * (playerIDList.size() - 1)));
        currPlayerID = firstPlayerID;
    }
}
