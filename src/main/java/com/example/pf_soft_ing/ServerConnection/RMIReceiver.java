package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.game.networkControllers.NewPlayerController;
import com.example.pf_soft_ing.network.RMI.ClientRMI;
import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private static HashMap<ClientRMIInterface, Decoder> clientRMIDecoderHashMap;
    private final GameController gameController;
    private MatchController matchController;

    public RMIReceiver(GameController gameController) throws RemoteException {
        clientRMIDecoderHashMap = new HashMap<>();
        this.gameController = gameController;
    }

    public static void addDecoder(ClientRMIInterface client, Decoder decoder){
        clientRMIDecoderHashMap.put(client,decoder);
    }

    @Override
    public void getMatches(ClientRMIInterface client) throws RemoteException {
        NewPlayerController.getMatches(gameController, client);
        //client.printMatches(gameController.getGameModel().getMatches());
    }

    @Override
    public void sendMatch(int matchID, ClientRMIInterface client) throws RemoteException {
        this.matchController = NewPlayerController.sendMatch(gameController, matchID, client);
//        MatchController matchController = gameController.getGameModel().getMatch(matchID);
//        if (matchController == null ){
//            client.failedMatch(gameController.getGameModel().getMatches());
//        }
//        else {
//            client.joinMatch(matchController.getMatchModel().getMatchID(), matchController.getMatchModel().getNicknames());
//            this.matchController = matchController;
//        }
    }

    @Override
    public void sendNickname(String nickname, ClientRMIInterface client) throws RemoteException {
        NewPlayerController.sendNickname(matchController, nickname, client);
//        try {
//          Integer playerId = matchController.addPlayer(nickname, client);
//          client.addNickname(playerId, matchController.getMatchModel().getIDToPlayerMap().get(playerId).getNickname(), matchController.getMatchModel().getNicknamesMap(playerId));
//      }
//      catch (GameIsFullException | NicknameAlreadyExistsException e) {
//          client.failedNickname(matchController.getMatchModel().getNicknames());
//      }
    }


    @Override
    public void createMatch(int numberOfPlayers, ClientRMIInterface client) throws RemoteException {
        matchController = NewPlayerController.createMatch(gameController, numberOfPlayers, client);
//        matchController = gameController.createGame(numberOfPlayers);
//        client.joinMatch(matchController.getMatchModel().getMatchID(), new ArrayList<String>());
    }

    @Override
    public void placeCard(int id, Position pos, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).placeCard(id, pos);
    }

    @Override
    public void drawResourceCard(ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawResourceCard();
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawVisibleResourceCard(playerID,index);
    }

    @Override
    public void drawGoldenCard(int playerID, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawGoldenCard(playerID);
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index, ClientRMIInterface client) throws RemoteException {
        clientRMIDecoderHashMap.get(client).drawVisibleGoldenCard(playerID, index);
    }

    @Override
    public void requestError(ClientRMIInterface client) throws RemoteException {
        //clientRMIDecoderHashMap.get(client).requestError();
        System.out.println("Method request error called");
        client.sendID(4);
    }

    @Override
    public void createGame(ClientRMIInterface client, String nickname, int numberOfPlayers) throws RemoteException {
        //gameController.createGame(nickname, numberOfPlayers);
    }

    @Override
    public MatchController selectMatch(ClientRMIInterface client, int matchID) throws RemoteException {
        return gameController.selectMatch(matchID);
    }

    @Override
    public void joinMatch(MatchController matchController, String nickname, ClientRMIInterface client) throws NicknameAlreadyExistsException, GameIsFullException, RemoteException {
        ///gameController.joinMatch(matchController, nickname, client);
    }

    @Override
    public boolean foo(String name) throws RemoteException {
        int id = 1;
        System.out.println(name + "called foo");
        return true;
    }
}