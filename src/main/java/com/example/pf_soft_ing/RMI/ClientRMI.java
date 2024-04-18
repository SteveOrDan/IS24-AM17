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
    private static ServerGameControllerInterface stub;
    private String nickname;
    private Registry registry;

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
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        this.nickname = nickname;
        return stub.addPlayer(nickname);
    }

    @Override
    public void placeCard(String hostName, int port, int playerID, int cardID, Position pos) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.placeCard(playerID,cardID,pos);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flipCard(String hostName, int port,int playerID, int cardID) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.flipCard(playerID,cardID);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endTurn(String hostName, int port) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.endTurn();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

}
