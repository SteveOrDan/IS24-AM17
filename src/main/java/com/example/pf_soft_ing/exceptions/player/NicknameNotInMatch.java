package com.example.pf_soft_ing.exceptions.player;

public class NicknameNotInMatch extends Exception{
    public NicknameNotInMatch() {
        super("The chosen nickname is not present in the match.");
    }
}
