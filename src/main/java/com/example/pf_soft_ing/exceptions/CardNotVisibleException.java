package com.example.pf_soft_ing.exceptions;

public class CardNotVisibleException extends Exception{
    public CardNotVisibleException(){
        super("Invalid card: The card is not visible");
    }
}
