package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class MatchCreatedMsg extends Message {

    private final int matchID;
    private final String hostNickname;

    public MatchCreatedMsg(int matchID, String hostNickname) {
        this.matchID = matchID;
        this.hostNickname = hostNickname;
    }

    /**
     * @return Match ID
     */
    public int getMatchID() {
        return matchID;
    }

    /**
     * @return Host nickname
     */
    public String getHostNickname() {
        return hostNickname;
    }

    @Override
    public String toString() {
        return "Created match with ID: " + matchID;
    }
}
