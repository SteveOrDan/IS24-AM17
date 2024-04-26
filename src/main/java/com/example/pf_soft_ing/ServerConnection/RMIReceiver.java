package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.RMI.ClientRMI;
import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private static HashMap<ClientRMIInterface, Decoder> clientRMIDecoderHashMap;
    public static GameController gameController;

    public RMIReceiver() throws RemoteException {
        clientRMIDecoderHashMap = new HashMap<>();
        gameController = new GameController();
    }

    public static void addDecoder(ClientRMIInterface client, Decoder decoder){
        clientRMIDecoderHashMap.put(client,decoder);
    }


    @Override
    public void placeCard(int id, Position pos, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).placeCard(id, pos);
    }

    @Override
    public void drawResourceCard(ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawResourceCard();
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawVisibleResourceCard(playerID,index);
    }

    @Override
    public void drawGoldenCard(int playerID, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawVisibleGoldenCard(playerID, index);
    }

    @Override
    public void requestError(ClientRMIInterface client) throws RemoteException {
        //clientRMIDecoderHashMap.get(client).requestError();
        System.out.println("Method request error called");
        client.sendID(4);
    }

    @Override
    public void createGame(ClientRMIInterface client, String nickname, int numberOfPlayers) throws RemoteException {
        //gameController.createGame(nickname, numberOfPlayers);
    }

    @Override
    public MatchController selectMatch(ClientRMIInterface client, int matchID) throws RemoteException {
        return gameController.selectMatch(matchID);
    }

    @Override
    public void joinMatch(MatchController matchController, String nickname, ClientRMIInterface client) throws NicknameAlreadyExistsException, GameIsFullException, RemoteException {
        ///gameController.joinMatch(matchController, nickname, client);
    }

    @Override
    public boolean prova(String name) throws RemoteException {
        int id = 1;
        System.out.println(name + "called prova");
        return true;
    }

}