package com.example.pf_soft_ing;

import com.example.pf_soft_ing.exceptions.InvalidMatchIDException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.network.client.ClientMain;
import com.example.pf_soft_ing.network.client.ClientSender;
import com.example.pf_soft_ing.network.client.TUIView;
import com.example.pf_soft_ing.network.client.View;
import com.example.pf_soft_ing.network.server.ServerMain;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkMatchTest {

    private ClientSender startRMI() throws NotBoundException, RemoteException {
        View view = new TUIView();

        return ClientMain.startRMI("127.0.0.1", Integer.parseInt("1234"), view);
    }

    private ClientSender startSocket() throws IOException {
        View view = new TUIView();

        return ClientMain.startSocket("127.0.0.1", Integer.parseInt("1235"), view);
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            fail("Error while sleeping");
        }
    }

    @Test
    void simulateMatch(){
        // Start server
//        ServerMain.main(new String[]{"1234"});
//
//        GameController gc = ServerMain.getGameController();
//
//        assertNotNull(gc);
//
//        new Thread(() -> {
//            ClientSender sender1 = null;
//            ClientSender sender2 = null;
//
//            try {
//                sender1 = startRMI();
//                sender2 = startSocket();
//            }
//            catch (NotBoundException | IOException e) {
//                fail("Error while starting the clients");
//            }
//
//            sender1.createMatch(2, "Steve");
//
//            sleep(1000);
//
//            int matchID = gc.getGameModel().getMatches().keySet().iterator().next();
//
//            sender2.selectMatch(matchID);
//
//            sender2.chooseNickname("Abdo");
//
//            try {
//                assertTrue(gc.getGameModel().getMatchByID(matchID).getNicknames().contains("Abdo"));
//                assertTrue(gc.getGameModel().getMatchByID(matchID).getNicknames().contains("Steve"));
//                assertTrue(gc.getGameModel().getMatchByID(matchID).getNicknames().contains("FCvgubh"));
//            }
//            catch (InvalidMatchIDException e) {
//                fail("Error while getting the match by ID");
//            }
//        }).start();
    }
}
