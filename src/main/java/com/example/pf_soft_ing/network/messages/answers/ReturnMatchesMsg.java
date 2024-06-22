package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

import java.util.List;
import java.util.Map;

public class ReturnMatchesMsg extends Message {

    private final Map<Integer, List<String>> matches;

    private final int playerID;

    public ReturnMatchesMsg(Map<Integer, List<String>> matches, int playerID) {
        this.matches = matches;
        this.playerID = playerID;
    }

    /**
     * @return Map of match IDs with the corresponding nicknames
     */
    public Map<Integer, List<String>> getMatches() {
        return matches;
    }

    /**
     * @return playerID
     */
    public int getPlayerID() {
        return playerID;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Matches found:");

        for (Integer matchID : matches.keySet()) {
            result.append("\n\t").append(matchID).append(" with players:");

            for (String player : matches.get(matchID)) {
                result.append("\n\t\t- ").append(player);
            }

            result.append("\n");
        }

        return result.toString();
    }
}
