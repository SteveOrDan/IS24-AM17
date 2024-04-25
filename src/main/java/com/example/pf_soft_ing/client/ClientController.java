package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.Encoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController {
    private HashMap<Integer, OpponentModel> otherPlayerMap;

    private final ClientModel clientModel;

    private final View view;

    private int matchID;

    public ClientController(ClientEncoder clientEncoder, View view){
        this.clientModel = new ClientModel(clientEncoder);
        this.view = view;
    }

    public void getMatches(){
        clientModel.getMatches();
    }

    public void printMatches(Map<Integer, List<String>> matches){
        if (matches.isEmpty()){
            String inputPlayers = view.noMatches();
            while (!isNumeric(inputPlayers) || (Integer.parseInt(inputPlayers) > 1 && Integer.parseInt(inputPlayers) < 5)){
                inputPlayers = view.noMatches();
            }
            createMatch(Integer.parseInt(inputPlayers));
        }
        String input = view.printMatches(matches);
        boolean inputFlag = true;
        while (inputFlag){
            if (input == "new") {
                inputFlag = false;
                String inputPlayers = view.numberOfPlayers();
                while (!isNumeric(inputPlayers) || (Integer.parseInt(inputPlayers) > 1 && Integer.parseInt(inputPlayers) < 5)){
                    inputPlayers = view.numberOfPlayers();
                }
                createMatch(Integer.parseInt(inputPlayers));
            } else {
                if (matches.keySet().contains(Integer.parseInt(input))){
                    inputFlag = false;

                    sendMatch(Integer.parseInt(input));
                } else {
                    input = view.failedMatch(matches);
                }
            }
        }
    }

    public void failedMatch(Map<Integer, List<String>> matches){
        view.failedMatch(matches);
    }

    private void createMatch(int numberOfPlayers){
        clientModel.createMatch(numberOfPlayers);
    }

    private void sendMatch(int matchID){
        clientModel.sendMatch(matchID);
    }

    private boolean isNumeric(String input){
        int intValue;
        try {
            intValue = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void joinMatch(int matchID, List<String> nicknames){
        this.matchID = matchID;
        sendNickname(view.askNickname(nicknames));
    }
    private void sendNickname(String nickname){
        clientModel.sendNickname(nickname);
    }

    public void failedNickname(List<String> nicknames){
        sendNickname(view.failedNickname(nicknames));
    }

    public void addNickname(Integer playerID, String nickname, Map<Integer, String> opponents){
        clientModel.setId(playerID);
        clientModel.setNickname(nickname);
        for (int opponentId : opponents.keySet()){
            otherPlayerMap.put(opponentId, new OpponentModel(opponents.get(opponentId), opponentId));
        }
        view.entered();
    }
}
