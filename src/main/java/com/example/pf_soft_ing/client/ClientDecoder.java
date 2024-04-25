package com.example.pf_soft_ing.client;

import java.util.List;
import java.util.Map;

public class ClientDecoder {

    private ClientController clientController;

    public ClientDecoder(ClientController clientController) {
        this.clientController = clientController;
    }

    public void printMatches(Map<Integer, List<String>> matches){
        clientController.printMatches(matches);
    }

    public void failedMatch(Map<Integer, List<String>> matches){
        clientController.failedMatch(matches);
    }

    public void joinMatch(int matchID, List<String> nicknames){
        clientController.joinMatch(matchID, nicknames);
    }

    public void failedNickname(List<String> nicknames){
        clientController.failedNickname(nicknames);
    }

    public void addNickname(Integer playerID, String nickname, Map<Integer, String> opponents){
        clientController.addNickname(playerID, nickname, opponents);
    }
}
