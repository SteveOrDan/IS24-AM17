package com.example.pf_soft_ing.ServerConnection.messages.answers;

import com.example.pf_soft_ing.ServerConnection.messages.Message;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.PlayerModel;

import java.util.List;
import java.util.Map;

public class ReturnMatchesMsg extends Message {

    private final List<MatchController> matches;

    public ReturnMatchesMsg(List<MatchController> matches) {
        this.matches = matches;
    }

    /**
     * @return List of match controllers
     */
    public List<MatchController> getMatches() {
        return matches;
    }

    @Override
    public String toString() {
        String result = "Matches found:";

        for (MatchController match : matches) {
            result += "\n\t" + match.getMatchModel().getMatchID() + " with players:";

            for (String player : match.getMatchModel().getNicknames()) {
                result += "\n\t\t- " + player;
            }

            result += "\n";
        }

        return result;
    }
}
