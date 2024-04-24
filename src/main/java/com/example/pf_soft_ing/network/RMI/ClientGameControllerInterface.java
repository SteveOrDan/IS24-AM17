package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.GameFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientGameControllerInterface extends Remote {
    // server su client
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
