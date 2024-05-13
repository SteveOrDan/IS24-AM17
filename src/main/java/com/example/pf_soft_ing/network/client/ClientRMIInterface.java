package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.*;
import java.util.List;
import java.util.Map;

public interface ClientRMIInterface extends Remote {
    // server su client
    void showMatches(Map<Integer, List<String>> matchesNicknames, int playerID) throws RemoteException;

    void failedMatch(Map<Integer, List<String>> matchesNicknames) throws RemoteException;

    void joinMatch(int matchID, List<String> nicknames) throws RemoteException;

    void addNickname(Integer playerID, String nickname, Map<Integer, String> opponents) throws RemoteException;

    void failedNickname(List<String> nicknames) throws RemoteException;

    void setState(PlayerState state) throws RemoteException;

    void setCurrScore(int score) throws RemoteException;

    void setToken(TokenColors color) throws RemoteException;

    void setObjectivesToChoose(List<Integer> objectiveIDs) throws RemoteException;

    void setFirstPlayerToken(TokenColors color) throws RemoteException;

    void addCardToPlayerHand(int id) throws RemoteException;

    void setSecretObjective(int id) throws RemoteException;

    void setStarterCard(int id) throws RemoteException;

    void placeStarterCard() throws RemoteException;

    void placeCard() throws RemoteException;

    void sendError(String errorMsg) throws RemoteException;

    void selectMatchResult(int matchID, List<String> nicknames) throws RemoteException;

    void createMatchResult(int matchID, String hostNickname) throws RemoteException;

    void chooseNicknameResult(String hostNickname) throws RemoteException;
    void startGame(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                   int starterCardID) throws RemoteException;

    void sendPlayerTurn(int playerID, String playerNickname) throws RemoteException;

    void showNewPlayer(String nickname) throws RemoteException;
}
