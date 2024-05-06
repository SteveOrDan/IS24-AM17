package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    int getMatches(ClientRMIInterface client) throws RemoteException;

    void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException;

    void selectMatch(int playerID, int matchID) throws RemoteException;

    void chooseNickname(int playerID, String nickname) throws RemoteException;

    void placeCard(int playerID, int cardID, Position pos) throws RemoteException;

    void drawResourceCard(int playerID) throws RemoteException;

    void drawVisibleResourceCard(int playerID, int index) throws RemoteException;

    void drawGoldenCard(int playerID) throws RemoteException;

    void drawVisibleGoldenCard(int playerID, int index) throws RemoteException;
}
