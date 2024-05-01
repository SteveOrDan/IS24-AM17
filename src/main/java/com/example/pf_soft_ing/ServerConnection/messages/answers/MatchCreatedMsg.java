package com.example.pf_soft_ing.ServerConnection.messages.answers;

import com.example.pf_soft_ing.ServerConnection.messages.Message;

public class MatchCreatedMsg extends Message {

    private final int matchID;

    public MatchCreatedMsg(int matchID) {
        this.matchID = matchID;
    }

    /**
     * @return Match ID
     */
    public int getMatchID() {
        return matchID;
    }

    @Override
    public String toString() {
        return "Created match with ID: " + matchID;
    }
}
