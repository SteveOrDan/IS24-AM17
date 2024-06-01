package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;

public class PingMsg extends Message {

    @Override
    public String toString() {
        return "Ping message from server";
    }
}
