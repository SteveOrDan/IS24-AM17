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
        new Thread(() -> {
            try {
                serverInterface.getMatches(client);
            } catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void createMatch(int numberOfPlayers, String nickname) {
        new Thread(() -> {
            try {
                serverInterface.createMatch(playerID, numberOfPlayers, nickname);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void selectMatch(int matchID) {
        new Thread(() -> {
            try {
                serverInterface.selectMatch(playerID, matchID);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void chooseNickname(String nickname) {
        new Thread(() -> {
            try {
                serverInterface.chooseNickname(playerID, nickname);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void placeStarterCard(CardSideType side) {
        new Thread(() -> {
            try {
                serverInterface.placeStarterCard(playerID, side);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void chooseSecretObjective(int cardID) {
        new Thread(() -> {
            try {
                serverInterface.chooseSecretObj(playerID, cardID);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void placeCard(int cardID, CardSideType side, Position pos) {
        new Thread(() -> {
            try {
                serverInterface.placeCard(playerID, cardID, side, pos);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void drawResourceCard(int playerID) {
        new Thread(() -> {
            try {
                serverInterface.drawResourceCard(playerID);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) {
        new Thread(() -> {
            try {
                serverInterface.drawVisibleResourceCard(playerID, index);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void drawGoldenCard(int playerID) {
        new Thread(() -> {
            try {
                serverInterface.drawGoldenCard(playerID);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) {
        new Thread(() -> {
            try {
                serverInterface.drawVisibleGoldenCard(playerID, index);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void sendChatMessage(String recipient, String message) {
        new Thread(() -> {
            try {
                serverInterface.sendChatMessage(recipient, message);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }
}
