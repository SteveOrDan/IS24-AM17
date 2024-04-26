package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.RMI.ClientRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    void placeCard(int id, Position pos, ClientRMI client) throws RemoteException;
    void drawResourceCard(ClientRMI client) throws RemoteException;
    void drawVisibleResourceCard(int playerID, int index, ClientRMI client) throws RemoteException;
    void drawGoldenCard(int playerID, ClientRMI client) throws RemoteException;
    void drawVisibleGoldenCard(int playerID, int index, ClientRMI client) throws RemoteException;
    void requestError(ClientRMI client) throws RemoteException;

    // metodi iniziali
    void createGame(ClientRMI client, String nickname, int numberOfPlayers) throws RemoteException;
    MatchController selectMatch(ClientRMI client, int matchID) throws RemoteException;
    void joinMatch(MatchController matchController, String nickname, ClientRMI client) throws NicknameAlreadyExistsException, GameIsFullException, RemoteException;

    //prova
    boolean prova(String name) throws RemoteException;

}
