package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.network.RMI.ServerRMI;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

public abstract class MessageEncoder {

    public void sendID(int id){}

    public void setState(PlayerState state){}

    public void setCurrScore(int score){}

    public void setToken(Token token){}

    public void setObjectivesToChoose(List<ObjectiveCard> objectives){}

    public void setFirstPlayerToken(Token token){}

    public void addCardToPlayerHand(PlaceableCard card){}

    public void setSecretObjective(ObjectiveCard card){}

    public void setStarterCard(PlaceableCard card){}

    public void placeStarterCard(boolean placed){}

    public void placeCard(boolean placed){}
}
