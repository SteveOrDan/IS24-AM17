package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

import java.util.List;

public class RankingMsg extends Message {

    private final List<String> rankings;

    public RankingMsg(List<String> rankings) {
        this.rankings = rankings;
    }

    public List<String> getRankings() {
        return rankings;
    }

    @Override
    public String toString() {
        return "This is a ranking message.";
    }
}
