package com.example.pf_soft_ing.exceptions;

public class StarterCardAlreadyPlacedException extends Exception{
    public StarterCardAlreadyPlacedException(){
        super("Starter card already placed in the play area.");
    }
}
