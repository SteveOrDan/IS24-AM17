package com.example.pf_soft_ing.exceptions;

public class CardNotPlacedException extends Exception{
    public CardNotPlacedException(){
        super("Card has not been placed yet.");
    }
}
