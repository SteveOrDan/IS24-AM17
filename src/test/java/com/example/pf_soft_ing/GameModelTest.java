package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    GameModel gameModel = new GameModel();

    @Test
    void initializeDecks() {
        gameModel.initializeDecks();

        assertNotNull(gameModel.getResourceCardsDeck());
        assertNotNull(gameModel.getGoldenCardsDeck());
        assertNotNull(gameModel.getObjectiveCardsDeck());
        assertNotNull(gameModel.getStarterCardsDeck());
    }

    @Test
    void shuffleDeck(){
        gameModel.initializeDecks();

        List<PlaceableCard> resourceCardsDeck = new ArrayList<>(gameModel.getResourceCardsDeck().getDeck());
        List<PlaceableCard> goldenCardsDeck = new ArrayList<>(gameModel.getGoldenCardsDeck().getDeck());
        List<PlaceableCard> starterCardsDeck = new ArrayList<>(gameModel.getStarterCardsDeck().getDeck());
        List<ObjectiveCard> objectiveCardsDeck = new ArrayList<>(gameModel.getObjectiveCardsDeck().getDeck());

        gameModel.shuffleAllDecks();

        boolean isSame = true;
        for(PlaceableCard card : resourceCardsDeck){
            if (resourceCardsDeck.indexOf(card) != gameModel.getResourceCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);

        isSame = true;
        for(PlaceableCard card : goldenCardsDeck){
            if (goldenCardsDeck.indexOf(card) != gameModel.getGoldenCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);

        isSame = true;
        for(PlaceableCard card : starterCardsDeck){
            if (starterCardsDeck.indexOf(card) != gameModel.getStarterCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);

        isSame = true;
        for(ObjectiveCard card : objectiveCardsDeck){
            if (objectiveCardsDeck.indexOf(card) != gameModel.getObjectiveCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);
    }

    @Test
    void setVisibleCards(){
        gameModel.initializeDecks();
        gameModel.setVisibleCards();

        assertNotNull(gameModel.getResourceCardsDeck().getVisibleCards());
        assertNotNull(gameModel.getGoldenCardsDeck().getVisibleCards());

        assertEquals(2, gameModel.getResourceCardsDeck().getVisibleCards().size());
        assertEquals(2, gameModel.getGoldenCardsDeck().getVisibleCards().size());

        assertFalse(gameModel.getResourceCardsDeck().getDeck().contains(gameModel.getResourceCardsDeck().getVisibleCards().getFirst()));
        assertFalse(gameModel.getResourceCardsDeck().getDeck().contains(gameModel.getResourceCardsDeck().getVisibleCards().get(1)));

        assertFalse(gameModel.getGoldenCardsDeck().getDeck().contains(gameModel.getGoldenCardsDeck().getVisibleCards().getFirst()));
        assertFalse(gameModel.getGoldenCardsDeck().getDeck().contains(gameModel.getGoldenCardsDeck().getVisibleCards().get(1)));
    }

    @Test
    void drawResourceCard() {
        gameModel.initializeDecks();
        gameModel.setVisibleCards();

        PlaceableCard card = null;
        try {
            card = gameModel.drawResourceCard();
            assertNotNull(card);
            assertFalse(gameModel.getResourceCardsDeck().getDeck().contains(card));
        } catch (NotEnoughCardsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void drawGoldenCard() {
        gameModel.initializeDecks();
        gameModel.setVisibleCards();

        PlaceableCard card = null;
        try {
            card = gameModel.drawGoldenCard();
            assertNotNull(card);
            assertFalse(gameModel.getGoldenCardsDeck().getDeck().contains(card));
        } catch (NotEnoughCardsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void drawStarterCard() {
        gameModel.initializeDecks();
        gameModel.setVisibleCards();

        PlaceableCard card = gameModel.drawStarterCard();

        assertNotNull(card);
        assertFalse(gameModel.getStarterCardsDeck().getDeck().contains(card));
    }

    @Test
    void drawObjectiveCard() {
        gameModel.initializeDecks();
        gameModel.setVisibleCards();

        ObjectiveCard card = gameModel.drawObjectiveCard();

        assertNotNull(card);
        assertFalse(gameModel.getObjectiveCardsDeck().getDeck().contains(card));
    }
}