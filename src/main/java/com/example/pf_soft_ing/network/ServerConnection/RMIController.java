package com.example.pf_soft_ing.network.ServerConnection;

import com.example.pf_soft_ing.ServerConnection.Decoder;
import com.example.pf_soft_ing.ServerConnection.RMIReceiver;
import com.example.pf_soft_ing.ServerConnection.RMIReceiverInterface;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.network.RMI.ClientRMI;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import static java.lang.System.exit;

public class RMIController {

//    public static void startRMIReceiver(int portNumber){
//            Registry registry = null;
//            RMIReceiverInterface rmiReceiver;
//            try {
//                rmiReceiver = new RMIReceiver();
//                registry = LocateRegistry.createRegistry(portNumber);
//                registry.bind("RMIReceiver", rmiReceiver);
//            } catch (AccessException e) {
//                throw new RuntimeException(e);
//            } catch (AlreadyBoundException e) {
//                throw new RuntimeException(e);
//            } catch (RemoteException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println(STR."Server started! Port:\{portNumber}");
//
//    }
    public static void startRMIReceiver(String args[]){

       Registry registry = null;
        RMIReceiverInterface rmiReceiver;

        if (args.length != 1) {
            System.out.println("Cannot start port.");
            exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        boolean foundPort = false;
        while (!foundPort) {
            try {

                rmiReceiver = new RMIReceiver();
                registry = LocateRegistry.createRegistry(portNumber);

                try {
                    registry.bind("RMIReceiver", rmiReceiver);
                } catch (AlreadyBoundException e) {
                    throw new RuntimeException(e);
                }
                foundPort = true;
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
                portNumber++;
            }
        }
        System.out.println(STR."Server started! Port:\{portNumber}");
    }


//    public static void ClientStarterRequest(ClientRMI client, RMIReceiverInterface stub){
//        Scanner input = new Scanner(System.in);
//        String clientRequest = input.nextLine();
//
//        switch (clientRequest){
//            case "createGame" :{
//                System.out.println("Nickname: ");
//                String nickname = input.nextLine();
//                System.out.println("NumberOfPlayers: ");
//                int numberOfPlayers = Integer.parseInt(input.nextLine());
//                try {
//                    stub.createGame(client, nickname, numberOfPlayers);
//                } catch (RemoteException e) {
//                    throw new RuntimeException(e);
//                }
//                break;
//            }
//            case "showMeMatches":{
//                break;
//            }
//        }
//
//        clientRequest = input.nextLine();
//        switch (clientRequest){
//            case "selectMatch" :{
//                System.out.println("Insert matchID: ");
//                int matchID = Integer.parseInt(input.nextLine());
//                try {
//                    stub.selectMatch(client, matchID);
//                } catch (RemoteException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
//    public static void ClientRequests(ClientRMI client, RMIReceiverInterface stub){
//        Scanner input = new Scanner(System.in);
//        boolean flag = true;
//
//        while (flag) {
//            System.out.println("Write command: ");
//            String userInput = input.nextLine();
//            if (userInput != null){
//                switch (userInput){
//                    case "placeCard" : {
//                        System.out.println("Card Id: ");
//                        int id = Integer.parseInt(input.nextLine());
//                        System.out.println("Position x: ");
//                        int x = Integer.parseInt(input.nextLine());
//                        System.out.println("Position y: ");
//                        int y = Integer.parseInt(input.nextLine());
//                        Position pos = new Position(x,y);
//                        try {
//                            stub.placeCard(id, pos, client);
//                        } catch (RemoteException e) {
//                            throw new RuntimeException(e);
//                        }
//                        break;
//                    }
//                    case "drawResourceCard": {
//                        try {
//                            stub.drawResourceCard(client);
//                        } catch (RemoteException e) {
//                            throw new RuntimeException(e);
//                        }
//                        break;
//                    }
//                    case "drawVisibleResourceCard" : {
//                        System.out.println("Card 0 or Card 1: ");
//                        int index = Integer.parseInt(input.nextLine());
//                        //stub.drawVisibleResourceCard();
//                        break;
//                    }
//                    case "drawGoldenCard" : {
//                        //stub.drawGoldenCard();
//                        break;
//                    }
//                    case "drawVisibleGoldenCard" :{
//                        //stub.drawVisibleGoldenCard();
//                        break;
//                    }
//                    case "requestError" :{
//                        try {
//                            stub.requestError(client);
//                        } catch (RemoteException e) {
//                            throw new RuntimeException(e);
//                        }
//                        break;
//                    }
//                    case "end":{
//                        flag = false;
//                    }
//                    default: System.out.println("Try again");
//                }
//            }
//        }
//    }

//    public static void startRMIReceiver() throws RemoteException, AlreadyBoundException {
//        RMIReceiver rmiReceiver = new RMIReceiver();
//        Registry registry = LocateRegistry.createRegistry(1099);
//        registry.bind("RMIReceiver", rmiReceiver);
//        System.out.println("Waiting for invocations from clients");
//    }
}
