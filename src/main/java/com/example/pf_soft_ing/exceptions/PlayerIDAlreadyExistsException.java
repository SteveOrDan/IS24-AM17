package com.example.pf_soft_ing.exceptions;

public class PlayerIDAlreadyExistsException extends Exception{
    public PlayerIDAlreadyExistsException(){
        super("Invalid player ID: player ID already exists in the game.");
    }
}
