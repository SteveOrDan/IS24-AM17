package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.network.messages.answers.*;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;
import com.example.pf_soft_ing.player.TokenColors;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class SocketSender extends Sender {

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
     * Send the ID to the Player
     */
    @Override
    public void sendID(int id){
//        SendMessage(STR."20 \{id}");
    }

    /**
     * Sets the state for the Player
     */
    @Override
    public void setState(PlayerState state){
//        SendMessage(STR."21 \{state.ordinal()}");
        /*
            In the client, to invert use: PlayerState state = PlayerState.values()[ordinal];
         */
    }

    /**
     * Sets the score for the Player
     */
    @Override
    public void setCurrScore(int score){
//        SendMessage(STR."22 \{score}");
    }

    /**
     * Sets the token for the Player
     */
    @Override
    public void setToken(Token token){
//        SendMessage(STR."23 \{token.getColor()}");
    }

    @Override
    protected void setTokenEncoded(TokenColors color) {

    }

    /**
     * Sets the list of ObjectiveCard to choose for the Player
     */
    @Override
    protected void setObjectivesToChooseEncoded(List<Integer> objectiveIDs){
//        StringBuilder output = new StringBuilder("24");
//
//        for (Integer i : objectiveIDs){
//            output.append(" ");
//            output.append(i);
//        }
//        SendMessage(output.toString());
    }

    /**
     * Sets the first player's token
     */
    @Override
    public void setFirstPlayerToken(Token token){
//        String color;
//
//        if (token == null){
//            color = "null";
//        }
//        else {
//            color = "black";
//        }
//        SendMessage(STR."5 \{color}");
    }

    @Override
    protected void setFirstPlayerTokenEncoded(TokenColors color) {

    }

    /**
     * Method to add a cardID to Player hand
     * @param id CardID to add
     */
    @Override
    protected void addCardToPlayerHandEncoded(int id){
//        SendMessage(STR."6 \{id}");
    }

    /**
     * Sets the id of the secret objective card for the Player
     */
    @Override
    protected void setSecretObjectiveEncoded(int id){
//        SendMessage(STR."7 \{id}");
    }

    /**
     * Sets the starter cardID for the Player
     */
    @Override
    protected void setStarterCardEncoded(int id){
//        SendMessage(STR."8 \{id}");
    }

    /**
     * Method to place starter card
     * @param placed boolean
     */
    @Override
    public void placeStarterCard(boolean placed){
//        SendMessage(STR."9 \{placed}");
    }

    /**
     * Method to place a card
     * @param placed boolean
     */
    @Override
    public void placeCard(boolean placed){
//        SendMessage(STR."10 \{placed}");
    }

    /**
     * Method to requestError to the Player
     */
    @Override
    public void sendError(String errorMsg){
        sendMessage(new ErrorMessage(errorMsg));
    }

    @Override
    public void sendMatches(Map<Integer, List<String>> matches) {
        sendMessage(new ReturnMatchesMsg(matches));
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
}
