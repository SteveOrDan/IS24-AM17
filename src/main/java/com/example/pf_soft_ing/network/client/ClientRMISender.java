package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.network.server.RMIReceiverInterface;

import java.rmi.RemoteException;

public class ClientRMISender extends ClientSender {

    private final RMIReceiverInterface serverInterface;

    private ClientRMIInterface client;

    public ClientRMISender(RMIReceiverInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    @Override
    public void startInputReading() {

    }

    @Override
    public void setClient(ClientRMIInterface client) {
        this.client = client;
    }

    @Override
    public void getMatches() {
        try {
            serverInterface.getMatches(client);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createMatch(int numberOfPlayers, String nickname) {

    }

    @Override
    public void selectMatch(int matchID) {

    }

    @Override
    public void chooseNickname(String nickname) {

    }

    @Override
    public void placeStarterCard(int cardID, String side) {

    }

    @Override
    public void chooseSecretObjective(int cardID) {

    }

    @Override
    public void placeCard(int id, int side, int pos) {

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
