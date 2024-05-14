package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class ConfirmPrivateMessageMsg extends Message {
    private final int recipientID;

    private final String message;

    private final int senderID;

    public ConfirmPrivateMessageMsg(int recipientID, String message, int senderID) {
        this.recipientID = recipientID;
        this.message = message;
        this.senderID = senderID;
    }

    public int getRecipientID() {
        return recipientID;
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
