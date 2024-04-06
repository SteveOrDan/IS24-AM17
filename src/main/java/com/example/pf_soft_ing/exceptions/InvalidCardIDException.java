package com.example.pf_soft_ing.exceptions;

public class InvalidCardIDException extends Exception{
    public InvalidCardIDException(){
        super("Invalid card ID: card ID is not in the game.");
    }
}
