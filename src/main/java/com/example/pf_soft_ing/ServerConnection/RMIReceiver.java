package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private final GameController gameController;
    private static final HashMap<ClientRMIInterface, MatchController> matchControllerMap = new HashMap<>();
    private static final HashMap<Integer, ClientRMIInterface> playerIDToClient = new HashMap<>();

    public RMIReceiver(GameController gameController) throws RemoteException {
        this.gameController = gameController;
    }

    private static void addMatchController(ClientRMIInterface client, MatchController matchController){
        matchControllerMap.put(client, matchController);
    }

    private static void addPlayerID(ClientRMIInterface client, int playerID){
        playerIDToClient.put(playerID, client);
    }

    private MatchController getMatchController(ClientRMIInterface client){
        return matchControllerMap.get(client);
    }

    @Override
    public void getMatches(ClientRMIInterface client) throws RemoteException {

    }

    @Override
    public MatchController createMatch(ClientRMIInterface client, int numberOfPlayers) throws RemoteException {
        return null;
    }

    @Override
    public MatchController selectMatch(ClientRMIInterface client, int playerID, int matchID) throws RemoteException {
        return null;
    }

    @Override
    public void chooseNickname(ClientRMIInterface client, String nickname) throws RemoteException {

    }

    @Override
    public void placeCard(ClientRMIInterface client, int id, Position pos) throws RemoteException {

    }

    @Override
    public void drawResourceCard(ClientRMIInterface client, int playerID) throws RemoteException {

    }

    @Override
    public void drawVisibleResourceCard(ClientRMIInterface client, int playerID, int index) throws RemoteException {

    }

    @Override
    public void drawGoldenCard(ClientRMIInterface client, int playerID) throws RemoteException {

    }

    @Override
    public void drawVisibleGoldenCard(ClientRMIInterface client, int playerID, int index) throws RemoteException {

    }
}