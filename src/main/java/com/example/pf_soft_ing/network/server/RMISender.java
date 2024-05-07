package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.network.client.ClientRMIInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class RMISender implements Sender {

    ClientRMIInterface client;

    public RMISender(ClientRMIInterface client){
        this.client = client;
    }

    @Override
    public void sendError(String errorMsg) {
        try {
            client.sendError(errorMsg);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void sendMatches(Map<Integer, List<String>> matches) {
        try {
            client.showMatches(matches);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void createMatchResult(int matchID, String hostNickname) {
        try {
            client.createMatchResult(matchID, hostNickname);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void selectMatchResult(int matchID, List<String> nicknames) {
        try {
            client.selectMatchResult(matchID, nicknames);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void chooseNicknameResult(String nickname) {

    }

    @Override
    public void sendGameStart(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                              int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                              int starterCardID) {

    }

    /**
     * Method to place starter card
     * @param placed boolean
     */
    public void placeStarterCard(boolean placed){
        try {
            client.placeStarterCard(placed);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to place a card
     * @param placed boolean
     */
    public void placeCard(boolean placed){
        try {
            client.placeCard(placed);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
