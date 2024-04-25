package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.RMI.ClientRMI;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;



public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private static HashMap<ClientRMI, Decoder> clientRMIDecoderHashMap;
    public static GameController gameController;

    public RMIReceiver() throws RemoteException {
        clientRMIDecoderHashMap = new HashMap<>();
        gameController = new GameController();
    }

    public static void addDecoder(ClientRMI client, Decoder decoder){
        clientRMIDecoderHashMap.put(client,decoder);
    }
    @Override
    public void placeCard(int id, Position pos, ClientRMI client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).placeCard(id, pos);
    }

    @Override
    public void drawResourceCard(ClientRMI client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawResourceCard();
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index, ClientRMI client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawVisibleResourceCard(playerID,index);
    }

    @Override
    public void drawGoldenCard(int playerID, ClientRMI client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index, ClientRMI client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawVisibleGoldenCard(playerID, index);
    }

    @Override
    public void requestError(ClientRMI client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).requestError();
    }

    @Override
    public void createGame(ClientRMI client, String nickname, int numberOfPlayers) throws RemoteException {
        gameController.createGame(nickname, numberOfPlayers);
    }

    @Override
    public MatchController selectMatch(ClientRMI client, int matchID) throws RemoteException {
        return gameController.selectMatch(matchID);
    }

    @Override
    public void joinMatch(MatchController matchController, String nickname, ClientRMI client) throws NicknameAlreadyExistsException, GameIsFullException, RemoteException {
        gameController.joinMatch(matchController, nickname, client);
    }

    @Override
    public boolean prova(ClientRMI client, String name) throws RemoteException {
        System.out.println(name + "called prova");
        int id = 1;
        client.sendID(id);
        return true;
    }

}