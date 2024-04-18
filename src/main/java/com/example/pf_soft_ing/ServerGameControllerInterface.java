package com.example.pf_soft_ing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerGameControllerInterface extends Remote {
    Integer addPlayer(String nickname) throws RemoteException;
    void placeCard(int playerID, int cardID, Position pos) throws RemoteException;
    void flipCard(int playerID, int cardID) throws RemoteException;
    void endTurn() throws RemoteException;
}
