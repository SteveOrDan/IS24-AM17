package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class ClientRMIReceiver extends UnicastRemoteObject implements ClientRMIInterface {

    private final View view;

    public ClientRMIReceiver(View view) throws RemoteException {
        super();
        this.view = view;
    }

    public ClientRMIInterface getClient(){
        return this;
    }

    @Override
    public void showMatches(Map<Integer, List<String>> matchesNicknames, int playerID) throws RemoteException {
        view.setID(playerID);
        view.showMatches(matchesNicknames);
    }

    @Override
    public void placeStarterCard() throws RemoteException {

    }

    @Override
    public void placeCardResult(int playerID, int cardID, Position pos, CardSideType chosenSide) throws RemoteException {
        view.placeCard();
    }

    @Override
    public void sendError(String errorMsg) throws RemoteException {
        view.errorMessage(errorMsg);
    }

    @Override
    public void selectMatchResult(int matchID, List<String> nicknames) throws RemoteException {
        view.selectMatch(matchID, nicknames);
    }

    @Override
    public void createMatchResult(int matchID, String hostNickname) throws RemoteException {
        view.createMatch(matchID, hostNickname);
    }

    @Override
    public void chooseNicknameResult(String nickname) throws RemoteException {
        view.chooseNickname(nickname);
    }

    @Override
    public void startGame(String nickname, Map<Integer, String> IDtoNicknameMap,
                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) throws RemoteException {
        view.startGame(nickname, IDtoNicknameMap, resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, starterCardID);
    }

    @Override
    public void sendFirstPlayerTurn(int playerID, Map<Integer, Map<Position, Integer>> IDtoOpponentPlayArea,
                                    int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                    int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) throws RemoteException {
        view.updateDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
        view.showFirstPlayerTurn(playerID, IDtoOpponentPlayArea);
    }

    @Override
    public void setMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2, int secretObjectiveCardID1, int secretObjectiveCardID2) throws RemoteException {
        view.setMissingSetUp(resourceCardID1, resourceCardID2, goldenCardID, tokenColor, commonObjectiveCardID1, commonObjectiveCardID2, secretObjectiveCardID1, secretObjectiveCardID2);
    }

    @Override
    public void setNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                                 int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                 int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        view.updateDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
        view.showNewPlayerTurn(drawnCardID, lastPlayerID, newPlayerID);
    }

    @Override
    public void opponentPlaceCard(int playerID, int cardID, Position pos, CardSideType chosenSide) throws RemoteException {
        view.opponentPlaceCard(playerID, cardID, pos, chosenSide);
    }

    @Override
    public void showRanking(List<String> rankings)throws RemoteException {
        view.showRanking(rankings);
    }

    @Override
    public void confirmSecretObjective() throws RemoteException {
        view.confirmSecretObjective();
    }

    @Override
    public void sendChatMessage(String senderNickname, String recipientNickname, String message)throws RemoteException {
        view.receiveChatMessage(senderNickname, recipientNickname, message);
    }

    @Override
    public void setNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID, int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, GameState gameState) throws RemoteException {
        view.updateDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
        view.showNewPlayerTurnNewState(drawnCardID, lastPlayerID, newPlayerID, gameState);
    }

    @Override
    public void setNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID) throws RemoteException {
        view.showNewPlayerExtraTurn(cardID, lastPlayerID, pos, side, newPlayerID);
    }

}
