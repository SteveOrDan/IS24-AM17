package com.example.pf_soft_ing.exceptions;

public class GameFullException extends Exception{
    public GameFullException(){
        super("Cannot join the game: game is full.");
    }
}
