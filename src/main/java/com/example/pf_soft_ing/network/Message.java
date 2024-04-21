package com.example.pf_soft_ing.network;

import java.io.Serializable;

public class Message implements Serializable {
    String header;
    String[] parameters;

    public Message(String header, String[] parameters) {
        this.header = header;
        this.parameters = parameters;
    }

    public String getHeader() {
        return header;
    }

    public String[] getParameters() {
        return parameters;
    }
}
