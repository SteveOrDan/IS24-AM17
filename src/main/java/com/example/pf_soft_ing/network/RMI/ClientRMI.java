package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class ClientRMI implements ClientGameControllerInterface {
    private static ServerGameControllerInterface stub;
    private String nickname;
    private Registry registry;
    private ServerRMI objServer;

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
                    case "end" : {
                        flag = false;
                        break;
                    }
                    case "addPlayer" : {
                        System.out.println("Inserisci nickname: ");
                        String nickname = input.nextLine();
                        try {
                            idClient1 = client1.addPlayer(hostName, portNumber, nickname);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case "placeCard" :{
                        if (idClient1 != -1){
                            System.out.println("IdCarta 1 : ");
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
                        break;
                    }
                    case "flipCard" :{
                        if (idClient1 != -1){
                            System.out.println("IdCarta: ");
                            int idCard = input.nextInt();
                            client1.flipCard(hostName, portNumber, idClient1, idCard);
                        }else{
                            System.out.println("Giocatore non esistente");
                        }
                        break;
                    }
                    case "endTurn" :{
                        client1.endTurn(hostName, portNumber);
                        break;
                    }
                    case "drawResourceCard" : {
                        if (idClient1 != -1){
                            client1.drawResourceCard(hostName, portNumber, idClient1);
                        } else {
                            System.out.println("Giocatore non esistente");
                        }
                        break;
                    }
                    case "drawVisibleResourceCard" :{
                        if (idClient1 != -1){
                            System.out.println("Carta 0 o Carta 1");
                            int index = input.nextInt();
                            switch (index){
                                case 0 : client1.drawVisibleResourceCard(hostName, portNumber, idClient1, index);
                                case 1 : client1.drawVisibleResourceCard(hostName, portNumber, idClient1, index);
                                default: System.out.println("Carta non valida");
                            }
                        } else {
                            System.out.println("Giocatore non esistente");
                        }
                        break;
                    }
                    case "drawGoldenCard" : {
                        if (idClient1 != -1){
                            client1.drawGoldenCard(hostName, portNumber, idClient1);
                        } else {
                            System.out.println("Giocatore non esistente");
                        }
                        break;
                    }
                    case "drawVisibleGoldenCard" :{
                        if (idClient1 != -1){
                            System.out.println("Carta 0 o Carta 1");
                            int index = input.nextInt();
                            switch (index){
                                case 0 : client1.drawVisibleGoldenCard(hostName, portNumber, idClient1, index);
                                case 1 : client1.drawVisibleGoldenCard(hostName, portNumber, idClient1, index);
                                default: System.out.println("Carta non valida");
                            }
                        } else {
                            System.out.println("Giocatore non esistente");
                        }
                        break;
                    }
                    case "drawStarterCard" :{
                        if (idClient1 != -1){
                            client1.drawStarterCard(hostName, portNumber, idClient1);
                        } else {
                            System.out.println("Giocatore non esistente");
                        }
                        break;
                    }
                    default: System.out.println("Comando non riconosciuto, riprova.");
                }
            }
        }

    }

    @Override
    public Integer addPlayer(String hostName, int port,String nickname) throws RemoteException, GameFullException, NicknameAlreadyExistsException {
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

    //server su client
    @Override
    public void setObjectivesToChoose(int playerID) throws RemoteException {
        System.out.println("Server called setObjectivesToChoose");

    }

    @Override
    public void setRandomFirstPlayer() throws RemoteException {

    }

    @Override
    public void fillPlayerHand(int playerID) throws RemoteException {

    }

    @Override
    public void setCommonObjectives() throws RemoteException {

    }

    @Override
    public void setVisibleCards() throws RemoteException {

    }
}
