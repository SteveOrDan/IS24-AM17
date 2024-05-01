package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.RMIReceiverInterface;
import com.example.pf_soft_ing.ServerConnection.ClientRMIInterface;

import java.rmi.RemoteException;

public class ClientRMISender extends ClientSender {

    private final RMIReceiverInterface server;

    private ClientRMIInterface client;

    public ClientRMISender(RMIReceiverInterface server) {
        this.server = server;
    }

    @Override
    public void startInputReading() {

    }

    @Override
    public void setClient(ClientRMIInterface client) {
        this.client = client;
    }

    @Override
    public void getMatches(int playerID) {
        try {
            server.getMatches(client);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createMatch(int playerID, int numberOfPlayers, String nickname) {

    }

    @Override
    public void selectMatch(int playerID, int matchID) {

    }

    @Override
    public void chooseNickname(int playerID, String nickname) {

    }

    @Override
    public void placeCard(int playerID, int id, int side, int pos) {

    }

    @Override
    public void drawResourceCard(int playerID) {

    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) {

    }

    @Override
    public void drawGoldenCard(int playerID) {

    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) {

    }
}
