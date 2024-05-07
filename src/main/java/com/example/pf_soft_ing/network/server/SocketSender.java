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
}
