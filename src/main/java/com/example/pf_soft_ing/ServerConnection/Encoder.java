package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class Encoder {

    public void sendID(int id){}

    public void setState(PlayerState state){}

    public void setCurrScore(int score){}

    public void setToken(Token token){}

    public void setObjectivesToChoose(List<ObjectiveCard> objectives){
        List<Integer> objectiveIDs = new ArrayList<Integer>();
        for (ObjectiveCard card : objectives) {
            objectiveIDs.add(card.getId());
        }
        setObjectivesToChooseEncoded(objectiveIDs);
    }
    protected void setObjectivesToChooseEncoded(List<Integer> objectiveIDs){}

    public void setFirstPlayerToken(Token token){}

    public void addCardToPlayerHand(PlaceableCard card){
        addCardToPlayerHandEncoded(card.getId());
    }

    protected void addCardToPlayerHandEncoded(int id){}

    public void setSecretObjective(ObjectiveCard card){
        setSecretObjectiveEncoded(card.getId());
    }

    protected void setSecretObjectiveEncoded(int id){}

    public void setStarterCard(PlaceableCard card){
        setStarterCardEncoded(card.getId());
    }

    protected void setStarterCardEncoded(int id){}

    public void placeStarterCard(boolean placed){}

    public void placeCard(boolean placed){}

    public void requestError(){}
}
