package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.network.messages.answers.*;
import com.example.pf_soft_ing.player.TokenColors;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class SocketSender implements Sender {

    private final ObjectOutputStream out;

    public SocketSender(ObjectOutputStream out){
        this.out = out;
    }

    protected void sendMessage(Message output){
        new Thread(() -> {
            try {
                out.writeObject(output);
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    /**
     * Method to requestError to the Player
     */
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
    public void sendGameStart(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                              int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                              int starterCardID) {
        sendMessage(new GameStartMsg(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, starterCardID));
    }

    @Override
    public void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2, int secretObjectiveCardID1, int secretObjectiveCardID2) {
        sendMessage(new MissingSetupMsg(resourceCardID1, resourceCardID2, goldenCardID, tokenColor, commonObjectiveCardID1, commonObjectiveCardID2, secretObjectiveCardID1, secretObjectiveCardID2));
    }

    @Override
    public void confirmSecretObjective() {
        sendMessage(new ConfirmSecretObjectiveMsg());
    }

    @Override
    public void placeCard(){
        sendMessage(new ConfirmPlaceCardMsg());
    }

    @Override
    public void sendFirstPlayerTurn(int playerID, String playerNickname, Map<Integer, String> IDtoOpponentNickname, Map<Integer, Map<Position, Integer>>IDtoOpponentPlayArea) {
        sendMessage(new FirstPlayerTurnMsg(playerID, playerNickname, IDtoOpponentNickname, IDtoOpponentPlayArea));
    }

    @Override
    public void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID, String playerNickname,
                                      int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                      int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {

        sendMessage(new NewPlayerTurnMsg(drawnCardID, lastPlayerID, newPlayerID, playerNickname,
                resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2));
    }

    @Override
    public void opponentPlaceCard(int playerID, int cardID, Position pos, CardSideType chosenSide) {
        sendMessage(new OpponentPlaceCardMsg(playerID, cardID, pos, chosenSide));
    }
}
