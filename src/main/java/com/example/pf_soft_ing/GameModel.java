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

    public List<Integer> getPlayerIDList() {
        return playerIDList;
    }

    public int getCurrPlayerID() {
        return currPlayerID;
    }

    public int getFirstPlayerID() {
        return firstPlayerID;
    }

    public void initializeDecks(){
        resourceCardsDeck = new UsableCardsDeck(gameResources.getResourcesDeck());
        goldenCardsDeck = new UsableCardsDeck(gameResources.getGoldenDeck());
        objectiveCardsDeck = new ObjectiveCardsDeck(gameResources.getObjectiveDeck());
        starterCardsDeck = new StarterCardsDeck(gameResources.getStarterDeck());
    }

    public void shuffleAllDecks(){
        resourceCardsDeck.shuffleDeck();
        goldenCardsDeck.shuffleDeck();
        objectiveCardsDeck.shuffleDeck();
        starterCardsDeck.shuffleDeck();
    }

    public void setVisibleCards(){
        resourceCardsDeck.restoreVisibleCards();
        goldenCardsDeck.restoreVisibleCards();
    }

    public PlaceableCard drawResourceCard(){
        return resourceCardsDeck.drawCard();
    }

    public PlaceableCard drawGoldenCard(){
        return goldenCardsDeck.drawCard();
    }

    public PlaceableCard drawStarterCard(){
        return starterCardsDeck.drawCard();
    }

    public ObjectiveCard drawObjectiveCard(){
        return objectiveCardsDeck.drawCard();
    }
}
