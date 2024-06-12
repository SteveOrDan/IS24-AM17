package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.GoldenCard;
import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {

    private final static int numOfPlayers = 4;
    private final MatchController matchController = new MatchController(numOfPlayers, 1);
    private final GameController gameController = new GameController();

    @Test
    void checkResources(){
        matchController.initializeDecks();

        assertEquals(86, GameResources.getIDToPlaceableCardMap().size());
    }
    @Test
    void placeStarterCardForPlayer(){
        PlayerModel player1 = gameController.createPlayer(new TestSender());
        PlayerModel player2 = gameController.createPlayer(new TestSender());
        gameController.getMatches(player1.getID());
        MatchController matchController = gameController.createMatch(player1.getID(), 2, "player1");
        gameController.getMatches(player2.getID());
        MatchController matchController1 = gameController.selectMatch(player2.getID(), matchController.getMatchID());
        matchController1.placeStarterCardForPlayer(player2.getID(), CardSideType.BACK);

        gameController.chooseNickname(player2.getID(), "player2",matchController1);

        matchController1.placeStarterCardForPlayer(player1.getID(), CardSideType.BACK);

        matchController1.disconnectPlayer(player1.getID()); // Disconnect player1
        assertThrows(NicknameNotInMatch.class, () -> matchController1.reconnectPlayer("Random", new TestSender())); // Nickname not in match
        assertThrows(SpecifiedPlayerNotDisconnected.class, () -> matchController1.reconnectPlayer(player2.getNickname(), new TestSender())); // Player not disconnected
        assertDoesNotThrow(() -> matchController1.reconnectPlayer(player1.getNickname(), new TestSender()));

        matchController1.disconnectPlayer(player2.getID()); // Disconnect player2
        assertDoesNotThrow(() -> matchController1.reconnectPlayer(player2.getNickname(), new TestSender()));
        matchController1.placeStarterCardForPlayer(player2.getID(), CardSideType.BACK);

        try {
            assertEquals(CardSideType.BACK, player1.getStarterCard().getCurrSideType());
            assertEquals(CardSideType.BACK, player2.getStarterCard().getCurrSideType());
        } catch (StarterCardNotSetException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    void placeCardExceptions(){
        PlayerModel player1 = gameController.createPlayer(new TestSender());
        PlayerModel player2 = gameController.createPlayer(new TestSender());
        gameController.getMatches(player1.getID());
        gameController.getMatches(player2.getID());
        MatchController matchController1 = gameController.createMatch(player1.getID(), 2, "player1");
        MatchController matchController2 = gameController.selectMatch(player2.getID(), matchController1.getMatchID());
        gameController.chooseNickname(player2.getID(), "player2",matchController2);
        matchController1.placeStarterCardForPlayer(player1.getID(), CardSideType.BACK);
        matchController2.placeStarterCardForPlayer(player2.getID(), CardSideType.BACK);
        matchController1.setSecretObjectiveForPlayer(player1.getID(), 86);

        matchController1.disconnectPlayer(player1.getID()); // Disconnect player1
        assertDoesNotThrow(() -> matchController1.reconnectPlayer(player1.getNickname(), new TestSender())); // Reconnect player1

        matchController2.setSecretObjectiveForPlayer(player2.getID(), 87);

        if (player1.getState() == PlayerState.PLACING){
            // current player is player1
            PlaceableCard card1 = player1.getHand().getFirst();
            PlaceableCard card2 = player2.getHand().getFirst();

            // Card not in current player's hand
            matchController1.placeCard(player1.getID(), card2.getID(), new Position(0, 0), CardSideType.FRONT);
            assertEquals(PlayerState.PLACING, player1.getState());

            // not current player tries to Place a card
            matchController2.placeCard(player2.getID(), card2.getID(), new Position(0, 0), CardSideType.FRONT);
            assertEquals(PlayerState.PLACING, player1.getState());

            // Current player tries to place an invalid card
            matchController1.placeCard(player1.getID(), 100, new Position(0, 0), CardSideType.FRONT);
            assertEquals(PlayerState.PLACING, player1.getState());

            // Game State is in Extra Round
            matchController1.setGameState(GameState.EXTRA_ROUND);
            matchController1.placeCard(player1.getID(), card1.getID(), new Position(1, 1), CardSideType.FRONT);
            matchController1.disconnectPlayer(player1.getID()); // Disconnect player1
            assertDoesNotThrow(() -> matchController1.reconnectPlayer(player1.getNickname(), new TestSender())); // Reconnect player1
            assertEquals(PlayerState.WAITING, player1.getState());

        } else {
            // current player is player2
            PlaceableCard card1 = player1.getHand().getFirst();
            PlaceableCard card2 = player2.getHand().getFirst();

            // Card not in current player's hand
            matchController2.placeCard(player2.getID(), card1.getID(), new Position(0, 0), CardSideType.FRONT);
            assertEquals(PlayerState.PLACING, player2.getState());

            // not current player tries to Place a card
            matchController1.placeCard(player1.getID(), card1.getID(), new Position(0, 0), CardSideType.FRONT);
            assertEquals(PlayerState.PLACING, player2.getState());

            // Current player tries to place a invalid card
            matchController2.placeCard(player2.getID(), 100, new Position(0, 0), CardSideType.FRONT);
            assertEquals(PlayerState.PLACING, player2.getState());

            // Game State is in Extra Round
            matchController2.setGameState(GameState.EXTRA_ROUND);
            matchController2.placeCard(player2.getID(), card2.getID(), new Position(1, 1), CardSideType.FRONT);
            assertEquals(PlayerState.WAITING, player2.getState());
        }
        }

    @Test
    void setSecretObjectiveForPlayer(){
        PlayerModel player1 = gameController.createPlayer(new TestSender());
        PlayerModel player2 = gameController.createPlayer(new TestSender());
        gameController.getMatches(player1.getID());
        gameController.getMatches(player2.getID());
        MatchController matchController1 = gameController.createMatch(player1.getID(), 2, "player1");
        MatchController matchController2 = gameController.selectMatch(player2.getID(), matchController1.getMatchID());
        gameController.chooseNickname(player2.getID(), "player2",matchController2);
        matchController1.placeStarterCardForPlayer(player1.getID(), CardSideType.BACK);
        matchController2.placeStarterCardForPlayer(player2.getID(), CardSideType.BACK);
        matchController1.setSecretObjectiveForPlayer(player1.getID(), 86);
        matchController2.setSecretObjectiveForPlayer(player2.getID(), 87);
        assertEquals(86, player1.getSecretObjective().getID());
        assertEquals(87, player2.getSecretObjective().getID());
    }

    @Test
    void chatMessage(){
        PlayerModel player1 = gameController.createPlayer(new TestSender());
        PlayerModel player2 = gameController.createPlayer(new TestSender());
        gameController.getMatches(player1.getID());
        gameController.getMatches(player2.getID());
        MatchController matchController1 = gameController.createMatch(player1.getID(), 2, "player1");
        MatchController matchController2 = gameController.selectMatch(player2.getID(), matchController1.getMatchID());
        gameController.chooseNickname(player2.getID(), "player2",matchController2);
        matchController1.placeStarterCardForPlayer(player1.getID(), CardSideType.BACK);
        matchController2.placeStarterCardForPlayer(player2.getID(), CardSideType.BACK);
        matchController1.setSecretObjectiveForPlayer(player1.getID(), 86);
        matchController2.setSecretObjectiveForPlayer(player2.getID(), 87);
        matchController1.chatMessage(player1.getID(),player2.getNickname(), "Hello");

        matchController2.chatMessage(player1.getID(), "player3","Hello");
        matchController2.chatMessage(player1.getID(),"all", "Hello");
    }

    @Test
    void addPlayer() {
        assertDoesNotThrow(() -> addPlayers("Player 1"));
        assertThrows(NicknameAlreadyExistsException.class, () -> addPlayers("Player 1"));
        assertDoesNotThrow(() -> addPlayers("Player 2"));
        assertDoesNotThrow(() -> addPlayers("Player 3"));
        assertDoesNotThrow(() -> addPlayers("Player 4"));
        assertThrows(GameIsFullException.class, () -> addPlayers("Player 5"));

        assertEquals(4, matchController.getIDToPlayerMap().size());
    }

    private void addPlayers(String name) throws GameIsFullException, NicknameAlreadyExistsException, IOException {
        PlayerModel player = gameController.createPlayer(new TestSender());
        matchController.checkNickname(name);
        matchController.addCurrPlayer(player);
        matchController.addReadyPlayer();

        player.setNickname(name);
        player.setState(PlayerState.MATCH_LOBBY);
        player.setMatchID(1);
    }

    @Test
    void randomGameSetUp(){
        // Add players
        for (int i = 1; i <= 4; i++){
            int finalI = i;
            assertDoesNotThrow(() -> addPlayers("Player " + finalI));
        }

        assertEquals(4, matchController.getIDToPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        matchController.setUpGame();

        // Foreach player...
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            PlayerModel player = matchController.getIDToPlayerMap().get(i);

            // Set starter card
            PlaceableCard starterCard = matchController.drawStarterCard();
            matchController.getIDToPlayerMap().get(i).setStarterCard(starterCard);

            player.placeStarterCard(CardSideType.FRONT);

            // Choose token
            player.setToken(TokenColors.getColorFromInt(i % 4));

            assertDoesNotThrow(() -> matchController.fillPlayerHand(i));
        }


        // Set common objectives
        matchController.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            List<Integer> objectivesID = assertDoesNotThrow(() -> matchController.setObjectivesToChoose(i));

            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(objectivesID.getFirst()));
        }

        // Set first player
        matchController.setRandomFirstPlayer();

        matchController.calculateOrderOfPlayers();

        int firstPlayers = 0;

        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            if (matchController.getIDToPlayerMap().get(i).isFirstPlayer()){
                firstPlayers++;
            }

            try {
                assertNotNull(matchController.getIDToPlayerMap().get(i).getStarterCard());
            } catch (StarterCardNotSetException e) {
                System.out.println(e.getMessage());
            }

            assertEquals(3, matchController.getIDToPlayerMap().get(i).getHand().size());

            assertNotNull(matchController.getIDToPlayerMap().get(i).getSecretObjective());
        }

        assertEquals(1, firstPlayers);
    }

//    @Test
//    void simulateRandomGame(){
//        // region Game setup
//        // Add players
//        List<Integer> usedPlayerIDs = new ArrayList<>();
//        Random rng = new Random();
//
//        for (int i = 1; i <= numOfPlayers; i++){
//            int rand = rng.nextInt(1000);
//
//            while (usedPlayerIDs.contains(rand)){
//                rand = rng.nextInt(1000);
//            }
//
//            usedPlayerIDs.add(rand);
//            int finalI = i;
//            assertDoesNotThrow(() -> addPlayers("Player " + finalI));
//        }
//
//        assertEquals(numOfPlayers, matchController.getIDToPlayerMap().size());
//
//        // Set up decks (initialize, shuffle and set visible cards)
//        matchController.setUpGame();
//
//        // For each player...
//        for (Integer i : matchController.getIDToPlayerMap().keySet()) {
//            PlayerModel player = matchController.getIDToPlayerMap().get(i);
//            PlaceableCard starterCard = null;
//
//            // Set starter card
//            matchController.drawStarterCard(i);
//
//            try {
//                starterCard = player.getStarterCard();
//            } catch (StarterCardNotSetException e) {
//                System.out.println(e.getMessage());
//            }
//
//            assertNotNull(starterCard);
//
//            player.placeStarterCard(CardSideType.FRONT);
//
//            assertTrue(player.getPlayArea().containsKey(new Position(0, 0)));
//            assertEquals(player.getPlayArea().get(new Position(0, 0)), starterCard);
//
//            // Choose token
//            player.setToken(TokenColors.getColorFromInt(i % 4));
//
//            try {
//                matchController.fillPlayerHand(i);
//            }
//            catch (InvalidGameStateException | NotEnoughCardsException | InvalidPlayerIDException e) {
//                System.out.println(e.getMessage());
//            }
//
//            assertEquals(3, player.getHand().size());
//        }
//
//        // Set common objectives
//        matchController.setCommonObjectives();
//
//        // Set objectives to choose
//        for (Integer i : matchController.getIDToPlayerMap().keySet()){
//            List<Integer> objectivesID = assertDoesNotThrow(() -> matchController.setObjectivesToChoose(i));
//
//            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(objectivesID.getFirst()));
//
//            assertNotNull(matchController.getIDToPlayerMap().get(i).getSecretObjective());
//        }
//
//        // Set first player
//        matchController.setRandomFirstPlayer();
//
//        matchController.calculateOrderOfPlayers();
//
//        int firstPlayers = 0;
//
//        for (Integer i : matchController.getIDToPlayerMap().keySet()){
//            if (matchController.getIDToPlayerMap().get(i).isFirstPlayer()){
//                firstPlayers++;
//            }
//        }
//
//        assertEquals(1, firstPlayers);
//
//        matchController.endGameSetUp();
//        // endregion
//
//        // region Game simulation
//        HashMap<Integer, ArrayList<Position>> plIdToLegalPos = new HashMap<>();
//        HashMap<Integer, ArrayList<Position>> plIdToIllegalPos = new HashMap<>();
//
//        for (Integer i : matchController.getIDToPlayerMap().keySet()){
//            plIdToLegalPos.put(i, new ArrayList<>());
//            plIdToIllegalPos.put(i, new ArrayList<>());
//
//            updateIlLegalPositions(plIdToLegalPos, plIdToIllegalPos, i, new Position(0, 0));
//        }
//
//        // =============================== GAME START ===============================
//
//        while (matchController.getGameState() == GameState.PLAYING || matchController.getGameState() == GameState.FINAL_ROUND){
//            placeCardAction(plIdToLegalPos, plIdToIllegalPos);
//
//            drawCardAction();
//        }
//
//        while (matchController.getGameState() == GameState.EXTRA_ROUND){
//            placeCardAction(plIdToLegalPos, plIdToIllegalPos);
//        }
//
//        // =============================== GAME END ===============================
//        System.out.println("Game ended");
//        // endregion
//    }

    private void placeCardAction(HashMap<Integer, ArrayList<Position>> plIdToLegalPos, HashMap<Integer, ArrayList<Position>> plIdToIllegalPos){
        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        Random rng = new Random();

        ArrayList<Integer> cardIDsInHand = new ArrayList<>();

        for (PlaceableCard card : currPlayer.getHand()){
            if (card.hasEnoughRequiredResources(currPlayer.getNumOfResourcesArr(), CardSideType.FRONT)){
                cardIDsInHand.add(card.getID());
            }
        }

        CardSideType side = CardSideType.FRONT;

        // If we can't place any card because of missing resources, side is now back
        if (cardIDsInHand.isEmpty()){
            side = CardSideType.BACK;
            for (PlaceableCard card : currPlayer.getHand()){
                cardIDsInHand.add(card.getID());
            }
        }

        int cardIndexInHand = rng.nextInt(cardIDsInHand.size());

        int cardToPlaceID = cardIDsInHand.get(cardIndexInHand);

        int positionIndex = rng.nextInt(plIdToLegalPos.get(currPlayerID).size());

        Position positionToPlace = plIdToLegalPos.get(currPlayerID).get(positionIndex);

        CardSideType finalSide = side;
        assertDoesNotThrow(()-> matchController.placeCard(currPlayerID, cardToPlaceID, positionToPlace, finalSide));

        // Update legal and illegal positions
        updateIlLegalPositions(plIdToLegalPos, plIdToIllegalPos, currPlayerID, positionToPlace);
    }

    private void drawCardAction(){
        int currPlayerID = getCurrentPlayerId();

        Random rng = new Random();

        // 0: visible resource, 1: deck resource, 2: visible golden, 3: deck golden
        ArrayList<Integer> drawOptions = new ArrayList<>(){{add(0); add(1); add(2); add(3);}};

        while (getCurrentPlayer().getHand().size() < 3){
            int rand = rng.nextInt(drawOptions.size());

            if (drawOptions.isEmpty()){
                break;
            }

            switch (drawOptions.get(rand)){
                case 0:
                    // Draw visible resource
                    matchController.drawVisibleResourceCard(currPlayerID, rng.nextInt(2));
                    break;
                case 1:
                    // Draw deck resource
                    try {
                        matchController.drawResourceCard(currPlayerID);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    // Draw visible golden
                    matchController.drawVisibleGoldenCard(currPlayerID, rng.nextInt(2));
                    break;
                case 3:
                    // Draw deck golden
                    try {
                        matchController.drawGoldenCard(currPlayerID);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }

            drawOptions.remove(drawOptions.get(rand));
        }
    }

    private void updateIlLegalPositions(HashMap<Integer, ArrayList<Position>> plIdToLegalPos,
                                                              HashMap<Integer, ArrayList<Position>> plIdToIllegalPos,
                                                              int playerID, Position pos){

        PlayerModel player = matchController.getIDToPlayerMap().get(playerID);

        ArrayList<Position> newLegalPos = new ArrayList<>();
        ArrayList<Position> newIllegalPos = new ArrayList<>();

        Side chosenSide = player.getPlayArea().get(pos).getCurrSide();

        if (chosenSide.getBLCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }

        if (chosenSide.getBRCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }

        if (chosenSide.getTLCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }

        if (chosenSide.getTRCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
        }

        for (Position p : newIllegalPos){
            if (!plIdToIllegalPos.get(playerID).contains(p)){
                plIdToIllegalPos.get(playerID).add(p);
            }

            plIdToLegalPos.get(playerID).remove(p);
        }

        for (Position p : newLegalPos){
            if (!plIdToLegalPos.get(playerID).contains(p) && !plIdToIllegalPos.get(playerID).contains(p)){
                plIdToLegalPos.get(playerID).add(p);
            }
        }

        // Set the new card's position as illegal (a card just got placed)
        plIdToLegalPos.get(playerID).remove(pos);

        if (!plIdToIllegalPos.get(playerID).contains(pos)){
            plIdToIllegalPos.get(playerID).add(pos);
        }
    }

    private PlayerModel getCurrentPlayer(){
        return matchController.getIDToPlayerMap().get(getCurrentPlayerId());
    }

    private int getCurrentPlayerId(){
        return matchController.getCurrPlayerID();
    }

//    @Test
//    void testPlaceAndDrawExceptions(){
//        setUpGame();
//
//        PlayerModel currPlayer = getCurrentPlayer();
//
//        PlayerModel notCurrPlayer = matchController.getIDToPlayerMap().keySet().stream().filter(i -> i != matchController.getCurrPlayerID()).findFirst().map(i -> matchController.getIDToPlayerMap().get(i)).orElse(null);
//
//        assert notCurrPlayer != null;
//        assertThrows(InvalidPlayerIDException.class, () -> matchController.placeCard(-1, 1, new Position(0, 0), CardSideType.FRONT)); // Invalid player id
//        assertThrows(NotPlayerTurnException.class, () -> matchController.placeCard(notCurrPlayer.getID(), 1, new Position(0, 0), CardSideType.FRONT)); // Not curr player
//        assertThrows(InvalidCardIDException.class, () -> matchController.placeCard(currPlayer.getID(), -1, new Position(0, 0), CardSideType.FRONT)); // Invalid card id
//
//        int cardNotInHandId = 0;
//        boolean found = true;
//        while (true){
//            for (PlaceableCard card : currPlayer.getHand()){
//                if (card.getID() == cardNotInHandId){
//                    found = false;
//                    break;
//                }
//            }
//
//            if (found){
//                break;
//            }
//            else{
//                found = true;
//                cardNotInHandId++;
//            }
//        }
//
//        int finalCardNotInHandId = cardNotInHandId;
//        assertThrows(CardNotInHandException.class, () -> matchController.placeCard(currPlayer.getID(), finalCardNotInHandId, new Position(1, 1), CardSideType.FRONT)); // Card not in hand
//
//        cardNotInHandId = 0;
//        while (true){
//            for (PlaceableCard card : currPlayer.getHand()){
//                if (card.getID() == cardNotInHandId){
//                    found = false;
//                    break;
//                }
//            }
//
//            if (found){
//                break;
//            }
//            else{
//                found = true;
//                cardNotInHandId++;
//            }
//        }
//
//        matchController.setGameState(GameState.SET_UP);
//        assertThrows(InvalidGameStateException.class, () -> matchController.placeCard(currPlayer.getID(), currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT)); // Invalid game state
//
//        matchController.setGameState(GameState.PLAYING);
//        assertThrows(InvalidPlayerStateException.class, () -> matchController.drawResourceCard(currPlayer.getID())); // Not drawing state
//
//        // Place card without resources
//        int cardWithoutResourcesId = currPlayer.getHand().stream().filter(card -> !card.hasEnoughRequiredResources(currPlayer.getNumOfResourcesArr(), CardSideType.FRONT)).findFirst().map(PlaceableCard::getID).orElse(0);
//        assertThrows(MissingResourcesException.class, ()->matchController.placeCard(currPlayer.getID(), cardWithoutResourcesId, new Position(1, 1), CardSideType.FRONT)); // Not enough resources
//
//        assertDoesNotThrow(() -> matchController.placeCard(currPlayer.getID(), currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT)); // Correct
//
//        matchController.drawVisibleResourceCard(-1, 0);
//        matchController.drawVisibleResourceCard(currPlayer.getID(), -1);
//        matchController.drawVisibleResourceCard(currPlayer.getID(), 5);
//        matchController.drawVisibleGoldenCard(-1, 0);
//        matchController.drawVisibleGoldenCard(currPlayer.getID(), -1);
//        matchController.drawVisibleGoldenCard(currPlayer.getID(), 5);
//
//        matchController.setGameState(GameState.SET_UP);
//        assertThrows(InvalidGameStateException.class, () -> matchController.drawResourceCard(currPlayer.getID())); // Invalid game state
//
//        matchController.setGameState(GameState.PLAYING);
//        assertThrows(NotPlayerTurnException.class, () -> matchController.drawResourceCard(notCurrPlayer.getID())); // Not curr player
//    }

    @Test
    void testPlaceAndDraw(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        // Place card
        int finalCurrPlayerID = currPlayerID;
        PlayerModel finalCurrPlayer = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID, finalCurrPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK));
        // Draw card
        int finalCurrPlayerID4 = currPlayerID;
        assertDoesNotThrow(() -> matchController.drawResourceCard(finalCurrPlayerID4));

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        int finalCurrPlayerID1 = currPlayerID;
        PlayerModel finalCurrPlayer1 = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID1, finalCurrPlayer1.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK));
        // Draw card
        int finalCurrPlayerID5 = currPlayerID;
        assertDoesNotThrow(() -> matchController.drawGoldenCard(finalCurrPlayerID5));

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        int finalCurrPlayerID2 = currPlayerID;
        PlayerModel finalCurrPlayer2 = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID2, finalCurrPlayer2.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleGoldenCard(currPlayerID, 0);

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        int finalCurrPlayerID3 = currPlayerID;
        PlayerModel finalCurrPlayer3 = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID3, finalCurrPlayer3.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleResourceCard(currPlayerID, 0);
    }

    @Test
    void restoreVisibleResourceCardEmptyDeck(){
        setUpGame();

        clearResCardDeck();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();
        // Place card
        assertDoesNotThrow(() -> matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleResourceCard(currPlayerID, 0);
    }

    @Test
    void restoreVisibleGoldenCardEmptyDeck(){
        setUpGame();

        clearGoldCardDeck();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();
        // Place card
        assertDoesNotThrow(() -> matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleGoldenCard(currPlayerID, 0);
    }

//    @Test
//    void emptyResDeckDraw(){
//        setUpGame();
//
//        clearResCardDeck();
//
//        PlayerModel currPlayer = getCurrentPlayer();
//        int currPlayerID = getCurrentPlayerId();
//
//        assertDoesNotThrow(() -> matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK) );
//
//        assertThrows(NotEnoughCardsException.class, () -> matchController.drawResourceCard(currPlayerID)); // Empty resource deck
//    }

//    @Test
//    void emptyGoldDeckDraw(){
//        setUpGame();
//
//        clearGoldCardDeck();
//
//        PlayerModel currPlayer = getCurrentPlayer();
//        int currPlayerID = getCurrentPlayerId();
//
//        assertDoesNotThrow(() -> matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK) );
//
//        assertThrows(NotEnoughCardsException.class, () -> matchController.drawGoldenCard(currPlayerID)); // Empty golden deck
//    }

    @Test
    void testPosition(){
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(0, 0);

        assertEquals(pos1, pos2);
        assertNotEquals(pos1, null);
    }

    @Test
    void setUpGameWithErrors(){
        // region Game setup
        // Add players
        for (int i = 1 ; i <= 4; i++){
            int finalI = i;
            assertDoesNotThrow(() -> addPlayers("Player " + finalI));
        }

        assertEquals(4, matchController.getIDToPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        matchController.setUpGame();

        matchController.setGameState(GameState.PREGAME);

        assertThrows(InvalidGameStateException.class, () -> matchController.fillPlayerHand(-1)); // Invalid game state


        matchController.setGameState(GameState.SET_UP);

        assertThrows(InvalidPlayerIDException.class, () -> matchController.fillPlayerHand(-1)); // Invalid player id

        // Foreach player...
        for (Integer i : matchController.getIDToPlayerMap().keySet()) {
            PlayerModel player = matchController.getIDToPlayerMap().get(i);

            // Set starter card
            PlaceableCard starterCard = matchController.drawStarterCard();
            matchController.getIDToPlayerMap().get(i).setStarterCard(starterCard);

            assertDoesNotThrow(player::getStarterCard);

            player.placeStarterCard(CardSideType.FRONT);
            player.placeStarterCard(CardSideType.FRONT);

            assertTrue(player.getPlayArea().containsKey(new Position(0, 0)));

            // Choose token
            player.setToken(TokenColors.getColorFromInt(i % 4));

            assertDoesNotThrow(() -> matchController.fillPlayerHand(i));

            assertEquals(3, player.getHand().size());
        }

        // Set common objectives
        matchController.setCommonObjectives();

        assertThrows(InvalidPlayerIDException.class, () -> matchController.setObjectivesToChoose(-1)); // Invalid player id

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            List<Integer> objectivesID = assertDoesNotThrow(() -> matchController.setObjectivesToChoose(i));

            assertThrows(InvalidObjectiveCardIDException.class, () -> matchController.getIDToPlayerMap().get(i).setSecretObjective(-3)); // Wrong objective card id
            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(objectivesID.getFirst())); // Correct

            assertNotNull(matchController.getIDToPlayerMap().get(i).getSecretObjective());
        }

        // Set first player
        matchController.setRandomFirstPlayer();

        matchController.calculateOrderOfPlayers();

        int firstPlayers = 0;

        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            if (matchController.getIDToPlayerMap().get(i).isFirstPlayer()){
                firstPlayers++;
            }
        }

        assertEquals(1, firstPlayers);

        matchController.endGameSetUp();
        // endregion
    }

//    @Test
//    void testMaxPointsGame(){
//        setUpGame();
//
//        // region Game simulation
//        HashMap<Integer, ArrayList<Position>> plIdToLegalPos = new HashMap<>();
//        HashMap<Integer, ArrayList<Position>> plIdToIllegalPos = new HashMap<>();
//
//        for (Integer i : matchController.getIDToPlayerMap().keySet()){
//            plIdToLegalPos.put(i, new ArrayList<>());
//            plIdToIllegalPos.put(i, new ArrayList<>());
//
//            updateIlLegalPositions(plIdToLegalPos, plIdToIllegalPos, i, new Position(0, 0));
//        }
//
//        // =============================== GAME START ===============================
//
//        while (matchController.getGameState() == GameState.PLAYING || matchController.getGameState() == GameState.FINAL_ROUND){
//            placeCardAction(plIdToLegalPos, plIdToIllegalPos);
//
//            drawCardAction();
//        }
//
//        // Set points to max
//        for (Integer i : matchController.getIDToPlayerMap().keySet()){
//            matchController.getIDToPlayerMap().get(i).setCurrScore(29);
//        }
//
//        while (matchController.getGameState() == GameState.EXTRA_ROUND){
//            placeCardAction(plIdToLegalPos, plIdToIllegalPos);
//        }
//
//        // =============================== GAME END ===============================
//        System.out.println("Game ended");
//        // endregion
//    }

    private void setUpGame(){
        // region Game setup
        // Add players
        for (int i = 1 ; i <= 4; i++){
            int finalI = i;
            assertDoesNotThrow(() -> addPlayers("Player " + finalI));
        }

        // Set up decks (initialize, shuffle and set visible cards)
        matchController.setUpGame();

        // Foreach player...
        for (Integer i : matchController.getIDToPlayerMap().keySet()) {
            PlayerModel player = matchController.getIDToPlayerMap().get(i);

            // Set starter card
            PlaceableCard starterCard = matchController.drawStarterCard();
            matchController.getIDToPlayerMap().get(i).setStarterCard(starterCard);

            try {
                player.getStarterCard();
            } catch (StarterCardNotSetException e) {
                System.out.println(e.getMessage());
            }

            player.placeStarterCard(CardSideType.FRONT);

            // Choose token
            player.setToken(TokenColors.getColorFromInt(i % 4));

            assertDoesNotThrow(() -> matchController.fillPlayerHand(i));
        }

        // Set common objectives
        matchController.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            List<Integer> objectivesID = assertDoesNotThrow(() -> matchController.setObjectivesToChoose(i));

            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(Math.random() < 0.5 ? objectivesID.getFirst() : objectivesID.getLast()));
        }

        // Set first player
        matchController.setRandomFirstPlayer();

        matchController.calculateOrderOfPlayers();

        matchController.endGameSetUp();
        // endregion
    }
    private void clearResCardDeck(){
        for (int i = 0; i < (38 - numOfPlayers * 2); i++){
            PlayerModel currPlayer = getCurrentPlayer();
            int currPlayerID = getCurrentPlayerId();
            int cardID = currPlayer.getHand().stream().filter(card -> card instanceof ResourceCard).findFirst().map(PlaceableCard::getID).orElse(0);

            // Place a resource card
            int finalI = i;
            assertDoesNotThrow(() -> matchController.placeCard(currPlayerID, cardID, new Position((finalI / numOfPlayers) + 1, (finalI / numOfPlayers) + 1), CardSideType.BACK));

            // Draw a resource card
            try {
                matchController.drawResourceCard(currPlayerID);
            }catch (Exception e){
                break;
            }
        }
    }

    private void clearGoldCardDeck(){
        for (int i = 0; i < (38 - numOfPlayers); i++){
            PlayerModel currPlayer = getCurrentPlayer();
            int currPlayerID = getCurrentPlayerId();
            int cardID = currPlayer.getHand().stream().filter(card -> card instanceof GoldenCard).findFirst().map(PlaceableCard::getID).orElse(0);

            // Place a golden card
            int finalI = i;
            assertDoesNotThrow(() -> matchController.placeCard(currPlayerID, cardID, new Position((finalI / numOfPlayers) + 1, -(finalI / numOfPlayers) - 1), CardSideType.BACK));

            // Draw a golden card
            try {
                matchController.drawGoldenCard(currPlayerID);
            }catch (Exception e){
                break;
            }
        }
    }
}
