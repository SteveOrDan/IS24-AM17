package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private final GameController gameController;
    private final Map<Integer, PlayerModel> IDToPlayer = new HashMap<>();
    private final Map<Integer, MatchController> playerIDToMatch = new HashMap<>();
    private final Map<Integer, DisconnectionManager> playerIDToDiscMan = new HashMap<>();

    public RMIReceiver(GameController gameController) throws RemoteException {
        this.gameController = gameController;
    }

    @Override
    public void connect(ClientRMIInterface client) throws RemoteException {
        // Create player and add to IDToPlayerMap
        PlayerModel playerModel = gameController.createPlayer(new RMISender(client));
        IDToPlayer.put(playerModel.getID(), playerModel);

        // Send matches to client
        getMatches(playerModel.getID());
    }

    @Override
    public void getMatches(int playerID) throws RemoteException {
        gameController.getMatches(playerID);
    }

    @Override
    public void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException{
        gameController.createMatch(playerID, numberOfPlayers, nickname);
        try {
            playerIDToMatch.put(playerID, gameController.getMatchByID(gameController.getMatchIDWithPlayer(playerID)));
            DisconnectionManager discMan = new DisconnectionManager(playerIDToMatch.get(playerID), playerIDToMatch.get(playerID).getPlayerSender(playerID), playerID);
            playerIDToDiscMan.put(playerID, discMan);
            discMan.startPing();
        }
        catch (InvalidMatchIDException e) {
            System.out.println("Already notified client, failed match selection");
        }
    }

    @Override
    public void selectMatch(int playerID, int matchID) throws RemoteException {
        gameController.selectMatch(playerID, matchID);
        try {
            playerIDToMatch.put(playerID, gameController.getMatchByID(gameController.getMatchIDWithPlayer(playerID)));
            DisconnectionManager discMan = new DisconnectionManager(playerIDToMatch.get(playerID), playerIDToMatch.get(playerID).getPlayerSender(playerID), playerID);
            playerIDToDiscMan.put(playerID, discMan);
            discMan.startPing();
        }
        catch (InvalidMatchIDException e) {
            System.out.println("Already notified client, failed match selection");
        }
    }

    @Override
    public void chooseNickname(int playerID, String nickname) throws RemoteException {
        gameController.chooseNickname(playerID, nickname, playerIDToMatch.get(playerID));
    }

    @Override
    public void placeStarterCard(int playerID, CardSideType side) throws RemoteException {
        playerIDToMatch.get(playerID).placeStarterCardForPlayer(playerID, side);
    }

    @Override
    public void chooseSecretObj(int playerID, int cardID) throws RemoteException {
        playerIDToMatch.get(playerID).setSecretObjectiveForPlayer(playerID, cardID);
    }

    @Override
    public void placeCard(int playerID, int cardID, CardSideType side, Position pos) throws RemoteException {
        playerIDToMatch.get(playerID).placeCard(playerID, cardID, pos, side);
    }

    @Override
    public void drawResourceCard(int playerID) throws RemoteException {
        playerIDToMatch.get(playerID).drawResourceCard(playerID);
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {
        playerIDToMatch.get(playerID).drawVisibleResourceCard(playerID, index);
    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {
        playerIDToMatch.get(playerID).drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {
        playerIDToMatch.get(playerID).drawVisibleGoldenCard(playerID, index);
    }

    @Override
    public void sendChatMessage(int playerID, String recipientNickname, String message) throws RemoteException {
        playerIDToMatch.get(playerID).chatMessage(playerID, recipientNickname, message);
    }

    @Override
    public void sendPong(int playerID) throws RemoteException {
        playerIDToDiscMan.get(playerID).resetPacketLoss();
    }
}