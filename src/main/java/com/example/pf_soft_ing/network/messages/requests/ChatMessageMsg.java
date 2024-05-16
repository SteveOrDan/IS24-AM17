package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class ChatMessageMsg extends Message {

    private final String recipient;
    private final String message;

    public ChatMessageMsg(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return null;
    }
}
