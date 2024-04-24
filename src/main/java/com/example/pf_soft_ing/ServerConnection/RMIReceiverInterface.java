package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    void placeCard(int id, Position pos) throws RemoteException;
    void drawResourceCard() throws RemoteException;
    void drawVisibleResourceCard(int playerID, int index) throws RemoteException;
    void drawGoldenCard(int playerID) throws RemoteException;
    void drawVisibleGoldenCard(int playerID, int index) throws RemoteException;
    void requestError() throws RemoteException;
}
