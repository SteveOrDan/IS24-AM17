package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.player.TokenColors;

import java.util.List;
import java.util.Map;

public class TUIView implements View {

    @Override
    public void showMatches(Map<Integer, List<String>> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matches available.");
            System.out.println("To create a new match type CreateMatch, number of players and nickname.");
            return;
        }

        System.out.println("Matches:");
        for (Map.Entry<Integer, List<String>> entry : matches.entrySet()) {
            System.out.println("\tMatch with ID " + entry.getKey() + " and players:");
            System.out.println("\t\t- " + entry.getValue());
        }
        System.out.println("To create a new match type CreateMatch, number of players and nickname.");
        System.out.println("To join a match type SelectMatch and MatchID.");
    }

    @Override
    public void createMatch(int matchID, String hostNickname) {
        System.out.println("Match created with ID: " + matchID + " and host nickname: " + hostNickname);
        System.out.println("Wait for players to join...");
    }

    @Override
    public void selectMatch(int matchID, List<String> nicknames) {
//        System.out.println("Match selected with ID: " + matchID + " and players:");
//
//        int i = 1;
//        for (String nickname : nicknames) {
//            if (nickname == null){
//                System.out.println("\t- Player " + i);
//                i++;
//            }
//            else{
//                System.out.println("\t- " + nickname);
//            }
//        }
        System.out.println("Match selected with ID: " + matchID);
        System.out.println("Type ChooseNickname and the nickname");
    }

    @Override
    public void chooseNickname(String nickname) {
        System.out.println("Joined match with nickname: " + nickname);
    }

    @Override
    public void startGame(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) {
        System.out.println("Game started.");
    }

    @Override
    public void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2, int secretObjectiveCardID1, int secretObjectiveCardID2) {

    }

    @Override
    public void confirmSecretObjective(int secretObjectiveCardID) {

    }

    @Override
    public void errorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showNewPlayer(String nicknames) {
        System.out.println("New player joined: " + nicknames);
        System.out.println("Wait for players to join...");
    }

    @Override
    public void showPlayerTurn(int playerID) {

    }
}
