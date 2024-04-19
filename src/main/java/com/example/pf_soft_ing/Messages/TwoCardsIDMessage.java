package com.example.pf_soft_ing.Messages;

public class TwoCardsIDMessage extends Message{
    int cardID1;
    int cardID2;

    public TwoCardsIDMessage(int cardID1, int cardID2){
        this.cardID1 = cardID1;
        this.cardID2 = cardID2;
    }

    public int getCardID1() {
        return cardID1;
    }

    public int getCardID2() {
        return cardID2;
    }
}
