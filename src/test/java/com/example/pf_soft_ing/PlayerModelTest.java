package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.exceptions.NoAdjacentCardsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {
    CardCorner emptyCorner = new EmptyCorner();
    CardCorner hiddenCorner = new HiddenCorner();
    CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);
    Side starterFront1 = new Front(hiddenCorner, pCorner, iCorner, fCorner);
    Side starerBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.FUNGI);}});
    PlaceableCard starterCard1 = new StarterCard(2, starterFront1, starerBack1);
    int resourceCardPoints = 2;
    Side normalFront = new Front(aCorner, kCorner, mCorner, qCorner);
    Side normalBack = new Back(emptyCorner, emptyCorner, emptyCorner,emptyCorner, new ArrayList<>(){{add(ResourceType.ANIMAL);}});
    ResourceCard normalCard = new ResourceCard(resourceCardPoints, CardElementType.ANIMAL, 3, normalFront, normalBack);
    PlayerModel playerModel = new PlayerModel("John Smith", 34);

    @DisplayName("Test for placing a starter card in the player area")
    @Test
    void placeStarterCard() {
        playerModel.setStarterCard(starterCard1);
        starterCard1.flipCard();
        starterCard1.flipCard();
        playerModel.placeStarterCard();

        assertEquals(0, playerModel.getCurrScore());
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.INSECT.getValue()]);
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.PLANT.getValue()]);
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.FUNGI.getValue()]);
        assertEquals(1, playerModel.getCurrMaxPriority());
        assertTrue(playerModel.getPlayArea().containsValue(starterCard1) && playerModel.getPlayArea().containsKey(new Position(0,0)));
    }

    @DisplayName("Test for placing a card in a valid position")
    @Test
    void placeCardInValidPos() {
        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard();
        assertEquals(0, playerModel.getCurrScore());

        //Now place a card in a valid position
        Position pos = new Position(1,1);
        playerModel.placeCard(normalCard, pos);

        assertTrue(playerModel.getPlayArea().containsKey(pos));
        assertEquals(playerModel.getPlayArea().get(pos), normalCard);
        assertEquals(playerModel.getCurrScore(), resourceCardPoints);
    }

    @DisplayName("Test for placing a card in an invalid position")
    @Test
    void placeCardInInvalidPos() {
        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard();
        assertEquals(0, playerModel.getCurrScore());

        //Now place a card in a valid position
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,0);
        Position pos3 = new Position(-1,-1);

        playerModel.placeCard(normalCard, pos1);
        playerModel.placeCard(normalCard, pos2);
        playerModel.placeCard(normalCard, pos3);

        assertFalse(playerModel.getPlayArea().containsValue(normalCard));

        assertFalse(playerModel.getPlayArea().containsKey(pos2));
        assertFalse(playerModel.getPlayArea().containsKey(pos3));

        assertEquals(0, playerModel.getCurrScore());
    }


    @DisplayName("Test for the top right to bottom left diagonal objective card")
    @Test
    void checkTRBLDiagonalObjective(){
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        PlayerModel playerModel = new PlayerModel("John Smith", 34);

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard();

        //Use 3 fungi resource cards and flip them
        ResourceCard resourceCard1 = (ResourceCard) resourceDeck.get(0);
        ResourceCard resourceCard2 = (ResourceCard) resourceDeck.get(1);
        ResourceCard resourceCard3 = (ResourceCard) resourceDeck.get(2);
        resourceCard1.flipCard();
        resourceCard2.flipCard();
        resourceCard3.flipCard();

        //Place the resource cards
        playerModel.placeCard(resourceCard1, new Position(-1, 1));
        playerModel.placeCard(resourceCard2, new Position(0, 2));
        playerModel.placeCard(resourceCard3, new Position(-2, 0));

        //Same thing with 3 animal golden cards
        GoldenCard goldenCard1 = (GoldenCard) goldenDeck.get(20);
        GoldenCard goldenCard2 = (GoldenCard) goldenDeck.get(21);
        GoldenCard goldenCard3 = (GoldenCard) goldenDeck.get(22);
        goldenCard1.flipCard();
        goldenCard2.flipCard();
        goldenCard3.flipCard();

        playerModel.placeCard(goldenCard1, new Position(1, -1));
        playerModel.placeCard(goldenCard2, new Position(2, 0));
        playerModel.placeCard(goldenCard3, new Position(0, -2));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = objectiveDeck.get(0);
        ObjectiveCard objectiveCard2 = objectiveDeck.get(2);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());
    }

    @DisplayName("Test for the top left to bottom right diagonal objective card")
    @Test
    void checkTLBRDiagonalObjective(){
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        PlayerModel playerModel = new PlayerModel("John Smith", 34);

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard();

        //Use 3 fungi resource cards and flip them
        ResourceCard resourceCard1 = (ResourceCard) resourceDeck.get(10);
        ResourceCard resourceCard2 = (ResourceCard) resourceDeck.get(11);
        ResourceCard resourceCard3 = (ResourceCard) resourceDeck.get(12);
        resourceCard1.flipCard();
        resourceCard2.flipCard();
        resourceCard3.flipCard();

        //Place the resource cards
        playerModel.placeCard(resourceCard1, new Position(1, 1));
        playerModel.placeCard(resourceCard2, new Position(0, 2));
        playerModel.placeCard(resourceCard3, new Position(2, 0));

        //Same thing with 3 animal golden cards
        GoldenCard goldenCard1 = (GoldenCard) goldenDeck.get(30);
        GoldenCard goldenCard2 = (GoldenCard) goldenDeck.get(31);
        GoldenCard goldenCard3 = (GoldenCard) goldenDeck.get(32);
        goldenCard1.flipCard();
        goldenCard2.flipCard();
        goldenCard3.flipCard();

        playerModel.placeCard(goldenCard1, new Position(-1, -1));
        playerModel.placeCard(goldenCard2, new Position(-2, 0));
        playerModel.placeCard(goldenCard3, new Position(0, -2));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = objectiveDeck.get(1);
        ObjectiveCard objectiveCard2 = objectiveDeck.get(3);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());
    }


    @Test
    void checkDiagonalCases(){
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        PlayerModel playerModel = new PlayerModel("John Smith", 34);

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard();

        //Use 3 fungi resource cards and flip them
        ResourceCard resourceCard1 = (ResourceCard) resourceDeck.get(0);
        ResourceCard resourceCard2 = (ResourceCard) resourceDeck.get(1);
        ResourceCard resourceCard3 = (ResourceCard) resourceDeck.get(2);
        ResourceCard resourceCard4 = (ResourceCard) resourceDeck.get(3);
        ResourceCard resourceCard5 = (ResourceCard) resourceDeck.get(4);
        ResourceCard resourceCard6 = (ResourceCard) resourceDeck.get(5);
        resourceCard1.flipCard();
        resourceCard2.flipCard();
        resourceCard3.flipCard();
        resourceCard4.flipCard();
        resourceCard5.flipCard();
        resourceCard6.flipCard();

        //Place the resource cards
        playerModel.placeCard(resourceCard1, new Position(-1, 1));
        playerModel.placeCard(resourceCard2, new Position(0, 2));
        playerModel.placeCard(resourceCard3, new Position(-2, 0));
        playerModel.placeCard(resourceCard4, new Position(1, 3));

        //Same thing with 3 animal golden cards
        GoldenCard goldenCard1 = (GoldenCard) goldenDeck.get(20);
        GoldenCard goldenCard2 = (GoldenCard) goldenDeck.get(21);
        GoldenCard goldenCard3 = (GoldenCard) goldenDeck.get(22);
        GoldenCard goldenCard4 = (GoldenCard) goldenDeck.get(23);
        GoldenCard goldenCard5 = (GoldenCard) goldenDeck.get(24);
        GoldenCard goldenCard6 = (GoldenCard) goldenDeck.get(25);
        goldenCard1.flipCard();
        goldenCard2.flipCard();
        goldenCard3.flipCard();
        goldenCard4.flipCard();
        goldenCard5.flipCard();
        goldenCard6.flipCard();

        playerModel.placeCard(goldenCard1, new Position(1, -1));
        playerModel.placeCard(goldenCard2, new Position(2, 0));
        playerModel.placeCard(goldenCard3, new Position(0, -2));
        playerModel.placeCard(goldenCard4, new Position(3, 1));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = objectiveDeck.get(0);
        ObjectiveCard objectiveCard2 = objectiveDeck.get(2);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());

        //Place a fifth card in the diagonal
        playerModel.placeCard(resourceCard5, new Position(-3, -1));
        playerModel.placeCard(goldenCard5, new Position(4,2));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());

        //Place a sixth card in the diagonal
        playerModel.placeCard(resourceCard6, new Position(-4, -2));
        playerModel.placeCard(goldenCard6, new Position(5,3));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(4, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(8, playerModel.getCurrScore());
    }
}