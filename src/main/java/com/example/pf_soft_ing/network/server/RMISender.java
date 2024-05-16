package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class RMISender implements Sender {

    ClientRMIInterface client;

    public RMISender(ClientRMIInterface client){
        this.client = client;
    }


    @Override
    public void sendError(String errorMsg) {
        try {
            client.sendError(errorMsg);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void sendMatches(Map<Integer, List<String>> matches, int playerID) {
        try {
            client.showMatches(matches, playerID);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void createMatchResult(int matchID, String hostNickname) {
        try {
            client.createMatchResult(matchID, hostNickname);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void selectMatchResult(int matchID, List<String> nicknames) {
        try {
            client.selectMatchResult(matchID, nicknames);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Client is not reachable");
        }
    }

    @Override
    public void chooseNicknameResult(String nickname) {
        try {
            client.chooseNicknameResult(nickname);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Nickname is not available");
        }
    }

    @Override
    public void sendGameStart(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                              int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                              int starterCardID) {
        try {
            client.startGame(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, starterCardID);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Game not started");
        }
    }

    @Override
    public void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                                 int commonObjectiveCardID1, int commonObjectiveCardID2,
                                 int secretObjectiveCardID1, int secretObjectiveCardID2) {
        try {
            client.setMissingSetup(resourceCardID1, resourceCardID2, goldenCardID, tokenColor, commonObjectiveCardID1, commonObjectiveCardID2, secretObjectiveCardID1, secretObjectiveCardID2);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Missing setup");
        }
    }

    @Override
    public void confirmSecretObjective() {
        try {
            client.confirmSecretObjective();
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Confirm secret objective not shown");
        }
    }

    @Override
    public void placeCard(){
        try {
            client.placeCardResult();
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Place card not shown");
        }
    }

    @Override
    public void sendFirstPlayerTurn(int playerID, String playerNickname , Map<Integer, String> IDtoOpponentNickname, Map<Integer, Map<Position, Integer>> IDtoOpponentPlayArea) {
        try {
            client.sendFirstPlayerTurn(playerID, playerNickname, IDtoOpponentNickname, IDtoOpponentPlayArea);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". New first player turn not shown");
        }
    }

    @Override
    public void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID, String playerNickname,
                                  int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                  int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                  GameState gameState) {
        try {
            client.setNewPlayerTurn(drawnCardID, lastPlayerID, newPlayerID, playerNickname,
                    resDeckCardID, visibleResCardID1, visibleResCardID2,
                    goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2,
                    gameState);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". New player's turn not shown");
        }
    }

    @Override
    public void opponentPlaceCard(int playerID, int cardID, Position pos, CardSideType chosenSide) {
        try {
            client.opponentPlaceCard(playerID, cardID, pos, chosenSide);
        }
        catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Opponent's card not placed");
        }
    }

    @Override
    public void sendMatchMessage(String message, int senderID) {
        try {
            client.sendMatchMessage(message, senderID);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Match message not sent");
        }
    }

    @Override
    public void sendPrivateMessage(String message, int senderID) {
        try {
            client.sendPrivateMessage(message, senderID);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Private message not sent");
        }
    }

    @Override
    public void confirmPrivateMessage(int recipientID, String message, int senderID) {
        try {
            client.confirmPrivateMessage(recipientID, message, senderID);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Private message not confirmed");
        }
    }

    @Override
    public void recipientNotFound(int recipientID) {
        try {
            client.recipientNotFound(recipientID);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Recipient not found");
        }
    }

    @Override
    public void sendRanking(List<String> rankings) {
        try {
            client.showRanking(rankings);
        } catch (RemoteException e) {
            System.out.println("Error: " + e.getMessage() + ". Ranking not shown");
        }
    }
}
