package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;

import java.io.PrintWriter;
import java.util.List;

public class SocketSender extends Encoder {

    private static PrintWriter out;
    public SocketSender(PrintWriter out){
        this.out = out;
    }

    protected static void SendMessage(String output){
        new Thread(){
            public void run(){
                out.println(output);
            }
        }.start();
    }

    @Override
    public void sendID(int id){
        SendMessage(STR."20 \{id}");
    }

    @Override
    public void setState(PlayerState state){
        SendMessage(STR."21 \{state.ordinal()}");
        /*
            In the client, to invert use: PlayerState state = PlayerState.values()[ordinal];
         */
    }

    @Override
    public void setCurrScore(int score){
        SendMessage(STR."22 \{score}");
    }

    @Override
    public void setToken(Token token){
        SendMessage(STR."23 \{token.getColor()}");
    }

    @Override
    protected void setObjectivesToChooseEncoded(List<Integer> objectiveIDs){
        StringBuilder output = new StringBuilder("24");

        for (Integer i : objectiveIDs){
            output.append(" ");
            output.append(i);
        }
        SendMessage(output.toString());
    }

    @Override
    public void setFirstPlayerToken(Token token){
        String color;

        if (token == null){
            color = "null";
        }
        else {
            color = "black";
        }
        SendMessage(STR."5 \{color}");
    }

    @Override
    protected void addCardToPlayerHandEncoded(int id){
        SendMessage(STR."6 \{id}");
    }

    @Override
    protected void setSecretObjectiveEncoded(int id){
        SendMessage(STR."7 \{id}");
    }

    @Override
    protected void setStarterCardEncoded(int id){
        SendMessage(STR."8 \{id}");
    }

    @Override
    public void placeStarterCard(boolean placed){
        SendMessage(STR."9 \{placed}");
    }

    @Override
    public void placeCard(boolean placed){
        SendMessage(STR."10 \{placed}");
    }

    @Override
    public void requestError(){
        SendMessage("11 requestError");
    }
}
