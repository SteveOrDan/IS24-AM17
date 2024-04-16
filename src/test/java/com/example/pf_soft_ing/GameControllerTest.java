package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    static int numOfPlayers = 4;
    GameController gc = new GameController();

    @Test
    void checkResources(){
        gc.initializeDecks();

        assertEquals(86, gc.getIDPlaceableCardMap().size());
    }

    @Test
    void addPlayer() {
        gc.addPlayer("Player 1");
        gc.addPlayer("Player 1");
        gc.addPlayer("Player 2");
        gc.addPlayer("Player 3");
        gc.addPlayer("Player 4");
        gc.addPlayer("Player 5");

        assertEquals(4, gc.getIDPlayerMap().size());
    }

    @Test
    void randomGameSetUp(){
        // Add players
        for (int i = 1; i <= 6; i++){
            gc.addPlayer("Player " + i);
        }

        assertEquals(4, gc.getIDPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        gc.setUpGame();

        // Foreach player...
        for (Integer i : gc.getIDPlayerMap().keySet()){
            PlayerModel player = gc.getIDPlayerMap().get(i);

            // Set starter card
            gc.drawStarterCard(i);

            player.flipStarterCard();
            player.flipStarterCard();

            player.placeStarterCard();

            // Choose token
            player.setToken(new Token(TokenColors.getColorFromInt(i % 4)));

            gc.fillPlayerHand(i);
        }


        // Set common objectives
        gc.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : gc.getIDPlayerMap().keySet()){
            gc.setObjectivesToChoose(i);

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            gc.getIDPlayerMap().get(i).setSecretObjective(randIndex);
        }

        // Set first player
        gc.setRandomFirstPlayer();

        gc.calculateOrderOfPlayers();

        int firstPlayers = 0;

        for (Integer i : gc.getIDPlayerMap().keySet()){
            if (gc.getIDPlayerMap().get(i).isFirstPlayer()){
                firstPlayers++;
            }

            assertNotNull(gc.getIDPlayerMap().get(i).getStarterCard());

            assertEquals(3, gc.getIDPlayerMap().get(i).getHand().size());

            assertNotNull(gc.getIDPlayerMap().get(i).getSecretObjective());
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
            gc.addPlayer("Player " + i);
        }

        assertEquals(numOfPlayers, gc.getIDPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        gc.setUpGame();

        // Foreach player...
        for (Integer i : gc.getIDPlayerMap().keySet()) {
            PlayerModel player = gc.getIDPlayerMap().get(i);

            // Set starter card
            gc.drawStarterCard(i);

            assertNotNull(player.getStarterCard());

            player.flipStarterCard();
            player.flipStarterCard();

            player.placeStarterCard();

            assertTrue(player.getPlayArea().containsKey(new Position(0, 0)));
            assertEquals(player.getPlayArea().get(new Position(0, 0)), player.getStarterCard());

            // Choose token
            player.setToken(new Token(TokenColors.getColorFromInt(i % 4)));

            gc.fillPlayerHand(i);

            assertEquals(3, player.getHand().size());
        }

        // Set common objectives
        gc.setCommonObjectives();

        assertEquals(2, gc.getGameModel().getObjectiveCardsDeck().getCommonObjectives().size());

        // Set objectives to choose
        for (Integer i : gc.getIDPlayerMap().keySet()){
            gc.setObjectivesToChoose(i);

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            gc.getIDPlayerMap().get(i).setSecretObjective(randIndex);

            assertNotNull(gc.getIDPlayerMap().get(i).getSecretObjective());
        }

        // Set first player
        gc.setRandomFirstPlayer();

        gc.calculateOrderOfPlayers();

        int firstPlayers = 0;

        for (Integer i : gc.getIDPlayerMap().keySet()){
            if (gc.getIDPlayerMap().get(i).isFirstPlayer()){
                firstPlayers++;
            }
        }

        assertEquals(1, firstPlayers);

        gc.endGameSetUp();
        // endregion

        // region Game simulation
        HashMap<Integer, ArrayList<Position>> plIdToLegalPos = new HashMap<>();
        HashMap<Integer, ArrayList<Position>> plIdToIllegalPos = new HashMap<>();

        for (Integer i : gc.getIDPlayerMap().keySet()){
            plIdToLegalPos.put(i, new ArrayList<>());
            plIdToIllegalPos.put(i, new ArrayList<>());

            updateIlLegalPositions(plIdToLegalPos, plIdToIllegalPos, i, new Position(0, 0));
        }

        // =============================== GAME START ===============================

        while (gc.getGameModel().getGameState() == GameState.PLAYING || gc.getGameModel().getGameState() == GameState.FINAL_ROUND){
            placeCardAction(plIdToLegalPos, plIdToIllegalPos);

            drawCardAction();
        }

        while (gc.getGameModel().getGameState() == GameState.EXTRA_ROUND){
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
            if (card.hasEnoughRequiredResources(currPlayer.getNumOfResourcesArr())){
                cardIDsInHand.add(card.id);
            }
        }

        // If cant place any card because of resources, flip all cards
        if (cardIDsInHand.isEmpty()){
            for (PlaceableCard card : currPlayer.getHand()){
                cardIDsInHand.add(card.id);

                gc.flipCard(currPlayerID, card.id);
            }
        }

        int cardIndexInHand = rng.nextInt(cardIDsInHand.size());

        int cardToPlaceID = cardIDsInHand.get(cardIndexInHand);

        int positionIndex = rng.nextInt(plIdToLegalPos.get(currPlayerID).size());

        Position positionToPlace = plIdToLegalPos.get(currPlayerID).get(positionIndex);

        gc.placeCard(currPlayerID, cardToPlaceID, positionToPlace);

        while (!currPlayer.getPlayArea().containsKey(positionToPlace)){
            // Remove the card from the options
            cardIDsInHand.remove((Integer) cardToPlaceID);

            // If all card options have been exhausted, place a random card flipped
            if (cardIDsInHand.isEmpty()){
                for (PlaceableCard card : currPlayer.getHand()){
                    cardIDsInHand.add(card.id);
                }

                cardIndexInHand = rng.nextInt(currPlayer.getHand().size());

                cardToPlaceID = currPlayer.getHand().get(cardIndexInHand).id;

                gc.flipCard(currPlayerID, cardToPlaceID);
            }
            else{
                cardIndexInHand = rng.nextInt(currPlayer.getHand().size());

                cardToPlaceID = currPlayer.getHand().get(cardIndexInHand).id;
            }

            positionIndex = rng.nextInt(plIdToLegalPos.get(currPlayerID).size());

            positionToPlace = plIdToLegalPos.get(currPlayerID).get(positionIndex);

            gc.placeCard(currPlayerID, cardToPlaceID, positionToPlace);
        }

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
                    gc.drawVisibleResourceCard(currPlayerID, rng.nextInt(2));
                    break;
                case 1:
                    // Draw deck resource
                    gc.drawResourceCard(currPlayerID);
                    break;
                case 2:
                    // Draw visible golden
                    gc.drawVisibleGoldenCard(currPlayerID, rng.nextInt(2));
                    break;
                case 3:
                    // Draw deck golden
                    gc.drawGoldenCard(currPlayerID);
                    break;
            }

            drawOptions.remove(drawOptions.get(rand));
        }
    }

    private void updateIlLegalPositions(HashMap<Integer, ArrayList<Position>> plIdToLegalPos,
                                                              HashMap<Integer, ArrayList<Position>> plIdToIllegalPos,
                                                              int playerID, Position pos){

        PlayerModel player = gc.getIDPlayerMap().get(playerID);

        ArrayList<Position> newLegalPos = new ArrayList<>();
        ArrayList<Position> newIllegalPos = new ArrayList<>();

        Side currSide = player.getPlayArea().get(pos).getCurrSide();

        if (currSide.getBLCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }

        if (currSide.getBRCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }

        if (currSide.getTLCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }

        if (currSide.getTRCorner().isAvailable()){
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
        return gc.getIDPlayerMap().get(gc.getGameModel().getCurrPlayerID());
    }

    private int getCurrentPlayerId(){
        return gc.getGameModel().getCurrPlayerID();
    }

    @Test
    public void testPlaceAndDrawExceptions(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();

        PlayerModel notCurrPlayer = null;
        for (Integer i : gc.getIDPlayerMap().keySet()){
            if (i != gc.getGameModel().getCurrPlayerID()){
                notCurrPlayer = gc.getIDPlayerMap().get(i);
                break;
            }
        }

        assert notCurrPlayer != null;
        gc.placeCard(-1, 1, new Position(0, 0)); // Invalid player id
        gc.placeCard(notCurrPlayer.getId(), 1, new Position(0, 0)); // Not curr player
        gc.placeCard(currPlayer.getId(), -1, new Position(0, 0)); // Invalid card id

        int cardNotInHandId = 0;
        boolean found = true;
        while (true){
            for (PlaceableCard card : currPlayer.getHand()){
                if (card.getId() == cardNotInHandId){
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

        gc.placeCard(currPlayer.getId(), cardNotInHandId, new Position(1, 1)); // Card not in hand

        gc.flipCard(-1, 1); // Invalid player id
        gc.flipCard(currPlayer.getId(), -1); // Invalid card id

        cardNotInHandId = 0;
        while (true){
            for (PlaceableCard card : currPlayer.getHand()){
                if (card.getId() == cardNotInHandId){
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
        gc.flipCard(currPlayer.getId(), cardNotInHandId); // Card not in hand

        gc.getGameModel().setGameState(GameState.SET_UP);
        gc.placeCard(currPlayer.getId(), currPlayer.getHand().getFirst().getId(), new Position(1, 1)); // Invalid game state

        gc.getGameModel().setGameState(GameState.PLAYING);
        gc.drawResourceCard(currPlayer.getId()); // Not drawing state
        gc.placeCard(currPlayer.getId(), currPlayer.getHand().getFirst().getId(), new Position(1, 1)); // Correct

        gc.drawVisibleResourceCard(-1, 0);
        gc.drawVisibleResourceCard(currPlayer.getId(), -1);
        gc.drawVisibleResourceCard(currPlayer.getId(), 5);
        gc.drawVisibleGoldenCard(-1, 0);
        gc.drawVisibleGoldenCard(currPlayer.getId(), -1);
        gc.drawVisibleGoldenCard(currPlayer.getId(), 5);

        gc.getGameModel().setGameState(GameState.SET_UP);
        gc.drawResourceCard(currPlayer.getId()); // Invalid game state

        gc.getGameModel().setGameState(GameState.PLAYING);
        gc.drawResourceCard(notCurrPlayer.getId()); // Correct
    }

    @Test
    public void testPlaceAndDraw(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(1, 1));
        // Draw card
        gc.drawResourceCard(currPlayerID);

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(1, 1));
        // Draw card
        gc.drawGoldenCard(currPlayerID);

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(1, 1));
        // Draw card
        gc.drawVisibleGoldenCard(currPlayerID, 0);

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(1, 1));
        // Draw card
        gc.drawVisibleResourceCard(currPlayerID, 0);
    }

    @Test
    public void restoreVisibleResourceCardEmptyDeck(){
        setUpGame();

        gc.getGameModel().getResourceCardsDeck().getDeck().clear();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();
        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(-1, 1));
        // Draw card
        gc.drawVisibleResourceCard(currPlayerID, 0);

        gc.getGameModel().getGoldenCardsDeck().getDeck().clear();

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(-1, 1));
        // Draw card
        gc.drawVisibleResourceCard(currPlayerID, 0);
    }

    @Test
    public void restoreVisibleGoldenCardEmptyDeck(){
        setUpGame();

        gc.getGameModel().getGoldenCardsDeck().getDeck().clear();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();
        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(-1, 1));
        // Draw card
        gc.drawVisibleGoldenCard(currPlayerID, 0);

        gc.getGameModel().getResourceCardsDeck().getDeck().clear();

        currPlayer = getCurrentPlayer();
        currPlayerID = getCurrentPlayerId();
        // Place card
        gc.flipCard(currPlayerID, currPlayer.getHand().getFirst().getId());
        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(-1, 1));
        // Draw card
        gc.drawVisibleGoldenCard(currPlayerID, 0);
    }

    @Test
    public void emptyDeckDraw(){
        setUpGame();

        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        gc.placeCard(currPlayerID, currPlayer.getHand().getFirst().getId(), new Position(1, 1));

        gc.getGameModel().getResourceCardsDeck().getDeck().clear();
        gc.getGameModel().getGoldenCardsDeck().getDeck().clear();

        gc.drawResourceCard(currPlayerID); // Empty deck
        gc.drawGoldenCard(currPlayerID); // Empty deck
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
        gc.addPlayer("Player 1");
        gc.addPlayer("Player 2");
        gc.addPlayer("Player 3");
        gc.addPlayer("Player 4");

        assertEquals(4, gc.getIDPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        gc.setUpGame();

        gc.getGameModel().setGameState(GameState.PREGAME);
        gc.drawStarterCard(-1); // Invalid game state
        gc.fillPlayerHand(-1); // Invalid game state

        gc.getGameModel().setGameState(GameState.SET_UP);
        gc.drawStarterCard(-1); // Invalid id
        gc.fillPlayerHand(-1); // Invalid id

        // Foreach player...
        for (Integer i : gc.getIDPlayerMap().keySet()) {
            PlayerModel player = gc.getIDPlayerMap().get(i);

            // Set starter card
            gc.drawStarterCard(i);

            assertNotNull(player.getStarterCard());

            player.flipStarterCard();
            player.flipStarterCard();

            player.placeStarterCard();

            assertTrue(player.getPlayArea().containsKey(new Position(0, 0)));
            assertEquals(player.getPlayArea().get(new Position(0, 0)), player.getStarterCard());

            // Choose token
            player.setToken(new Token(TokenColors.getColorFromInt(i % 4)));

            gc.fillPlayerHand(i);

            assertEquals(3, player.getHand().size());
        }

        // Set common objectives
        gc.setCommonObjectives();

        assertEquals(2, gc.getGameModel().getObjectiveCardsDeck().getCommonObjectives().size());

        gc.setObjectivesToChoose(-1); // Invalid id

        // Set objectives to choose
        for (Integer i : gc.getIDPlayerMap().keySet()){
            gc.setObjectivesToChoose(i);

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            gc.getIDPlayerMap().get(i).setSecretObjective(3); // Wrong index
            gc.getIDPlayerMap().get(i).setSecretObjective(randIndex);

            assertNotNull(gc.getIDPlayerMap().get(i).getSecretObjective());
        }

        // Set first player
        gc.setRandomFirstPlayer();

        gc.calculateOrderOfPlayers();

        int firstPlayers = 0;

        for (Integer i : gc.getIDPlayerMap().keySet()){
            if (gc.getIDPlayerMap().get(i).isFirstPlayer()){
                firstPlayers++;
            }
        }

        assertEquals(1, firstPlayers);

        gc.endGameSetUp();
        // endregion

    }

    public void setUpGame(){
        // region Game setup
        // Add players
        gc.addPlayer("Player 1");
        gc.addPlayer("Player 2");
        gc.addPlayer("Player 3");
        gc.addPlayer("Player 4");

        // Set up decks (initialize, shuffle and set visible cards)
        gc.setUpGame();

        // Foreach player...
        for (Integer i : gc.getIDPlayerMap().keySet()) {
            PlayerModel player = gc.getIDPlayerMap().get(i);

            // Set starter card
            gc.drawStarterCard(i);

            player.placeStarterCard();

            // Choose token
            player.setToken(new Token(TokenColors.getColorFromInt(i % 4)));

            gc.fillPlayerHand(i);
        }

        // Set common objectives
        gc.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : gc.getIDPlayerMap().keySet()){
            gc.setObjectivesToChoose(i);

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            gc.getIDPlayerMap().get(i).setSecretObjective(randIndex);
        }

        // Set first player
        gc.setRandomFirstPlayer();

        gc.calculateOrderOfPlayers();

        gc.endGameSetUp();
        // endregion
    }
}
