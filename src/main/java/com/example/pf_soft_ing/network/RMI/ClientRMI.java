package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
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
                    case "join" : {
                        //stub.join(this);
                    }
                    case "leave": {
                        //stub.leave(this);
                    }
                    case "end" : {
                        flag = false;
                        break;
                    }
                    case "addPlayer" : {
                        System.out.println("Add nickname: ");
                        String nickname = input.nextLine();
                        //IDclient = stub.addPlayer(nickname);
                        break;
                    }
                    case "placeCard" :{
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

    @Override
    public void sendID(int id) throws RemoteException {
        IDclient = id;
        System.out.println("Your id is " + id);
    }

    @Override
    public void setState(PlayerState state) throws RemoteException {

    }

    @Override
    public void setCurrScore(int score) throws RemoteException {

    }

    @Override
    public void setToken(TokenColors color) throws RemoteException {
    }

    @Override
    public void setObjectivesToChoose(List<Integer> objectiveIDs) throws RemoteException {

    }

    @Override
    public void setFirstPlayerToken(TokenColors color) throws RemoteException {

    }

    @Override
    public void addCardToPlayerHand(int id) throws RemoteException {

    }

    @Override
    public void setSecretObjective(int id) throws RemoteException {

    }

    @Override
    public void setStarterCard(int id) throws RemoteException {

    }

    @Override
    public void placeStarterCard(boolean placed) throws RemoteException {

    }

    @Override
    public void placeCard(boolean placed) throws RemoteException {

    }
}
