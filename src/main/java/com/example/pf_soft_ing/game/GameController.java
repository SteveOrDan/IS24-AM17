package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.ServerConnection.*;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.network.RMI.ClientRMI;
import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerModel;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
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

    public Integer joinMatch(MatchController matchController, String nickname, PrintWriter out, BufferedReader in) throws NicknameAlreadyExistsException, GameIsFullException {
        // Adds player to the match
        // Updates player's view
        int playerId = matchController.addPlayer(nickname, out);
        Decoder decoder = new Decoder(matchController, playerId);
        new SocketReceiver(decoder, in);

        analyzePlayerNumber(matchController);
        return playerId;
    }

    public Integer joinMatch(MatchController matchController, String nickname, ClientRMIInterface client) throws NicknameAlreadyExistsException, GameIsFullException {
        // Adds player to the match
        // Updates player's view
        int playerId = matchController.addPlayer(nickname, client);
        Decoder decoder = new Decoder(matchController, playerId);

        RMIReceiver.addDecoder(client,decoder);
        analyzePlayerNumber(matchController);
        return playerId;
    }

    protected void analyzePlayerNumber(MatchController matchController){
        MatchModel match = matchController.getMatchModel();

        if (match.getCurrPlayers() == match.getMaxPlayers()){
            gameModel.startGame(matchController);
        }
    }



    private static void sendMessage(String output, PrintWriter out){
        out.println(output);
    }

    private String getMatchesSoket(){
        StringBuilder output = new StringBuilder();
        Map<Integer, List<String>> matchesNicknames = getMatches();
        for (Integer matchID : matchesNicknames.keySet()){
            output.append(" ");
            output.append(STR."M \{matchID}");
            output.append(STR." P \{matchesNicknames.get(matchID).size()}");
            for (String s : matchesNicknames.get(matchID)){
                output.append(" ");
                output.append(s);
            }
        }
        System.out.println(output.toString());
        return output.toString();
    }

    private Map<Integer, List<String>> getMatches(){
        return this.getGameModel().getMatches();
    }

    private static List<String> getMatchesNicknames(MatchController matchController){
        return matchController.getMatchModel().getNicknames();
    }

    public void getMatches(PrintWriter out){
        String output = STR."0\{getMatchesSoket()}";
        sendMessage(output, out);
    }

    public void getMatches(ClientRMIInterface client) throws RemoteException {
        client.printMatches(getMatches());
    }

    private MatchController sendMatch(int matchID){
        return getGameModel().getMatch(matchID);
    }

    public MatchController sendMatch(int matchID, PrintWriter out){
        MatchController matchController = sendMatch(matchID);
        StringBuilder output = new StringBuilder();
        if (matchController == null ){
            output.append(STR."1\{getMatchesSoket()}");
        } else {
            output.append(STR."2 \{matchID}");
            for (String s : matchController.getMatchModel().getNicknames()){
                output.append(" ");
                output.append(s);
            }
        }

        sendMessage(output.toString(), out);
        return matchController;
    }

    public  MatchController sendMatch(int matchID, ClientRMIInterface client) throws RemoteException {
        MatchController matchController = sendMatch(matchID);
        if (matchController == null ){
            new Thread() {
                public void run() {
                    try {
                        client.failedMatch(getMatches());;
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();
        } else {
            new Thread() {
                public void run() {
                    try {
                        client.joinMatch(matchController.getMatchModel().getMatchID(), getMatchesNicknames(matchController));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();

        }
        return matchController;
    }

    public void sendNickname(MatchController matchController, String nickname, PrintWriter out, BufferedReader in){
        StringBuilder output = new StringBuilder();
        try {
            Integer playerId = joinMatch(matchController, nickname, out, in);
            output.append(STR."4 \{playerId} \{matchController.getMatchModel().getIDToPlayerMap().get(playerId).getNickname()}");
            for (int id : matchController.getMatchModel().getNicknamesMap(playerId).keySet()){
                output.append(STR." \{id}");
                output.append(STR." \{matchController.getMatchModel().getIDToPlayerMap().get(id).getNickname()}");
            }
            sendMessage(output.toString(), out);
        } catch (GameIsFullException e) {
            output.append("3");
            for (String s : matchController.getMatchModel().getNicknames()){
                output.append(STR." \{s}");
            }
            sendMessage(output.toString(), out);
        } catch (NicknameAlreadyExistsException e) {
            output.append("3");
            for (String s : matchController.getMatchModel().getNicknames()){
                output.append(STR." \{s}");
            }
            sendMessage(output.toString(), out);
//            throw new RuntimeException(e);
        }
    }

    public void sendNickname(MatchController matchController, String nickname, ClientRMIInterface client) throws RemoteException {
        try {
            Integer playerId = joinMatch(matchController, nickname, client);
            client.addNickname(playerId, matchController.getMatchModel().getIDToPlayerMap().get(playerId).getNickname(), matchController.getMatchModel().getNicknamesMap(playerId));
        } catch (GameIsFullException e) {
            client.failedNickname(matchController.getMatchModel().getNicknames());
        } catch (NicknameAlreadyExistsException e) {
            client.failedNickname(matchController.getMatchModel().getNicknames());
//            throw new RuntimeException(e);
        }
    }

    private MatchController createMatch(int numberOfPlayers){
        return this.createGame(numberOfPlayers);
    }

    public MatchController createMatch(int numberOfPlayers, PrintWriter out){
        MatchController matchController = createMatch(numberOfPlayers);
        String output = STR."2 \{matchController.getMatchModel().getMatchID()}";
        sendMessage(output, out);
        return matchController;
    }

    public MatchController createMatch(int numberOfPlayers, ClientRMIInterface client) throws RemoteException {
        MatchController matchController = createMatch(numberOfPlayers);
        new Thread() {
            public void run() {
                try {
                    client.joinMatch(matchController.getMatchModel().getMatchID(), new ArrayList<String>());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
        return matchController;
    }
}
