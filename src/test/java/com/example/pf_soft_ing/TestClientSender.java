package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.client.ClientSender;

public class TestClientSender implements ClientSender {
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
    public void placeStarterCard(CardSideType side) {

    }

    @Override
    public void chooseSecretObjective(int cardID) {

    }

    @Override
    public void placeCard(int cardID, CardSideType side, Position pos) {

    }

    @Override
    public void drawResourceCard() {

    }

    @Override
    public void drawVisibleResourceCard(int index) {

    }

    @Override
    public void drawGoldenCard() {

    }

    @Override
    public void drawVisibleGoldenCard(int index) {

    }

    @Override
    public void sendChatMessage(String recipient, String message) {

    }

    @Override
    public void sendPong() {

    }
}
