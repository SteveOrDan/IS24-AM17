package com.example.pf_soft_ing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientGameControllerInterface extends Remote {
    Integer addPlayer(String hostName, int port,String nickname) throws RemoteException;
    void placeCard(String hostName, int port,int playerID, int cardID, Position pos) throws RemoteException;
    void flipCard(String hostName, int port,int playerID, int cardID) throws RemoteException;
    void endTurn(String hostName, int port) throws RemoteException;
}
