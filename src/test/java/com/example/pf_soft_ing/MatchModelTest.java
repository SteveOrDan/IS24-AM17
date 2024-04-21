package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;
import com.example.pf_soft_ing.game.MatchModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchModelTest {

    MatchModel matchModel = new MatchModel();

    @Test
    void initializeDecks() {
        matchModel.initializeDecks();

        assertNotNull(matchModel.getResourceCardsDeck());
        assertNotNull(matchModel.getGoldenCardsDeck());
        assertNotNull(matchModel.getObjectiveCardsDeck());
        assertNotNull(matchModel.getStarterCardsDeck());
    }

    @Test
    void shuffleDeck(){
        matchModel.initializeDecks();

        List<PlaceableCard> resourceCardsDeck = new ArrayList<>(matchModel.getResourceCardsDeck().getDeck());
        List<PlaceableCard> goldenCardsDeck = new ArrayList<>(matchModel.getGoldenCardsDeck().getDeck());
        List<PlaceableCard> starterCardsDeck = new ArrayList<>(matchModel.getStarterCardsDeck().getDeck());
        List<ObjectiveCard> objectiveCardsDeck = new ArrayList<>(matchModel.getObjectiveCardsDeck().getDeck());

        matchModel.shuffleAllDecks();

        boolean isSame = true;
        for(PlaceableCard card : resourceCardsDeck){
            if (resourceCardsDeck.indexOf(card) != matchModel.getResourceCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);

        isSame = true;
        for(PlaceableCard card : goldenCardsDeck){
            if (goldenCardsDeck.indexOf(card) != matchModel.getGoldenCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);

        isSame = true;
        for(PlaceableCard card : starterCardsDeck){
            if (starterCardsDeck.indexOf(card) != matchModel.getStarterCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);

        isSame = true;
        for(ObjectiveCard card : objectiveCardsDeck){
            if (objectiveCardsDeck.indexOf(card) != matchModel.getObjectiveCardsDeck().getDeck().indexOf(card)){
                isSame = false;
                break;
            }
        }

        assertFalse(isSame);
    }

    @Test
    void setVisibleCards(){
        matchModel.initializeDecks();
        matchModel.setVisibleCards();

        assertNotNull(matchModel.getResourceCardsDeck().getVisibleCards());
        assertNotNull(matchModel.getGoldenCardsDeck().getVisibleCards());

        assertEquals(2, matchModel.getResourceCardsDeck().getVisibleCards().size());
        assertEquals(2, matchModel.getGoldenCardsDeck().getVisibleCards().size());

        assertFalse(matchModel.getResourceCardsDeck().getDeck().contains(matchModel.getResourceCardsDeck().getVisibleCards().getFirst()));
        assertFalse(matchModel.getResourceCardsDeck().getDeck().contains(matchModel.getResourceCardsDeck().getVisibleCards().get(1)));

        assertFalse(matchModel.getGoldenCardsDeck().getDeck().contains(matchModel.getGoldenCardsDeck().getVisibleCards().getFirst()));
        assertFalse(matchModel.getGoldenCardsDeck().getDeck().contains(matchModel.getGoldenCardsDeck().getVisibleCards().get(1)));
    }

    @Test
    void drawResourceCard() {
        matchModel.initializeDecks();
        matchModel.setVisibleCards();

        PlaceableCard card = null;
        try {
            card = matchModel.drawResourceCard();
            assertNotNull(card);
            assertFalse(matchModel.getResourceCardsDeck().getDeck().contains(card));
        } catch (NotEnoughCardsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void drawGoldenCard() {
        matchModel.initializeDecks();
        matchModel.setVisibleCards();

        PlaceableCard card = null;
        try {
            card = matchModel.drawGoldenCard();
            assertNotNull(card);
            assertFalse(matchModel.getGoldenCardsDeck().getDeck().contains(card));
        } catch (NotEnoughCardsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void drawStarterCard() {
        matchModel.initializeDecks();
        matchModel.setVisibleCards();

        PlaceableCard card = matchModel.drawStarterCard();

        assertNotNull(card);
        assertFalse(matchModel.getStarterCardsDeck().getDeck().contains(card));
    }

    @Test
    void drawObjectiveCard() {
        matchModel.initializeDecks();
        matchModel.setVisibleCards();

        ObjectiveCard card = matchModel.drawObjectiveCard();

        assertNotNull(card);
        assertFalse(matchModel.getObjectiveCardsDeck().getDeck().contains(card));
    }
}