package com.example.pf_soft_ing.network.server.socket;

import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.mvc.model.game.GameState;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.network.messages.answers.*;
import com.example.pf_soft_ing.network.messages.requests.PingMsg;
import com.example.pf_soft_ing.mvc.model.player.TokenColors;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class SocketSender implements Sender {

    private final ObjectOutputStream out;

    public SocketSender(ObjectOutputStream out){
        this.out = out;
    }

    /**
     * Sends a message to the client
     * @param output Message to send
     */
    protected void sendMessage(Message output){
        try {
            out.writeObject(output);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sendError(String errorMsg){
        sendMessage(new ErrorMessage(errorMsg));
    }

    @Override
    public void sendMatches(Map<Integer, List<String>> matches, int playerID) {
        sendMessage(new ReturnMatchesMsg(matches, playerID));
    }

    @Override
    public void createMatchResult(int matchID, String hostNickname) {
        sendMessage(new MatchCreatedMsg(matchID, hostNickname));
    }

    @Override
    public void selectMatchResult(int matchID, List<String> nicknames) {
        sendMessage(new SelectMatchResultMsg(matchID, nicknames));
    }

    @Override
    public void chooseNicknameResult(String nickname) {
        sendMessage(new ChosenNicknameMsg(nickname));
    }

    @Override
    public void sendGameStart(Map<Integer, String> IDtoOpponentNickname,
                              int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                              int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                              int starterCardID) {
        sendMessage(new GameStartMsg(IDtoOpponentNickname,
                resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2,
                starterCardID));
    }

    @Override
    public void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                                 int commonObjectiveCardID1, int commonObjectiveCardID2,
                                 int secretObjectiveCardID1, int secretObjectiveCardID2) {
        sendMessage(new MissingSetupMsg(resourceCardID1, resourceCardID2, goldenCardID, tokenColor,
                commonObjectiveCardID1, commonObjectiveCardID2,
                secretObjectiveCardID1, secretObjectiveCardID2));
    }

    @Override
    public void confirmSecretObjective() {
        sendMessage(new ConfirmSecretObjectiveMsg());
    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos, CardSideType chosenSide, int score){
        sendMessage(new ConfirmPlaceCardMsg(playerID, cardID, pos, chosenSide, score));
    }

    @Override
    public void sendFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs,
                                    CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                                    int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                    int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        sendMessage(new FirstPlayerTurnMsg(lastPlayerID, playerID, playerIDs, starterCardIDs, starterCardSides, tokenColors, playerHands,
                resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2));
    }

    @Override
    public void sendFirstPlayerTurn(int playerID, int[] playerIDs, int[] starterCardIDs, CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands, int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        sendMessage(new FirstPlayerTurnAfterRecMsg(playerID, playerIDs, starterCardIDs, starterCardSides, tokenColors, playerHands,
                resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2));
    }

    @Override
    public void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                                      int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                      int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {

        sendMessage(new NewPlayerTurnMsg(drawnCardID, lastPlayerID, newPlayerID,
                resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2));
    }

    @Override
    public void sendNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side,
                                       int newPlayerID, int score) {
        sendMessage(new NewPlayerExtraTurnMsg(cardID, lastPlayerID, pos, side,
                newPlayerID, score));
    }

    @Override
    public void sendNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID,
                                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                          GameState gameState) {
        sendMessage(new NewPlayerTurnNewStateMsg(drawnCardID, lastPlayerID, newPlayerID,
                resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2,
                gameState));
    }

    @Override
    public void sendChatMessage(String sender, String recipient, String message) {
        sendMessage(new ReceiveChatMessageMsg(sender, recipient, message));
    }

    @Override
    public void sendRanking(int lastPlayerID, int cardID, Position pos, CardSideType side , int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives) {
        sendMessage(new RankingMsg(lastPlayerID, cardID, pos, side, deltaScore, nicknames, scores, numOfSecretObjectives));
    }

    @Override
    public void sendPlayerDisconnection(int playerID) {
        sendMessage(new PlayerDisconnectionMsg(playerID));
    }

    @Override
    public void sendPlayerDisconnectionWithOnePlayerLeft(int playerID) {
        sendMessage(new PlayerDisconnectionWithOnePlayerLeftMsg(playerID));
    }

    @Override
    public void sendReOnStarterPlacement(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards) {
        sendMessage(new ReOnStarterPlacementMsg(playerID, IDToNicknameMap,gameSetupCards));
    }

    @Override
    public void sendReOnObjectiveChoice(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {
        sendMessage(new ReOnObjectiveChoiceMsg(playerID, IDToNicknameMap, gameSetupCards, starterSide, tokenColor));
    }

    @Override
    public void sendReAfterSetup(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {
        sendMessage(new ReAfterSetupMsg(playerID, IDToNicknameMap, gameSetupCards, starterSide, tokenColor));
    }

    @Override
    public void sendNormalReconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands,
                                    List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                                    int[] playersScores, int[] gameSetupCards, int currPlayerID) {
        sendMessage(new NormalReconnectMsg(playerID, playersIDs, playersNicknames, playersTokenColors, playersHands,
                playersPlacedCardsPos, playersPlacedCardsIDs, playersPlacedCardsSides, playersPlacedCardsPriorities,
                playersScores, gameSetupCards, currPlayerID));
    }

    @Override
    public void sendUndoCardPlacement(int playerID, Position pos, int score, int nextPlayerID) {
        sendMessage(new UndoCardPlacementMsg(playerID, pos, score, nextPlayerID));
    }

    @Override
    public void sendUndoPlaceWithOnePlayerLeft(int playerID, Position pos, int score) {
        sendMessage(new UndoPlaceWithOnePlayerLeftMsg(playerID, pos, score));
    }

    @Override
    public void sendPlayerReconnection(int playerID) {
        sendMessage(new PlayerReconnectionMsg(playerID));
    }

    @Override
    public void sendSoleWinnerMessage() {
        sendMessage(new SoleWinnerMsg());
    }

    @Override
    public void sendPing() {
        sendMessage(new PingMsg());
    }
}
