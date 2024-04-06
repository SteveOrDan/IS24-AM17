package com.example.pf_soft_ing.exceptions;

public class GameIsFullException extends Exception{
    public GameIsFullException(){
        super("Cannot join the game: game is full.");
    }
}
