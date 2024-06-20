package com.example.pf_soft_ing.exceptions.cards;

public class InvalidVisibleCardIndexException extends Exception{
    public InvalidVisibleCardIndexException(int index){
        super("Invalid visible card index: " + index + ". Index must be either 1 or 0.");
    }
}
