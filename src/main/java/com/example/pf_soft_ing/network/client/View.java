package com.example.pf_soft_ing.network.client;

import java.util.List;
import java.util.Map;

public abstract class View {

    public abstract void getMatches(Map<Integer, List<String>> matches);

    public abstract void createMatch(int matchID, String hostNickname);

    public abstract void selectMatch(int matchID, List<String> nicknames);

    public abstract void chooseNickname(String nickname);

    public abstract void errorMessage(String errorMessage);
}
