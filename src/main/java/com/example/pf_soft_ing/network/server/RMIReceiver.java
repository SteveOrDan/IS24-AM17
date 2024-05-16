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

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private final GameController gameController;
    private static final HashMap<Integer, PlayerModel> IDToPlayer = new HashMap<>();
    private static final HashMap<Integer, MatchController> playerIDToMatch = new HashMap<>();

    public RMIReceiver(GameController gameController) throws RemoteException {
        this.gameController = gameController;
    }

    @Override
    public void getMatches(ClientRMIInterface client) throws RemoteException {
        PlayerModel playerModel = gameController.createPlayer(new RMISender(client));
        IDToPlayer.put(playerModel.getID(), playerModel);

        gameController.getMatches(playerModel.getID());
    }

    @Override
    public void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException{
        gameController.createMatch(playerID, numberOfPlayers, nickname);
        try {
            playerIDToMatch.put(playerID, gameController.getMatchByID(gameController.getMatchIDWithPlayer(playerID)));
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
    public void sendChatMessage(String recipientNickname, String message) throws RemoteException {
        // TODO: Either change String or find player ID
        // playerIDToMatch.get(recipientID).privateMessage(recipientID, message, playerID);
    }
}