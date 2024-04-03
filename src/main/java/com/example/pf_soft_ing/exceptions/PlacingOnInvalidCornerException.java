package com.example.pf_soft_ing.exceptions;

public class PlacingOnInvalidCornerException extends Exception{
    public PlacingOnInvalidCornerException(){
        super("Invalid card position: trying to place on hidden corner.");
    }
}
