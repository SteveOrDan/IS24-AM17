package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    GameModel gameModel = new GameModel(new ArrayList<>(){{
        add(1);
        add(2);
        add(3);
        add(4);
    }});

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

        PlaceableCard card = gameModel.drawResourceCard();

        assertNotNull(card);
        assertFalse(gameModel.getResourceCardsDeck().getDeck().contains(card));
    }

    @Test
    void drawGoldenCard() {
        gameModel.initializeDecks();
        gameModel.setVisibleCards();

        PlaceableCard card = gameModel.drawGoldenCard();

        assertNotNull(card);
        assertFalse(gameModel.getGoldenCardsDeck().getDeck().contains(card));
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

    @Test
    void simulateGame(){
        GameModel gm = new GameModel(new ArrayList<>(){{
            add(1);
            add(2);
            add(3);
            add(4);
        }});

        // Decks set up
        gm.initializeDecks();

        gm.shuffleAllDecks();

        gm.setVisibleCards();

        // Foreach player...
        for (Integer i : gm.getIDToPlayerMap().keySet()){
            PlayerModel player = gm.getIDToPlayerMap().get(i);

            // Set starter card
            player.setStarterCard(gm.drawStarterCard());

            player.flipStarterCard();
            player.flipStarterCard();

            player.placeStarterCard();

            // Choose token


            // Draw 2 resource cards and 1 golden card
            player.drawCard(gm.drawResourceCard());
            player.drawCard(gm.drawResourceCard());
            player.drawCard(gm.drawGoldenCard());
        }

        // Set common objectives
        gm.getObjectiveCardsDeck().setCommonObjectives();

        // Set objectives to choose
        for (Integer i : gm.getIDToPlayerMap().keySet()){
            PlayerModel player = gm.getIDToPlayerMap().get(i);

            List<ObjectiveCard> objectives = new ArrayList<>();
            objectives.add(gm.drawObjectiveCard());
            objectives.add(gm.drawObjectiveCard());

            player.setObjectivesToChoose(objectives);

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            player.setSecretObjective(randIndex);
        }

        // Set first player
        gm.setRandomFirstPlayer();

        for (Integer i : gm.getIDToPlayerMap().keySet()){
            assertNotNull(gm.getIDToPlayerMap().get(i).getStarterCard());

            assertEquals(3, gm.getIDToPlayerMap().get(i).getHand().size());

            assertNotNull(gm.getIDToPlayerMap().get(i).getSecretObjective());
        }
    }
}