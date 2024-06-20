package com.example.pf_soft_ing.exceptions.cards;

public class InvalidIndexException extends Exception{
    public InvalidIndexException(){
        super("Invalid index: index is out of bounds, please insert either '0' or '1'.");
    }
}
