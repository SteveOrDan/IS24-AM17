package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

public class SocketEncoder extends MessageEncoder {

    private static PrintWriter out;
    public SocketEncoder(PrintWriter out, BufferedReader in){
        this.out = out;
    }

    private static void SendMessage(String output){
        new Thread(){
            public void run(){
                out.println(output);
            }
        }.start();
    }

    @Override
    public void sendID(int id){
        SendMessage(STR."0 \{id}");
    }

    @Override
    public void setState(PlayerState state){
        SendMessage(STR."1 \{state.ordinal()}");
        /*
            In the client, to invert use: PlayerState state = PlayerState.values()[ordinal];
         */
    }

    @Override
    public void setCurrScore(int score){
        SendMessage(STR."2 \{score}");
    }

    @Override
    public void setToken(Token token){
        SendMessage(STR."3 \{token.getColor()}");
    }

    @Override
    public void setObjectivesToChoose(List<ObjectiveCard> objectives){
        StringBuilder output = new StringBuilder("4");
        for (ObjectiveCard card : objectives){
            output.append(" ");
            output.append(card.getId());
        }
        SendMessage(output.toString());
    }

    @Override
    public void setFirstPlayerToken(Token token){
        String color;
        if (token == null){
            color = "null";
        } else {
            color = "black";
        }
        SendMessage(STR."5 \{color}");
    }

    @Override
    public void addCardToPlayerHand(PlaceableCard card){
        SendMessage(STR."6 \{card.getId()}");
    }

    @Override
    public void setSecretObjective(ObjectiveCard card){
        SendMessage(STR."7 \{card.getId()}");
    }

    @Override
    public void setStarterCard(PlaceableCard card){
        SendMessage(STR."8 \{card.getId()}");
    }

    @Override
    public void placeStarterCard(boolean placed){
        SendMessage(STR."9 \{placed}");
    }

    @Override
    public void placeCard(boolean placed){
        SendMessage(STR."10 \{placed}");
    }
}
