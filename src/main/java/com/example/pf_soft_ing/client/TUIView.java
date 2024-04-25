package com.example.pf_soft_ing.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class TUIView extends View{

    @Override
    public String printMatches(Map<Integer, List<String>> matches) {
        for (Integer matchID : matches.keySet()){
            System.out.println(STR."Match \{matchID}, players:");
            if (matches.get(matchID).isEmpty()){
                System.out.println("\tEmpty match");
            } else {
                for (String s : matches.get(matchID)) {
                    System.out.println(STR."\t- \{s}");
                }
            }
            System.out.println("\n");
        }
        System.out.println("\nDigit the code of the match you want to join or \'new\' to create a new match:\n");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        try {
            input = stdIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    @Override
    public String failedMatch(Map<Integer, List<String>> matches) {
        System.out.println("\nYou choose an invalid ID.\n");
        return printMatches(matches);
    }

    @Override
    public String noMatches() {
        System.out.println("There is no match, a new match will be created.\nDigit the number of the players:\n");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        try {
            input = stdIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    @Override
    public String numberOfPlayers() {
        System.out.println("\nDigit the number of the players:\n");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        try {
            input = stdIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    @Override
    public String askNickname(List<String> nicknames) {
        System.out.println("\nYou entered the match.");
        return printNicknames(nicknames);
    }

    @Override
    public String failedNickname(List<String> nicknames) {
        System.out.println("\nYour nickname is not available:");
        return printNicknames(nicknames);
    }

    private String printNicknames(List<String> nicknames){
        if (!nicknames.isEmpty()) {
            System.out.println(".\nYour oppenents nicknames are:");
            for (String s : nicknames) {
                System.out.println(STR."\t- \{s}");
            }
            System.out.println("\nDigit your nickname:\n");
        } else {
            System.out.println("\nYou are the first player of this match.\nDigit your nickname:\n");
        }
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        try {
            input = stdIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    @Override
    public void entered() {
        System.out.println(".\nYou entered the match.");
    }
}
