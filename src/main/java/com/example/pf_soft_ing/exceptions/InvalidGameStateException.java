package com.example.pf_soft_ing.exceptions;

public class InvalidGameStateException extends Exception{
    public InvalidGameStateException(String currState, String expectedState){
        super("Invalid game state: " + currState + " => Expected state: " + expectedState + ".");
    }
}
