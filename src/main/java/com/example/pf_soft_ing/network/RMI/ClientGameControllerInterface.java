package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientGameControllerInterface extends Remote {
    // server su client
    void setObjectivesToChoose(int playerID) throws RemoteException;
    void setRandomFirstPlayer() throws RemoteException;
    void fillPlayerHand(int playerID) throws RemoteException;
    void setCommonObjectives() throws RemoteException;
    void setVisibleCards() throws RemoteException;
}
