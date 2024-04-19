package com.example.pf_soft_ing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientGameControllerInterface extends Remote {
    Integer addPlayer(String hostName, int port,String nickname) throws RemoteException;
    void placeCard(String hostName, int port, int playerID, int cardID, Position pos) throws RemoteException;
    void flipCard(String hostName, int port, int playerID, int cardID) throws RemoteException;
    void endTurn(String hostName, int port) throws RemoteException;
    void drawResourceCard(String hostName, int port, int playerID) throws RemoteException;
    void drawVisibleResourceCard(String hostName, int port, int playerID, int index) throws RemoteException;
    void drawGoldenCard(String hostName, int port, int playerID) throws RemoteException;
    void drawVisibleGoldenCard(String hostName, int port, int playerID, int index) throws RemoteException;
    void drawStarterCard(String hostName, int port, int playerID) throws RemoteException;

    // server su client
    void setObjectivesToChoose(String hostName, int port, int playerID) throws RemoteException;
    void setRandomFirstPlayer(String hostName, int port) throws RemoteException;
    void fillPlayerHand(String hostName, int port, int playerID) throws RemoteException;
    void setCommonObjectives() throws RemoteException;
    void setVisibleCards() throws RemoteException;
}
