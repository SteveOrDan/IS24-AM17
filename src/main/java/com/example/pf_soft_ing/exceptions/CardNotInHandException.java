package com.example.pf_soft_ing.exceptions;

public class CardNotInHandException extends Exception{
    public CardNotInHandException(){
        super("Invalid card ID: card ID is not in the player's hand.");
    }
}
