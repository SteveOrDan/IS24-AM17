package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
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
    void simulateGame(){
        // region Add players

        PlayerModel player17 = new PlayerModel("Player 17", 17);
        PlayerModel player86 = new PlayerModel("Player 86", 86);

        gc.addPlayer(player17);
        gc.addPlayer(player86);

        assertEquals(2, gc.getIDPlayerMap().size());

        // endregion

        // region Game setup

        // Set up decks (initialize, shuffle and set visible cards)
        gc.setUpGame(10, 30, 40, 60);

        // Foreach player set starter card, token and draw cards
        // region Player 17

        // Set starter card
        gc.drawStarterCard(17, 80);

        player17.placeStarterCard();

        // Choose token
        player17.setToken(new Token(TokenColors.BLUE));

        // Draw 2 resource cards and 1 golden card
        gc.fillPlayerHand(17, 0, 20, 50);

        // endregion

        // region Player 86

        // Set starter card
        gc.drawStarterCard(86, 81);

        player86.placeStarterCard();

        // Choose token
        player86.setToken(new Token(TokenColors.RED));

        // Draw 2 resource cards and 1 golden card
        gc.fillPlayerHand(86, 1, 11, 70);

        // endregion

        // Set common objectives
        gc.setCommonObjectives(94, 95); // Num of Fungi ; Num of Plants

        // Set objectives to choose
        gc.setObjectivesToChoose(17, 86, 90);
        gc.setObjectivesToChoose(86, 88, 101);

        player17.setSecretObjective(0);
        player86.setSecretObjective(1);

        // Set first player
        gc.setFirstPlayer(86);

        gc.calculateOrderOfPlayers();

        gc.endGameSetUp();

        // endregion

        // region Before game simulation

        // =============================== BEFORE GAME START ===============================
        // Visible Resource Cards: 10, 30
        // Visible Golden Cards: 40, 60
        // Player 17 hand: 0, 20, 50
        // Player 86 hand: 1, 11, 70
        // First player: 86
        // Current player: 86

        assertTrue(gc.getGameModel().getResourceCardsDeck().getVisibleCards().contains(gc.getIDPlaceableCardMap().get(10)));
        assertTrue(gc.getGameModel().getResourceCardsDeck().getVisibleCards().contains(gc.getIDPlaceableCardMap().get(30)));
        assertTrue(gc.getGameModel().getGoldenCardsDeck().getVisibleCards().contains(gc.getIDPlaceableCardMap().get(40)));
        assertTrue(gc.getGameModel().getGoldenCardsDeck().getVisibleCards().contains(gc.getIDPlaceableCardMap().get(60)));

        assertTrue(player17.getHand().contains(gc.getIDPlaceableCardMap().get(0)));
        assertTrue(player17.getHand().contains(gc.getIDPlaceableCardMap().get(20)));
        assertTrue(player17.getHand().contains(gc.getIDPlaceableCardMap().get(50)));

        assertTrue(player86.getHand().contains(gc.getIDPlaceableCardMap().get(1)));
        assertTrue(player86.getHand().contains(gc.getIDPlaceableCardMap().get(11)));
        assertTrue(player86.getHand().contains(gc.getIDPlaceableCardMap().get(70)));

        assertEquals(86, gc.getGameModel().getCurrPlayerID());
        assertEquals(86, gc.getGameModel().getFirstPlayerID());

        // endregion

        // region Game simulation

        // =============================== GAME START ===============================

        gc.placeCard(86, 11, new Position(1, 1));

        gc.drawVisibleResourceCard(86, 10);

        gc.setResourceVisibleCard(2);

        // =================== NEXT TURN ===================

        gc.placeCard(17, 0, new Position(-1, 1));

        gc.drawVisibleGoldenCard(17, 40);

        gc.setGoldenVisibleCard(47);

        // =================== NEXT TURN ===================

        gc.placeCard(86, 1, new Position(1, -1));

        gc.drawVisibleGoldenCard(86, 47);

        gc.setGoldenVisibleCard(62);

        // =================== NEXT TURN ===================

        gc.placeCard(17, 40, new Position(1, -1));

        gc.drawVisibleResourceCard(17, 2);

        gc.setResourceVisibleCard(15);

        // =================== CHECK SITUATION ===================

        checkPlayerSituation(86, 0, new int[]{0, 3, 3, 0, 0, 0, 0});

        checkPlayerSituation(17, 1, new int[]{0, 1, 2, 1, 1, 0, 0});

        //=================== NEXT TURN ===================

        gc.placeCard(86, 10, new Position(-1, 1));

        gc.drawResourceCard(86, 19);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 2, new Position(-2, 0));

        gc.drawResourceCard(17, 5);

        //=================== NEXT TURN ===================

        gc.placeCard(86, 47, new Position(0, 2));

        gc.drawResourceCard(86, 34);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 5, new Position(0, 2));

        gc.drawGoldenCard(17, 73);

        // =================== CHECK SITUATION ===================

        checkPlayerSituation(86, 3, new int[]{0, 3, 3, 0, 1, 0, 0});

        checkPlayerSituation(17, 1, new int[]{1, 1, 4, 1, 1, 1, 0});

        //=================== NEXT TURN ===================

        gc.placeCard(86, 34, new Position(2, -2));

        gc.drawResourceCard(86, 14);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 20, new Position(-3, 1));

        gc.drawResourceCard(17, 7);

        //=================== NEXT TURN ===================

        gc.placeCard(86, 14, new Position(1, 3));

        gc.drawGoldenCard(86, 43);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 7, new Position(2, 0));

        gc.drawGoldenCard(17, 49);

        // =================== CHECK SITUATION ===================

        checkPlayerSituation(86, 3, new int[]{1, 4, 3, 2, 3, 0, 0});

        checkPlayerSituation(17, 2, new int[]{3, 1, 5, 1, 1, 1, 0});

        //=================== NEXT TURN ===================

        gc.placeCard(86, 70, new Position(2, 0));

        gc.drawResourceCard(86, 26);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 49, new Position(0, -2));

        gc.drawGoldenCard(17, 53);

        //=================== NEXT TURN ===================

        gc.placeCard(86, 26, new Position(2, 4));

        gc.drawResourceCard(86, 3);

        //=================== NEXT TURN ===================

        gc.flipCard(17, 50);

        gc.placeCard(17, 50, new Position(-3, -1));

        gc.drawResourceCard(17, 13);

        // =================== CHECK SITUATION ===================

        checkPlayerSituation(86, 7, new int[]{2, 4, 3, 1, 5, 0, 0});

        checkPlayerSituation(17, 7, new int[]{3, 2, 4, 1, 1, 1, 0});

        //=================== NEXT TURN ===================

        gc.placeCard(86, 3, new Position(3, 3));

        gc.drawVisibleGoldenCard(86, 62);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 13, new Position(-2, -2));

        gc.drawResourceCard(17, 4);

        //=================== NEXT TURN ===================

        gc.placeCard(86, 43, new Position(2, 2));

        gc.drawResourceCard(86, 12);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 53, new Position(-1, -1));

        gc.drawResourceCard(17, 31);

        // =================== CHECK SITUATION ===================

        checkPlayerSituation(86, 13, new int[]{2, 2, 4, 1, 5, 0, 0});

        checkPlayerSituation(17, 15, new int[]{3, 3, 3, 0, 1, 1, 0});

        //=================== NEXT TURN ===================

        gc.placeCard(86, 62, new Position(1, -3));

        gc.drawGoldenCard(86, 44);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 31, new Position(2, -2));

        gc.drawGoldenCard(17, 45);

        //=================== NEXT TURN ===================

        gc.placeCard(86, 44, new Position(3, -1));

        gc.drawResourceCard(86, 31);

        //=================== NEXT TURN ===================

        gc.placeCard(17, 45, new Position(1, 1));

        gc.drawResourceCard(17, 31);

        // =================== CHECK SITUATION ===================

        checkPlayerSituation(86, 23, new int[]{1, 2, 4, 1, 5, 0, 0});

        checkPlayerSituation(17, 21, new int[]{2, 2, 3, 2, 0, 1, 0});

        // endregion
    }

    public void checkPlayerSituation(int playerID, int score, int[] resources){
        PlayerModel player = gc.getIDPlayerMap().get(playerID);

        assertEquals(score, player.getCurrScore());
        assertEquals(resources[ResourceType.ANIMAL.getValue()], player.getNumOfResourcesArr()[ResourceType.ANIMAL.getValue()]);
        assertEquals(resources[ResourceType.PLANT.getValue()], player.getNumOfResourcesArr()[ResourceType.PLANT.getValue()]);
        assertEquals(resources[ResourceType.FUNGI.getValue()], player.getNumOfResourcesArr()[ResourceType.FUNGI.getValue()]);
        assertEquals(resources[ResourceType.INSECT.getValue()], player.getNumOfResourcesArr()[ResourceType.INSECT.getValue()]);
        assertEquals(resources[ResourceType.QUILL.getValue()], player.getNumOfResourcesArr()[ResourceType.QUILL.getValue()]);
        assertEquals(resources[ResourceType.INKWELL.getValue()], player.getNumOfResourcesArr()[ResourceType.INKWELL.getValue()]);
        assertEquals(resources[ResourceType.MANUSCRIPT.getValue()], player.getNumOfResourcesArr()[ResourceType.MANUSCRIPT.getValue()]);
    }
}
