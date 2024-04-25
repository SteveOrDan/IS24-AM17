package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.ServerConnection.RMIReceiverInterface;

import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;



public class ClientRMI extends UnicastRemoteObject implements ClientGameControllerInterface {


    public ClientRMI() throws RemoteException {
    }

    public void startClient(String serverHost) throws RemoteException, NotBoundException, MalformedURLException {

        RMIReceiverInterface stub = (RMIReceiverInterface) Naming.lookup("//" + serverHost + "/RMIReceiver");
        boolean risposta = stub.prova(this, "Caterina");
        System.out.println(risposta);

    }

    // server su client
    @Override
    public void sendID(int id) throws RemoteException {
        System.out.println(id);
    }

    @Override
    public void setState(PlayerState state) throws RemoteException {}

    @Override
    public void setCurrScore(int score) throws RemoteException {}

    @Override
    public void setToken(TokenColors color) throws RemoteException {}

    @Override
    public void setObjectivesToChoose(List<Integer> objectiveIDs) throws RemoteException {}

    @Override
    public void setFirstPlayerToken(TokenColors color) throws RemoteException {}

    @Override
    public void addCardToPlayerHand(int id) throws RemoteException {}

    @Override
    public void setSecretObjective(int id) throws RemoteException {}

    @Override
    public void setStarterCard(int id) throws RemoteException {}

    @Override
    public void placeStarterCard(boolean placed) throws RemoteException {}

    @Override
    public void placeCard(boolean placed) throws RemoteException {}

    public static void main(String args[]) throws Exception {
        new ClientRMI().startClient("localhost");
    }

}
