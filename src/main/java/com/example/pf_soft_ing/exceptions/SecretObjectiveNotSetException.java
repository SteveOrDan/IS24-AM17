package com.example.pf_soft_ing.exceptions;

public class SecretObjectiveNotSetException extends Exception{
    public SecretObjectiveNotSetException(){
        super("Cannot calculate points: secret objective not set.");
    }
}
