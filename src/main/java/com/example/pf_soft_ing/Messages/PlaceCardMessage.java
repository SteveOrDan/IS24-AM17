package com.example.pf_soft_ing.Messages;

public class PlaceCardMessage extends Message{
    int cardID;
    int x;
    int y;
    boolean isFront;

    public PlaceCardMessage(int cardID, int x, int y, boolean isFront){
        this.cardID = cardID;
        this.x = x;
        this.y = y;
        this.isFront = isFront;
    }

    public int getCardID() {
        return cardID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFront() {
        return isFront;
    }
}
