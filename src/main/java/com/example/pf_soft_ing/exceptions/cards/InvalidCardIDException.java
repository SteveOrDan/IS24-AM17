package com.example.pf_soft_ing.exceptions.cards;

public class InvalidCardIDException extends Exception{
    public InvalidCardIDException(){
        super("Invalid card ID: card ID is not in the game.");
    }
}
