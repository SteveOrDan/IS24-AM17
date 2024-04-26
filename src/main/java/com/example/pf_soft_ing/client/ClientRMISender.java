package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.RMIReceiverInterface;
import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;

import java.rmi.RemoteException;

public class ClientRMISender extends ClientEncoder{

    private final RMIReceiverInterface server;

    private ClientRMIInterface client;

    public ClientRMISender(RMIReceiverInterface server) {
        this.server = server;
    }

    @Override
    public void setClient(ClientRMIInterface client) {
        this.client = client;
    }

    @Override
    public void getMatches() {
        try {
            server.getMatches(client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMatch(int matchID) {
        try {
            server.sendMatch(matchID, client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendNickname(String nickname) {
        try {
            server.sendNickname(nickname, client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createMatch(int numberOfPlayers) {
        try {
            server.createMatch(numberOfPlayers, client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
