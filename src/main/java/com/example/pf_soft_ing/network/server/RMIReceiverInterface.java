package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    void getMatches(ClientRMIInterface client) throws RemoteException;

    void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException;

    void selectMatch(int playerID, int matchID) throws RemoteException;

    void chooseNickname(int playerID, String nickname) throws RemoteException;

    void placeStarterCard(int playerID, CardSideType side) throws RemoteException;

    void chooseSecretObj(int playerID, int cardID) throws RemoteException;

    void placeCard(int playerID, int cardID, CardSideType side, Position pos) throws RemoteException;

    void drawResourceCard(int playerID) throws RemoteException;

    void drawVisibleResourceCard(int playerID, int index) throws RemoteException;

    void drawGoldenCard(int playerID) throws RemoteException;

    void drawVisibleGoldenCard(int playerID, int index) throws RemoteException;

    void sendChatMessage(int playerID, String recipientNickname, String message) throws RemoteException;
}
