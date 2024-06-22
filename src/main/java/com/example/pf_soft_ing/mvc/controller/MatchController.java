package com.example.pf_soft_ing.mvc.controller;

import com.example.pf_soft_ing.mvc.model.game.GameModel;
import com.example.pf_soft_ing.mvc.model.game.GameResources;
import com.example.pf_soft_ing.mvc.model.game.GameState;
import com.example.pf_soft_ing.mvc.model.game.MatchModel;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.cards.*;
import com.example.pf_soft_ing.exceptions.match.GameIsFullException;
import com.example.pf_soft_ing.exceptions.match.InvalidGameStateException;
import com.example.pf_soft_ing.exceptions.player.*;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.mvc.model.player.PlayerModel;
import com.example.pf_soft_ing.mvc.model.player.PlayerState;
import com.example.pf_soft_ing.mvc.model.player.TokenColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchController {

    private final MatchModel matchModel;

    public MatchController(int maxPlayers, int matchID){
        matchModel = new MatchModel(maxPlayers, matchID);
    }

    /**
     * Getter
     * @param playerID ID of the player
     * @return Sender object of the player
     */
    public Sender getPlayerSender(int playerID){
        return matchModel.getIDToPlayerMap().get(playerID).getSender();
    }

    /**
     * Getter
     * @return getIDToPlayerMap with keys as player IDs and values as PlayerModel objects
     */
    public Map<Integer, PlayerModel> getIDToPlayerMap() {
        return matchModel.getIDToPlayerMap();
    }

    /**
     * Getter
     * @return IDToNicknameMap with keys as player IDs and values as player nicknames
     */
    public Map<Integer, String> getIDToNicknameMap() {
        return matchModel.getIDtoNicknameMap();
    }

    /**
     * Getter
     * @return ID of the match
     */
    public int getMatchID(){
        return matchModel.getMatchID();
    }

    /**
     * Getter
     * @return List of player nicknames
     */
    public List<String> getNicknames() {
        return matchModel.getNicknames();
    }

    /**
     * Adds the first player to the match as the host
     * @param host Player model of the host to add
     */
    public void addHost(PlayerModel host){
        matchModel.addHost(host);
    }

    /**
     * Checks if the match can start. Match needs to be full and all players need to be ready
     * @return True if the match can start, false otherwise
     */
    public boolean checkForGameStart() {
        return matchModel.getPlayersReady() == matchModel.getMaxPlayers();
    }

    /**
     * Initialize decks, shuffle them and set visible cards
     */
    public void setUpGame(){
        matchModel.setGameState(GameState.SET_UP);

        initializeDecks();
        shuffleAllDecks();
        setVisibleCards();
    }

    /**
     * Initializes all decks
     */
    public void initializeDecks(){
        matchModel.initializeDecks();
    }

    /**
     * Shuffles all decks
     */
    public void shuffleAllDecks(){
        matchModel.shuffleAllDecks();
    }

    /**
     * Sets visible cards for the game
     */
    public void setVisibleCards(){
        matchModel.setVisibleCards();
    }

    /**
     * Getter
     * @return List of visible resource cards
     */
    public List<PlaceableCard> getVisibleResourceCards(){
        return matchModel.getVisibleResourceCards();
    }

    /**
     * Getter
     * @return List of visible golden cards
     */
    public List<PlaceableCard> getVisibleGoldenCards(){
        return matchModel.getVisibleGoldenCards();
    }

    /**
     * Getter
     * @return the first card of the resource deck without removing it
     */
    public PlaceableCard checkFirstResDeckCard() {
        return matchModel.getResourceCardsDeck().getDeck().getFirst();
    }

    /**
     * Getter
     * @return the first card of the golden deck without removing it
     */
    public PlaceableCard checkFirstGoldDeckCard() {
        return matchModel.getGoldenCardsDeck().getDeck().getFirst();
    }

    /**
     * Places the starter card for the player
     * @param playerID ID of the player
     * @param side Side of the card to place
     */
    public void placeStarterCardForPlayer(int playerID, CardSideType side){
        try {
            List<Integer> hand = fillPlayerHand(playerID); // return cards
            TokenColors color = setPlayerToken(playerID); // ret token color
            List<Integer> commonObjectives = getCommonObjectivesID(); // ret common objectives
            List<Integer> objToChoose = setObjectivesToChoose(playerID); // ret secret objectives

            getIDToPlayerMap().get(playerID).placeStarterCard(side);

            getPlayerSender(playerID).sendMissingSetup(hand.getFirst(), hand.get(1), hand.get(2), color, commonObjectives.getFirst(), commonObjectives.get(1), objToChoose.getFirst(), objToChoose.get(1));
        }
        catch (InvalidPlayerIDException | InvalidGameStateException | NotEnoughCardsException e) {
            getPlayerSender(playerID).sendError(e.getMessage());
        }
    }

    /**
     * Setter
     * Set secret objective
     * @param playerID ID of the player
     * @param cardID ID of the secret objective card choose
     */
    public void setSecretObjectiveForPlayer(int playerID, int cardID){
        try{
            // Set secret objective
            getIDToPlayerMap().get(playerID).setSecretObjective(cardID);

            // If all players are ready, start the turn order phase
            if (checkForTurnOrderPhase()) {
                matchModel.startGame(playerID, false);
            }
            else {
                getPlayerSender(playerID).confirmSecretObjective();
            }
        }
        catch (InvalidObjectiveCardIDException | StarterCardNotSetException e) {
            getPlayerSender(playerID).sendError(e.getMessage());
        }
    }

    /**
     * Sets the common objective cards for the game
     */
    public void setCommonObjectives(){
        matchModel.getObjectiveCardsDeck().setCommonObjectives();
    }

    /**
     * Sets the objectives to choose for the player
     * @param playerID ID of the player
     * @return List of objective card IDs to choose from
     */
    public List<Integer> setObjectivesToChoose(int playerID) throws InvalidPlayerIDException {
        if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        List<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(matchModel.drawObjectiveCard());
        objectives.add(matchModel.drawObjectiveCard());

        matchModel.getIDToPlayerMap().get(playerID).setSecretObjectiveCardIDs(objectives.getFirst().getID(), objectives.get(1).getID());

        return List.of(objectives.get(0).getID(), objectives.get(1).getID());
    }

    /**
     * Set the player's token color and returns it
     * @param playerID ID of the player
     * @return Token color of the player
     * @throws InvalidPlayerIDException If the player ID is invalid
     */
    public TokenColors setPlayerToken(int playerID) throws InvalidPlayerIDException {
        if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        PlayerModel player = matchModel.getIDToPlayerMap().get(playerID);

        TokenColors color = matchModel.getTokenColor();

        player.setToken(color);

        return color;
    }

    /**
     * Getter
     * @return List of common objective card IDs
     */
    public List<Integer> getCommonObjectivesID(){
        List<Integer> commonObjectivesID = new ArrayList<>();
        for (ObjectiveCard objectiveCard : matchModel.getObjectiveCardsDeck().getCommonObjectives()){
            commonObjectivesID.add(objectiveCard.getID());
        }
        return commonObjectivesID;
    }

    /**
     * Sets the first player randomly
     */
    public void setRandomFirstPlayer(){
        matchModel.setRandomFirstPlayer();
    }

    /**
     * Calculates the order of the players based on the first player
     */
    public void calculateOrderOfPlayers(){
        matchModel.calculateOrderOfPlayers();
    }

    /**
     * Places a card on player's play area
     * Handles exceptions for invalid player ID, not player's turn, invalid card ID, card not in player's hand
     * @param playerID ID of the player
     * @param cardID ID of the card
     * @param pos Position to place the card
     */
    public void placeCard(int playerID, int cardID, Position pos, CardSideType chosenSide) {
        synchronized (this) {
            try {
                if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
                    // Invalid player ID
                    throw new InvalidPlayerIDException();
                }

                if (matchModel.getCurrPlayerID() != playerID){
                    // Not player's turn
                    throw new NotPlayerTurnException();
                }

                if (!GameResources.getIDToPlaceableCardMap().containsKey(cardID)){
                    // Invalid card ID
                    throw new InvalidCardIDException();
                }

                if (!matchModel.getIDToPlayerMap().get(playerID).getHand().contains(GameResources.getPlaceableCardByID(cardID))){
                    // Card not in player's hand
                    throw new CardNotInHandException();
                }

                if (matchModel.getGameState() != GameState.PLAYING &&
                        matchModel.getGameState() != GameState.FINAL_ROUND &&
                        matchModel.getGameState() != GameState.EXTRA_ROUND){
                    // Not playing game state
                    throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.PLAYING + " or " + GameState.FINAL_ROUND + " or " + GameState.EXTRA_ROUND);
                }

                int oldScore = getIDToPlayerMap().get(playerID).getCurrScore();

                PlayerModel player = matchModel.getIDToPlayerMap().get(playerID);

                player.placeCard(GameResources.getPlaceableCardByID(cardID), pos, chosenSide);

                int deltaScore = getIDToPlayerMap().get(playerID).getCurrScore() - oldScore;

                if (matchModel.getGameState() == GameState.EXTRA_ROUND){
                    endTurn();

                    // Send new player 'extra' turn or ranking if game ended
                    if (matchModel.getGameState() == GameState.END_GAME){
                        broadcastRanking(playerID, cardID, pos, chosenSide, deltaScore,
                                matchModel.getNicknamesRanked(), matchModel.getScoresRanked(),
                                matchModel.getNumOfCompletedObjectivesRanked());
                        GameModel.removeMatch(this);
                    }
                    else {
                        broadcastNewPlayerExtraTurn(playerID, cardID, pos, chosenSide, deltaScore);
                    }
                }
                else {
                    broadcastPlaceCard(playerID, cardID, pos, chosenSide, deltaScore);
                }
            }
            catch (Exception e) {
                getPlayerSender(playerID).sendError(e.getMessage());
            }
        }
    }

    /**
     * Fill player's hand with 2 resource cards and 1 golden card
     * @param playerID ID of the player
     * @return List of card IDs of the drawn cards
     * @throws InvalidGameStateException If the game state is not "set-up"
     * @throws InvalidPlayerIDException If the player ID is invalid
     * @throws NotEnoughCardsException If there are not enough cards in the deck
     */
    public List<Integer> fillPlayerHand(int playerID) throws InvalidGameStateException, InvalidPlayerIDException, NotEnoughCardsException {
        if (matchModel.getGameState() != GameState.SET_UP){
            // Not in game set up
            throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.SET_UP.toString());
        }

        if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        PlayerModel player = matchModel.getIDToPlayerMap().get(playerID);

        PlaceableCard resCard1 = matchModel.drawResourceCard();
        PlaceableCard resCard2 = matchModel.drawResourceCard();
        PlaceableCard goldCard = matchModel.drawGoldenCard();

        player.drawCard(resCard1);
        player.drawCard(resCard2);
        player.drawCard(goldCard);

        List<Integer> cardIDs = new ArrayList<>();
        cardIDs.add(resCard1.getID());
        cardIDs.add(resCard2.getID());
        cardIDs.add(goldCard.getID());

        return cardIDs;
    }

    /**
     * Checks if all players have completed the set-up
     * @return true if all players have completed the set-up, false otherwise
     */
    public boolean checkForTurnOrderPhase(){
        return matchModel.checkForTurnOrderPhase();
    }

    /**
     * Getter
     * @return ID of the current player
     */
    public int getCurrPlayerID(){
        return matchModel.getCurrPlayerID();
    }

    /**
     * Draws the starter card for the player
     * @return PlaceableCard object of the drawn card
     */
    public PlaceableCard drawStarterCard() {
        return matchModel.drawStarterCard();
    }

    /**
     * Draws a resource card from the deck and add to the player's hand
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state
     * @param playerID ID of the player
     */
    public void drawResourceCard(int playerID) {
        synchronized (this) {
            try {
                checkPlayerDrawExceptions(playerID);

                PlaceableCard drawnCard = matchModel.drawResourceCard();

                matchModel.getIDToPlayerMap().get(playerID).drawCard(drawnCard);

                GameState oldGameState = matchModel.getGameState();
                endTurn();
                GameState currGameState = matchModel.getGameState();

                broadcastNewPlayerTurn(drawnCard.getID(), playerID, currGameState != oldGameState);
            }
            catch (Exception e) {
                getPlayerSender(playerID).sendError(e.getMessage());
            }
        }
    }

    /**
     * Draws a visible resource card
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state, card not visible
     * @param playerID ID of the player
     * @param index index of the visible resource card (either 0 or 1)
     */
    public void drawVisibleResourceCard(int playerID, int index){
        synchronized (this) {
            try {
                checkPlayerDrawExceptions(playerID);

                PlaceableCard drawnCard = matchModel.drawVisibleResourceCard(index);

                matchModel.getIDToPlayerMap().get(playerID).drawCard(drawnCard);

                matchModel.restoreVisibleResourceCard();

                GameState oldGameState = matchModel.getGameState();
                endTurn();
                GameState currGameState = matchModel.getGameState();

                broadcastNewPlayerTurn(drawnCard.getID(), playerID, currGameState != oldGameState);
            }
            catch (Exception e) {
                getPlayerSender(playerID).sendError(e.getMessage());
            }
        }
    }

    /**
     * Draws a golden card from the deck and add to the player's hand
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state
     * @param playerID ID of the player
     */
    public void drawGoldenCard(int playerID) {
        synchronized (this) {
            try {
                checkPlayerDrawExceptions(playerID);

                PlaceableCard drawnCard = matchModel.drawGoldenCard();

                matchModel.getIDToPlayerMap().get(playerID).drawCard(drawnCard);

                GameState oldGameState = matchModel.getGameState();
                endTurn();
                GameState currGameState = matchModel.getGameState();

                broadcastNewPlayerTurn(drawnCard.getID(), playerID, currGameState != oldGameState);
            }
            catch (Exception e) {
                getPlayerSender(playerID).sendError(e.getMessage());
            }
        }
    }

    /**
     * Draws a visible resource card
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state, card not visible
     * @param playerID ID of the player
     * @param index index of the visible resource card (either 0 or 1)
     */
    public void drawVisibleGoldenCard(int playerID, int index){
        synchronized (this) {
            try {
                checkPlayerDrawExceptions(playerID);

                PlaceableCard drawnCard = matchModel.drawVisibleGoldenCard(index);

                matchModel.getIDToPlayerMap().get(playerID).drawCard(drawnCard);

                matchModel.restoreVisibleGoldenCard();

                GameState oldGameState = matchModel.getGameState();
                endTurn();
                GameState currGameState = matchModel.getGameState();

                broadcastNewPlayerTurn(drawnCard.getID(), playerID, currGameState != oldGameState);
            }
            catch (Exception e) {
                getPlayerSender(playerID).sendError(e.getMessage());
            }
        }
    }

    /**
     * Ends the game set-up phase and set the game state to "playing"
     */
    public void endGameSetUp(){
        matchModel.setGameState(GameState.PLAYING);
    }

    /**
     * Ends the current player's turn
     */
    public void endTurn(){
        matchModel.endTurn();
    }

    /**
     * Checks for any exceptions when a player is drawing a card
     */
    private void checkPlayerDrawExceptions(int playerID) throws InvalidGameStateException, InvalidPlayerIDException, NotPlayerTurnException, InvalidPlayerStateException {
        if (matchModel.getGameState() != GameState.PLAYING &&
                matchModel.getGameState() != GameState.FINAL_ROUND){
            // Not playing game state
            throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.PLAYING + " or " + GameState.FINAL_ROUND);
        }

        if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        if (matchModel.getCurrPlayerID() != playerID){
            // Not player's turn
            throw new NotPlayerTurnException();
        }

        if (matchModel.getIDToPlayerMap().get(playerID).getState() != PlayerState.DRAWING){
            // Not in drawing state
            throw new InvalidPlayerStateException(matchModel.getIDToPlayerMap().get(playerID).getState().toString(), PlayerState.DRAWING.toString());
        }
    }

    /**
     * Checks if the nicknames is valid
     * @param nickname Nickname to check
     * @throws NicknameAlreadyExistsException If the nickname is invalid
     */
    public void checkNickname(String nickname) throws NicknameAlreadyExistsException {
        matchModel.checkNickname(nickname);
    }

    /**
     * Adds 1 to the counter of ready players
     */
    public void addReadyPlayer() {
        matchModel.addReadyPlayer();
    }

    /**
     * Adds a player to the match
     * @param player Player to add
     * @throws GameIsFullException If the game is full
     */
    public void addCurrPlayer(PlayerModel player) throws GameIsFullException {
        matchModel.addCurrPlayer(player);
    }

    /**
     * Getter
     * @return Current state of the game
     */
    public GameState getGameState() {
        return matchModel.getGameState();
    }

    /**
     * Setter
     * @param gameState New state of the game
     */
    public void setGameState(GameState gameState) {
        matchModel.setGameState(gameState);
    }

    /**
     * Handles player disconnection
     * @param playerID ID of the player that disconnected
     */
    public void disconnectPlayer(int playerID) {
        synchronized (this) {
            GameState oldGameState = getGameState();
            PlayerState oldPlayerState = getIDToPlayerMap().get(playerID).getState();
            // Set the player's state to "disconnected"
            getIDToPlayerMap().get(playerID).setState(PlayerState.DISCONNECTED);

            // If the game is in the pre-game phase, remove the player from the game
            if (oldGameState == GameState.PREGAME) {
                matchModel.removePlayer(playerID);
            }
            // If the game is in the set-up phase, simply broadcast the disconnection to other players
            else if (oldGameState == GameState.SET_UP) {
                broadcastPlayerDisconnection(playerID);
            }
            else if (oldGameState == GameState.PLAYING || oldGameState == GameState.FINAL_ROUND || oldGameState == GameState.EXTRA_ROUND) {
                // If the game is in the playing phase or the final round and the player disconnected after placing a card, undo the card placement
                if (getCurrPlayerID() == playerID && oldPlayerState == PlayerState.DRAWING) {
                    matchModel.undoCardPlacement(playerID);
                    // If there is only one player online remaining, send a special message to the last player standing
                    if (matchModel.checkForLastPlayerStanding()) {
                        matchModel.getIDToPlayerMap().get(getCurrPlayerID()).setState(PlayerState.WAITING);
                        matchModel.getIDToPlayerMap().get(getCurrPlayerID()).getSender().sendUndoPlaceWithOnePlayerLeft(playerID,
                                getIDToPlayerMap().get(playerID).getLastCardPlacedPos(), getIDToPlayerMap().get(playerID).getCurrScore());
                    }
                    else {
                        broadcastUndoCardPlacement(playerID, getIDToPlayerMap().get(playerID).getLastCardPlacedPos(),
                                getIDToPlayerMap().get(playerID).getCurrScore(), matchModel.getCurrPlayerID());
                    }
                }
                // If the game is in the playing phase, the final or extra round and the player disconnected before placing a card
                else if(getCurrPlayerID() == playerID && oldPlayerState == PlayerState.PLACING) {
                    endTurn();
                    // If there is only one player online remaining, send a special message to the last player standing
                    if (matchModel.checkForLastPlayerStanding()) {
                        matchModel.getIDToPlayerMap().get(getCurrPlayerID()).setState(PlayerState.WAITING);
                        matchModel.getIDToPlayerMap().get(getCurrPlayerID()).getSender().sendPlayerDisconnectionWithOnePlayerLeft(playerID);
                    }
                    else {
                        broadcastPlayerDisconnection(playerID);
                    }
                }
                // If it isn't the player's turn, simply broadcast the disconnection to other players
                else {
                    // If there is only one player online remaining, send a special message to the last player standing
                    if (matchModel.checkForLastPlayerStanding()) {
                        matchModel.getIDToPlayerMap().get(getCurrPlayerID()).setState(PlayerState.WAITING);
                        matchModel.getIDToPlayerMap().get(getCurrPlayerID()).getSender().sendPlayerDisconnectionWithOnePlayerLeft(playerID);
                    }
                    else {
                        broadcastPlayerDisconnection(playerID);
                    }
                }
            }
        }
    }

    /**
     * Handles the reconnection of a player
     * @param nickname Nickname of the player
     * @param newSender New sender of the player
     * @return ID of the player that reconnected if the operation was successful, -1 otherwise
     * @throws SpecifiedPlayerNotDisconnected If the player with the given nickname is not disconnected
     * @throws NicknameNotInMatch If the nickname is not in the match
     */
    public int reconnectPlayer(String nickname, Sender newSender) throws SpecifiedPlayerNotDisconnected, NicknameNotInMatch {
        synchronized (this) {
            int playerID = matchModel.reconnectPlayer(nickname, newSender);

            if (playerID != -1) {
                broadcastPlayerReconnection(playerID);
            }
            return playerID;
        }
    }

    /**
     * Sends a chat message to a player (or all players)
     * @param senderID ID of the sender
     * @param recipient Recipient of the message
     * @param message Message to send
     */
    public void chatMessage(int senderID, String recipient, String message) {
        try {
            String senderNickname = matchModel.getIDToPlayerMap().get(senderID).getNickname();

            if (recipient.equals("all")) {
                for (int broadcastID : getIDToPlayerMap().keySet()) {
                    getPlayerSender(broadcastID).sendChatMessage(senderNickname, recipient, message);
                }
            }
            else {
                int recipientID = -1;

                for (int ID : getIDToPlayerMap().keySet()) {
                    if (getIDToPlayerMap().get(ID).getNickname().equals(recipient)) {
                        recipientID = ID;
                        break;
                    }
                }

                if (recipientID == -1) {
                    throw new InvalidRecipientException();
                }

                // Send the message to both the sender and the recipient
                getPlayerSender(senderID).sendChatMessage(senderNickname, recipient, message);
                getPlayerSender(recipientID).sendChatMessage(senderNickname, recipient, message);
            }
        }
        catch (Exception e) {
            getPlayerSender(senderID).sendError(e.getMessage());
        }
    }

    /**
     * Broadcasts the new player's turn to all players
     * @param drawnCardID ID of the drawn card
     * @param playerID ID of the player that drew the card
     * @param changedState true if the game state changed, false otherwise
     */
    private void broadcastNewPlayerTurn(int drawnCardID, int playerID, boolean changedState) {
        int newPlayerID = matchModel.getCurrPlayerID();

        int resDeckCardID = -1;
        int visibleResCardID1 = -1;
        int visibleResCardID2 = -1;

        int goldDeckCardID = -1;
        int visibleGoldCardID1 = -1;
        int visibleGoldCardID2 = -1;

        if (!matchModel.getResourceCardsDeck().getDeck().isEmpty()) {
            resDeckCardID = matchModel.getResourceCardsDeck().getDeck().getFirst().getID();
        }

        if (!matchModel.getVisibleResourceCards().isEmpty()) {
            visibleResCardID1 = matchModel.getVisibleResourceCards().getFirst().getID();
        }

        if (matchModel.getVisibleResourceCards().size() > 1) {
            visibleResCardID2 = matchModel.getVisibleResourceCards().get(1).getID();
        }

        if (!matchModel.getGoldenCardsDeck().getDeck().isEmpty()) {
            goldDeckCardID = matchModel.getGoldenCardsDeck().getDeck().getFirst().getID();
        }

        if (!matchModel.getVisibleGoldenCards().isEmpty()) {
            visibleGoldCardID1 = matchModel.getVisibleGoldenCards().getFirst().getID();
        }

        if (matchModel.getVisibleGoldenCards().size() > 1) {
            visibleGoldCardID2 = matchModel.getVisibleGoldenCards().get(1).getID();
        }

        if (changedState){
            for (Integer broadcastID : getIDToPlayerMap().keySet()) {
                if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED) {
                    getPlayerSender(broadcastID).sendNewPlayerTurnNewState(drawnCardID, playerID, newPlayerID,
                            resDeckCardID, visibleResCardID1, visibleResCardID2,
                            goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2,
                            matchModel.getGameState());
                }
            }
        }
        else {
            for (Integer broadcastID : getIDToPlayerMap().keySet()) {
                if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED) {
                    getPlayerSender(broadcastID).sendNewPlayerTurn(drawnCardID, playerID, newPlayerID,
                            resDeckCardID, visibleResCardID1, visibleResCardID2,
                            goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
                }
            }
        }
    }

    /**
     * Broadcasts the new player's extra turn to all players
     * @param playerID ID of the player that drew the card
     * @param cardID ID of the drawn card
     * @param pos Position to place the card
     * @param side Side of the card
     */
    private void broadcastNewPlayerExtraTurn(int playerID, int cardID, Position pos, CardSideType side, int score) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED) {
                getPlayerSender(broadcastID).sendNewPlayerExtraTurn(cardID, playerID, pos, side, matchModel.getCurrPlayerID(), score);
            }
        }
    }

    /**
     * Broadcasts the player's placed card
     * @param playerID ID of the player that placed the card
     * @param cardID ID of the placed card
     * @param pos Position of the placed card
     * @param side Side of the placed card
     */
    private void broadcastPlaceCard(int playerID, int cardID, Position pos, CardSideType side, int score) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED) {
                getPlayerSender(broadcastID).placeCard(playerID, cardID, pos, side, score);
            }
        }
    }

    /**
     * Broadcasts the ranking of the players to all players
     * @param nicknames Array of nicknames of the players in the ranking order
     * @param scores Array of scores of the players in the ranking order
     * @param numOfSecretObjectives Array of the number of secret objectives completed by the players in the ranking order
     */
    private void broadcastRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED) {
                getPlayerSender(broadcastID).sendRanking(lastPlayerID, cardID, pos, side, deltaScore, nicknames, scores, numOfSecretObjectives);
            }
        }
    }

    /**
     * Broadcasts the player disconnection to all players
     * @param playerID ID of the player that disconnected
     */
    private void broadcastPlayerDisconnection(int playerID) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            // Broadcast the disconnection to all players online excluding the disconnected player
            if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED && broadcastID != playerID) {
                getPlayerSender(broadcastID).sendPlayerDisconnection(playerID);
            }
        }
    }

    /**
     * Broadcasts the disconnection of all players to all players
     * @param playerID ID of the player that disconnected
     * @param pos Position of the last card placed by the player
     * @param score new score of the player
     * @param nextPlayerID ID of the next player
     */
    private void broadcastUndoCardPlacement(int playerID, Position pos, int score, int nextPlayerID) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            // Broadcast the undo card placement to all players online excluding the player that disconnected
            if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED && broadcastID != playerID) {
                getPlayerSender(broadcastID).sendUndoCardPlacement(playerID, pos, score, nextPlayerID);
            }
        }
    }

    /**
     * Broadcasts the reconnection of a player to all other online players
     * @param playerID ID of the player that reconnected
     */
    private void broadcastPlayerReconnection(int playerID) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            // Broadcast the reconnection to all players online excluding the reconnected player
            if (getIDToPlayerMap().get(broadcastID).getState() != PlayerState.DISCONNECTED && broadcastID != playerID) {
                getPlayerSender(broadcastID).sendPlayerReconnection(playerID);
            }
        }
    }

    /**
     * Getter
     * @return True if the match has no players, false otherwise
     */
    public boolean hasNoPlayers() {
        return matchModel.hasNoPlayers();
    }

    /**
     * Getter
     * @return True if the match has no players online, false otherwise
     */
    public boolean hasNoPlayersOnline() {
        return matchModel.hasNoPlayersOnline();
    }

    /**
     * Getter
     * @return True if the match is already over, false otherwise
     */
    public boolean isOver() {
        return matchModel.isOver();
    }
}
