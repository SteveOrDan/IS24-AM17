package com.example.pf_soft_ing.exceptions;

public class NoAdjacentCardsException extends Exception {
    public NoAdjacentCardsException(){
        super("Invalid card position: card has no adjacent cards.");
    }
}
