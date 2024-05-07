package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.server.SocketSender;
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

    static int numOfPlayers = 4;
    MatchController matchController = new MatchController(numOfPlayers, 1);

    GameController gameController = new GameController();

    @Test
    void checkResources(){
        matchController.initializeDecks();

        assertEquals(86, GameResources.getIDToPlaceableCardMap().size());
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
        PlayerModel player = gameController.getGameModel().createPlayer(new SocketSender(new ObjectOutputStream(System.out)));
        matchController.getMatchModel().checkNickname(name);
        matchController.getMatchModel().addCurrPlayer(player);
        matchController.getMatchModel().addReadyPlayer();

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
            matchController.drawStarterCard(i);

            player.placeStarterCard(CardSideType.FRONT);

            // Choose token
            player.setToken(TokenColors.getColorFromInt(i % 4));

            assertDoesNotThrow(() -> matchController.fillPlayerHand(i));
        }


        // Set common objectives
        matchController.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            assertDoesNotThrow(() -> matchController.setObjectivesToChoose(i));

            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(matchController.getIDToPlayerMap().get(i).getObjectivesToChoose().getFirst().getID()));
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

    @Test
    void simulateRandomGame(){
        // region Game setup
        // Add players
        List<Integer> usedPlayerIDs = new ArrayList<>();
        Random rng = new Random();

        for (int i = 1; i <= numOfPlayers; i++){
            int rand = rng.nextInt(1000);

            while (usedPlayerIDs.contains(rand)){
                rand = rng.nextInt(1000);
            }

            usedPlayerIDs.add(rand);
            int finalI = i;
            assertDoesNotThrow(() -> addPlayers("Player " + finalI));
        }

        assertEquals(numOfPlayers, matchController.getIDToPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        matchController.setUpGame();

        // For each player...
        for (Integer i : matchController.getIDToPlayerMap().keySet()) {
            PlayerModel player = matchController.getIDToPlayerMap().get(i);
            PlaceableCard starterCard = null;

            // Set starter card
            matchController.drawStarterCard(i);

            try {
                starterCard = player.getStarterCard();
            } catch (StarterCardNotSetException e) {
                System.out.println(e.getMessage());
            }

            assertNotNull(starterCard);

            player.placeStarterCard(CardSideType.FRONT);

            assertTrue(player.getPlayArea().containsKey(new Position(0, 0)));
            assertEquals(player.getPlayArea().get(new Position(0, 0)), starterCard);

            // Choose token
            player.setToken(TokenColors.getColorFromInt(i % 4));

            try {
                matchController.fillPlayerHand(i);
            }
            catch (InvalidGameStateException | NotEnoughCardsException | InvalidPlayerIDException e) {
                System.out.println(e.getMessage());
            }

            assertEquals(3, player.getHand().size());
        }

        // Set common objectives
        matchController.setCommonObjectives();

        assertEquals(2, matchController.getMatchModel().getObjectiveCardsDeck().getCommonObjectives().size());

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            try {
                matchController.setObjectivesToChoose(i);
            }
            catch (InvalidPlayerIDException e) {
                System.out.println(e.getMessage());
            }

            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(matchController.getIDToPlayerMap().get(i).getObjectivesToChoose().getFirst().getID()));

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

        // region Game simulation
        HashMap<Integer, ArrayList<Position>> plIdToLegalPos = new HashMap<>();
        HashMap<Integer, ArrayList<Position>> plIdToIllegalPos = new HashMap<>();

        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            plIdToLegalPos.put(i, new ArrayList<>());
            plIdToIllegalPos.put(i, new ArrayList<>());

            updateIlLegalPositions(plIdToLegalPos, plIdToIllegalPos, i, new Position(0, 0));
        }

        // =============================== GAME START ===============================

        while (matchController.getMatchModel().getGameState() == GameState.PLAYING || matchController.getMatchModel().getGameState() == GameState.FINAL_ROUND){
            placeCardAction(plIdToLegalPos, plIdToIllegalPos);

            drawCardAction();
        }

        while (matchController.getMatchModel().getGameState() == GameState.EXTRA_ROUND){
            placeCardAction(plIdToLegalPos, plIdToIllegalPos);
        }

        // =============================== GAME END ===============================
        System.out.println("Game ended");
        // endregion
    }

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
                    };
                    break;
                case 2:
                    // Draw visible golden
                    matchController.drawVisibleGoldenCard(currPlayerID, rng.nextInt(2));
                    break;
                case 3:
                    // Draw deck golden
                    matchController.drawGoldenCard(currPlayerID);
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
        return matchController.getMatchModel().getCurrPlayerID();
    }

    @Test
    public void testPlaceAndDrawExceptions(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();

        PlayerModel notCurrPlayer = matchController.getIDToPlayerMap().keySet().stream().filter(i -> i != matchController.getMatchModel().getCurrPlayerID()).findFirst().map(i -> matchController.getIDToPlayerMap().get(i)).orElse(null);

        assert notCurrPlayer != null;
        assertThrows(InvalidPlayerIDException.class, () -> matchController.placeCard(-1, 1, new Position(0, 0), CardSideType.FRONT)); // Invalid player id
        assertThrows(NotPlayerTurnException.class, () -> matchController.placeCard(notCurrPlayer.getID(), 1, new Position(0, 0), CardSideType.FRONT)); // Not curr player
        assertThrows(InvalidCardIDException.class, () -> matchController.placeCard(currPlayer.getID(), -1, new Position(0, 0), CardSideType.FRONT)); // Invalid card id

        int cardNotInHandId = 0;
        boolean found = true;
        while (true){
            for (PlaceableCard card : currPlayer.getHand()){
                if (card.getID() == cardNotInHandId){
                    found = false;
                    break;
                }
            }

            if (found){
                break;
            }
            else{
                found = true;
                cardNotInHandId++;
            }
        }

        int finalCardNotInHandId = cardNotInHandId;
        assertThrows(CardNotInHandException.class, () -> matchController.placeCard(currPlayer.getID(), finalCardNotInHandId, new Position(1, 1), CardSideType.FRONT)); // Card not in hand

        cardNotInHandId = 0;
        while (true){
            for (PlaceableCard card : currPlayer.getHand()){
                if (card.getID() == cardNotInHandId){
                    found = false;
                    break;
                }
            }

            if (found){
                break;
            }
            else{
                found = true;
                cardNotInHandId++;
            }
        }

        matchController.getMatchModel().setGameState(GameState.SET_UP);
        assertThrows(InvalidGameStateException.class, () -> matchController.placeCard(currPlayer.getID(), currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT)); // Invalid game state

        matchController.getMatchModel().setGameState(GameState.PLAYING);
        assertThrows(InvalidPlayerStateException.class, () -> matchController.drawResourceCard(currPlayer.getID())); // Not drawing state

        // Place card without resources
        int cardWithoutResourcesId = currPlayer.getHand().stream().filter(card -> !card.hasEnoughRequiredResources(currPlayer.getNumOfResourcesArr(), CardSideType.FRONT)).findFirst().map(PlaceableCard::getID).orElse(0);
        assertThrows(MissingResourcesException.class, ()->matchController.placeCard(currPlayer.getID(), cardWithoutResourcesId, new Position(1, 1), CardSideType.FRONT)); // Not enough resources

        assertDoesNotThrow(() -> matchController.placeCard(currPlayer.getID(), currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT)); // Correct

        matchController.drawVisibleResourceCard(-1, 0);
        matchController.drawVisibleResourceCard(currPlayer.getID(), -1);
        matchController.drawVisibleResourceCard(currPlayer.getID(), 5);
        matchController.drawVisibleGoldenCard(-1, 0);
        matchController.drawVisibleGoldenCard(currPlayer.getID(), -1);
        matchController.drawVisibleGoldenCard(currPlayer.getID(), 5);

        matchController.getMatchModel().setGameState(GameState.SET_UP);
        assertThrows(InvalidGameStateException.class, () -> matchController.drawResourceCard(currPlayer.getID())); // Invalid game state

        matchController.getMatchModel().setGameState(GameState.PLAYING);
        assertThrows(NotPlayerTurnException.class, () -> matchController.drawResourceCard(notCurrPlayer.getID())); // Not curr player
    }

    @Test
    public void testPlaceAndDraw(){
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
        matchController.drawGoldenCard(currPlayerID);

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
    public void restoreVisibleResourceCardEmptyDeck(){
        setUpGame();

        matchController.getMatchModel().getResourceCardsDeck().getDeck().clear();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();
        // Place card
        int finalCurrPlayerID = currPlayerID;
        PlayerModel finalCurrPlayer = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID, finalCurrPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleResourceCard(currPlayerID, 0);

        matchController.getMatchModel().getGoldenCardsDeck().getDeck().clear();

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        int finalCurrPlayerID1 = currPlayerID;
        PlayerModel finalCurrPlayer1 = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID1, finalCurrPlayer1.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleResourceCard(currPlayerID, 0);
    }

    @Test
    public void restoreVisibleGoldenCardEmptyDeck(){
        setUpGame();

        matchController.getMatchModel().getGoldenCardsDeck().getDeck().clear();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();
        // Place card
        int finalCurrPlayerID = currPlayerID;
        PlayerModel finalCurrPlayer = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID, finalCurrPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleGoldenCard(currPlayerID, 0);

        matchController.getMatchModel().getResourceCardsDeck().getDeck().clear();

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        int finalCurrPlayerID1 = currPlayerID;
        PlayerModel finalCurrPlayer1 = currPlayer;
        assertDoesNotThrow(() -> matchController.placeCard(finalCurrPlayerID1, finalCurrPlayer1.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK));
        // Draw card
        matchController.drawVisibleGoldenCard(currPlayerID, 0);
    }

    @Test
    public void emptyDeckDraw(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        assertDoesNotThrow(() -> matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT) );

        matchController.getMatchModel().getResourceCardsDeck().getDeck().clear();
        matchController.getMatchModel().getGoldenCardsDeck().getDeck().clear();

        assertThrows(NotEnoughCardsException.class, () -> matchController.drawResourceCard(currPlayerID)); // Empty deck
        matchController.drawGoldenCard(currPlayerID); // Empty deck
    }

    @Test
    public void testPosition(){
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(0, 0);

        assertEquals(pos1, pos2);
        assertNotEquals(pos1, null);
    }

    @Test
    public void setUpGameWithErrors(){
        // region Game setup
        // Add players
        for (int i = 1 ; i <= 4; i++){
            int finalI = i;
            assertDoesNotThrow(() -> addPlayers("Player " + finalI));
        }

        assertEquals(4, matchController.getIDToPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        matchController.setUpGame();

        matchController.getMatchModel().setGameState(GameState.PREGAME);
        matchController.drawStarterCard(-1); // Invalid game state

        assertThrows(InvalidGameStateException.class, () -> matchController.fillPlayerHand(-1)); // Invalid game state


        matchController.getMatchModel().setGameState(GameState.SET_UP);
        matchController.drawStarterCard(-1); // Invalid id

        assertThrows(InvalidPlayerIDException.class, () -> matchController.fillPlayerHand(-1)); // Invalid player id

        // Foreach player...
        for (Integer i : matchController.getIDToPlayerMap().keySet()) {
            PlayerModel player = matchController.getIDToPlayerMap().get(i);

            // Set starter card
            matchController.drawStarterCard(i);

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

        assertEquals(2, matchController.getMatchModel().getObjectiveCardsDeck().getCommonObjectives().size());

        assertThrows(InvalidPlayerIDException.class, () -> matchController.setObjectivesToChoose(-1)); // Invalid player id

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            assertDoesNotThrow(() -> matchController.setObjectivesToChoose(i));

            assertThrows(InvalidObjectiveCardException.class, () -> matchController.getIDToPlayerMap().get(i).setSecretObjective(-3)); // Wrong objective card id
            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(matchController.getIDToPlayerMap().get(i).getObjectivesToChoose().getFirst().getID())); // Correct

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

    @Test
    public void testMaxPointsGame(){
        setUpGame();

        // region Game simulation
        HashMap<Integer, ArrayList<Position>> plIdToLegalPos = new HashMap<>();
        HashMap<Integer, ArrayList<Position>> plIdToIllegalPos = new HashMap<>();

        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            plIdToLegalPos.put(i, new ArrayList<>());
            plIdToIllegalPos.put(i, new ArrayList<>());

            updateIlLegalPositions(plIdToLegalPos, plIdToIllegalPos, i, new Position(0, 0));
        }

        // =============================== GAME START ===============================

        while (matchController.getMatchModel().getGameState() == GameState.PLAYING || matchController.getMatchModel().getGameState() == GameState.FINAL_ROUND){
            placeCardAction(plIdToLegalPos, plIdToIllegalPos);

            drawCardAction();
        }

        // Set points to max
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            matchController.getIDToPlayerMap().get(i).setCurrScore(29);
        }

        while (matchController.getMatchModel().getGameState() == GameState.EXTRA_ROUND){
            placeCardAction(plIdToLegalPos, plIdToIllegalPos);
        }

        // =============================== GAME END ===============================
        System.out.println("Game ended");
        // endregion
    }

    public void setUpGame(){
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
            matchController.drawStarterCard(i);

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
            assertDoesNotThrow(() -> matchController.setObjectivesToChoose(i));

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            assertDoesNotThrow(() -> matchController.getIDToPlayerMap().get(i).setSecretObjective(matchController.getIDToPlayerMap().get(i).getObjectivesToChoose().getFirst().getID()));
        }

        // Set first player
        matchController.setRandomFirstPlayer();

        matchController.calculateOrderOfPlayers();

        matchController.endGameSetUp();
        // endregion
    }
}
