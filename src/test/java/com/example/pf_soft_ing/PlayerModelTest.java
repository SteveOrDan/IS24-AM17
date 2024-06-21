package com.example.pf_soft_ing;

import com.example.pf_soft_ing.MVC.model.game.GameResources;
import com.example.pf_soft_ing.MVC.model.player.PlayerModel;
import com.example.pf_soft_ing.MVC.model.player.PlayerState;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.cards.NoAdjacentCardsException;
import com.example.pf_soft_ing.exceptions.cards.PlacingOnInvalidCornerException;
import com.example.pf_soft_ing.exceptions.cards.PositionAlreadyTakenException;
import com.example.pf_soft_ing.exceptions.match.MissingResourcesException;
import com.example.pf_soft_ing.exceptions.player.InvalidPlayerStateException;
import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.utils.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {
    // TODO: Fix the tests adding the necessary set state for the player model and using game resources to get the cards

    private PlayerModel playerModel;

    public void createPlayerModel(){
        playerModel = new PlayerModel(34, new TestSender());
    }

    @DisplayName("Test for placing a starter card in the player area")
    @Test
    void placeStarterCard() {
        GameResources.initializeAllDecks();

        PlaceableCard starterCard1 = GameResources.getPlaceableCardByID(80);

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
        GameResources.initializeAllDecks();

        PlaceableCard starterCard1 = GameResources.getPlaceableCardByID(80);
        PlaceableCard resourceCard = GameResources.getPlaceableCardByID(0);

        createPlayerModel();

        playerModel.setState(PlayerState.PLACING);

        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard(CardSideType.FRONT);
        assertEquals(0, playerModel.getCurrScore());

        //Now place a card in a valid position
        Position pos = new Position(1,1);
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard, pos, CardSideType.FRONT));

        assertTrue(playerModel.getPlayArea().containsKey(pos));
        assertEquals(playerModel.getPlayArea().get(pos), resourceCard);
        assertEquals(0, playerModel.getCurrScore());
        assertEquals(PlayerState.DRAWING, playerModel.getState());
    }

    @DisplayName("Test for placing a card in an invalid position")
    @Test
    void placeCardInInvalidPos() {
        GameResources.initializeAllDecks();

        PlaceableCard starterCard = GameResources.getPlaceableCardByID(84);
        PlaceableCard resourceCard = GameResources.getPlaceableCardByID(0);

        createPlayerModel();

        playerModel.setState(PlayerState.PLACING_STARTER);

        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard);
        playerModel.placeStarterCard(CardSideType.BACK);
        assertEquals(0, playerModel.getCurrScore());

        //Now place a card in an invalid position
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,0);
        Position pos3 = new Position(-1,-1);

        playerModel.setState(PlayerState.PLACING);
        assertThrows(PositionAlreadyTakenException.class, () -> playerModel.placeCard(resourceCard, pos1, CardSideType.FRONT));
        assertThrows(NoAdjacentCardsException.class, () -> playerModel.placeCard(resourceCard, pos2, CardSideType.FRONT));
        assertThrows(PlacingOnInvalidCornerException.class, () -> playerModel.placeCard(resourceCard, pos3, CardSideType.FRONT));

        assertFalse(playerModel.getPlayArea().containsValue(resourceCard));

        assertFalse(playerModel.getPlayArea().containsKey(pos2));
        assertFalse(playerModel.getPlayArea().containsKey(pos3));

        assertEquals(0, playerModel.getCurrScore());
    }

    @DisplayName("Test for the top right to bottom left diagonal objective card")
    @Test
    void checkTRBLDiagonalObjective(){
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 3 fungi resource cards and flip them
        PlaceableCard resourceCard1 = GameResources.getPlaceableCardByID(0);
        PlaceableCard resourceCard2 = GameResources.getPlaceableCardByID(1);
        PlaceableCard resourceCard3 = GameResources.getPlaceableCardByID(2);

        //Place the resource cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(-1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(0, 2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(-2, 0), CardSideType.BACK));

        //Same thing with 3 animal golden cards
        PlaceableCard goldenCard1 = GameResources.getPlaceableCardByID(60);
        PlaceableCard goldenCard2 = GameResources.getPlaceableCardByID(61);
        PlaceableCard goldenCard3 = GameResources.getPlaceableCardByID(62);

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(1, -1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(2, 0), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(0, -2), CardSideType.BACK));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = GameResources.getObjectiveCardByID(86);
        ObjectiveCard objectiveCard2 = GameResources.getObjectiveCardByID(88);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());

        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());
    }

    @DisplayName("Test for the top left to bottom right diagonal objective card")
    @Test
    void checkTLBRDiagonalObjective(){
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 3 plant resource cards and flip them
        PlaceableCard resourceCard1 = GameResources.getPlaceableCardByID(10);
        PlaceableCard resourceCard2 = GameResources.getPlaceableCardByID(11);
        PlaceableCard resourceCard3 = GameResources.getPlaceableCardByID(12);

        //Place the resource cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(0, 2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(2, 0), CardSideType.BACK));

        //Same thing with 3 insect golden cards
        PlaceableCard goldenCard1 = GameResources.getPlaceableCardByID(70);
        PlaceableCard goldenCard2 = GameResources.getPlaceableCardByID(71);
        PlaceableCard goldenCard3 = GameResources.getPlaceableCardByID(72);

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(-1, -1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(-2, 0), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(0, -2), CardSideType.BACK));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = GameResources.getObjectiveCardByID(87);
        ObjectiveCard objectiveCard2 = GameResources.getObjectiveCardByID(89);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());

        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());
    }

    @DisplayName("Different diagonal cases from top right to bottom left")
    @Test
    void checkTRBLDiagonalCases(){
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 6 fungi resource cards and flip them
        PlaceableCard resourceCard1 = GameResources.getPlaceableCardByID(0);
        PlaceableCard resourceCard2 = GameResources.getPlaceableCardByID(1);
        PlaceableCard resourceCard3 = GameResources.getPlaceableCardByID(2);
        PlaceableCard resourceCard4 = GameResources.getPlaceableCardByID(3);
        PlaceableCard resourceCard5 = GameResources.getPlaceableCardByID(4);
        PlaceableCard resourceCard6 = GameResources.getPlaceableCardByID(5);

        //Place the 4 resource cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(-1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(0, 2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(-2, 0), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard4, new Position(1, 3), CardSideType.BACK));

        //Same thing with 6 animal golden cards
        PlaceableCard goldenCard1 = GameResources.getPlaceableCardByID(60);
        PlaceableCard goldenCard2 = GameResources.getPlaceableCardByID(61);
        PlaceableCard goldenCard3 = GameResources.getPlaceableCardByID(62);
        PlaceableCard goldenCard4 = GameResources.getPlaceableCardByID(63);
        PlaceableCard goldenCard5 = GameResources.getPlaceableCardByID(64);
        PlaceableCard goldenCard6 = GameResources.getPlaceableCardByID(65);

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(1, -1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(2, 0), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(0, -2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard4, new Position(3, 1), CardSideType.BACK));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = GameResources.getObjectiveCardByID(86);
        ObjectiveCard objectiveCard2 = GameResources.getObjectiveCardByID(88);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());

        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());


        //Place a fifth card in the diagonal
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard5, new Position(2, 4), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard5, new Position(4, 2), CardSideType.BACK));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());

        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());


        //Place a sixth card in the diagonal
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard6, new Position(3, 5), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard6, new Position(5, 3), CardSideType.BACK));

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
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 6 fungi resource cards and flip them
        PlaceableCard resourceCard1 = GameResources.getPlaceableCardByID(10);
        PlaceableCard resourceCard2 = GameResources.getPlaceableCardByID(11);
        PlaceableCard resourceCard3 = GameResources.getPlaceableCardByID(12);
        PlaceableCard resourceCard4 = GameResources.getPlaceableCardByID(13);
        PlaceableCard resourceCard5 = GameResources.getPlaceableCardByID(14);
        PlaceableCard resourceCard6 = GameResources.getPlaceableCardByID(15);

        //Place the 4 resource cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard1, new Position(1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard2, new Position(0, 2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard3, new Position(2, 0), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard4, new Position(-1, 3), CardSideType.BACK));

        //Same thing with 6 animal golden cards
        PlaceableCard goldenCard1 = GameResources.getPlaceableCardByID(70);
        PlaceableCard goldenCard2 = GameResources.getPlaceableCardByID(71);
        PlaceableCard goldenCard3 = GameResources.getPlaceableCardByID(72);
        PlaceableCard goldenCard4 = GameResources.getPlaceableCardByID(73);
        PlaceableCard goldenCard5 = GameResources.getPlaceableCardByID(74);
        PlaceableCard goldenCard6 = GameResources.getPlaceableCardByID(75);

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard1, new Position(-1, -1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard2, new Position(-2, 0), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard3, new Position(0, -2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard4, new Position(-3, 1), CardSideType.BACK));

        //Select 2 objective cards
        ObjectiveCard objectiveCard1 = GameResources.getObjectiveCardByID(87);
        ObjectiveCard objectiveCard2 = GameResources.getObjectiveCardByID(89);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());

        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());


        //Place a fifth card in the diagonal
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard5, new Position(-2, 4), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard5, new Position(-4, 2), CardSideType.BACK));

        //Reset the player's score
        playerModel.setCurrScore(0);

        //Check if the calculated score of the objective cards is correct
        playerModel.calculateObjectivePoints(objectiveCard1);
        assertEquals(2, playerModel.getCurrScore());

        playerModel.calculateObjectivePoints(objectiveCard2);
        assertEquals(4, playerModel.getCurrScore());


        //Place a sixth card in the diagonal
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(resourceCard6, new Position(-3, 5), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(goldenCard6, new Position(-5, 3), CardSideType.BACK));

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
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 fungus golden card and 2 animal resource cards and flip them
        PlaceableCard card0 = GameResources.getPlaceableCardByID(0);
        PlaceableCard card1 = GameResources.getPlaceableCardByID(20);
        PlaceableCard card2 = GameResources.getPlaceableCardByID(21);

        //Place the cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card0, new Position(2,2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(1,-1), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = GameResources.getObjectiveCardByID(92);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }

    @DisplayName("Test for the top left L shape objective card")
    @Test
    void checkTLLShapeObjective(){
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 fungus golden card and 2 animal resource cards and flip them
        PlaceableCard card0 = GameResources.getPlaceableCardByID(20);
        PlaceableCard card1 = GameResources.getPlaceableCardByID(30);
        PlaceableCard card2 = GameResources.getPlaceableCardByID(31);

        //Place the cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card0, new Position(0,2), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(1,-1), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = GameResources.getObjectiveCardByID(93);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }

    @DisplayName("Test for the bottom right L shape objective card")
    @Test
    void checkBRLShapeObjective(){
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 fungus golden card and 2 animal resource cards and flip them
        PlaceableCard card0 = GameResources.getPlaceableCardByID(10);
        PlaceableCard card1 = GameResources.getPlaceableCardByID(0);
        PlaceableCard card2 = GameResources.getPlaceableCardByID(1);

        //Place the cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(1,-1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card0, new Position(2,-2), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = GameResources.getObjectiveCardByID(90);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }

    @DisplayName("Test for the bottom left L shape objective card")
    @Test
    void checkBLLShapeObjective(){
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 fungus golden card and 2 animal resource cards and flip them
        PlaceableCard card0 = GameResources.getPlaceableCardByID(30);
        PlaceableCard card1 = GameResources.getPlaceableCardByID(10);
        PlaceableCard card2 = GameResources.getPlaceableCardByID(11);

        //Place the cards
        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card1, new Position(1, 1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card2, new Position(1,-1), CardSideType.BACK));

        playerModel.setState(PlayerState.PLACING);
        assertDoesNotThrow(() -> playerModel.placeCard(card0, new Position(0,-2), CardSideType.BACK));

        //Select an objective card
        ObjectiveCard objectiveCard = GameResources.getObjectiveCardByID(91);

        //Check if the calculated score of the objective card is correct
        playerModel.calculateObjectivePoints(objectiveCard);
        assertEquals(3, playerModel.getCurrScore());
    }

    @DisplayName("Test for place card exceptions")
    @Test
    void placeCardExceptions() {
        //Initialize the decks
        GameResources.initializeAllDecks();

        //Initialize the player
        createPlayerModel();

        // Create cards
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(80);

        //Set the player's starter card
        playerModel.setStarterCard(starterCard);

        //Place the starter card
        playerModel.placeStarterCard(CardSideType.FRONT);

        //Use 1 fungus golden card and 2 animal resource cards and flip them
        PlaceableCard card0 = GameResources.getPlaceableCardByID(69);

        //Place the cards
        playerModel.setState(PlayerState.PLACING);
        assertThrows(PositionAlreadyTakenException.class, () -> playerModel.placeCard(card0, new Position(0, 0), CardSideType.BACK));

        //Place the cards
        playerModel.setState(PlayerState.PLACING);
        assertThrows(MissingResourcesException.class, () -> playerModel.placeCard(card0, new Position(1, 1), CardSideType.FRONT));

        //Place the cards
        playerModel.setState(PlayerState.DRAWING);
        assertThrows(InvalidPlayerStateException.class, () -> playerModel.placeCard(card0, new Position(1, 1), CardSideType.BACK));
    }
}