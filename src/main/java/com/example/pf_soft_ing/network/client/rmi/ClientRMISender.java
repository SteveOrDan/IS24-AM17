package com.example.pf_soft_ing.network.client.rmi;

import com.example.pf_soft_ing.network.client.ClientSender;
import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.server.rmi.RMIReceiverInterface;

import java.rmi.RemoteException;

public class ClientRMISender implements ClientSender {

    private final RMIReceiverInterface serverInterface;
    private final ClientRMIInterface client;
    private int playerID;

    public ClientRMISender(RMIReceiverInterface serverInterface, ClientRMIInterface client) {
        this.serverInterface = serverInterface;
        this.client = client;
    }

    /**
     * Setter
     * @param playerID ID of the player
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public void connect() {
        new Thread(() -> {
            try {
                serverInterface.connect(client);
            }
            catch (RemoteException e) {
                System.out.println("Client: Connection to server lost");
            }
        }).start();
    }

    @Override
    public void getMatches() {
        new Thread(() -> {
            try {
                serverInterface.getMatches(playerID);
            }
            catch (RemoteException e) {
                System.out.println("Client: Connection to server lost");
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
    public void reconnectToMatch(int matchID, String nickname) {
        new Thread(() -> {
            try {
                serverInterface.reconnectToMatch(playerID, nickname, matchID);
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
    public void placeStarterCard(int playerID, CardSideType side) {
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
    public void chooseSecretObjective(int playerID, int cardID) {
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
    public void placeCard(int playerID, int cardID, CardSideType side, Position pos) {
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
    public void sendChatMessage(int playerID, String recipient, String message) {
        new Thread(() -> {
            try {
                serverInterface.sendChatMessage(playerID, recipient, message);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }

    @Override
    public void sendPong(int playerID) {
        new Thread(() -> {
            try {
                serverInterface.sendPong(playerID);
            }
            catch (RemoteException e) {
                System.out.println("Connection to server lost");
            }
        }).start();
    }
}
