package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

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

        for (int i = 1; i <= 2; i++){
            int rand = rng.nextInt(1000);

            while (usedPlayerIDs.contains(rand)){
                rand = rng.nextInt(1000);
            }

            usedPlayerIDs.add(rand);
            gc.addPlayer(new PlayerModel("Player " + i, rand));
        }

        assertEquals(2, gc.getIDPlayerMap().size());

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
        HashMap<Integer, ArrayList<Position>> playerIdToAvailablePositions = new HashMap<>();
        HashMap<Integer, ArrayList<Position>> playerIdToUsedPositions = new HashMap<>();

        for (Integer i : gc.getIDPlayerMap().keySet()){
            ArrayList<Position> newUsedPositions = new ArrayList<>();
            newUsedPositions.add(new Position(0, 0));
            playerIdToUsedPositions.put(i, newUsedPositions);

            ArrayList<Position> newAvailablePositions = calculateNewPossiblePositions(i, new Position(0, 0));
            playerIdToAvailablePositions.put(i, newAvailablePositions);
        }

        // =============================== GAME START ===============================

        while (gc.getGameModel().getGameState() == GameState.PLAYING){
            placeCardAction(playerIdToAvailablePositions, playerIdToUsedPositions);

            drawCardAction();
        }

        // endregion
    }

    private void placeCardAction(HashMap<Integer, ArrayList<Position>> playerIdToAvailablePositions, HashMap<Integer, ArrayList<Position>> playerIdToUsedPositions){
        PlayerModel currPlayer = getCurrentPlayer();
        int currPlayerID = getCurrentPlayerId();

        Random rng = new Random();

        ArrayList<Integer> cardIDsInHand = new ArrayList<>();

        for (PlaceableCard card : currPlayer.getHand()){
            cardIDsInHand.add(card.id);
        }

        int cardIndexInHand = rng.nextInt(cardIDsInHand.size());

        int cardToPlaceID = cardIDsInHand.get(cardIndexInHand);

        int positionIndex = rng.nextInt(playerIdToAvailablePositions.get(currPlayerID).size());

        Position positionToPlace = playerIdToAvailablePositions.get(currPlayerID).get(positionIndex);

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

            positionIndex = rng.nextInt(playerIdToAvailablePositions.get(currPlayerID).size());

            positionToPlace = playerIdToAvailablePositions.get(currPlayerID).get(positionIndex);

            gc.placeCard(currPlayerID, cardToPlaceID, positionToPlace);
        }

        // Remove the position from the available positions
        playerIdToAvailablePositions.get(currPlayerID).remove(positionToPlace);

        // Add the position to the used positions
        playerIdToUsedPositions.get(currPlayerID).add(positionToPlace);

        // All new possible positions based on card corners (need to remove the ones that are already used)
        ArrayList<Position> newPossiblePos = calculateNewPossiblePositions(currPlayerID, positionToPlace);

        newPossiblePos.removeIf(p -> playerIdToUsedPositions.get(currPlayerID).contains(p));

        playerIdToAvailablePositions.get(currPlayerID).addAll(newPossiblePos);
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

    private ArrayList<Position> calculateNewPossiblePositions(int playerID, Position pos){
        PlayerModel player = gc.getIDPlayerMap().get(playerID);

        ArrayList<Position> newAvailablePositions = new ArrayList<>();

        Side currSide = player.getPlayArea().get(pos).getCurrSide();

        if (currSide.getBLCorner().isAvailable()){
            newAvailablePositions.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }
        if (currSide.getBRCorner().isAvailable()){
            newAvailablePositions.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }
        if (currSide.getTLCorner().isAvailable()){
            newAvailablePositions.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }
        if (currSide.getTRCorner().isAvailable()){
            newAvailablePositions.add(new Position(pos.getX() + 1, pos.getY() + 1));
        }

        return newAvailablePositions;
    }

    private PlayerModel getCurrentPlayer(){
        return gc.getIDPlayerMap().get(gc.getGameModel().getCurrPlayerID());
    }

    private int getCurrentPlayerId(){
        return gc.getGameModel().getCurrPlayerID();
    }

//    private void printPlayerInfo(int currPlayerID, PlayerModel currPlayer, HashMap<Integer, ArrayList<Position>> playerIdToAvailablePositions, HashMap<Integer, ArrayList<Position>> playerIdToUsedPositions){
//        System.out.println("\nPlay area of player " + currPlayerID);
//        for (Position p : currPlayer.getPlayArea().keySet()){
//            PlaceableCard card = currPlayer.getPlayArea().get(p);
//            System.out.println("Position: " + p + " => CardID: " + card.getId() + " (" + card.currSide.sideType + ")");
//        }
//
//        System.out.println("\nAvailable positions of player " + currPlayerID);
//        for (Position p : playerIdToAvailablePositions.get(currPlayerID)){
//            System.out.println("Pos: " + p);
//        }
//
//        System.out.println("\nUsed positions of player " + currPlayerID);
//        for (Position p : playerIdToUsedPositions.get(currPlayerID)){
//            System.out.println("Pos: " + p);
//        }
//    }
}
