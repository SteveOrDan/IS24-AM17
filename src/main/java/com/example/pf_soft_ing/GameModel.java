package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;

import java.util.HashMap;
import java.util.List;

public class GameModel {
    private final GameResources gameResources;
    private HashMap<Integer, Integer> board;
    private UsableCardsDeck resourceCardsDeck;
    private UsableCardsDeck goldenCardsDeck;
    private ObjectiveCardsDeck objectiveCardsDeck;
    private StarterCardsDeck starterCardsDeck;
    private final List<Integer> playerIDList;
    private int currPlayerID;
    private int firstPlayerID;

    public GameModel(GameResources gameResources, List<Integer> playerIDList) {
        this.gameResources = gameResources;
        this.playerIDList = playerIDList;
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
        resourceCardsDeck = new UsableCardsDeck(gameResources.getResourcesDeck());
        goldenCardsDeck = new UsableCardsDeck(gameResources.getGoldenDeck());
        objectiveCardsDeck = new ObjectiveCardsDeck(gameResources.getObjectiveDeck());
        starterCardsDeck = new StarterCardsDeck(gameResources.getStarterDeck());
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
}
