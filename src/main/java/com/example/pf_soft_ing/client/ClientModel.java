package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.Encoder;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientModel {

    private String nickname;
    private int id;

    private final List<PlaceableCard> hand = new ArrayList<>();

    private List<ObjectiveCard> objectivesToChoose;
    private ObjectiveCard secretObjective;

    private PlaceableCard starterCard;

    private final HashMap<Position, PlaceableCard> playArea = new HashMap<>();
    private final int[] numOfResourcesArr = new int[7];

    private int currScore = 0;
    private int numOfCompletedObjectives = 0;

    private Token token;
    private Token firstPlayerToken;

    private PlayerState state = PlayerState.PRE_GAME;

    private int currMaxPriority = 0;

    private ClientEncoder clientEncoder;

    public ClientModel(ClientEncoder clientEncoder){
        this.clientEncoder = clientEncoder;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void getMatches(){
        clientEncoder.getMatches();
    }

    public void sendMatch(int matchID){
        clientEncoder.sendMatch(matchID);
    }

    public void sendNickname(String nickname){
        clientEncoder.sendNickname(nickname);
    }

    public void createMatch(int numberOfPlayers){
        clientEncoder.createMatch(numberOfPlayers);
    }
}
