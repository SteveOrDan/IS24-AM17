package com.example.pf_soft_ing.exceptions;

public class InvalidPlayerIDException extends Exception {
    public InvalidPlayerIDException() {
        super("Invalid player ID: player ID is not in the game.");
    }
}
