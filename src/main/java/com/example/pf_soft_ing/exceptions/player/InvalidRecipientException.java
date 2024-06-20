package com.example.pf_soft_ing.exceptions.player;

public class InvalidRecipientException extends Exception {
    public InvalidRecipientException() {
        super("Invalid recipient. Please try again.");
    }
}
