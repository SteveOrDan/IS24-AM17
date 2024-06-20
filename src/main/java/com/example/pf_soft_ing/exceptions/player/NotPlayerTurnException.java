package com.example.pf_soft_ing.exceptions.player;

public class NotPlayerTurnException extends Exception{
    public NotPlayerTurnException(){
        super("Invalid action: it is not the player's turn.");
    }
}
