package com.example.pf_soft_ing.exceptions;

public class InvalidMatchIDException extends Exception{
    public InvalidMatchIDException() {
        super("Invalid match ID. Please try again.");
    }
}
