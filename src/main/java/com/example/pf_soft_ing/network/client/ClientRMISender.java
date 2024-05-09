package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.server.RMIReceiverInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

public class ClientRMISender implements ClientSender {

    private final RMIReceiverInterface serverInterface;
    private final ClientRMIInterface client;
    private int playerID;
    private BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public ClientRMISender(RMIReceiverInterface serverInterface, ClientRMIInterface client) {
        this.serverInterface = serverInterface;
        this.client = client;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public void startInputReading() {
        new Thread(() -> {
            while (true) {
                try {
                    String userInput = stdIn.readLine();

                    if (userInput != null) {
                        interpretInput(userInput);
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void interpretInput(String userInput) {
        String[] parts = userInput.split(" ");
        String command = parts[0];

        try {
            switch (command) {
                case "CreateMatch":
                    if (parts.length != 3) {
                        client.sendError("Error: CreateMatch takes exactly 2 arguments (num of players, nickname)");
                    }
                    else {
                        serverInterface.createMatch(playerID, Integer.parseInt(parts[1]), parts[2]);
                    }
                    break;

                case "SelectMatch":
                    if (parts.length != 2) {
                        client.sendError("Error: SelectMatch takes exactly 1 argument (match ID)");
                    }
                    else {
                        serverInterface.selectMatch(playerID, Integer.parseInt(parts[1]));
                    }
                    break;

                case "ChooseNickname":
                    if (parts.length != 2) {
                        client.sendError("Error: ChooseNickname takes exactly 1 argument (nickname)");
                    }
                    else {
                        serverInterface.chooseNickname(playerID, parts[1]);
                    }
                    break;

                case "exit", "quit":
                    System.exit(0);

                default:
                    client.sendError("Error: " + command + " is not a valid command. Please try again.");
            }
        }
        catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
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
        } catch (RemoteException e) {
            System.out.println("Connection to server lost");
        }
    }

    @Override
    public void chooseSecretObjective(int cardID) {
        try {
            serverInterface.chooseSecretObj(playerID, cardID);
        } catch (RemoteException e) {
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
}
