package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.network.RMI.ClientRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private final Decoder decoder;


    protected RMIReceiver(Decoder decoder) throws RemoteException {
        this.decoder = decoder;
    }


    @Override
    public void placeCard(int id, Position pos) throws RemoteException {
        decoder.placeCard(id,pos);
    }

    @Override
    public void drawResourceCard() throws RemoteException {
        //serverObj.decoder.drawResourceCard();
        decoder.drawResourceCard();
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {
        decoder.drawVisibleResourceCard(playerID,index);
    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {
        decoder.drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {
        decoder.drawVisibleGoldenCard(playerID,index);
    }

    @Override
    public void requestError() throws RemoteException {
    }
}
