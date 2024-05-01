package com.example.pf_soft_ing.exceptions;

public class InvalidNumOfPlayers extends Exception{
    public InvalidNumOfPlayers() {
        super("Invalid number of players. Must be between 2 and 4.");
    }
}
