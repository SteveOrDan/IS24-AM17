package com.example.pf_soft_ing.client;

import java.util.List;
import java.util.Map;

public abstract class View {

    public abstract String printMatches(Map<Integer, List<String>> matches);

    public abstract String failedMatch(Map<Integer, List<String>> matches);

    public abstract void matchCreated(int matchID);

    public abstract String noMatches();

    public abstract String errorNoMatches();

    public abstract String numberOfPlayers();

    public abstract String errorNumberOfPlayers();

    public abstract String askNickname(List<String> nicknames);

    public abstract String failedNickname(List<String> nicknames);

    public abstract void entered();

    public abstract void errorMessage(String message);
}
