package com.example.pf_soft_ing.Messages;

public class DrawResultMessage extends Message{
    int cardID;
    boolean isSuccess;

    public DrawResultMessage(int cardID, boolean isSuccess){
        this.cardID = cardID;
        this.isSuccess = isSuccess;
    }

    public int getCardID() {
        return cardID;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
