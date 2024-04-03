package com.example.pf_soft_ing.RMI;

import com.example.pf_soft_ing.PlayerModel;
import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.Token;
import com.example.pf_soft_ing.card.Card;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.StarterCard;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServerRMI implements RMIController {
    static int PORT = 1234;
    public HashMap<Integer, PlaceableCard> CardIDMap;
    public HashMap<Integer, PlayerModel> PlayerIDMap;

    public static void main( String[] args ){

        System.out.println("Hello from Server!");
        ServerRMI obj = new ServerRMI();
        try {

            RMIController stub = (RMIController) UnicastRemoteObject.exportObject(obj, PORT);
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("Loggable", stub);
            System.out.println("Server ready");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean login(String nick) throws RemoteException {
        System.out.println(nick + " is logging..");
        return false;
    }

    @Override
    public void logout(String nick) throws RemoteException {
    }

    @Override
    public String flipStarterCard(int playerId) throws RemoteException {
        System.out.println("flipping card ID :" + playerId);
        PlayerIDMap.get(playerId).flipStarterCard();
        return "Card Flipped";
    }

    @Override
    public String setToken(int playerId, Token token) throws RemoteException {
        System.out.println("Setting token for " + playerId);
        PlayerIDMap.get(playerId).setToken(token);
        return "Token setted";
    }

    @Override
    public String placeStarterCard(int playerId) throws RemoteException {
        System.out.println("Placing starter card for " + playerId);
        PlayerIDMap.get(playerId).placeStarterCard();
        return "Starter Card Placed";
    }

    @Override
    public String placeCard(int cardId, Position pos, int playerId) throws RemoteException {
        //da sistemare, CHIEDERE
        System.out.println("Placing card for " + playerId);
        PlaceableCard card = CardIDMap.get(cardId);
        PlayerIDMap.get(playerId).placeCard(card, pos);
        return "Card placed";
    }

}
