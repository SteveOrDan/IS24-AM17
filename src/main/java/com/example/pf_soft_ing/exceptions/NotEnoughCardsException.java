package com.example.pf_soft_ing.exceptions;

public class NotEnoughCardsException extends Exception{
    public NotEnoughCardsException(){
        super("Cannot draw card: deck has not enough cards.");
    }
}
