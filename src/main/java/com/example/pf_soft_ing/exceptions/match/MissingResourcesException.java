package com.example.pf_soft_ing.exceptions.match;

public class MissingResourcesException extends Exception{
    public MissingResourcesException(){
        super("Cannot place card: placement requirements are not met.");
    }
}
