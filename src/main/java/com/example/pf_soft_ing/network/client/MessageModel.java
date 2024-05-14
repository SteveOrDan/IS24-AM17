package com.example.pf_soft_ing.network.client;

public class MessageModel {

    private final String senderNickname;

    private final String messageContents;

    public MessageModel(String senderNickname, String messageContents) {
        this.senderNickname = senderNickname;
        this.messageContents = messageContents;
    }

    public String printMessage(){
        return (senderNickname + ": " + messageContents);
    }
}
