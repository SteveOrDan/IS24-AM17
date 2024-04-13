package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    static int numOfPlayers = 2;
    GameController gc = new GameController();

    @Test
    void checkResources(){
        gc.initializeDecks();

        assertEquals(86, gc.getIDPlaceableCardMap().size());
    }

    @Test
    void addPlayer() {
        PlayerModel player1 = new PlayerModel("Player 1", 1);
        PlayerModel player1copy = new PlayerModel("Player 1", 1);
        PlayerModel player1SameID = new PlayerModel("Player 1 same ID", 1);
        PlayerModel player1SameNick = new PlayerModel("Player 1", 10);
        PlayerModel player2 = new PlayerModel("Player 2", 2);
        PlayerModel player3 = new PlayerModel("Player 3", 3);
        PlayerModel player4 = new PlayerModel("Player 4", 4);
        PlayerModel player5 = new PlayerModel("Player 5", 5);

        gc.addPlayer(player1);
        gc.addPlayer(player1copy);
        gc.addPlayer(player1SameID);
        gc.addPlayer(player1SameNick);
        gc.addPlayer(player2);
        gc.addPlayer(player3);
        gc.addPlayer(player4);
        gc.addPlayer(player5);

        assertEquals(4, gc.getIDPlayerMap().size());
        assertFalse(gc.getIDPlayerMap().containsValue(player1copy));
        assertFalse(gc.getIDPlayerMap().containsValue(player1SameID));
        assertFalse(gc.getIDPlayerMap().containsValue(player1SameNick));
        assertFalse(gc.getIDPlayerMap().containsValue(player5));
    }

    @Test
    void randomGameSetUp(){
        // Add players
        List<Integer> usedPlayerIDs = new ArrayList<>();
        Random rng = new Random();

        for (int i = 1; i <= 6; i++){
            int rand = rng.nextInt(1000);

            while (usedPlayerIDs.contains(rand)){
                rand = rng.nextInt(1000);
            }

            usedPlayerIDs.add(rand);
            gc.addPlayer(new PlayerModel("Player " + i, rand));
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
            gc.addPlayer(new PlayerModel("Player " + i, rand));
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
}
