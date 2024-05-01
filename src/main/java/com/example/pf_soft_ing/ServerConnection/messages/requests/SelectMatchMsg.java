package com.example.pf_soft_ing.ServerConnection.messages.requests;

import com.example.pf_soft_ing.ServerConnection.messages.Message;

public class SelectMatchMsg extends Message {

    private final int matchID;

    public SelectMatchMsg(int matchID) {
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
        return "Selected Match is: " + matchID;
    }
}
