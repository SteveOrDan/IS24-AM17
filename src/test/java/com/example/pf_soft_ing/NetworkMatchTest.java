package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.InvalidMatchIDException;
import com.example.pf_soft_ing.exceptions.InvalidPlayerIDException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.server.DisconnectionManager;
import com.example.pf_soft_ing.network.server.RMIReceiver;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.rmi.RemoteException;
import java.util.Timer;

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
