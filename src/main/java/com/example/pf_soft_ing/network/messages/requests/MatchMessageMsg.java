package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class MatchMessageMsg extends Message {

    private final String message;

    public MatchMessageMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return null;
    }
}
