package com.example.pf_soft_ing.card;

public abstract class Card {
    public int id;

    public Card(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}