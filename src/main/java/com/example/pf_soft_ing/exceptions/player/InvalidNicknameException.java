package com.example.pf_soft_ing.exceptions.player;

public class InvalidNicknameException extends Exception {
    public InvalidNicknameException() {
        super("Invalid nickname. Nickname must not be empty or contain only spaces.");
    }
}
