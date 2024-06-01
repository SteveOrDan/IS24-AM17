package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class ReconnectToMatchMsg extends Message {

    private final int matchID;
    private final String nickname;

    public ReconnectToMatchMsg(int matchID, String nickname) {
        this.matchID = matchID;
        this.nickname = nickname;
    }

    public int getMatchID() {
        return matchID;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "ReconnectToMatchMsg";
    }
}
