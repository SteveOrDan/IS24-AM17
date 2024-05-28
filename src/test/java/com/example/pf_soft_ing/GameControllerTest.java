package com.example.pf_soft_ing;

import com.example.pf_soft_ing.exceptions.InvalidMatchIDException;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.PlayerModel;
import org.junit.jupiter.api.Test;
import com.example.pf_soft_ing.game.GameController;
import static org.junit.jupiter.api.Assertions.*;


class GameControllerTest {
    private final GameController gameController = new GameController();
    private final static int numOfPlayers = 4;
    private final PlayerModel player1 = gameController.createPlayer(new TestSender());
    private final PlayerModel player2 = gameController.createPlayer(new TestSender());


    @Test
    void getMatchByID() {

        gameController.getMatches(player1.getID());

        MatchController matchController = gameController.createMatch(player1.getID(), numOfPlayers, "player1");
        int matchID = matchController.getMatchID();

        assertDoesNotThrow(() -> gameController.getMatchByID(matchID));

        try {
            assertEquals(matchController, gameController.getMatchByID(matchID));
        } catch (InvalidMatchIDException e) {
            throw new RuntimeException(e);
        }

        int invalidMatchID = matchID + 1;
        assertThrows(InvalidMatchIDException.class, () -> gameController.getMatchByID(invalidMatchID));

    }

    @Test
    void getMatches() {
        int playerID = player1.getID() + player2.getID() + 1 ;
        assertThrows(NullPointerException.class, () -> gameController.getMatches(playerID));
        int player2ID = player2.getID();
        assertDoesNotThrow(() -> gameController.getMatches(player2ID));
    }

    @Test
    void createMatch() {

        gameController.getMatches(player1.getID());
        assertNull(gameController.createMatch(player1.getID(), 0, "player1"));

        gameController.getMatches(player1.getID());
        assertNotNull(gameController.createMatch(player1.getID(), numOfPlayers, "player1"));

        assertNull(gameController.createMatch(player1.getID(), numOfPlayers, "player1"));
    }

    @Test
    void selectMatch() {
        gameController.getMatches(player1.getID());
        gameController.getMatches(player2.getID());

        MatchController matchController1 = gameController.createMatch(player1.getID(), numOfPlayers, "player1");

        int matchID = matchController1.getMatchID();

        assertNull(gameController.selectMatch(player2.getID(), matchID + 1));
        assertNotNull(gameController.selectMatch(player2.getID(), matchID));

    }

    @Test
    void chooseNickname() {
        gameController.getMatches(player1.getID());
        gameController.getMatches(player2.getID());

        MatchController matchController1 = gameController.createMatch(player1.getID(), numOfPlayers, "player1");
        int matchID = matchController1.getMatchID();

        MatchController matchController2 = gameController.selectMatch(player2.getID(), matchID);

        gameController.chooseNickname(player2.getID(), "player1", matchController2);

        assertEquals("", player2.getNickname());

        gameController.chooseNickname(player2.getID(), "all", matchController2);
        assertEquals("", player2.getNickname());

        gameController.chooseNickname(player2.getID(), "(you)", matchController2);
        assertEquals("", player2.getNickname());

        gameController.chooseNickname(player2.getID(), "player2", matchController2);
        assertEquals("player2", player2.getNickname());

        PlayerModel player3 = gameController.createPlayer(new TestSender());
        PlayerModel playe4 = gameController.createPlayer(new TestSender());

        gameController.getMatches(player3.getID());
        gameController.getMatches(playe4.getID());

        MatchController matchController3 = gameController.selectMatch(player3.getID(), matchID);
        MatchController matchController4 = gameController.selectMatch(playe4.getID(), matchID);
        gameController.chooseNickname(player3.getID(), "player3", matchController3);
        gameController.chooseNickname(playe4.getID(), "player4", matchController4);

        assertEquals(true, matchController1.checkForGameStart());
        assertEquals(true, matchController2.checkForGameStart());
        assertEquals(true, matchController3.checkForGameStart());
        assertEquals(true, matchController4.checkForGameStart());


    }

    @Test
    void createPlayer() {
        assertNotNull(gameController.createPlayer(new TestSender()));
    }

    @Test
    void getMatchIDWithPlayer() {
        gameController.getMatches(player1.getID());

        MatchController matchController1 = gameController.createMatch(player1.getID(), numOfPlayers, "player1");
        int matchID = matchController1.getMatchID();

        assertEquals(matchID, gameController.getMatchIDWithPlayer(player1.getID()));

        int invalidID = player1.getID() + 1;

        assertThrows(NullPointerException.class, () -> gameController.getMatchIDWithPlayer(invalidID));

    }

    @Test
    void getPlayerSender() {

        int invalidID = player1.getID() + 1;
        assertThrows(NullPointerException.class, () -> gameController.getPlayerSender(invalidID));

        gameController.getMatches(player1.getID());
        assertNotNull(gameController.getPlayerSender(player1.getID()));
    }
}