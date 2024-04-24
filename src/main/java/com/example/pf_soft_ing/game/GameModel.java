package com.example.pf_soft_ing.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
    private final List<MatchController> matches = new ArrayList<>();

    public MatchController getMatch(int matchID){
        for (MatchController match : matches) {
            if (match.getMatchModel().getMatchID() == matchID) {
                return match;
            }
        }
        return null;
    }

    public void createGame(String nickname, int numberOfPlayers){
        try {
            List<Integer> matchIDs = new ArrayList<>();
            matches.forEach(match -> matchIDs.add(match.getMatchModel().getMatchID()));

            Random rng = new Random();
            int newID = rng.nextInt(1000);

            while(matchIDs.contains(newID)){
                newID = rng.nextInt(1000);
            }

            MatchController match = new MatchController(numberOfPlayers, newID);

            matches.add(match);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectGame(int matchId){

    }

    public void startGame(MatchController matchController){
        new Thread(matchController::setUpGame).start();
    }
}
