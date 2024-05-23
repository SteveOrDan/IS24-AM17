package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.network.messages.*;
import com.example.pf_soft_ing.network.messages.answers.*;
import com.example.pf_soft_ing.network.messages.requests.CloseConnectionMsg;
import com.example.pf_soft_ing.network.messages.requests.PingMsg;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientSocketReceiver {

    private final View view;

    public ClientSocketReceiver(View view) {
        this.view = view;
    }

    /**
     * Starts a new thread that listens for incoming messages from the server
     * until it receives a CloseConnectionMsg
     * @param inputStream ObjectInputStream
     */
    public void startReceivingThread(ObjectInputStream inputStream){
        new Thread(){
            public void run(){
                boolean isRunning = true;

                while (isRunning) {
                    Message input;

                    try {
                        input = (Message) inputStream.readObject();

                        if (input instanceof CloseConnectionMsg) {
                            isRunning = false;
                        }

                        decodeMessage(input);
                    }
                    catch (IOException | ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                        isRunning = false;
                    }
                }
                interrupt();
            }
        }.start();
    }

    private void decodeMessage(Message message){
        switch (message) {

            case ReturnMatchesMsg castedMsg -> {
                view.setID(castedMsg.getPlayerID());
                view.showMatches(castedMsg.getMatches());
            }

            case MatchCreatedMsg castedMsg -> view.createMatch(castedMsg.getMatchID(), castedMsg.getHostNickname());

            case SelectMatchResultMsg castedMsg -> view.selectMatch(castedMsg.getMatchID(), castedMsg.getNicknames());

            case ChosenNicknameMsg castedMsg -> view.chooseNickname(castedMsg.getNickname());

            case ErrorMessage castedMsg -> view.errorMessage(castedMsg.getMessage());

            case GameStartMsg castedMsg -> view.startGame(castedMsg.getIDToNicknameMap(),
                    castedMsg.getResDeckCardID(), castedMsg.getVisibleResCardID1(), castedMsg.getVisibleResCardID2(),
                    castedMsg.getGoldDeckCardID(), castedMsg.getVisibleGoldCardID1(), castedMsg.getVisibleGoldCardID2(),
                    castedMsg.getStarterCardID()
            );

            case MissingSetupMsg castedMsg -> view.setMissingSetUp(
                    castedMsg.getResourceCardID1(), castedMsg.getResourceCardID2(), castedMsg.getGoldenCardID(),
                    castedMsg.getTokenColor(), castedMsg.getCommonObjectiveCardID1(), castedMsg.getCommonObjectiveCardID2(),
                    castedMsg.getSecretObjectiveCardID1(), castedMsg.getSecretObjectiveCardID2()
            );

            case ConfirmSecretObjectiveMsg ignored -> view.confirmSecretObjective();

            case FirstPlayerTurnMsg castedMsg -> {
                view.updateDrawArea(
                        castedMsg.getResDeckCardID(), castedMsg.getVisibleResCardID1(), castedMsg.getVisibleResCardID2(),
                        castedMsg.getGoldDeckCardID(), castedMsg.getVisibleGoldCardID1(), castedMsg.getVisibleGoldCardID2()
                );
                view.showFirstPlayerTurn(castedMsg.getLastPlayerID(), castedMsg.getPlayerID(),
                        castedMsg.getPlayerIDs(), castedMsg.getStarterCardIDs(), castedMsg.getStarterCardSides(),
                        castedMsg.getTokenColors(), castedMsg.getPlayerHands());
            }

            case ConfirmPlaceCardMsg castedMsg -> view.placeCard(castedMsg.getPlayerID(), castedMsg.getCardID(), castedMsg.getPos(), castedMsg.getSide(), castedMsg.getScore()) ;

            case NewPlayerTurnMsg castedMsg -> {
                view.updateDrawArea(
                        castedMsg.getResDeckCardID(), castedMsg.getVisibleResCardID1(), castedMsg.getVisibleResCardID2(),
                        castedMsg.getGoldDeckCardID(), castedMsg.getVisibleGoldCardID1(), castedMsg.getVisibleGoldCardID2());
                view.showNewPlayerTurn(castedMsg.getDrawnCardID(), castedMsg.getLastPlayerID(), castedMsg.getNewPlayerID());
            }

            case NewPlayerTurnNewStateMsg castedMsg -> {
                view.updateDrawArea(
                        castedMsg.getResDeckCardID(), castedMsg.getVisibleResCardID1(), castedMsg.getVisibleResCardID2(),
                        castedMsg.getGoldDeckCardID(), castedMsg.getVisibleGoldCardID1(), castedMsg.getVisibleGoldCardID2());
                view.showNewPlayerTurnNewState(castedMsg.getDrawnCardID(), castedMsg.getLastPlayerID(), castedMsg.getNewPlayerID(), castedMsg.getGameState());
            }

            case NewPlayerExtraTurnMsg castedMsg -> view.showNewPlayerExtraTurn(castedMsg.getCardID(), castedMsg.getLastPlayerID(), castedMsg.getPos(), castedMsg.getSide(), castedMsg.getNewPlayerID(), castedMsg.getScore());

            case ReceiveChatMessageMsg castedMsg -> view.receiveChatMessage(castedMsg.getSenderNickname(), castedMsg.getRecipientNickname(), castedMsg.getMessage());

            case RankingMsg castedMsg -> view.showRanking(castedMsg.getLastPlayerID(), castedMsg.getCardID(), castedMsg.getPos(), castedMsg.getSide(), castedMsg.getDeltaScore(), castedMsg.getNicknames(), castedMsg.getScores(), castedMsg.getNumOfSecretObjectives());

            case PingMsg ignored -> view.receivePing();

            case null, default -> System.out.println("Invalid message type");
        }
    }
}
