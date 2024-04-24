package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.exceptions.GameFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.game.MatchController;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class ServerRMI extends UnicastRemoteObject implements ServerGameControllerInterface {

    private final MatchController matchController;
    //private final MessageDecoder messageDecoder;
    private List<ClientRMI> clients;
    private static ServerRMI serverObj = null;
    private static Registry registry = null;

    public ServerRMI(int port) throws RemoteException {
        matchController = new MatchController(4, 0);
        clients = new ArrayList<ClientRMI>();
    }

    public static void bind(int port) throws RemoteException {
        try {
            // Creo ggetto che implementa interfaccia
            serverObj = new ServerRMI(port);

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

    //@Override
    //public Integer addPlayer(String nickname) throws RemoteException, GameFullException, NicknameAlreadyExistsException {
      //  return serverObj.matchController.addPlayer(nickname);
    //}

     @Override
    public void placeCard(int playerID, int cardID, Position pos) throws RemoteException {
        serverObj.matchController.placeCard(playerID,cardID,pos);
    }

    @Override
    public void flipCard(int playerID, int cardID) throws RemoteException {
        serverObj.matchController.flipCard(playerID,cardID);
    }

    @Override
    public void endTurn() throws RemoteException {
        serverObj.matchController.endTurn();
    }


    @Override
    public void drawResourceCard(int playerID) throws RemoteException {
        serverObj.matchController.drawResourceCard(playerID);
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {
        serverObj.matchController.drawVisibleResourceCard(playerID, index);
    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {
        serverObj.matchController.drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {
        serverObj.matchController.drawVisibleGoldenCard(playerID, index);
    }

    @Override
    public void drawStarterCard(int playerID) throws RemoteException {
        serverObj.drawStarterCard(playerID);
    }
}
