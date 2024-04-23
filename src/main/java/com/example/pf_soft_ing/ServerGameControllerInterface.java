package com.example.pf_soft_ing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerGameControllerInterface extends Remote {
    Integer addPlayer(String nickname) throws RemoteException;
    void placeCard(int playerID, int cardID, Position pos) throws RemoteException;
    void flipCard(int playerID, int cardID) throws RemoteException;
    void endTurn() throws RemoteException;
    void drawResourceCard(int playerID) throws RemoteException;
    void drawVisibleResourceCard(int playerID, int index) throws RemoteException;
    void drawGoldenCard(int playerID) throws RemoteException;
    void drawVisibleGoldenCard(int playerID, int index) throws RemoteException;
    void drawStarterCard(int playerID) throws RemoteException;

    // client su server
    void setObjectivesToChoose(int playerID) throws RemoteException;
    void fillPlayerHand(int playerID) throws RemoteException;
    void setRandomFirstPlayer() throws RemoteException;
    void setCommonObjectives() throws RemoteException;
    void setVisibleCards() throws RemoteException;
}
