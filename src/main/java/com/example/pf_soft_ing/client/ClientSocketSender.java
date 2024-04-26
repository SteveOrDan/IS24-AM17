package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;

import java.io.PrintWriter;
import java.util.List;

public class ClientSocketSender extends ClientEncoder{

    private static PrintWriter out;
    public ClientSocketSender(PrintWriter out){
        this.out = out;
    }

    protected static void SendMessage(String output){
        out.println(output);
    }

    @Override
    public void getMatches() {
        SendMessage("0");
    }

    @Override
    public void sendMatch(int matchID) {
        SendMessage(STR."1 \{matchID}");
    }

    @Override
    public void createMatch(int numberOfPlayers) {
        SendMessage(STR."2 \{numberOfPlayers}");
    }

    @Override
    public void sendNickname(String nickname) {
        SendMessage(STR."3 \{nickname}");
    }
}
