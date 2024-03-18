package com.example.pf_soft_ing.exceptions;

public class MissingResourcesException extends Exception{
    public MissingResourcesException(){
        super("Cannot place card: placement requirements are not met.");
    }
}
