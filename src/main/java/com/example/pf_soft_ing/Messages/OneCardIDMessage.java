package com.example.pf_soft_ing.Messages;

public class OneCardIDMessage extends Message{
    int cardID;

    public OneCardIDMessage(int cardID){
        this.cardID = cardID;
    }

    public int getCardID() {
        return cardID;
    }

    @Override
    public String toString() {
        return "OneCardIDMessage{" +
                "cardID=" + cardID +
                '}';
    }
}
