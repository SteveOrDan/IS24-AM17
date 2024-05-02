package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class CreateMatchMsg extends Message {

    private final int numberOfPlayers;
    private final String nickname;

    public CreateMatchMsg(int numberOfPlayers, String nickname) {
        this.numberOfPlayers = numberOfPlayers;
        this.nickname = nickname;
    }

    /**
     * @return Maximum number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * @return Host nickname
     */
    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "Creating match with " + numberOfPlayers + " players and " + nickname + " as host.";
    }
}
