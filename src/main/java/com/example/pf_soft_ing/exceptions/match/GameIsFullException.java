package com.example.pf_soft_ing.exceptions.match;

public class GameIsFullException extends Exception{
    public GameIsFullException(){
        super("The game is full.");
    }
}
