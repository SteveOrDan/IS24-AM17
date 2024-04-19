package com.example.pf_soft_ing.RMI;

import com.example.pf_soft_ing.ClientGameControllerInterface;
import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.ServerGameControllerInterface;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;


public class ClientRMI implements ClientGameControllerInterface {
    private static ServerGameControllerInterface stub;
    private String nickname;
    private Registry registry;

    public void main(String[] args) throws RemoteException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        ClientRMI client1 = new ClientRMI();
        int idClient1 = -1;
        boolean flag = true;
        Scanner input = new Scanner(System.in);

        while (flag) {
            System.out.println("Inserisci comando: ");
            String userInput = input.nextLine();
            if (userInput != null){
                switch (userInput){
                    case "end" : flag = false;
                    case "addPlayer" : {
                        System.out.println("Inserisci nickname: ");
                        String nickname = input.nextLine();
                        try {
                            idClient1 = client1.addPlayer(hostName, portNumber, nickname);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case "placeCard" :{
                        if (idClient1 != -1){
                            System.out.println("IdCarta: ");
                            int idCard = input.nextInt();
                            System.out.println("Insert position:");
                            System.out.println("x :");
                            int x = input.nextInt();
                            System.out.println("y :");
                            int y = input.nextInt();
                            Position position = new Position(x, y);
                            // da sistemare non posso mandargli un oggetto Position
                            client1.placeCard(hostName, portNumber, idClient1, idCard, position);
                        }else{
                            System.out.println("Giocatore non esistente");
                        }
                    }
                    case "flipCard" :{

                    }
                    case "endTurn" :{

                    }
                    case "drawResourceCard" : {

                    }
                    case "drawVisibleResourceCard" :{

                    }
                    case "drawGoldenCard" : {

                    }
                    case "drawVisibleGoldenCard" :{

                    }
                    case "drawStarterCard" :{

                    }
                }

            }
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

    @Override
    public void setObjectivesToChoose(String hostName, int port, int playerID) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.setObjectivesToChoose(playerID);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setRandomFirstPlayer(String hostName, int port) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.setRandomFirstPlayer();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fillPlayerHand(String hostName, int port, int playerID) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.fillPlayerHand(playerID);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setCommonObjectives() throws RemoteException {

    }

    @Override
    public void setVisibleCards() throws RemoteException {

    }

    @Override
    public void drawResourceCard(String hostName, int port, int playerID) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.drawResourceCard(playerID);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawVisibleResourceCard(String hostName, int port, int playerID, int index) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.drawVisibleResourceCard(playerID, index);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void drawGoldenCard(String hostName, int port, int playerID) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.drawGoldenCard(playerID);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawVisibleGoldenCard(String hostName, int port, int playerID, int index) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.drawVisibleGoldenCard(playerID, index);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawStarterCard(String hostName, int port, int playerID) throws RemoteException {
        registry = LocateRegistry.getRegistry(hostName, port);
        try {
            stub = (ServerGameControllerInterface) registry.lookup("RemoteController");
            stub.drawStarterCard(playerID);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
