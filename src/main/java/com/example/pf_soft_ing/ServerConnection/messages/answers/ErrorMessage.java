package com.example.pf_soft_ing.ServerConnection.messages.answers;

import com.example.pf_soft_ing.ServerConnection.messages.Message;

public class ErrorMessage extends Message {

    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    /**
     * @return Error message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
