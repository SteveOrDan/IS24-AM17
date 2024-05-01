package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.ArrayList;
import java.util.List;

public abstract class Sender {

    /**
     * Send the ID to the Player
     */
    public abstract void sendID(int id);

    /**
     * Sets the state for the Player
     */
    public abstract void setState(PlayerState state);

    /**
     * Sets the score for the Player
     */
    public abstract void setCurrScore(int score);

    /**
     * Sets the token for the Player
     */
    public void setToken(Token token){
        TokenColors color = token.getColor();
        setTokenEncoded(color);
    }

    /**
     * Sets the token's color for the Player
     */
    protected abstract void setTokenEncoded(TokenColors color);


    /**
     * Sets the list of ObjectiveCard to choose for the Player
     */
    public void setObjectivesToChoose(List<ObjectiveCard> objectives){
        List<Integer> objectiveIDs = new ArrayList<>();
        for (ObjectiveCard card : objectives) {
            objectiveIDs.add(card.getId());
        }
        setObjectivesToChooseEncoded(objectiveIDs);
    }

    /**
     * Sets the list of IDs of objectives to choose for the Player
     */
    protected abstract void setObjectivesToChooseEncoded(List<Integer> objectiveIDs);

    /**
     * Sets the first player's token
     */
    public void setFirstPlayerToken(Token token){
        TokenColors color = token.getColor();
        setFirstPlayerTokenEncoded(color);
    }

    /**
     * Sets the color of the token for the first Player
     */
    protected abstract void setFirstPlayerTokenEncoded(TokenColors color);

    /**
     * Method to add a card to Player hand
     * @param card Card to add
     */
    public void addCardToPlayerHand(PlaceableCard card){
        addCardToPlayerHandEncoded(card.getID());
    }

    /**
     * Method to add a cardID to Player hand
     * @param id CardID to add
     */
    protected abstract void addCardToPlayerHandEncoded(int id);

    /**
     * Sets the secret objective card for the Player
     */
    public void setSecretObjective(ObjectiveCard card){
        setSecretObjectiveEncoded(card.getId());
    }

    /**
     * Sets the id of the secret objective card for the Player
     */
    protected abstract void setSecretObjectiveEncoded(int id);

    /**
     * Sets the starter card for the Player
     */
    public void setStarterCard(PlaceableCard card){
        setStarterCardEncoded(card.getID());
    }

    /**
     * Sets the starter cardID for the Player
     */
    protected abstract void setStarterCardEncoded(int id);

    /**
     * Method to place starter card
     * @param placed boolean
     */
    public abstract void placeStarterCard(boolean placed);

    /**
     * Method to place a card
     * @param placed boolean
     */
    public abstract void placeCard(boolean placed);

    /**
     * Method to requestError to the Player
     */
    public abstract void sendError(String errorMsg);

    /**
     * Method to return all the matches
     */
    public abstract void sendMatches(List<MatchController> matches);

    public abstract void createMatchResult(MatchController match);

    public abstract void selectMatchResult(MatchController match);

    public abstract void chooseNicknameResult();
}
