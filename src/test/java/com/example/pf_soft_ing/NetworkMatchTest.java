package com.example.pf_soft_ing;

import com.example.pf_soft_ing.mvc.controller.GameController;
import com.example.pf_soft_ing.mvc.controller.MatchController;
import com.example.pf_soft_ing.mvc.model.player.PlayerModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkMatchTest {

    @Test
    void simulateMatch() {
        GameController gameController = new GameController();

        PlayerModel player = gameController.createPlayer(new TestSender());

        MatchController mc = gameController.createMatch(player.getID(), 5, "NickHasName");
        assertNull(mc);

        mc = gameController.createMatch(player.getID(), 3, "NickHasName");
        assertNotNull(mc);
    }
}
