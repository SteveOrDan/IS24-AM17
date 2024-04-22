package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.exceptions.GameFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.game.MatchController;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ServerRMI extends UnicastRemoteObject implements ServerGameControllerInterface {

    private final MatchController matchController;
    private static ServerRMI obj = null;
    private static Registry registry = null;

    public ServerRMI(int port) throws RemoteException {
        super(port);
        matchController = new MatchController(4, 0);
    }

    public static ServerRMI bind(int port) throws RemoteException {
        try {
            obj = new ServerRMI(port);
            // Bind the remote object in the registry
            registry = LocateRegistry.createRegistry(port);
            getRegistry().rebind("RemoteController", obj);
            System.out.println("Server ready");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.err.println("Error Starting ServerRMI");
        }
        return getServerRMI(port);
    }
    public static ServerRMI getServerRMI(int port) throws RemoteException {
        if (obj == null){
            try {
                obj = new ServerRMI(port);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }
    public static Registry getRegistry() throws RemoteException {
        return registry;
    }

    public static void main(String[] args ){
        System.out.println("Hello from Server!");

        if (args.length != 1){
            System.err.println("Cannot start no port.");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerRMI prova = bind(portNumber);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer addPlayer(String nickname) throws RemoteException, GameFullException, NicknameAlreadyExistsException {
        return obj.matchController.addPlayer(nickname);
    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos) throws RemoteException {
        obj.matchController.placeCard(playerID,cardID,pos);
    }

    @Override
    public void flipCard(int playerID, int cardID) throws RemoteException {
        obj.matchController.flipCard(playerID,cardID);
    }

    @Override
    public void endTurn() throws RemoteException {
        obj.matchController.endTurn();
    }


    @Override
    public void drawResourceCard(int playerID) throws RemoteException {
        obj.matchController.drawResourceCard(playerID);
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {
        obj.matchController.drawVisibleResourceCard(playerID, index);
    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {
        obj.matchController.drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {
        obj.matchController.drawVisibleGoldenCard(playerID, index);
    }

    @Override
    public void drawStarterCard(int playerID) throws RemoteException {
        obj.matchController.drawStarterCard(playerID);
    }

    // server su client
    @Override
    public void setObjectivesToChoose(int playerID) throws RemoteException {
        obj.matchController.setObjectivesToChoose(playerID);
    }

    @Override
    public void setRandomFirstPlayer() throws RemoteException {
        obj.matchController.setRandomFirstPlayer();
    }

    @Override
    public void setCommonObjectives() throws RemoteException {

    }

    @Override
    public void setVisibleCards() throws RemoteException {

    }

    @Override
    public void fillPlayerHand(int playerID) throws RemoteException {
        obj.fillPlayerHand(playerID);
    }
}
