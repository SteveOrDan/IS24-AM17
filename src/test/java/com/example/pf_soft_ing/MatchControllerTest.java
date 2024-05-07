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
        addPlayers("Player 1");
        addPlayers("Player 1");
        addPlayers("Player 2");
        addPlayers("Player 3");
        addPlayers("Player 4");
        addPlayers("Player 5");

        assertEquals(4, matchController.getIDToPlayerMap().size());
    }

    private void addPlayers(String name) {
        try {
            PlayerModel player = gameController.getGameModel().createPlayer(new SocketSender(new ObjectOutputStream(System.out)));

            matchController.getMatchModel().addCurrPlayer(player);
            matchController.getMatchModel().addReadyPlayer();

            player.setNickname(name);
            player.setState(PlayerState.MATCH_LOBBY);
            player.setMatchID(1);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void randomGameSetUp(){
        // Add players
        for (int i = 1; i <= 6; i++){
            addPlayers("Player " + i);
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

            try {
                matchController.fillPlayerHand(i);
            }
            catch (InvalidGameStateException | NotEnoughCardsException | InvalidPlayerIDException e) {
                System.out.println(e.getMessage());
            }
        }


        // Set common objectives
        matchController.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            try {
                matchController.setObjectivesToChoose(i);
            }
            catch (InvalidPlayerIDException e) {
                System.out.println(e.getMessage());
            }

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            matchController.getIDToPlayerMap().get(i).setSecretObjective(randIndex);
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
            addPlayers("Player " + i);
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

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            matchController.getIDToPlayerMap().get(i).setSecretObjective(randIndex);

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

        matchController.placeCard(currPlayerID, cardToPlaceID, positionToPlace, side);

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
                    matchController.drawResourceCard(currPlayerID);
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
        return matchController.getIDToPlayerMap().get(matchController.getMatchModel().getCurrPlayerID());
    }

    private int getCurrentPlayerId(){
        return matchController.getMatchModel().getCurrPlayerID();
    }

    @Test
    public void testPlaceAndDrawExceptions(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();

        PlayerModel notCurrPlayer = null;
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            if (i != matchController.getMatchModel().getCurrPlayerID()){
                notCurrPlayer = matchController.getIDToPlayerMap().get(i);
                break;
            }
        }

        assert notCurrPlayer != null;
        matchController.placeCard(-1, 1, new Position(0, 0), CardSideType.FRONT); // Invalid player id
        matchController.placeCard(notCurrPlayer.getID(), 1, new Position(0, 0), CardSideType.FRONT); // Not curr player
        matchController.placeCard(currPlayer.getID(), -1, new Position(0, 0), CardSideType.FRONT); // Invalid card id

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

        matchController.placeCard(currPlayer.getID(), cardNotInHandId, new Position(1, 1), CardSideType.FRONT); // Card not in hand

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
        matchController.placeCard(currPlayer.getID(), currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT); // Invalid game state

        matchController.getMatchModel().setGameState(GameState.PLAYING);
        matchController.drawResourceCard(currPlayer.getID()); // Not drawing state

        // Place card without resources
        int cardWithoutResourcesId = 0;
        for (PlaceableCard card : currPlayer.getHand()){
            if (!card.hasEnoughRequiredResources(currPlayer.getNumOfResourcesArr(), CardSideType.FRONT)){
                cardWithoutResourcesId = card.getID();
                break;
            }
        }
        matchController.placeCard(currPlayer.getID(), cardWithoutResourcesId, new Position(1, 1), CardSideType.FRONT); // Not enough resources

        matchController.placeCard(currPlayer.getID(), currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT); // Correct

        matchController.drawVisibleResourceCard(-1, 0);
        matchController.drawVisibleResourceCard(currPlayer.getID(), -1);
        matchController.drawVisibleResourceCard(currPlayer.getID(), 5);
        matchController.drawVisibleGoldenCard(-1, 0);
        matchController.drawVisibleGoldenCard(currPlayer.getID(), -1);
        matchController.drawVisibleGoldenCard(currPlayer.getID(), 5);

        matchController.getMatchModel().setGameState(GameState.SET_UP);
        matchController.drawResourceCard(currPlayer.getID()); // Invalid game state

        matchController.getMatchModel().setGameState(GameState.PLAYING);
        matchController.drawResourceCard(notCurrPlayer.getID()); // Correct
    }

    @Test
    public void testPlaceAndDraw(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        // Place card
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK);
        // Draw card
        matchController.drawResourceCard(currPlayerID);

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK);
        // Draw card
        matchController.drawGoldenCard(currPlayerID);

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK);
        // Draw card
        matchController.drawVisibleGoldenCard(currPlayerID, 0);

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.BACK);
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
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK);
        // Draw card
        matchController.drawVisibleResourceCard(currPlayerID, 0);

        matchController.getMatchModel().getGoldenCardsDeck().getDeck().clear();

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK);
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
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK);
        // Draw card
        matchController.drawVisibleGoldenCard(currPlayerID, 0);

        matchController.getMatchModel().getResourceCardsDeck().getDeck().clear();

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(-1, 1), CardSideType.BACK);
        // Draw card
        matchController.drawVisibleGoldenCard(currPlayerID, 0);
    }

    @Test
    public void emptyDeckDraw(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        matchController.placeCard(currPlayerID, currPlayer.getHand().getFirst().getID(), new Position(1, 1), CardSideType.FRONT);

        matchController.getMatchModel().getResourceCardsDeck().getDeck().clear();
        matchController.getMatchModel().getGoldenCardsDeck().getDeck().clear();

        matchController.drawResourceCard(currPlayerID); // Empty deck
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
        addPlayers("Player 1");
        addPlayers("Player 2");
        addPlayers("Player 3");
        addPlayers("Player 4");

        assertEquals(4, matchController.getIDToPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        matchController.setUpGame();

        matchController.getMatchModel().setGameState(GameState.PREGAME);
        matchController.drawStarterCard(-1); // Invalid game state
        try {
            matchController.fillPlayerHand(-1); // Invalid game state
        }
        catch (InvalidGameStateException | InvalidPlayerIDException | NotEnoughCardsException e) {
            System.out.println(e.getMessage());
        }

        matchController.getMatchModel().setGameState(GameState.SET_UP);
        matchController.drawStarterCard(-1); // Invalid id
        try {
            matchController.fillPlayerHand(-1); // Invalid id
        }
        catch (InvalidGameStateException | InvalidPlayerIDException | NotEnoughCardsException e) {
            System.out.println(e.getMessage());
        }

        // Foreach player...
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
            player.placeStarterCard(CardSideType.FRONT);

            assertTrue(player.getPlayArea().containsKey(new Position(0, 0)));
            assertEquals(player.getPlayArea().get(new Position(0, 0)), starterCard);

            // Choose token
            player.setToken(TokenColors.getColorFromInt(i % 4));

            try {
                matchController.fillPlayerHand(i);
            }
            catch (InvalidGameStateException | InvalidPlayerIDException | NotEnoughCardsException e) {
                System.out.println(e.getMessage());
            }

            assertEquals(3, player.getHand().size());
        }

        // Set common objectives
        matchController.setCommonObjectives();

        assertEquals(2, matchController.getMatchModel().getObjectiveCardsDeck().getCommonObjectives().size());

        try {
            matchController.setObjectivesToChoose(-1); // Invalid id
        }
        catch (InvalidPlayerIDException e) {
            System.out.println(e.getMessage());
        }

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            try {
                matchController.setObjectivesToChoose(i);
            }
            catch (InvalidPlayerIDException e) {
                System.out.println(e.getMessage());
            }

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            matchController.getIDToPlayerMap().get(i).setSecretObjective(3); // Wrong index
            matchController.getIDToPlayerMap().get(i).setSecretObjective(-3); // Wrong index
            matchController.getIDToPlayerMap().get(i).setSecretObjective(randIndex);

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
        addPlayers("Player 1");
        addPlayers("Player 2");
        addPlayers("Player 3");
        addPlayers("Player 4");

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

            try {
                matchController.fillPlayerHand(i);
            }
            catch (InvalidGameStateException | InvalidPlayerIDException | NotEnoughCardsException e) {
                System.out.println(e.getMessage());
            }
        }

        // Set common objectives
        matchController.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : matchController.getIDToPlayerMap().keySet()){
            try {
                matchController.setObjectivesToChoose(i);
            }
            catch (InvalidPlayerIDException e) {
                System.out.println(e.getMessage());
            }

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            matchController.getIDToPlayerMap().get(i).setSecretObjective(randIndex);
        }

        // Set first player
        matchController.setRandomFirstPlayer();

        matchController.calculateOrderOfPlayers();

        matchController.endGameSetUp();
        // endregion
    }
}
