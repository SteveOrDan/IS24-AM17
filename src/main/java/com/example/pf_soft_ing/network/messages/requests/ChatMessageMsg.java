package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class ChatMessageMsg extends Message {

    private final int playerID;
    private final String recipient;
    private final String message;

    public ChatMessageMsg(int playerID, String recipient, String message) {
        this.playerID = playerID;
        this.recipient = recipient;
        this.message = message;
    }

    public int getPlayerID() {
        return playerID;
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
