package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class ClientRMIReceiver extends UnicastRemoteObject implements ClientRMIInterface {

    private final ClientDecoder clientDecoder;

    public ClientRMIReceiver(ClientDecoder clientDecoder) throws RemoteException {
        super();
        this.clientDecoder = clientDecoder;
    }

    public ClientRMIInterface getClient(){
        return this;
    }

    @Override
    public void printMatches(Map<Integer, List<String>> matchesNicknames) throws RemoteException {
        clientDecoder.printMatches(matchesNicknames);
    }

    @Override
    public void failedMatch(Map<Integer, List<String>> matchesNicknames) throws RemoteException {
        clientDecoder.failedMatch(matchesNicknames);
    }

    @Override
    public void joinMatch(int matchID, List<String> nicknames) throws RemoteException {
        clientDecoder.joinMatch(matchID, nicknames);
    }

    @Override
    public void addNickname(Integer playerID, String nickname, Map<Integer, String> opponents) {
        clientDecoder.addNickname(playerID, nickname, opponents);
    }

    @Override
    public void failedNickname(List<String> nicknames) throws RemoteException {
        clientDecoder.failedNickname(nicknames);
    }

    @Override
    public void sendID(int id) throws RemoteException {

    }

    @Override
    public void setState(PlayerState state) throws RemoteException {

    }

    @Override
    public void setCurrScore(int score) throws RemoteException {

    }

    @Override
    public void setToken(TokenColors color) throws RemoteException {

    }

    @Override
    public void setObjectivesToChoose(List<Integer> objectiveIDs) throws RemoteException {

    }

    @Override
    public void setFirstPlayerToken(TokenColors color) throws RemoteException {

    }

    @Override
    public void addCardToPlayerHand(int id) throws RemoteException {

    }

    @Override
    public void setSecretObjective(int id) throws RemoteException {

    }

    @Override
    public void setStarterCard(int id) throws RemoteException {

    }

    @Override
    public void placeStarterCard(boolean placed) throws RemoteException {

    }

    @Override
    public void placeCard(boolean placed) throws RemoteException {

    }
}
