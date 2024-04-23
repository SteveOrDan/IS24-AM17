package com.example.pf_soft_ing.network.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientRMI extends UnicastRemoteObject implements ClientGameControllerInterface {
    private static ServerGameControllerInterface stub;
    private String nickname;
    private int IDclient;
    private Registry registry;

    protected ClientRMI() throws RemoteException {

    }
    public void doJob(String hostName, int port) throws Exception{
        registry = LocateRegistry.getRegistry(hostName, port);
        stub = (ServerGameControllerInterface) registry.lookup("RemoteController");

        Scanner input = new Scanner(System.in);
        boolean flag = true;

        while (flag) {
            System.out.println("Write command: ");
            String userInput = input.nextLine();
            if (userInput != null){
                switch (userInput){
                    case "end" : {
                        flag = false;
                        break;
                    }
                    case "addPlayer" : {
                        System.out.println("Add nickname: ");
                        String nickname = input.nextLine();
                        IDclient = stub.addPlayer(nickname);
                        break;
                    }
                    case "placeCard" :{
                            // encoder
                            //stub.placeCard();
                        break;
                    }
                    case "flipCard" :{
                        //stub.flipCard();
                        break;
                    }
                    case "endTurn" :{
                        stub.endTurn();
                        break;
                    }
                    case "drawResourceCard" : {
                        //stub.drawResourceCard();
                        break;
                    }
                    case "drawVisibleResourceCard" :{
                        //stub.drawVisibleResourceCard();
                        break;
                    }
                    case "drawGoldenCard" : {
                        //stub.drawGoldenCard();
                        break;
                    }
                    case "drawVisibleGoldenCard" :{
                        //stub.drawVisibleGoldenCard();
                        break;
                    }
                    case "drawStarterCard" :{
                        //stub.drawStarterCard();
                        break;
                    }
                    default: System.out.println("Try again");
                }
            }
        }
    }

    //server su client
    @Override
    public void setObjectivesToChoose(int playerID) throws RemoteException {
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
