package com.example.pf_soft_ing.Messages;

public class PlaceStarterCardMessage extends Message{
    int cardID;
    boolean isFront;

    public PlaceStarterCardMessage(int cardID, boolean isFront){
        this.cardID = cardID;
        this.isFront = isFront;
    }

    public int getCardID() {
        return cardID;
    }

    public boolean isFront() {
        return isFront;
    }
}
