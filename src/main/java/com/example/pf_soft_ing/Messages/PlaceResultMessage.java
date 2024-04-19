package com.example.pf_soft_ing.Messages;

public class PlaceResultMessage extends Message{
    boolean isSuccess;
    int cardID;
    int x;
    int y;
    boolean isFront;

    public PlaceResultMessage(boolean isSuccess, int cardID, int x, int y, boolean isFront){
        this.isSuccess = isSuccess;
        this.cardID = cardID;
        this.x = x;
        this.y = y;
        this.isFront = isFront;
    }

    public boolean isSuccess() {
        return isSuccess;
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
