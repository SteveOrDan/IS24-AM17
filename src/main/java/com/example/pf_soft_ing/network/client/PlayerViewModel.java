package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerViewModel {

    private int matchID;

    private int playerID;
    private String nickname;
    private TokenColors tokenColor;



    private final Map<Position, PlaceableCard> playArea = new HashMap<>();

    public int getMatchID() {
        return matchID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getNickname() {
        return nickname;
    }

    public TokenColors getTokenColor() {
        return tokenColor;
    }

    public Map<Position, PlaceableCard> getPlayArea() {
        return playArea;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTokenColor(TokenColors tokenColor) {
        this.tokenColor = tokenColor;
    }
}
