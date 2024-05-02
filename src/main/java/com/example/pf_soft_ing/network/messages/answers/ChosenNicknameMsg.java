package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class ChosenNicknameMsg extends Message {

    private final String nickname;

    public ChosenNicknameMsg(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return The player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "Player's nickname: " + nickname;
    }
}
