package com.example.pf_soft_ing.exceptions.match;

public class InvalidMatchIDException extends Exception{
    public InvalidMatchIDException() {
        super("Invalid match ID. Please try again.");
    }
}
