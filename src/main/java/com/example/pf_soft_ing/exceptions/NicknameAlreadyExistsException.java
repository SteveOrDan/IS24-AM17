package com.example.pf_soft_ing.exceptions;

public class NicknameAlreadyExistsException extends Exception{
    public NicknameAlreadyExistsException(){
        super("Invalid nickname: nickname already exists. Please try again.");
    }
}
