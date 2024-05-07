package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.*;
import com.example.pf_soft_ing.network.server.SocketSender;
import com.example.pf_soft_ing.player.*;
import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
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

    PlayerModel playerModel;

    public void createPlayerModel(){
        try {
            playerModel = new PlayerModel(34, new SocketSender(new ObjectOutputStream(System.out)));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @DisplayName("Test for placing a starter card in the player area")
    @Test
    void placeStarterCard() {
        createPlayerModel();

        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard(CardSideType.FRONT);

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
        createPlayerModel();
        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard(CardSideType.FRONT);
        assertEquals(0, playerModel.getCurrScore());

        //Now place a card in a valid position
        Position pos = new Position(1,1);
        assertDoesNotThrow(() -> playerModel.placeCard(normalCard, pos, CardSideType.FRONT));

        assertTrue(playerModel.getPlayArea().containsKey(pos));
        assertEquals(playerModel.getPlayArea().get(pos), normalCard);
        assertEquals(playerModel.getCurrScore(), resourceCardPoints);
    }

    @DisplayName("Test for placing a card in an invalid position")
    @Test
    void placeCardInInvalidPos() {
        createPlayerModel();
        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard(CardSideType.FRONT);
        assertEquals(0, playerModel.getCurrScore());

        //Now place a card in an invalid position
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,0);
        Position pos3 = new Position(-1,-1);


        assertThrows(PositionAlreadyTakenException.class, () -> playerModel.placeCard(normalCard, pos1, CardSideType.FRONT));
        assertThrows(NoAdjacentCardsException.class, () -> playerModel.placeCard(normalCard, pos2, CardSideType.FRONT));
        assertThrows(PlacingOnInvalidCornerException.class, () -> playerModel.placeCard(normalCard, pos3, CardSideType.FRONT));

        assertFalse(playerModel.getPlayArea().containsValue(normalCard));

        assertFalse(playerModel.getPlayArea().containsKey(pos2));
        assertFalse(playerModel.getPlayArea().containsKey(pos3));

        assertEquals(0, playerModel.getCurrScore());
    }


    @DisplayName("Test for the top right to bottom left diagonal objective card")
    @Test
    void checkTRBLDiagonalObjective(){
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 3 fungi resource cards and flip them
        ResourceCard resourceCard1 = (ResourceCard) resourceDeck.get(0);
        ResourceCard resourceCard2 = (ResourceCard) resourceDeck.get(1);
        ResourceCard resourceCard3 = (ResourceCard) resourceDeck.get(2);

        //Place the resource cards
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(-1, 1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(0, 2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(-2, 0), CardSideType.BACK));

        //Same thing with 3 animal golden cards
        GoldenCard goldenCard1 = (GoldenCard) goldenDeck.get(20);
        GoldenCard goldenCard2 = (GoldenCard) goldenDeck.get(21);
        GoldenCard goldenCard3 = (GoldenCard) goldenDeck.get(22);

        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(1, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(2, 0), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(0, -2), CardSideType.BACK));

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
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);


        //Use 3 plant resource cards and flip them
        ResourceCard resourceCard1 = (ResourceCard) resourceDeck.get(10);
        ResourceCard resourceCard2 = (ResourceCard) resourceDeck.get(11);
        ResourceCard resourceCard3 = (ResourceCard) resourceDeck.get(12);

        //Place the resource cards
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(1, 1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(0, 2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(2, 0), CardSideType.BACK));

        //Same thing with 3 insect golden cards
        GoldenCard goldenCard1 = (GoldenCard) goldenDeck.get(30);
        GoldenCard goldenCard2 = (GoldenCard) goldenDeck.get(31);
        GoldenCard goldenCard3 = (GoldenCard) goldenDeck.get(32);

        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(-1, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(-2, 0), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(0, -2), CardSideType.BACK));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = objectiveDeck.get(1);
        ObjectiveCard objectiveCard2 = objectiveDeck.get(3);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());
    }

    @DisplayName("Different diagonal cases from top right to bottom left")
    @Test
    void checkTRBLDiagonalCases(){
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 6 fungi resource cards and flip them
        ResourceCard resourceCard1 = (ResourceCard) resourceDeck.get(0);
        ResourceCard resourceCard2 = (ResourceCard) resourceDeck.get(1);
        ResourceCard resourceCard3 = (ResourceCard) resourceDeck.get(2);
        ResourceCard resourceCard4 = (ResourceCard) resourceDeck.get(3);
        ResourceCard resourceCard5 = (ResourceCard) resourceDeck.get(4);
        ResourceCard resourceCard6 = (ResourceCard) resourceDeck.get(5);

        //Place the resource cards
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(-1, 1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(0, 2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(-2, 0), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard4, new Position(1, 3), CardSideType.BACK));

        //Same thing with 6 animal golden cards
        GoldenCard goldenCard1 = (GoldenCard) goldenDeck.get(20);
        GoldenCard goldenCard2 = (GoldenCard) goldenDeck.get(21);
        GoldenCard goldenCard3 = (GoldenCard) goldenDeck.get(22);
        GoldenCard goldenCard4 = (GoldenCard) goldenDeck.get(23);
        GoldenCard goldenCard5 = (GoldenCard) goldenDeck.get(24);
        GoldenCard goldenCard6 = (GoldenCard) goldenDeck.get(25);

        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(1, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(2, 0), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(0, -2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard4, new Position(3, 1), CardSideType.BACK));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = objectiveDeck.get(0);
        ObjectiveCard objectiveCard2 = objectiveDeck.get(2);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());

        //Place a fifth card in the diagonal
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard5, new Position(-3, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard5, new Position(4,2), CardSideType.BACK));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());

        //Place a sixth card in the diagonal
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard6, new Position(-4, -2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard6, new Position(5,3), CardSideType.BACK));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(4, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(8, playerModel.getCurrScore());
    }

    @DisplayName("Different diagonal cases from top left to bottom right")
    @Test
    void checkTLBRDiagonal(){
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 6 plant resource cards and flip them
        ResourceCard resourceCard1 = (ResourceCard) resourceDeck.get(10);
        ResourceCard resourceCard2 = (ResourceCard) resourceDeck.get(11);
        ResourceCard resourceCard3 = (ResourceCard) resourceDeck.get(12);
        ResourceCard resourceCard4 = (ResourceCard) resourceDeck.get(13);
        ResourceCard resourceCard5 = (ResourceCard) resourceDeck.get(14);
        ResourceCard resourceCard6 = (ResourceCard) resourceDeck.get(15);

        //Place the resource cards
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(1, 1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(2, 0), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(3, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard4, new Position(4, -2), CardSideType.BACK));

        //Same thing with 6 insect golden cards
        GoldenCard goldenCard1 = (GoldenCard) goldenDeck.get(30);
        GoldenCard goldenCard2 = (GoldenCard) goldenDeck.get(31);
        GoldenCard goldenCard3 = (GoldenCard) goldenDeck.get(32);
        GoldenCard goldenCard4 = (GoldenCard) goldenDeck.get(33);
        GoldenCard goldenCard5 = (GoldenCard) goldenDeck.get(34);
        GoldenCard goldenCard6 = (GoldenCard) goldenDeck.get(35);

        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(-1, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(0, -2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(1, -3), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard4, new Position(2, -4), CardSideType.BACK));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = objectiveDeck.get(1);
        ObjectiveCard objectiveCard2 = objectiveDeck.get(3);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());

        //Place a fifth card in the diagonal
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard5, new Position(5, -3), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard5, new Position(3, -5), CardSideType.BACK));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());

        //Place a sixth card in the diagonal
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard6, new Position(6, -4), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard6, new Position(4,-6), CardSideType.BACK));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(4, playerModel.getCurrScore());
        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(8, playerModel.getCurrScore());
    }

    @DisplayName("Test for the top right L shape objective card")
    @Test
    void checkTRLShapeObjective(){
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 fungi golden card and 2 animal resource cards and flip them
        PlaceableCard mainCard = goldenDeck.getFirst();
        PlaceableCard card1 = resourceDeck.get(20);
        PlaceableCard card2 = resourceDeck.get(21);

        //Place the cards
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(1, 1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(mainCard, new Position(2,2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(1,-1), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = objectiveDeck.get(6);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }

    @DisplayName("Test for the top left L shape objective card")
    @Test
    void checkTLLShapeObjective(){
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 animal golden card and 2 insect resource cards and flip them
        PlaceableCard mainCard = goldenDeck.get(20);
        PlaceableCard card1 = resourceDeck.get(30);
        PlaceableCard card2 = resourceDeck.get(31);

        //Place the cards
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(-1, 1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(mainCard, new Position(-2,2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(-1,-1), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = objectiveDeck.get(7);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }

    @DisplayName("Test for the bottom right L shape objective card")
    @Test
    void checkBRLShapeObjective(){
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 plant golden card and 2 fungi resource cards and flip them
        PlaceableCard mainCard = goldenDeck.get(10);
        PlaceableCard card1 = resourceDeck.get(0);
        PlaceableCard card2 = resourceDeck.get(1);

        //Place the cards
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(1, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(mainCard, new Position(2,-2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(1,1), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = objectiveDeck.get(4);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }

    @DisplayName("Test for the bottom left L shape objective card")
    @Test
    void checkBLLShapeObjective(){
        createPlayerModel();
        GameResources.initializeAllDecks();
        //Initialize the decks
        List<PlaceableCard> resourceDeck = new ArrayList<>(GameResources.getResourcesDeck());
        List<PlaceableCard> goldenDeck = new ArrayList<>(GameResources.getGoldenDeck());
        List<PlaceableCard> starterDeck = new ArrayList<>(GameResources.getStarterDeck());
        List<ObjectiveCard> objectiveDeck = new ArrayList<>(GameResources.getObjectiveDeck());

        //Initialize the player
        createPlayerModel();

        //Set the player's starter card
        playerModel.setStarterCard(starterDeck.getFirst());

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 fungus golden card and 2 animal resource cards and flip them
        PlaceableCard mainCard = goldenDeck.get(30);
        PlaceableCard card1 = resourceDeck.get(10);
        PlaceableCard card2 = resourceDeck.get(11);

        //Place the cards
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(-1, -1), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(mainCard, new Position(-2,-2), CardSideType.BACK));
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(-1,1), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = objectiveDeck.get(5);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }
}