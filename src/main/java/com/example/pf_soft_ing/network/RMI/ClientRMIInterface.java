package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.*;
import java.util.List;
import java.util.Map;

public interface ClientRMIInterface extends Remote {
    // server su client
    void printMatches(Map<Integer, List<String>> matchesNicknames) throws RemoteException;
    void failedMatch(Map<Integer, List<String>> matchesNicknames) throws RemoteException;
    void joinMatch(int matchID, List<String> nicknames) throws RemoteException;

    void addNickname(Integer playerID, String nickname, Map<Integer, String> opponents) throws RemoteException;
    void failedNickname(List<String> nicknames) throws RemoteException;
    void sendID(int id) throws RemoteException;
    void setState(PlayerState state) throws RemoteException;
    void setCurrScore(int score) throws RemoteException;
    void setToken(TokenColors color) throws RemoteException;
    void setObjectivesToChoose(List<Integer> objectiveIDs) throws RemoteException;
    void setFirstPlayerToken(TokenColors color) throws RemoteException;
    void addCardToPlayerHand(int id) throws RemoteException;
    void setSecretObjective(int id) throws RemoteException;
    void setStarterCard(int id) throws RemoteException;
    void placeStarterCard(boolean placed) throws RemoteException;
    void placeCard(boolean placed) throws RemoteException;
}
