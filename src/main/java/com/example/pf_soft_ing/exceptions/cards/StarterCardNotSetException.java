package com.example.pf_soft_ing.exceptions.cards;

public class StarterCardNotSetException extends Exception{
    public StarterCardNotSetException(){
        super("Starter card has not been set yet.");
    }
}
