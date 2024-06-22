package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.mvc.model.player.TokenColors;

import java.util.List;

public class NormalReconnectMsg extends Message {

    private final int playerID;
    private final int[] playersIDs;
    private final String[] playersNicknames;
    private final TokenColors[] playersTokenColors;
    private final int[][] playersHands;
    private final List<Position[]> playersPlacedCardsPos;
    private final List<int[]> playersPlacedCardsIDs;
    private final List<CardSideType[]> playersPlacedCardsSides;
    private final List<int[]> playersPlacedCardsPriorities;
    private final int[] playersScores;
    private final int[] gameSetupCards;
    private final int currPlayerID;

    public NormalReconnectMsg(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands,
                              List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                              int[] playersScores, int[] gameSetupCards, int currPlayerID) {
        this.playerID = playerID;
        this.playersIDs = playersIDs;
        this.playersNicknames = playersNicknames;
        this.playersTokenColors = playersTokenColors;
        this.playersHands = playersHands;
        this.playersPlacedCardsPos = playersPlacedCardsPos;
        this.playersPlacedCardsIDs = playersPlacedCardsIDs;
        this.playersPlacedCardsSides = playersPlacedCardsSides;
        this.playersPlacedCardsPriorities = playersPlacedCardsPriorities;
        this.playersScores = playersScores;
        this.gameSetupCards = gameSetupCards;
        this.currPlayerID = currPlayerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int[] getPlayersIDs() {
        return playersIDs;
    }

    public String[] getPlayersNicknames() {
        return playersNicknames;
    }

    public TokenColors[] getPlayersTokenColors() {
        return playersTokenColors;
    }

    public int[][] getPlayersHands() {
        return playersHands;
    }

    public List<Position[]> getPlayersPlacedCardsPos() {
        return playersPlacedCardsPos;
    }

    public List<int[]> getPlayersPlacedCardsIDs() {
        return playersPlacedCardsIDs;
    }

    public List<CardSideType[]> getPlayersPlacedCardsSides() {
        return playersPlacedCardsSides;
    }

    public List<int[]> getPlayersPlacedCardsPriorities() {
        return playersPlacedCardsPriorities;
    }

    public int[] getPlayersScores() {
        return playersScores;
    }

    public int[] getGameSetupCards() {
        return gameSetupCards;
    }

    public int getCurrPlayerID() {
        return currPlayerID;
    }

    @Override
    public String toString() {
        return "Gigantic reconnection message with all the data you need to reconnect to a match.";
    }
}
