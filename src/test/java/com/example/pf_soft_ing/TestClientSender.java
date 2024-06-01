package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.client.ClientSender;

public class TestClientSender implements ClientSender {
    @Override
    public void connect() {

    }

    @Override
    public void setPlayerID(int playerID) {

    }

    @Override
    public void getMatches() {

    }

    @Override
    public void createMatch(int numberOfPlayers, String nickname) {

    }

    @Override
    public void selectMatch(int matchID) {

    }

    @Override
    public void chooseNickname(String nickname) {

    }

    @Override
    public void reconnectToMatch(int matchID, String nickname) {

    }

    @Override
    public void placeStarterCard(int playerID, CardSideType side) {

    }

    @Override
    public void chooseSecretObjective(int playerID, int cardID) {

    }

    @Override
    public void placeCard(int playerID, int cardID, CardSideType side, Position pos) {

    }

    @Override
    public void drawResourceCard(int playerID) {

    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) {

    }

    @Override
    public void drawGoldenCard(int playerID) {

    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) {

    }

    @Override
    public void sendChatMessage(int playerID, String recipient, String message) {

    }

    @Override
    public void sendPong(int playerID) {

    }
}
