package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class RecipientNotFoundMsg extends Message {

    private final int recipientID;

    public RecipientNotFoundMsg(int recipientID) {
        this.recipientID = recipientID;
    }

    public int getRecipientID() {
        return recipientID;
    }

    @Override
    public String toString() {
        return null;
    }
}
