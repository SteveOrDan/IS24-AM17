package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.game.MatchController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClientController {

    private Map<Integer, OpponentModel> otherPlayerMap = new HashMap<>();

    private final ClientModel clientModel;

    private final View view;

    private int matchID;

    public ClientController(ClientSender clientSender, View view){
        this.clientModel = new ClientModel(clientSender);
        this.view = view;
    }

    public void getMatches(){
//        clientModel.getMatches();
    }

    public void printMatches(List<MatchController> matches){
//        System.out.println(matches);
//        if (matches.isEmpty()){
//            String inputPlayers = view.noMatches();
//
//            while (!validNumberOfPlayers(inputPlayers)){
//                inputPlayers = view.errorNoMatches();
//            }
//
//            createMatch(Integer.parseInt(inputPlayers));
//        } else {
//            String input = view.printMatches(matches);
//            boolean inputFlag = true;
//
//            while (inputFlag) {
//                if (Objects.equals(input, "new")) {
//                    inputFlag = false;
//                    String inputPlayers = view.numberOfPlayers();
//
//                    while (!validNumberOfPlayers(inputPlayers)) {
//                        inputPlayers = view.errorNumberOfPlayers();
//                    }
//
//                    createMatch(Integer.parseInt(inputPlayers));
//                } else {
//                    if (matches.containsKey(Integer.parseInt(input))) {
//                        inputFlag = false;
//
//                        sendMatch(Integer.parseInt(input));
//                    }
//                    else {
//                        input = view.failedMatch(matches);
//                    }
//                }
//            }
//        }
    }

    private boolean validNumberOfPlayers(String input){
        return (Objects.equals(input, "2") || Objects.equals(input, "3") || Objects.equals(input, "4"));
    }

    public void failedMatch(Map<Integer, List<String>> matches){
        view.failedMatch(matches);
    }

    private void createMatch(int playerID, int numberOfPlayers, String nickname){
//        clientModel.createMatch(playerID, numberOfPlayers, nickname);
    }


    public void joinMatch(int matchID, List<String> nicknames){
        this.matchID = matchID;
        sendNickname(view.askNickname(nicknames));
    }

    public void matchCreated(int matchID){
        view.matchCreated(matchID);
    }

    private void sendNickname(String nickname){
//        clientModel.sendNickname(nickname);
    }

    public void failedNickname(List<String> nicknames){
        sendNickname(view.failedNickname(nicknames));
    }

    public void addNickname(Integer playerID, String nickname, Map<Integer, String> opponents){
//        clientModel.setId(playerID);
//        clientModel.setNickname(nickname);
//
//        for (int opponentId : opponents.keySet()){
//            otherPlayerMap.put(opponentId, new OpponentModel(opponents.get(opponentId), opponentId));
//        }
//
//        view.entered();
    }

    public void errorMessage(String message){
        view.errorMessage(message);
    }
}
