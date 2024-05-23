package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerViewModel {

    private int playerID;
    private String nickname;
    private TokenColors tokenColor;
    private int score = 0;

    private int priority = 0;

    private final List<PlaceableCard> playerHand = new ArrayList<>();
    private final Map<Position, PlaceableCard> playArea = new HashMap<>();

    public int getPlayerID() {
        return playerID;
    }

    public String getNickname() {
        return nickname;
    }

    public TokenColors getTokenColor() {
        return tokenColor;
    }

    public int getScore() {
        return score;
    }

    public Map<Position, PlaceableCard> getPlayArea() {
        return playArea;
    }

    public List<PlaceableCard> getPlayerHand() {
        return playerHand;
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

    public void placeCard(PlaceableCard card, Position pos) {
        card.setPriority(priority);
        priority++;
        playArea.put(pos, card);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void placeCardInPlayArea(int cardID, CardSideType side, Position pos, int deltaScore) {
        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);

        card.setCurrSideType(side);
        card.setPriority(priority);
        priority++;

        playArea.put(pos, card);

        score += deltaScore;

        playerHand.remove(card);
    }

    public void drawCard(int cardID) {
        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);
        playerHand.add(card);
    }
}
