package com.example.pf_soft_ing;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

        for (int i = 1; i <= 6; i++){
            int rand = (int)(Math.random() * 1000);

            while (usedPlayerIDs.contains(rand)){
                rand = (int)(Math.random() * 1000);
            }

            usedPlayerIDs.add(rand);
            gc.addPlayer(new PlayerModel("Player " + i, rand));
        }

        assertEquals(4, gc.getIDPlayerMap().size());

        // Set up decks (initialize, shuffle and set visible cards)
        gc.initializeDecks();

        gc.shuffleAllDecks();

        gc.setVisibleCards();

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

            gc.drawResourceCard(i);
            gc.drawResourceCard(i);
            gc.drawGoldenCard(i);
        }


        // Set common objectives
        gc.setCommonObjectives();

        // Set objectives to choose
        for (Integer i : gc.getIDPlayerMap().keySet()){
            gc.setObjectiveToChoose(i);

            int randIndex = Math.random() < 0.5 ? 0 : 1;

            gc.getIDPlayerMap().get(i).setSecretObjective(randIndex);
        }

        // Set first player
        gc.setRandomFirstPlayer();

        gc.setOrderOfPlayers();

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
    void simulateGame(){
        // region Add players

        PlayerModel player17 = new PlayerModel("Player 17", 17);
        PlayerModel player86 = new PlayerModel("Player 86", 86);

        gc.addPlayer(player17);
        gc.addPlayer(player86);

        assertEquals(2, gc.getIDPlayerMap().size());

        // endregion

        // region Game setup

        // Decks set up
        gc.initializeDecks();
        // Don't shuffle decks for testing purposes
        gc.setVisibleCards(10, 30, 40, 60);

        // Foreach player set starter card, token and draw cards
        // region Player 17

        // Set starter card
        gc.drawStarterCard(17, 80);

        player17.placeStarterCard();

        // Choose token
        player17.setToken(new Token(TokenColors.BLUE));

        // Draw 2 resource cards and 1 golden card
        gc.drawResourceCard(17, 0);
        gc.drawResourceCard(17, 20);
        gc.drawGoldenCard(17, 50);
        // endregion

        // region Player 86

        // Set starter card
        gc.drawStarterCard(86, 81);

        player86.placeStarterCard();

        // Choose token
        player86.setToken(new Token(TokenColors.RED));

        // Draw 2 resource cards and 1 golden card
        gc.drawResourceCard(86, 1);
        gc.drawResourceCard(86, 11);
        gc.drawGoldenCard(86, 70);

        // endregion

        // Set common objectives
        gc.setCommonObjective(94); // Num of Fungi
        gc.setCommonObjective(95); // Num of Plants

        // Set objectives to choose
        gc.setObjectivesToChoose(17, 86, 90);
        gc.setObjectivesToChoose(86, 88, 101);

        player17.setSecretObjective(0);
        player86.setSecretObjective(1);

        // Set first player
        gc.setFirstPlayer(86);

        gc.setOrderOfPlayers();

        // endregion

        // region Game simulation

        // Player places card
        gc.placeCard(86, 11, new Position(1, 1));

        // Player draws card
        gc.drawVisibleResourceCard(86, 10);

        gc.setResourceVisibleCard(2);

        gc.endTurn();

        assertEquals(3, player86.getNumOfResourcesArr()[ResourceType.PLANT.getValue()]);
        assertEquals(1, player86.getNumOfResourcesArr()[ResourceType.FUNGI.getValue()]);
        assertEquals(1, player86.getNumOfResourcesArr()[ResourceType.INSECT.getValue()]);
        assertEquals(0, player86.getNumOfResourcesArr()[ResourceType.ANIMAL.getValue()]);

        assertEquals(1, player17.getNumOfResourcesArr()[ResourceType.PLANT.getValue()]);
        assertEquals(1, player17.getNumOfResourcesArr()[ResourceType.FUNGI.getValue()]);
        assertEquals(1, player17.getNumOfResourcesArr()[ResourceType.INSECT.getValue()]);
        assertEquals(1, player17.getNumOfResourcesArr()[ResourceType.ANIMAL.getValue()]);

        // endregion
    }
}