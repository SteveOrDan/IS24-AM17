package com.example.pf_soft_ing.exceptions.match;

public class InvalidNumOfPlayersException extends Exception{
    public InvalidNumOfPlayersException() {
        super("Invalid number of players. Must be between 2 and 4.");
    }
}
