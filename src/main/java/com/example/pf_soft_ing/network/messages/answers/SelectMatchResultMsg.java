package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

import java.util.List;

public class SelectMatchResultMsg extends Message {

    private final int matchID;
    private final List<String> nicknames;

    public SelectMatchResultMsg(int matchID, List<String> nicknames) {
        this.matchID = matchID;
        this.nicknames = nicknames;
    }

    /**
     * @return MatchID
     */
    public int getMatchID() {
        return matchID;
    }

    /**
     * @return All match nicknames
     */
    public List<String> getNicknames() {
        return nicknames;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Joined match with ID: " + matchID + ". Current players: ");

        for (String nickname : nicknames) {
            res.append(nickname).append(", ");
        }

        res = new StringBuilder(res.substring(0, res.length() - 2));

        return res.toString();
    }
}
