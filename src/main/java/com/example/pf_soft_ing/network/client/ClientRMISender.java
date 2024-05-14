package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.server.RMIReceiverInterface;

import java.rmi.RemoteException;

public class ClientRMISender implements ClientSender {

    private final RMIReceiverInterface serverInterface;
    private final ClientRMIInterface client;
    private int playerID;

    public ClientRMISender(RMIReceiverInterface serverInterface, ClientRMIInterface client) {
        this.serverInterface = serverInterface;
        this.client = client;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public void getMatches() {
        try {
            serverInterface.getMatches(client);
        }
        catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
    }

    @Override
    public void createMatch(int numberOfPlayers, String nickname) {
        try {
            serverInterface.createMatch(playerID, numberOfPlayers, nickname);
        }
        catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
    }

    @Override
    public void selectMatch(int matchID) {
        try {
            serverInterface.selectMatch(playerID, matchID);
        }
        catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
    }

    @Override
    public void chooseNickname(String nickname) {
        try {
            serverInterface.chooseNickname(playerID, nickname);
        }
        catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
    }

    @Override
    public void placeStarterCard(CardSideType side) {
        try {
            serverInterface.placeStarterCard(playerID, side);
        }
        catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
    }

    @Override
    public void chooseSecretObjective(int cardID) {
        try {
            serverInterface.chooseSecretObj(playerID, cardID);
        }
        catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
    }

    @Override
    public void placeCard(int cardID, CardSideType side, Position pos) {

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

    @Override
    public void sendMatchMessage(String message) {

    }

    @Override
    public void sendPrivateMessage(int recipientID, String message) {

    }
}
