package com.example.pf_soft_ing.RMI;

import com.example.pf_soft_ing.GameController;
import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.ServerGameControllerInterface;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ServerRMI extends UnicastRemoteObject implements ServerGameControllerInterface {

    private final GameController gameController;
    private static ServerRMI obj = null;
    private static Registry registry = null;

    public ServerRMI(int port) throws RemoteException {
        super(port);
        gameController = new GameController();
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
    public synchronized static ServerRMI getServerRMI(int port) throws RemoteException {
        if (obj == null){
            try {
                obj = new ServerRMI(port);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }
    public synchronized static Registry getRegistry() throws RemoteException {
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
    public Integer addPlayer(String nickname) throws RemoteException {
        return obj.gameController.addPlayer(nickname);
    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos) throws RemoteException {
        obj.gameController.placeCard(playerID,cardID,pos);
    }

    @Override
    public void flipCard(int playerID, int cardID) throws RemoteException {
        obj.gameController.flipCard(playerID,cardID);
    }

    @Override
    public void endTurn() throws RemoteException {
        obj.gameController.endTurn();
    }
}
