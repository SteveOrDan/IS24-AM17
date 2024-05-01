package com.example.pf_soft_ing.ServerConnection.messages.requests;

import com.example.pf_soft_ing.ServerConnection.messages.Message;

public class ChooseNicknameMsg extends Message {

    private final String nickname;

    public ChooseNicknameMsg(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return Nickname
     */
    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "Selected Nickname is: " + nickname;
    }
}
