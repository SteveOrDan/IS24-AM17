package com.example.pf_soft_ing.mvc.model.player;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.mvc.model.game.GameResources;

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

    /**
     * Getter
     * @return ID of the player
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Getter
     * @return Nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter
     * @return Token color of the player
     */
    public TokenColors getTokenColor() {
        return tokenColor;
    }

    /**
     * Getter
     * @return Score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter
     * @return Play area of the player
     */
    public Map<Position, PlaceableCard> getPlayArea() {
        return playArea;
    }

    /**
     * Getter
     * @return Hand of the player
     */
    public List<PlaceableCard> getPlayerHand() {
        return playerHand;
    }

    /**
     * Setter
     * @param playerID ID of the player
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Setter
     * @param nickname Nickname of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Setter
     * @param tokenColor Token color of the player
     */
    public void setTokenColor(TokenColors tokenColor) {
        this.tokenColor = tokenColor;
    }

    /**
     * Setter
     * @param score Score of the player
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Places a card in the play area, increases the priority, sets the score and removes the card from the hand
     * @param cardID ID of the card to place
     * @param side Side of the card to place
     * @param pos Position to place the card
     * @param deltaScore Score to add
     */
    public void placeCardInPlayArea(int cardID, CardSideType side, Position pos, int deltaScore) {
        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);

        card.setCurrSideType(side);
        card.setPriority(priority);
        priority++;

        playArea.put(pos, card);

        score += deltaScore;

        playerHand.remove(card);
    }

    /**
     * Adds a card to the play area
     * @param card Card to add
     * @param pos Position to add the card
     */
    public void addCardToPlayArea(PlaceableCard card, Position pos) {
        playArea.put(pos, card);
        priority++;
    }

    /**
     * Adds a card to the player's hand
     * @param cardID ID of the card to add
     */
    public void drawCard(int cardID) {
        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);
        playerHand.add(card);
    }

    /**
     * Moves a card from the play area to the player's hand and changes the score
     * @param pos Position of the card to remove
     * @param score Score to set
     */
    public void removeCard(Position pos, int score) {
        PlaceableCard card = playArea.remove(pos);
        priority--;

        this.score = score;

        card.setPriority(0);
        card.setCurrSideType(CardSideType.FRONT);
        playerHand.add(card);
    }
}
