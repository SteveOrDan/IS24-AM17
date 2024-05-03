package com.example.pf_soft_ing.network.client;

import java.util.List;
import java.util.Map;

public class TUIView implements View {

    @Override
    public void getMatches(Map<Integer, List<String>> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matches available");
            return;
        }

        System.out.println("Matches:");
        for (Map.Entry<Integer, List<String>> entry : matches.entrySet()) {
            System.out.println("\tMatch with ID " + entry.getKey() + " and players:");
            System.out.println("\t\t- " + entry.getValue());
        }
    }

    @Override
    public void createMatch(int matchID, String hostNickname) {
        System.out.println("Match created with ID: " + matchID + " and host nickname: " + hostNickname);
    }

    @Override
    public void selectMatch(int matchID, List<String> nicknames) {
        System.out.println("Match selected with ID: " + matchID + " and players:");

        int i = 1;
        for (String nickname : nicknames) {
            if (nickname == null){
                System.out.println("\t- Player " + i);
                i++;
            }
            else{
                System.out.println("\t- " + nickname);
            }
        }
    }

    @Override
    public void chooseNickname(String nickname) {
        System.out.println("Joined match with nickname: " + nickname);
    }

    @Override
    public void errorMessage(String message) {
        System.out.println(message);
    }
}
