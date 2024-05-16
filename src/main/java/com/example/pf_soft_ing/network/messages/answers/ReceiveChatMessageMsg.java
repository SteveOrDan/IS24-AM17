package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class ReceiveChatMessageMsg extends Message {

    private final String senderNickname;

    private final String recipientNickname;
    private final String message;

    public ReceiveChatMessageMsg(String senderNickname, String recipientNickname, String message) {
        this.senderNickname = senderNickname;
        this.recipientNickname = recipientNickname;
        this.message = message;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public String getRecipientNickname() {
        return recipientNickname;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return null;
    }
}
