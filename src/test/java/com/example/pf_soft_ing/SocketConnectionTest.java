package com.example.pf_soft_ing;

import com.example.pf_soft_ing.network.client.ClientMain;
import com.example.pf_soft_ing.network.client.ClientSender;
import com.example.pf_soft_ing.network.client.TUIView;
import com.example.pf_soft_ing.network.client.View;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class SocketConnectionTest {

    /**
     * To use this test start a server on port 1234
     */

    private ClientSender StartConnection() {
        View view = new TUIView();
        ClientSender sender = null;
        try {
            sender = ClientMain.startSocket("127.0.0.1", Integer.parseInt("1235"), view);
        } catch (IOException e) {
            fail();
        }

        sender.getMatches();
        return sender;
    }

    @Test
    void CreateMatchError1(){
        ClientSender sender = StartConnection();
        sender.createMatch(1, "Simo");
    }

    @Test
    void CreateMatchError2(){
        ClientSender sender = StartConnection();
        sender.createMatch(5, "Simo");
    }

    @Test
    void CreateMatch1(){
        ClientSender sender = StartConnection();
        sender.createMatch(2, "Simo");
    }

    @Test
    void CreateMatch2(){
        ClientSender sender = StartConnection();
        sender.createMatch(3, "Simo");
    }

    @Test
    void CreateMatch3(){
        ClientSender sender = StartConnection();
        sender.createMatch(4, "Simo");
    }
}
