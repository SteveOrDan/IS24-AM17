package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.List;
import java.util.Map;

public class TestSender implements Sender {
    @Override
    public void sendError(String errorMsg) {

    }

    @Override
    public void sendMatches(Map<Integer, List<String>> matches, int playerID) {

    }

    @Override
    public void createMatchResult(int matchID, String hostNickname) {

    }

    @Override
    public void selectMatchResult(int matchID, List<String> nicknames) {

    }

    @Override
    public void chooseNicknameResult(String nickname) {

    }

    @Override
    public void sendGameStart(String nickname, Map<Integer, String> IDtoOpponentNickname, int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, int starterCardID) {

    }

    @Override
    public void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2, int secretObjectiveCardID1, int secretObjectiveCardID2) {

    }

    @Override
    public void confirmSecretObjective() {

    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos, CardSideType chosenSide, int score) {

    }

    @Override
    public void sendFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs,
                                    CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                                    int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                    int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {

    }

    @Override
    public void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID, int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {

    }

    @Override
    public void sendNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int score) {

    }

    @Override
    public void sendNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID, int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, GameState gameState) {

    }

    @Override
    public void sendChatMessage(String sender, String recipient, String message) {

    }

    @Override
    public void sendRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives) {

    }

    @Override
    public void sendPlayerDisconnection(int playerID) {

    }

    @Override
    public void sendPlayerDisconnectionWithOnePlayerLeft(int playerID) {

    }

    @Override
    public void sendReOnStarterPlacement(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards) {

    }

    @Override
    public void sendReOnObjectiveChoice(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {

    }

    @Override
    public void sendReAfterSetup(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {

    }

    @Override
    public void sendNormalReconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands, List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities, int[] playersScores, int[] gameSetupCards, int currPlayerID) {

    }

    @Override
    public void sendUndoCardPlacement(int playerID, Position pos, int score, int nextPlayerID) {

    }

    @Override
    public void sendUndoPlaceWithOnePlayerLeft(int playerID, Position pos, int score) {

    }

    @Override
    public void sendPlayerReconnection(int playerID) {

    }

    @Override
    public void sendSoleWinnerMessage() {

    }

    @Override
    public void sendPing() {

    }
}
