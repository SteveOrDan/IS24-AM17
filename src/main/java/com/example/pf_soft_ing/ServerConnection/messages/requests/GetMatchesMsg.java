package com.example.pf_soft_ing.ServerConnection.messages.requests;


import com.example.pf_soft_ing.ServerConnection.messages.Message;

public class GetMatchesMsg extends Message {

    @Override
    public String toString() {
        return "Matches list requested";
    }
}
