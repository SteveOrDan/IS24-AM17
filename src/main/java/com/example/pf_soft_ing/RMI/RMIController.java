package com.example.pf_soft_ing.RMI;


import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.Token;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Creating Remote interface for our application
public interface RMIController extends Remote {
    boolean login(String nick) throws RemoteException;
    void logout(String nick) throws RemoteException;
    String flipStarterCard(int playerId) throws RemoteException;
    String setToken(int playerId, Token token) throws RemoteException;
    String placeStarterCard(int playerId) throws RemoteException;
    String placeCard(int cardId, Position pos, int playerId) throws RemoteException;

}