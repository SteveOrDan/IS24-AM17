package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;

public interface ClientSender {

    void setPlayerID(int playerID);
    void getMatches();
    void createMatch(int numberOfPlayers, String nickname);
    void selectMatch(int matchID);
    void chooseNickname(String nickname);
    void reconnectToMatch(int matchID, String nickname);

    void placeStarterCard(int playerID, CardSideType side);
    void chooseSecretObjective(int playerID, int cardID);
    void placeCard(int playerID, int cardID, CardSideType side, Position pos);
    void drawResourceCard(int playerID);
    void drawVisibleResourceCard(int playerID, int index);
    void drawGoldenCard(int playerID);
    void drawVisibleGoldenCard(int playerID, int index);

    void sendChatMessage(int playerID, String recipient, String message);
    void sendPong(int playerID);
}
