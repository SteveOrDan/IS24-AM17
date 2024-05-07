package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.network.client.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

public class RMIReceiver extends UnicastRemoteObject implements RMIReceiverInterface {

    private final GameController gameController;
    private static final HashMap<Integer, PlayerModel> IDToPlayer = new HashMap<>();
    private static final HashMap<Integer, MatchController> IDToMatch = new HashMap<>();

    public RMIReceiver(GameController gameController) throws RemoteException {
        this.gameController = gameController;
    }

    @Override
    public int getMatches(ClientRMIInterface client) throws RemoteException {
        PlayerModel playerModel = gameController.getGameModel().createPlayer(new RMISender(client));
        IDToPlayer.put(playerModel.getID(), playerModel);

        playerModel.getSender().sendMatches(gameController.getGameModel().getMatches());

        return playerModel.getID();
    }

    @Override
    public void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException {
        try {
            MatchController matchController = gameController.createMatch(playerID, numberOfPlayers, nickname);
            IDToMatch.put(playerID, matchController);

            IDToPlayer.get(playerID).getSender().createMatchResult(matchController.getMatchModel().getMatchID(), nickname);
        }
        catch (InvalidNumOfPlayers | InvalidPlayerStateException | GameIsFullException e) {
            IDToPlayer.get(playerID).getSender().sendError(e.getMessage());
        }
    }

    @Override
    public void selectMatch(int playerID, int matchID) throws RemoteException {
        try {
            MatchController matchController = gameController.selectMatch(playerID, matchID);
            IDToMatch.put(playerID, matchController);

            IDToPlayer.get(playerID).getSender().selectMatchResult(matchController.getMatchModel().getMatchID(), matchController.getMatchModel().getNicknames());
        }
        catch (InvalidMatchID | GameIsFullException e) {
            IDToPlayer.get(playerID).getSender().sendError(e.getMessage());
        }
    }

    @Override
    public void chooseNickname(int playerID, String nickname) throws RemoteException {
        try {
            gameController.chooseNickname(playerID, nickname);
            MatchController matchController = IDToMatch.get(playerID);

            // if match reached max players, start the game ; else send the nickname to the player

            if (gameController.checkForGameStart(matchController)) {

                gameController.getGameModel().startGame(matchController);

                List<PlaceableCard> visibleResCards = matchController.getVisibleResourceCards();
                List<PlaceableCard> visibleGoldCards = matchController.getVisibleGoldenCards();

                PlaceableCard resDeckCardID = matchController.getMatchModel().getResourceCardsDeck().getDeck().getFirst();
                PlaceableCard goldDeckCardID = matchController.getMatchModel().getGoldenCardsDeck().getDeck().getFirst();

                IDToPlayer.get(playerID).getSender().chooseNicknameResult(nickname);

                for (Integer ID : matchController.getIDToPlayerMap().keySet()) {
                    PlaceableCard starterCard = matchController.getMatchModel().drawStarterCard();

                    matchController.getIDToPlayerMap().get(ID).setStarterCard(starterCard);


                    matchController.getIDToPlayerMap().get(ID).getSender().sendGameStart(resDeckCardID.getID(), visibleResCards.getFirst().getID(), visibleResCards.get(1).getID(),
                            goldDeckCardID.getID(), visibleGoldCards.getFirst().getID(), visibleGoldCards.get(1).getID(),
                            starterCard.getID());
                }
            }
            else {
                IDToPlayer.get(playerID).getSender().chooseNicknameResult(nickname);
                // possiamo aggiungere una chiamata affinchè il client sappia che deve aspettare l'arrivo di nuovi giocatori
                for (Integer ID : matchController.getIDToPlayerMap().keySet()) {
                    if (ID != playerID) {
                        matchController.getIDToPlayerMap().get(ID).getSender().sendNewPlayer(nickname);
                    }
                }
            }
        } catch (InvalidMatchID | NicknameAlreadyExistsException e) {
            IDToPlayer.get(playerID).getSender().sendError(e.getMessage());
        }
    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos) throws RemoteException {

    }

    @Override
    public void drawResourceCard(int playerID) throws RemoteException {

    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) throws RemoteException {

    }

    @Override
    public void drawGoldenCard(int playerID) throws RemoteException {

    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) throws RemoteException {

    }
}