package com.example.pf_soft_ing.exceptions.cards;

public class NotEnoughCardsException extends Exception{
    public NotEnoughCardsException(){
        super("Cannot draw card: deck doesn't have enough cards.");
    }
}
