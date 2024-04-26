package com.example.pf_soft_ing.client;

import java.util.List;
import java.util.Map;

public class View {

    public String printMatches(Map<Integer, List<String>> matches){
        return null;
    }

    public String failedMatch(Map<Integer, List<String>> matches){
        return null;
    }

    public String noMatches(){return null;}
    public String errorNoMatches(){return null;}

    public String numberOfPlayers(){
        return null;
    }
    public String errorNumberOfPlayers(){
        return null;
    }

    public String askNickname(List<String> nicknames){return null;}

    public String failedNickname(List<String> nicknames){return null;}

    public void entered(){}
}
