package com.example.pf_soft_ing.network.client.socket;

import com.example.pf_soft_ing.network.client.ClientSender;
import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.network.messages.answers.PongMsg;
import com.example.pf_soft_ing.network.messages.requests.*;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSocketSender implements ClientSender {

    private final ObjectOutputStream out;

    public ClientSocketSender(ObjectOutputStream out){
        this.out = out;
    }

    /**
     * Sends a message to the server
     * @param output Message to send
     */
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
    }

    @Override
    public void connect() {
        getMatches();
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
    public void placeStarterCard(int playerID, CardSideType side) {
        sendMessage(new PlaceStarterCardMsg(playerID, side));
    }

    @Override
    public void chooseSecretObjective(int playerID, int cardID) {
        sendMessage(new ChooseSecretObjMsg(playerID, cardID));
    }

    @Override
    public void placeCard(int playerID, int cardID, CardSideType side, Position pos) {
        sendMessage(new PlaceCardMsg(playerID, cardID, side, pos));
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
    public void sendChatMessage(int playerID, String recipient, String message) {
        sendMessage(new ChatMessageMsg(playerID, recipient, message));
    }

    @Override
    public void sendPong(int playerID) {
        sendMessage(new PongMsg(playerID));
    }
}
