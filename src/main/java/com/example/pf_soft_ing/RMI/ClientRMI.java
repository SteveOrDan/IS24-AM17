package com.example.pf_soft_ing.RMI;

import com.example.pf_soft_ing.ClientGameControllerInterface;
import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.ServerGameControllerInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI implements ClientGameControllerInterface {
    private static ServerGameControllerInterface obj;
    private String nickname;
    private Registry registry;

    public void connectionRMI(String hostName, int port) {
        try {
            registry = LocateRegistry.getRegistry(hostName, port);
            obj = (ServerGameControllerInterface) registry.lookup("RemoteController");
            System.out.println("Client ready");
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void main(String[] args) {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        ClientRMI client1 = new ClientRMI();
        ClientRMI client2 = new ClientRMI();

        try {
            int idClient1 = client1.addPlayer(hostName,portNumber,"Cate");
            int idClient2 = client2.addPlayer(hostName,portNumber,"Ele");

            System.out.println(client1.nickname);
            System.out.println(client2.nickname);


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Integer addPlayer(String hostName, int port,String nickname) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            obj = (ServerGameControllerInterface) registry.lookup("RemoteController");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        this.nickname = nickname;
        return obj.addPlayer(nickname);
    }

    @Override
    public void placeCard(String hostName, int port, int playerID, int cardID, Position pos) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            obj = (ServerGameControllerInterface) registry.lookup("RemoteController");
            obj.placeCard(playerID,cardID,pos);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flipCard(String hostName, int port,int playerID, int cardID) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            obj = (ServerGameControllerInterface) registry.lookup("RemoteController");
            obj.flipCard(playerID,cardID);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endTurn(String hostName, int port) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            obj = (ServerGameControllerInterface) registry.lookup("RemoteController");
            obj.endTurn();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

}
