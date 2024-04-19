package com.example.pf_soft_ing.Messages;

public class IsFirstPlayerMessage extends Message{
    boolean isFirstPlayer;

    public IsFirstPlayerMessage(boolean isFirstPlayer){
        this.isFirstPlayer = isFirstPlayer;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }
}
