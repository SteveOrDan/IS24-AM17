package com.example.pf_soft_ing.exceptions;

public class PositionAlreadyTakenException extends Exception{
    public PositionAlreadyTakenException(){
        super("Invalid card position: position already taken.");
    }
}
