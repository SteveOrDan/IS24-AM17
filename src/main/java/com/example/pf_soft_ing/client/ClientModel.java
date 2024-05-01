package com.example.pf_soft_ing.client;

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
    private int ID;

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

    private ClientSender clientSender;

    public ClientModel(ClientSender clientSender){
        this.clientSender = clientSender;
    }

//    public void setClient(ClientRMIInterface client) {
//        clientSender.setClient(client);
//    }
//
//    public void getMatches(int playerID) {
//        clientSender.getMatches(playerID);
//    }
//
//    public void createMatch(int playerID, int numberOfPlayers, String nickname) {
//        clientSender.createMatch(playerID, numberOfPlayers, nickname);
//    }
//
//    public void selectMatch(int playerID, int matchID) {
//        clientSender.selectMatch(playerID, matchID);
//    }
//
//    public void chooseNickname(int playerID, String nickname) {
//        clientSender.chooseNickname(playerID, nickname);
//    }
//
//    public void placeCard(int playerID, int id, int side, int pos) {
//        clientSender.placeCard(playerID, id, side, pos);
//    }
//
//    public void drawResourceCard(int playerID) {
//        clientSender.drawResourceCard(playerID);
//    }
//
//    public void drawVisibleResourceCard(int playerID, int index) {
//        clientSender.drawVisibleResourceCard(playerID, index);
//    }
//
//    public void drawGoldenCard(int playerID) {
//        clientSender.drawGoldenCard(playerID);
//    }
//
//    public void drawVisibleGoldenCard(int playerID, int index) {
//        clientSender.drawVisibleGoldenCard(playerID, index);
//    }
}
