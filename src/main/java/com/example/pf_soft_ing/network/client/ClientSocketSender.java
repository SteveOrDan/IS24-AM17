package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.network.messages.requests.*;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSocketSender implements ClientSender {

    private int playerID;
    private final ObjectOutputStream out;

    public ClientSocketSender(ObjectOutputStream out){
        this.out = out;
    }

    public void sendMessage(Message output){
        try {
            out.writeObject(output);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    @Override
    public void getMatches() {
        sendMessage(new GetMatchesMsg());
    }

    @Override
    public void createMatch(int numberOfPlayers, String nickname) {
        sendMessage(new CreateMatchMsg(numberOfPlayers, nickname));
    }

    @Override
    public void selectMatch(int matchID) {
        sendMessage(new SelectMatchMsg(matchID));
    }

    @Override
    public void chooseNickname(String nickname) {
        sendMessage(new ChooseNicknameMsg(nickname));
    }

    @Override
    public void reconnectToMatch(int matchID, String nickname) {
        sendMessage(new ReconnectToMatchMsg(matchID, nickname));
    }

    @Override
    public void placeStarterCard(CardSideType side) {
        sendMessage(new PlaceStarterCardMsg(side));
    }

    @Override
    public void chooseSecretObjective(int cardID) {
        sendMessage(new ChooseSecretObjMsg(cardID));
    }

    @Override
    public void placeCard(int cardID, CardSideType side, Position pos) {
        sendMessage(new PlaceCardMsg(cardID, side, pos));
    }

    @Override
    public void drawResourceCard(int playerID) {
        sendMessage(new DrawCardMsg(playerID, false, false, -1));
    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) {
        sendMessage(new DrawCardMsg(playerID, false, true, index));
    }

    @Override
    public void drawGoldenCard(int playerID) {
        sendMessage(new DrawCardMsg(playerID, true, false, -1));
    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) {
        sendMessage(new DrawCardMsg(playerID, true, true, index));
    }

    @Override
    public void sendChatMessage(String recipient, String message) {
        sendMessage(new ChatMessageMsg(recipient, message));
    }

    @Override
    public void sendPong() {
        sendMessage(new PongMsg(this.playerID));
    }
}
