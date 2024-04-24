package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.ServerConnection.Decoder;
import com.example.pf_soft_ing.ServerConnection.Encoder;
import com.example.pf_soft_ing.ServerConnection.RMISender;
import com.example.pf_soft_ing.ServerConnection.SocketReceiver;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.network.RMI.ServerRMI;
import com.example.pf_soft_ing.player.PlayerModel;

import java.io.BufferedReader;
import java.util.List;

public class GameController {
    private final GameModel gameModel = new GameModel();

    public void beginGameCreation(){
        // Updates player's view
    }

    public void createGame(String nickname, int numberOfPlayers){
        // Creates a new match in the game model
        // Creates a new player in the match
        gameModel.createGame(nickname, numberOfPlayers);

        // Updates player's view
    }

    public MatchController selectMatch(int matchID){
        // Updates player's view
        return gameModel.getMatch(matchID);
    }

    public boolean checkNickname(MatchController matchController, String nickname){
        return matchController.getMatchModel().getIDToPlayerMap().values().stream().map(PlayerModel::getNickname).anyMatch(s -> s.contains(nickname));
    }

    public void joinGame(MatchController matchController, String nickname, Encoder encoder, BufferedReader in) throws NicknameAlreadyExistsException, GameIsFullException {
        // Adds player to the match
        // Updates player's view

        new SocketReceiver(playerCreator(matchController, nickname, encoder), in);

        analizePlayerNumber(matchController);
    }

    public void joinGame(MatchController matchController, String nickname, Encoder encoder, ServerRMI serverRMI) throws NicknameAlreadyExistsException, GameIsFullException {
        // Adds player to the match
        // Updates player's view

//        new RMIReceiver(playerCreator(matchController, nickname, encoder), serverRMI);

        analizePlayerNumber(matchController);
    }

    protected Decoder playerCreator(MatchController matchController, String nickname, Encoder encoder) throws NicknameAlreadyExistsException, GameIsFullException {
        int playerId = matchController.addPlayer(nickname, encoder);
        return new Decoder(matchController, playerId);
    }

    protected void analizePlayerNumber(MatchController matchController){
        MatchModel match = matchController.getMatchModel();

        if (match.getCurrPlayers() == match.getMaxPlayers()){
            gameModel.startGame(matchController);
        }
    }
}
