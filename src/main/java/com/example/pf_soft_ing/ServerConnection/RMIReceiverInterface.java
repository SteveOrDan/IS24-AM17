package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.RMI.ClientRMI;
import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    void getMatches(ClientRMIInterface client) throws RemoteException;

    void sendMatch(int matchID, ClientRMIInterface client) throws RemoteException;

    void sendNickname(String nickname, ClientRMIInterface client) throws RemoteException;

    void createMatch(int numberOfPlayers, ClientRMIInterface client) throws RemoteException;

    void placeCard(int id, Position pos, ClientRMIInterface client) throws RemoteException;
    void drawResourceCard(ClientRMIInterface client) throws RemoteException;
    void drawVisibleResourceCard(int playerID, int index, ClientRMIInterface client) throws RemoteException;
    void drawGoldenCard(int playerID, ClientRMIInterface client) throws RemoteException;
    void drawVisibleGoldenCard(int playerID, int index, ClientRMIInterface client) throws RemoteException;
    void requestError(ClientRMIInterface client) throws RemoteException;

    // metodi iniziali
    void createGame(ClientRMIInterface client, String nickname, int numberOfPlayers) throws RemoteException;
    MatchController selectMatch(ClientRMIInterface client, int matchID) throws RemoteException;
    void joinMatch(MatchController matchController, String nickname, ClientRMIInterface client) throws NicknameAlreadyExistsException, GameIsFullException, RemoteException;

    //prova
    boolean prova(String name) throws RemoteException;

}
