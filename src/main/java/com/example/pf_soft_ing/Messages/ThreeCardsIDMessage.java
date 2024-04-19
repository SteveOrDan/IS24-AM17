package com.example.pf_soft_ing.Messages;

public class ThreeCardsIDMessage extends Message{
    int cardID1;
    int cardID2;
    int cardID3;

    public ThreeCardsIDMessage(int cardID1, int cardID2, int cardID3){
        this.cardID1 = cardID1;
        this.cardID2 = cardID2;
        this.cardID3 = cardID3;
    }

    public int getCardID1() {
        return cardID1;
    }

    public int getCardID2() {
        return cardID2;
    }

    public int getCardID3() {
        return cardID3;
    }
}
