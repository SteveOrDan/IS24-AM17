package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.ServerConnection.*;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.network.RMI.ClientRMI;
import com.example.pf_soft_ing.player.PlayerModel;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private final GameModel gameModel = new GameModel();

    public GameModel getGameModel() {
        return gameModel;
    }

    public void beginGameCreation(){
        // Updates player's view
    }

    public MatchController createGame(int numberOfPlayers){
        // Creates a new match in the game model
        // Creates a new player in the match
        MatchController matchController = gameModel.createGame(numberOfPlayers);

        // Updates player's view
        return matchController;
    }

    public MatchController selectMatch(int matchID){
        // Updates player's view
        return gameModel.getMatch(matchID);
    }

    public void joinMatch(MatchController matchController, String nickname, PrintWriter out, BufferedReader in) throws NicknameAlreadyExistsException, GameIsFullException {
        // Adds player to the match
        // Updates player's view

        int playerId = matchController.addPlayer(nickname, out);
        Decoder decoder = new Decoder(matchController, playerId);
        new SocketReceiver(decoder, in);

        analizePlayerNumber(matchController);
    }

    public void joinMatch(MatchController matchController, String nickname, ClientRMI client) throws NicknameAlreadyExistsException, GameIsFullException, RemoteException {
        // Adds player to the match
        // Updates player's view

        int playerId = matchController.addPlayer(nickname, client);
        Decoder decoder = new Decoder(matchController, playerId);

        RMIReceiver.addDecoder(client,decoder);
        analizePlayerNumber(matchController);
    }

    protected void analizePlayerNumber(MatchController matchController){
        MatchModel match = matchController.getMatchModel();

        if (match.getCurrPlayers() == match.getMaxPlayers()){
            gameModel.startGame(matchController);
        }
    }
}
