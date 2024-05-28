package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;

public interface ClientSender {

    void connect();
    void setPlayerID(int playerID);
    void getMatches();
    void createMatch(int numberOfPlayers, String nickname);
    void selectMatch(int matchID);
    void chooseNickname(String nickname);

    void placeStarterCard(CardSideType side);
    void chooseSecretObjective(int cardID);
    void placeCard(int cardID, CardSideType side, Position pos);
    void drawResourceCard();
    void drawVisibleResourceCard(int index);
    void drawGoldenCard();
    void drawVisibleGoldenCard(int index);

    void sendChatMessage(String recipient, String message);
    void sendPong();
}
