package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.ServerConnection.RMIReceiverInterface;

import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

public class ClientRMI extends UnicastRemoteObject implements ClientGameControllerInterface {
    public final String hostName;
    public final int portNumber;
    public ClientRMI(String hostName, int portNumber) throws RemoteException {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public void startClient() throws NotBoundException {
        try {
            Registry registry = LocateRegistry.getRegistry(hostName, portNumber);
            RMIReceiverInterface stub = (RMIReceiverInterface) registry.lookup("RMIReceiver");
            boolean flag = true;

            // iniza ciclo di richieste

            Scanner stdin = new Scanner(System.in);
            while (flag){
                int command = Integer.parseInt(stdin.nextLine());
                switch (command){
                    case 1 : {
                        System.out.println("Insert nickname: ");
                        String nickname = stdin.nextLine();
                        stub.prova(nickname);
                        break;
                    }
                    case 2 : {
                        flag = false;
                        break;
                    }
                    case 3 : {
                        stub.requestError(this);
                        break;
                    }
                }

            }


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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

}
