package com.example.pf_soft_ing.Messages;

public class NewGameMessage extends Message{
    String nickname;
    int maxPlayersNum;

    public NewGameMessage(String nickname, int maxPlayersNum){
        this.nickname = nickname;
        this.maxPlayersNum = maxPlayersNum;
    }

    public String getNickname() {
        return nickname;
    }

    public int getMaxPlayersNum() {
        return maxPlayersNum;
    }
}
