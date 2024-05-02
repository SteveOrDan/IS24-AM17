package com.example.pf_soft_ing.network.client;

public abstract class ClientSender {

    public abstract void startInputReading();

    public abstract void setClient(ClientRMIInterface client);

    public abstract void getMatches();
    public abstract void createMatch(int playerID, int numberOfPlayers, String nickname);
    public abstract void selectMatch(int playerID, int matchID);
    public abstract void chooseNickname(int playerID, String nickname);

    public abstract void placeCard(int playerID, int id, int side, int pos);
    public abstract void drawResourceCard(int playerID);
    public abstract void drawVisibleResourceCard(int playerID, int index);
    public abstract void drawGoldenCard(int playerID);
    public abstract void drawVisibleGoldenCard(int playerID, int index);
}
