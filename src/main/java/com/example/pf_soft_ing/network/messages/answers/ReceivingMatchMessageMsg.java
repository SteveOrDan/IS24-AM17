package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class ReceivingMatchMessageMsg extends Message {

    private final String message;

    private final int senderID;

    public ReceivingMatchMessageMsg(String message, int senderID) {
        this.message = message;
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public int getSenderID() {
        return senderID;
    }

    @Override
    public String toString() {
        return null;
    }
}
