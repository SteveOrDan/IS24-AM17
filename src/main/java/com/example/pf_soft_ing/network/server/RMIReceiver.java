package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.InvalidMatchID;
import com.example.pf_soft_ing.exceptions.InvalidNumOfPlayers;
import com.example.pf_soft_ing.exceptions.InvalidPlayerStateException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private final GameController gameController;
    private static final HashMap<Integer, PlayerModel> IDToPlayer = new HashMap<>();

    public RMIReceiver(GameController gameController) throws RemoteException {
        this.gameController = gameController;
    }

    @Override
    public int getMatches(ClientRMIInterface client) throws RemoteException {
        PlayerModel playerModel = gameController.getGameModel().createPlayer(new RMISender(client));
        IDToPlayer.put(playerModel.getID(), playerModel);

        playerModel.getSender().sendMatches(gameController.getGameModel().getMatches());

        return playerModel.getID();
    }

    @Override
    public void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException {
        try {
            MatchController match = gameController.createMatch(playerID, numberOfPlayers, nickname);

            IDToPlayer.get(playerID).getSender().createMatchResult(match.getMatchModel().getMatchID(), nickname);
        }
        catch (InvalidNumOfPlayers | InvalidPlayerStateException | GameIsFullException e) {
            IDToPlayer.get(playerID).getSender().sendError(e.getMessage());
        }
    }

    @Override
    public void selectMatch(int playerID, int matchID) throws RemoteException {
        try {
            MatchController matchController = gameController.selectMatch(playerID, matchID);

            IDToPlayer.get(playerID).getSender().selectMatchResult(matchController.getMatchModel().getMatchID(), matchController.getMatchModel().getNicknames());
        }
        catch (InvalidMatchID | GameIsFullException e) {
            IDToPlayer.get(playerID).getSender().sendError(e.getMessage());
        }
    }

    @Override
    public void chooseNickname(int playerID, String nickname) throws RemoteException {

    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos) throws RemoteException {

    }

    @Override
    public void drawResourceCard(int playerID) throws RemoteException {

    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {

    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {

    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {

    }
}