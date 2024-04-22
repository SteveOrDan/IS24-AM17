package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.ServerConnection.MessageEncoder;

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

    public void selectGame(int matchID){
        // Updates player's view
    }

    public void joinGame(int matchID, String nickname){
        // Adds player to the match
        // Updates player's view
        gameModel.joinGame(matchID, nickname);

        MatchModel match = gameModel.getMatch(matchID).getMatchModel();

        if (match.getCurrPlayers() == match.getMaxPlayers()){
            startGame(matchID);
        }
    }

    public void startGame(int matchID){
        // Updates match's state to "SET_UP"
        // Updates player's state to "SET_UP_GAME"
        // Updates player's view
        gameModel.startGame(matchID);
    }
}
