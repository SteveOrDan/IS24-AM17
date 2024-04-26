package com.example.pf_soft_ing.client;

import java.io.PrintWriter;

public class ClientSocketSender extends ClientEncoder{

    private static PrintWriter out;

    public ClientSocketSender(PrintWriter out){
        ClientSocketSender.out = out;
    }

    protected static void sendMessage(String output){
        out.println(output);
    }

    @Override
    public void getMatches() {
        sendMessage("0");
    }

    @Override
    public void sendMatch(int matchID) {
        sendMessage(STR."1 \{matchID}");
    }

    @Override
    public void createMatch(int numberOfPlayers) {
        sendMessage(STR."2 \{numberOfPlayers}");
    }

    @Override
    public void sendNickname(String nickname) {
        sendMessage(STR."3 \{nickname}");
    }
}
