package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    void getMatches(ClientRMIInterface client) throws RemoteException;

    MatchController createMatch(ClientRMIInterface client, int numberOfPlayers) throws RemoteException;

    MatchController selectMatch(ClientRMIInterface client, int playerID, int matchID) throws RemoteException;

    void chooseNickname(ClientRMIInterface client, String nickname) throws RemoteException;

    void placeCard(ClientRMIInterface client, int id, Position pos) throws RemoteException;

    void drawResourceCard(ClientRMIInterface client, int playerID) throws RemoteException;

    void drawVisibleResourceCard(ClientRMIInterface client, int playerID, int index) throws RemoteException;

    void drawGoldenCard(ClientRMIInterface client, int playerID) throws RemoteException;

    void drawVisibleGoldenCard(ClientRMIInterface client, int playerID, int index) throws RemoteException;
}
