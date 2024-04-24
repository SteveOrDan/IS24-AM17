package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.ServerConnection.Encoder;
import com.example.pf_soft_ing.ServerConnection.RMIReceiver;
import com.example.pf_soft_ing.game.MatchController;

import java.io.BufferedReader;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerInterface extends Remote {
    void beginGameCreation() throws RemoteException;
    void createGame(String nickname, int numberOfPlayers) throws RemoteException;
    MatchController selectMatch(int matchID) throws RemoteException;
    boolean checkNickname(MatchController matchController, String nickname) throws RemoteException;
    void joinGame(MatchController matchController, String nickname, Encoder encoder, RMIReceiver serverRMI) throws RemoteException;


}
