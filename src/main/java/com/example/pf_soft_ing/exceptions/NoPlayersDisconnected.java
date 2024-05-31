package com.example.pf_soft_ing.exceptions;

public class NoPlayersDisconnected extends Exception{
    public NoPlayersDisconnected() {
        super("There are no disconnected players in the match.");
    }
}
