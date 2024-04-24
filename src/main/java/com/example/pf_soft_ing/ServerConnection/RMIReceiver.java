package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.network.RMI.ClientRMI;
import com.example.pf_soft_ing.network.RMI.ServerGameControllerInterface;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class RMIReceiver extends UnicastRemoteObject implements ServerGameControllerInterface {
    private final Decoder decoder;

    private List<ClientRMI> clients;
    private static RMIReceiver serverObj = null;
    private static Registry registry = null;

    public RMIReceiver(int port, Decoder decoder) throws RemoteException {
        this.decoder = decoder;
        //matchController = new MatchController(4, 0);
        clients = new ArrayList<ClientRMI>();
    }

    public static void bind(int port, Decoder decoder) throws RemoteException {
        try {
            // Creo ggetto che implementa interfaccia
            serverObj = new RMIReceiver(port, decoder);

            // Bind the remote object in the registry

            registry = LocateRegistry.createRegistry(port);
            registry.bind("RemoteController", serverObj);

            System.out.println("Server ready");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Error Starting ServerRMI");
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(int id, Position pos) throws RemoteException {
        serverObj.decoder.placeCard(id, pos);
    }

    @Override
    public void drawResourceCard() throws RemoteException {
        serverObj.decoder.drawResourceCard();
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {
        serverObj.decoder.drawVisibleResourceCard(playerID, index);
    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {
        serverObj.decoder.drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {
        serverObj.decoder.drawVisibleGoldenCard(playerID,index);
    }

    @Override
    public void requestError() throws RemoteException {

    }
}
