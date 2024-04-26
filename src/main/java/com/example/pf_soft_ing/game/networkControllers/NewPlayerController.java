package com.example.pf_soft_ing.game.networkControllers;

import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewPlayerController {

    private static void sendMessage(String output, PrintWriter out){
        out.println(output);
    }

    private static String getMatchesSoket(GameController gameController){
        StringBuilder output = new StringBuilder();
        Map<Integer, List<String>> matchesNicknames = getMatches(gameController);
        for (Integer matchID : matchesNicknames.keySet()){
            output.append(" ");
            output.append(STR."M \{matchID}");
            for (String s : matchesNicknames.get(matchID)){
                output.append(" P ");
                output.append(s);
            }
        }
        return output.toString();
    }

    private static Map<Integer, List<String>> getMatches(GameController gameController){
        return gameController.getGameModel().getMatches();
    }

    private static List<String> getMatchesNicknames(MatchController matchController){
        return matchController.getMatchModel().getNicknames();
    }

    public static void getMatches(GameController gameController, PrintWriter out){
        String output = STR."0\{getMatchesSoket(gameController)}";
        sendMessage(output, out);
    }

    public static void getMatches(GameController gameController, ClientRMIInterface client) throws RemoteException {
        client.printMatches(getMatches(gameController));
    }

    private static MatchController sendMatch(GameController gameController, int matchID){
        return gameController.getGameModel().getMatch(matchID);
    }

    public static MatchController sendMatch(GameController gameController, int matchID, PrintWriter out){
        MatchController matchController = sendMatch(gameController, matchID);
        StringBuilder output = new StringBuilder();
        if (matchController == null ){
            output.append(STR."1\{getMatchesSoket(gameController)}");
        } else {
            output.append("2");
            for (String s : matchController.getMatchModel().getNicknames()){
                output.append(" ");
                output.append(s);
            }
        }

        sendMessage(output.toString(), out);
        return matchController;
    }

    public static MatchController sendMatch(GameController gameController, int matchID, ClientRMIInterface client) throws RemoteException {
        MatchController matchController = sendMatch(gameController, matchID);
        if (matchController == null ){
            client.failedMatch(getMatches(gameController));
        } else {
            client.joinMatch(matchController.getMatchModel().getMatchID(), getMatchesNicknames(matchController));
        }
        return matchController;
    }

    public static void sendNickname(MatchController matchController, String nickname, PrintWriter out){
        StringBuilder output = new StringBuilder();
        try {
            Integer playerId = matchController.addPlayer(nickname, out);
            output.append(STR."4 \{playerId} \{matchController.getMatchModel().getIDToPlayerMap().get(playerId).getNickname()}");
            for (int id : matchController.getMatchModel().getNicknamesMap(playerId).keySet()){
                output.append(STR." \{id}");
                output.append(STR." \{matchController.getMatchModel().getIDToPlayerMap().get(id).getNickname()}");
            }
            sendMessage(output.toString(), out);
        } catch (GameIsFullException e) {
            output.append("3 ");
            for (String s : matchController.getMatchModel().getNicknames()){
                output.append(STR." \{s}");
            }
            sendMessage(output.toString(), out);
        } catch (NicknameAlreadyExistsException e) {
            output.append("3 ");
            for (String s : matchController.getMatchModel().getNicknames()){
                output.append(STR." \{s}");
            }
            sendMessage(output.toString(), out);
            throw new RuntimeException(e);
        }
    }

    public static void sendNickname(MatchController matchController, String nickname, ClientRMIInterface client) throws RemoteException {
        try {
            Integer playerId = matchController.addPlayer(nickname, client);
            client.addNickname(playerId, matchController.getMatchModel().getIDToPlayerMap().get(playerId).getNickname(), matchController.getMatchModel().getNicknamesMap(playerId));
        } catch (GameIsFullException e) {
            client.failedNickname(matchController.getMatchModel().getNicknames());
        } catch (NicknameAlreadyExistsException e) {
            client.failedNickname(matchController.getMatchModel().getNicknames());
            throw new RuntimeException(e);
        }
    }

    private static MatchController createMatch(GameController gameController, int numberOfPlayers){
        return gameController.createGame(numberOfPlayers);
    }

    public static MatchController createMatch(GameController gameController, int numberOfPlayers, PrintWriter out){
        MatchController matchController = createMatch(gameController, numberOfPlayers);
        String output = STR."2 \{matchController.getMatchModel().getMatchID()}";
        sendMessage(output, out);
        return matchController;
    }

    public static MatchController createMatch(GameController gameController, int numberOfPlayers, ClientRMIInterface client) throws RemoteException {
        MatchController matchController = createMatch(gameController, numberOfPlayers);
        client.joinMatch(matchController.getMatchModel().getMatchID(), new ArrayList<String>());
        return matchController;
    }
}
