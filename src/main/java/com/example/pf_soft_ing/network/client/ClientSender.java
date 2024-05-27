package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;

public interface ClientSender {

    void setPlayerID(int playerID);
    void getMatches();
    void createMatch(int numberOfPlayers, String nickname);
    void selectMatch(int matchID);
    void chooseNickname(String nickname);

    void placeStarterCard(CardSideType side);
    void chooseSecretObjective(int cardID);
    void placeCard(int cardID, CardSideType side, Position pos);
    void drawResourceCard(int playerID);
    void drawVisibleResourceCard(int playerID, int index);
    void drawGoldenCard(int playerID);
    void drawVisibleGoldenCard(int playerID, int index);

    void sendChatMessage(String recipient, String message);
    void sendPing();
}
