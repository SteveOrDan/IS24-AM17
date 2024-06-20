package com.example.pf_soft_ing.exceptions.player;

public class InvalidPlayerStateException extends Exception{
    public InvalidPlayerStateException(String currState, String expectedState){
        super("Invalid player state: " + currState + " => Expected state: " + expectedState + ".");
    }
}
