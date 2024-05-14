package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class PrivateMessageMsg extends Message {

    private final int recipientID;
    private final String message;

    public PrivateMessageMsg(int recipientID, String message) {
        this.recipientID = recipientID;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getRecipientID() {
        return recipientID;
    }

    @Override
    public String toString() {
        return null;
    }
}
