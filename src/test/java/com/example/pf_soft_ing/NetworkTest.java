package com.example.pf_soft_ing;

import com.example.pf_soft_ing.network.client.ClientMain;
import com.example.pf_soft_ing.network.client.ClientSocketSender;
import com.example.pf_soft_ing.network.client.TUIView;
import com.example.pf_soft_ing.network.messages.requests.GetMatchesMsg;
import com.example.pf_soft_ing.network.server.ServerMain;
import org.junit.jupiter.api.Test;

public class NetworkTest {

    @Test
    public void testConnection() {
        ServerMain.startRMIServer(new String[]{"1234"});
        ServerMain.startSocketServer(new String[]{"1235"});

        ClientSocketSender sender;

        try {
            sender = (ClientSocketSender) ClientMain.startSocket("localhost", 1235, new TUIView());

            System.out.println("Connection established");

            sender.sendMessage(new GetMatchesMsg());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
