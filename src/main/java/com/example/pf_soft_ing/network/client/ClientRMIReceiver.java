package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.player.PlayerState;
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
    public void failedMatch(Map<Integer, List<String>> matchesNicknames) throws RemoteException {
//        view.failedMatch(matchesNicknames);
    }

    @Override
    public void joinMatch(int matchID, List<String> nicknames) throws RemoteException {
//        view.joinMatch(matchID, nicknames);
    }

    @Override
    public void addNickname(Integer playerID, String nickname, Map<Integer, String> opponents) {
//        view.addNickname(playerID, nickname, opponents);
    }

    @Override
    public void failedNickname(List<String> nicknames) throws RemoteException {
//        view.failedNickname(nicknames);
    }

    @Override
    public void setState(PlayerState state) throws RemoteException {

    }

    @Override
    public void setCurrScore(int score) throws RemoteException {

    }

    @Override
    public void setToken(TokenColors color) throws RemoteException {

    }

    @Override
    public void setObjectivesToChoose(List<Integer> objectiveIDs) throws RemoteException {

    }

    @Override
    public void setFirstPlayerToken(TokenColors color) throws RemoteException {

    }

    @Override
    public void addCardToPlayerHand(int id) throws RemoteException {

    }

    @Override
    public void setSecretObjective(int id) throws RemoteException {

    }

    @Override
    public void setStarterCard(int id) throws RemoteException {

    }

    @Override
    public void placeStarterCard() throws RemoteException {

    }

    @Override
    public void placeCardResult() throws RemoteException {
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
    public void startGame(int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, int starterCardID) throws RemoteException {
        view.startGame(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, starterCardID);
    }

    @Override
    public void sendFirstPlayerTurn(int playerID, String playerNickname, Map<Integer, String> IDtoOpponentNickname, Map<Integer, Map<Position, Integer>> IDtoOpponentPlayArea) throws RemoteException {
        view.showFirstPlayerTurn(playerID, playerNickname, IDtoOpponentNickname, IDtoOpponentPlayArea);
    }

    @Override
    public void setMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2, int secretObjectiveCardID1, int secretObjectiveCardID2) throws RemoteException {
        view.setMissingSetUp(resourceCardID1, resourceCardID2, goldenCardID, tokenColor, commonObjectiveCardID1, commonObjectiveCardID2, secretObjectiveCardID1, secretObjectiveCardID2);
    }

    @Override
    public void setNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID, String playerNickname,
                                 int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                 int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                 GameState gameState) {
        view.updateDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
        view.showNewPlayerTurn(drawnCardID, lastPlayerID, newPlayerID, playerNickname, gameState);
    }

    @Override
    public void opponentPlaceCard(int playerID, int cardID, Position pos, CardSideType chosenSide) throws RemoteException {
        view.opponentPlaceCard(playerID, cardID, pos, chosenSide);
    }


}
