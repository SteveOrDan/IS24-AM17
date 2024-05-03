package com.example.pf_soft_ing.network.client;

import java.util.List;
import java.util.Map;

public interface View {

    void getMatches(Map<Integer, List<String>> matches);

    void createMatch(int matchID, String hostNickname);

    void selectMatch(int matchID, List<String> nicknames);

    void chooseNickname(String nickname);

    void errorMessage(String errorMessage);
}
