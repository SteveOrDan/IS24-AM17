package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchController {

    private final MatchModel matchModel;

    public MatchController(int maxPlayers, int matchID){
        matchModel = new MatchModel(maxPlayers, matchID);
    }

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

    public Map<Integer, String> getIDToNicknameMap() {
        return matchModel.getIDtoNicknameMap();
    }

    public int getMatchID(){
        return matchModel.getMatchID();
    }

    public List<String> getNicknames() {
        return matchModel.getNicknames();
    }

    public void addHost(PlayerModel host){
        matchModel.addHost(host);
    }

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
     * Initialize all decks
     */
    public void initializeDecks(){
        matchModel.initializeDecks();
    }

    /**
     * Shuffle all decks
     */
    public void shuffleAllDecks(){
        matchModel.shuffleAllDecks();
    }

    /**
     * Set visible cards for the game
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

    public PlaceableCard checkFirstResDeckCard() {
        return matchModel.getResourceCardsDeck().getDeck().getFirst();
    }

    public PlaceableCard checkFirstGoldDeckCard() {
        return matchModel.getGoldenCardsDeck().getDeck().getFirst();
    }

    /**
     * Place the starter card for the player
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

            // Mark the player as ready (set up completed)
            setPlayerEndedSetUp(playerID);

            // If all players are ready, start the turn order phase
            if (checkForTurnOrderPhase()) {
                startTurnOrderPhase();

                int currPlayerID = getCurrPlayerID();

                List<PlaceableCard> visibleResCards = getVisibleResourceCards();
                List<PlaceableCard> visibleGoldCards = getVisibleGoldenCards();

                PlaceableCard resDeckCardID = checkFirstResDeckCard();
                PlaceableCard goldDeckCardID = checkFirstGoldDeckCard();

                int[] playerIDs = new int[getIDToPlayerMap().size()];
                int[] starterCardIDs = new int[getIDToPlayerMap().size()];
                CardSideType[] starterCardSides = new CardSideType[getIDToPlayerMap().size()];
                TokenColors[] tokenColors = new TokenColors[getIDToPlayerMap().size()];
                int[][] playerHands = new int[getIDToPlayerMap().size()][3];

                int i = 0;
                for (int id : getIDToPlayerMap().keySet()) {
                    playerIDs[i] = getIDToPlayerMap().get(id).getID();
                    starterCardIDs[i] = getIDToPlayerMap().get(id).getStarterCard().getID();
                    starterCardSides[i] = getIDToPlayerMap().get(id).getStarterCard().getCurrSideType();
                    tokenColors[i] = getIDToPlayerMap().get(id).getToken();
                    for (int j = 0; j < 3; j++) {
                        playerHands[i][j] = getIDToPlayerMap().get(id).getHand().get(j).getID();
                    }

                    i++;
                }

                for (Integer ID : getIDToPlayerMap().keySet()) {
                    getPlayerSender(ID).sendFirstPlayerTurn(playerID, currPlayerID, playerIDs, starterCardIDs, starterCardSides,
                            tokenColors, playerHands,
                            resDeckCardID.getID(), visibleResCards.getFirst().getID(), visibleResCards.get(1).getID(),
                            goldDeckCardID.getID(), visibleGoldCards.getFirst().getID(), visibleGoldCards.get(1).getID());
                }
            }
            else {
                getPlayerSender(playerID).confirmSecretObjective();
            }
        }
        catch (InvalidPlayerIDException | InvalidObjectiveCardIDException | StarterCardNotSetException e) {
            getPlayerSender(playerID).sendError(e.getMessage());
        }
    }

    /**
     * Set the common objective cards for the game
     */
    public void setCommonObjectives(){
        matchModel.getObjectiveCardsDeck().setCommonObjectives();
    }

    /**
     * Set the objectives to choose for the player
     * @param playerID ID of the player
     * @return List of objective card IDs to choose from
     */
    public List<Integer> setObjectivesToChoose(int playerID) throws InvalidPlayerIDException {
        if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        PlayerModel player = matchModel.getIDToPlayerMap().get(playerID);

        List<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(matchModel.drawObjectiveCard());
        objectives.add(matchModel.drawObjectiveCard());

        return List.of(objectives.get(0).getID(), objectives.get(1).getID());
    }

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

    public List<Integer> getCommonObjectivesID(){
        List<Integer> commonObjectivesID = new ArrayList<>();
        for (ObjectiveCard objectiveCard : matchModel.getObjectiveCardsDeck().getCommonObjectives()){
            commonObjectivesID.add(objectiveCard.getID());
        }
        return commonObjectivesID;
    }

    /**
     * Set the first player randomly
     */
    public void setRandomFirstPlayer(){
        matchModel.setRandomFirstPlayer();
    }

    /**
     * Calculate the order of the players based on the first player
     */
    public void calculateOrderOfPlayers(){
        matchModel.calculateOrderOfPlayers();
    }

    /**
     * Place a card on player's play area
     * Handles exceptions for invalid player ID, not player's turn, invalid card ID, card not in player's hand
     * @param playerID ID of the player
     * @param cardID ID of the card
     * @param pos Position to place the card
     */
    public void placeCard(int playerID, int cardID, Position pos, CardSideType chosenSide) {
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
                    broadcastRanking(playerID, cardID, pos, chosenSide, deltaScore, matchModel.getNicknamesRanked(), matchModel.getScoresRanked(), matchModel.getNumOfCompletedObjectivesRanked());
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

    /**
     * Fill player's hand with 2 resource cards and 1 golden card
     * Handles exceptions for invalid game state, invalid player ID
     * @param playerID ID of the player
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
     * Changes the state of the player to "completed set up"
     * @param playerID ID of the player
     * @throws InvalidPlayerIDException if the player ID is invalid
     */
    public void setPlayerEndedSetUp(int playerID) throws InvalidPlayerIDException {
        if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        matchModel.getIDToPlayerMap().get(playerID).setState(PlayerState.COMPLETED_SETUP);
    }

    /**
     * Check if all players have completed the set-up
     * @return true if all players have completed the set-up, false otherwise
     */
    public boolean checkForTurnOrderPhase(){
        return matchModel.checkForTurnOrderPhase();
    }

    /**
     * Start the turn order phase
     */
    public void startTurnOrderPhase(){
        matchModel.startTurnOrderPhase();
    }

    public int getCurrPlayerID(){
        return matchModel.getCurrPlayerID();
    }

    public PlaceableCard drawStarterCard() {
        return matchModel.drawStarterCard();
    }

    /**
     * Draw a resource card from the deck and add to the player's hand
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state
     * @param playerID ID of the player
     */
    public void drawResourceCard(int playerID) {
        try {
            checkPlayerDrawExceptions(playerID);

            PlaceableCard drawnCard = matchModel.drawResourceCard();

            matchModel.getIDToPlayerMap().get(playerID).drawCard(drawnCard);

            matchModel.getIDToPlayerMap().get(playerID).setState(PlayerState.WAITING);

            GameState oldGameState = matchModel.getGameState();
            endTurn();
            GameState currGameState = matchModel.getGameState();

            broadcastNewPlayerTurn(drawnCard.getID(), playerID, currGameState != oldGameState);
        }
        catch (Exception e) {
            getPlayerSender(playerID).sendError(e.getMessage());
        }
    }

    /**
     * Draw a visible resource card
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state, card not visible
     * @param playerID ID of the player
     * @param index index of the visible resource card (either 0 or 1)
     */
    public void drawVisibleResourceCard(int playerID, int index){
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

    /**
     * Draw a golden card from the deck and add to the player's hand
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state
     * @param playerID ID of the player
     */
    public void drawGoldenCard(int playerID) {
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

    /**
     * Draw a visible resource card
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state, card not visible
     * @param playerID ID of the player
     * @param index index of the visible resource card (either 0 or 1)
     */
    public void drawVisibleGoldenCard(int playerID, int index){
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

    /**
     * Draw a starter card from the deck and set it as the player's starter card
     * Handles exceptions for invalid game state, invalid player ID
     * @param playerID ID of the player
     */
    public void drawStarterCard(int playerID){
        try {
            if (matchModel.getGameState() != GameState.SET_UP){
                // Not in game set up
                throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.SET_UP.toString());
            }

            if (!matchModel.getIDToPlayerMap().containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            matchModel.getIDToPlayerMap().get(playerID).setStarterCard(matchModel.drawStarterCard());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * End the game set up and set the game state to "playing"
     */
    public void endGameSetUp(){
        matchModel.setGameState(GameState.PLAYING);
    }

    /**
     * End the current player's turn
     */
    public void endTurn(){
        matchModel.endTurn();
    }

    /**
     * Checks for any exceptions when a player is drawing a card
     */
    private void checkPlayerDrawExceptions(int playerID) throws InvalidGameStateException, InvalidPlayerIDException, NotPlayerTurnException, InvalidPlayerStateException{
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

    public void checkNickname(String nickname) throws NicknameAlreadyExistsException {
        matchModel.checkNickname(nickname);
    }

    public void addReadyPlayer() {
        matchModel.addReadyPlayer();
    }

    public void addCurrPlayer(PlayerModel player) throws GameIsFullException {
        matchModel.addCurrPlayer(player);
    }

    public GameState getGameState() {
        return matchModel.getGameState();
    }

    public void setGameState(GameState gameState) {
        matchModel.setGameState(gameState);
    }

    /**
     * Method to handle player disconnection
     * @param playerID ID of the player that disconnected
     */
    public void disconnectPlayer(int playerID) {
        // TODO: Implement player disconnection handling
    }

    public void chatMessage(int senderID, String recipient, String message) {
        try {
            String senderNickname = matchModel.getIDToPlayerMap().get(senderID).getNickname();
            System.out.println(senderNickname + " sent a message to " + recipient + ": " + message);

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
     * Broadcast the new player's turn to all players
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

        System.out.println("New common res cards: " + resDeckCardID + " " + visibleResCardID1 + " " + visibleResCardID2);
        System.out.println("New common gold cards: " + goldDeckCardID + " " + visibleGoldCardID1 + " " + visibleGoldCardID2);

        if (changedState){
            for (Integer broadcastID : getIDToPlayerMap().keySet()) {
                getPlayerSender(broadcastID).sendNewPlayerTurnNewState(drawnCardID, playerID, newPlayerID,
                        resDeckCardID, visibleResCardID1, visibleResCardID2,
                        goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2,
                        matchModel.getGameState());
            }
        }
        else {
            for (Integer broadcastID : getIDToPlayerMap().keySet()) {
                getPlayerSender(broadcastID).sendNewPlayerTurn(drawnCardID, playerID, newPlayerID,
                        resDeckCardID, visibleResCardID1, visibleResCardID2,
                        goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);
            }
        }
    }

    /**
     * Broadcast the new player's extra turn to all players
     * @param playerID ID of the player that drew the card
     * @param cardID ID of the drawn card
     * @param pos Position to place the card
     * @param side Side of the card
     */
    private void broadcastNewPlayerExtraTurn(int playerID, int cardID, Position pos, CardSideType side, int score) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            getPlayerSender(broadcastID).sendNewPlayerExtraTurn(cardID, playerID, pos, side, matchModel.getCurrPlayerID(), score);
        }
    }

    /**
     * Broadcast the player's placed card
     * @param playerID ID of the player that placed the card
     * @param cardID ID of the placed card
     * @param pos Position of the placed card
     * @param side Side of the placed card
     */
    private void broadcastPlaceCard(int playerID, int cardID, Position pos, CardSideType side, int score) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            getPlayerSender(broadcastID).placeCard(playerID, cardID, pos, side, score);
        }
    }

    /**
     * Broadcast the ranking of the players to all players
     * @param nicknames Array of nicknames of the players in the ranking order
     * @param scores Array of scores of the players in the ranking order
     * @param numOfSecretObjectives Array of the number of secret objectives completed by the players in the ranking order
     */
    private void broadcastRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives) {
        for (Integer broadcastID : getIDToPlayerMap().keySet()) {
            getPlayerSender(broadcastID).sendRanking(lastPlayerID, cardID, pos, side, deltaScore, nicknames, scores, numOfSecretObjectives);
        }
    }
}
