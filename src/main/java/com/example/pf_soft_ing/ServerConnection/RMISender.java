package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.RemoteException;
import java.util.List;

public class RMISender extends Encoder {

    ClientRMIInterface client;

    public RMISender(ClientRMIInterface client){
        this.client = client;
    }

    public void sendID(int id){
        try {
            client.sendID(id);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setState(PlayerState state){
        try {
            client.setState(state);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCurrScore(int score){
        try {
            client.setCurrScore(score);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setObjectivesToChooseEncoded(List<Integer> objectiveIDs){
        try {
            client.setObjectivesToChoose(objectiveIDs);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setFirstPlayerTokenEncoded(TokenColors color){
        try {
            client.setFirstPlayerToken(color);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setTokenEncoded(TokenColors color){
        try {
            client.setToken(color);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addCardToPlayerHandEncoded(int id){
        try {
            client.addCardToPlayerHand(id);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setSecretObjectiveEncoded(int id){
        try {
            client.setSecretObjective(id);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setStarterCardEncoded(int id){
        try {
            client.setStarterCard(id);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void placeStarterCard(boolean placed){
        try {
            client.placeStarterCard(placed);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void placeCard(boolean placed){
        try {
            client.placeCard(placed);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
