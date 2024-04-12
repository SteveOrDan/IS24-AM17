package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameController {
    private final HashMap<Integer, PlaceableCard> IDPlaceableCardMap;
    private final HashMap<Integer, ObjectiveCard> IDObjectiveCardMap;
    private final HashMap<Integer, PlayerModel> IDPlayerMap;

    private final GameModel gameModel;

    public GameController(){
        IDPlaceableCardMap = new HashMap<>();
        IDObjectiveCardMap = new HashMap<>();
        IDPlayerMap = new HashMap<>();

        gameModel = new GameModel();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Getter
     * @return IDPlaceableCardMap with keys as card IDs and values as PlaceableCard objects
     */
    public HashMap<Integer, PlaceableCard> getIDPlaceableCardMap() {
        return IDPlaceableCardMap;
    }

    /**
     * Getter
     * @return IDObjectiveCardMap with keys as card IDs and values as ObjectiveCard objects
     */
    public HashMap<Integer, ObjectiveCard> getIDObjectiveCardMap() {
        return IDObjectiveCardMap;
    }

    /**
     * Getter
     * @return IDPlayerMap with keys as player IDs and values as PlayerModel objects
     */
    public HashMap<Integer, PlayerModel> getIDPlayerMap() {
        return IDPlayerMap;
    }

    /**
     * Add player to the game if there isn't already a player with the same ID or nickname or if there are already 4 players
     * Handles exceptions for player ID already exists, game is full, nickname already exists
     * @param player PlayerModel object to add
     */
    public void addPlayer(PlayerModel player){
        try {
            if (IDPlayerMap.containsKey(player.getId())){
                throw new PlayerIDAlreadyExistsException();
            }

            if (IDPlayerMap.size() >= 4){
                throw new GameIsFullException();
            }

            for (PlayerModel p : IDPlayerMap.values()){
                if (p.getNickname().equals(player.getNickname())){
                    throw new NicknameAlreadyExistsException();
                }
            }

            IDPlayerMap.put(player.getId(), player);

            gameModel.addPlayer(player);

            player.setState(PlayerState.PRE_GAME);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initialize decks, shuffle them and set visible cards
     */
    public void setUpGame(){
        gameModel.setGameState(GameState.SET_UP);

        initializeDecks();
        shuffleAllDecks();
        setVisibleCards();
    }

    /**
     * Initialize all decks
     */
    public void initializeDecks(){
        gameModel.initializeDecks();

        for (PlaceableCard card : GameResources.getResourcesDeck()){
            IDPlaceableCardMap.put(card.getId(), card);
        }

        for (PlaceableCard card : GameResources.getGoldenDeck()){
            IDPlaceableCardMap.put(card.getId(), card);
        }

        for (PlaceableCard card : GameResources.getStarterDeck()){
            IDPlaceableCardMap.put(card.getId(), card);
        }

        for (ObjectiveCard card : GameResources.getObjectiveDeck()){
            IDObjectiveCardMap.put(card.getId(), card);
        }
    }

    /**
     * Shuffle all decks
     */
    public void shuffleAllDecks(){
        gameModel.shuffleAllDecks();
    }

    /**
     * Set visible cards for the game
     */
    public void setVisibleCards(){
        gameModel.setVisibleCards();
    }

    /**
     * Set the visible cards for the game
     */
    public void setCommonObjectives(){
        gameModel.getObjectiveCardsDeck().setCommonObjectives();
    }

    /**
     * Set the objectives to choose for the player
     * @param playerID ID of the player
     */
    public void setObjectivesToChoose(int playerID){
        if (!IDPlayerMap.containsKey(playerID)){
            return;
        }

        PlayerModel player = IDPlayerMap.get(playerID);

        ArrayList<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(gameModel.drawObjectiveCard());
        objectives.add(gameModel.drawObjectiveCard());

        player.setObjectivesToChoose(objectives);
    }

    /**
     * Set the first player randomly
     */
    public void setRandomFirstPlayer(){
        gameModel.setRandomFirstPlayer();
    }

    /**
     * Calculate the order of the players based on the first player
     */
    public void calculateOrderOfPlayers(){
        gameModel.calculateOrderOfPlayers();
    }

    /**
     * Place a card on player's play area
     * Handles exceptions for invalid player ID, not player's turn, invalid card ID, card not in player's hand
     * @param playerID ID of the player
     * @param cardID ID of the card
     * @param pos Position to place the card
     */
    public void placeCard(int playerID, int cardID, Position pos){
        try {
            if (!IDPlayerMap.containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            if (gameModel.getCurrPlayerID() != playerID){
                // Not player's turn
                throw new NotPlayerTurnException();
            }

            if (!IDPlaceableCardMap.containsKey(cardID)){
                // Invalid card ID
                throw new InvalidCardIDException();
            }

            if (!IDPlayerMap.get(playerID).getHand().contains(IDPlaceableCardMap.get(cardID))){
                // Card not in player's hand
                throw new CardNotInHandException();
            }

            PlayerModel player = IDPlayerMap.get(playerID);

            player.placeCard(IDPlaceableCardMap.get(cardID), pos);

            player.setState(PlayerState.DRAWING);

            if (gameModel.getGameState() != GameState.END_GAME){
                checkForEndGame();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Fill player's hand with 2 resource cards and 1 golden card
     * Handles exceptions for invalid game state, invalid player ID
     * @param playerID ID of the player
     */
    public void fillPlayerHand(int playerID){
        try {
            if (gameModel.getGameState() != GameState.SET_UP){
                // Not in game set up
                throw new InvalidGameStateException(gameModel.getGameState().toString(), GameState.SET_UP.toString());
            }

            if (!IDPlayerMap.containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            PlayerModel player = IDPlayerMap.get(playerID);

            player.drawCard(gameModel.drawResourceCard());
            player.drawCard(gameModel.drawResourceCard());
            player.drawCard(gameModel.drawGoldenCard());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Draw a resource card from the deck and add to the player's hand
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state
     * @param playerID ID of the player
     */
    public void drawResourceCard(int playerID){
        try {
            checkPlayerDrawExceptions(playerID);

            IDPlayerMap.get(playerID).drawCard(gameModel.drawResourceCard());

            IDPlayerMap.get(playerID).setState(PlayerState.WAITING);

            endTurn();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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
            IDPlayerMap.get(playerID).drawCard(gameModel.drawVisibleResourceCard(index));
            gameModel.restoreVisibleResourceCard();
        }
        catch (InvalidVisibleCardIndexException e) {
            System.out.println(e.getMessage());
        }

        IDPlayerMap.get(playerID).setState(PlayerState.WAITING);

        endTurn();
    }

    /**
     * Draw a golden card from the deck and add to the player's hand
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state
     * @param playerID ID of the player
     */
    public void drawGoldenCard(int playerID){
        try {
            checkPlayerDrawExceptions(playerID);

            IDPlayerMap.get(playerID).drawCard(gameModel.drawGoldenCard());

            IDPlayerMap.get(playerID).setState(PlayerState.WAITING);

            endTurn();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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
            IDPlayerMap.get(playerID).drawCard(gameModel.drawVisibleGoldenCard(index));
            gameModel.restoreVisibleGoldenCard();
        }
        catch (InvalidVisibleCardIndexException e) {
            System.out.println(e.getMessage());
        }

        IDPlayerMap.get(playerID).setState(PlayerState.WAITING);

        endTurn();
    }

    /**
     * Draw a starter card from the deck and set it as the player's starter card
     * Handles exceptions for invalid game state, invalid player ID
     * @param playerID ID of the player
     */
    public void drawStarterCard(int playerID){
        try {
            if (gameModel.getGameState() != GameState.SET_UP){
                // Not in game set up
                throw new InvalidGameStateException(gameModel.getGameState().toString(), GameState.SET_UP.toString());
            }

            if (!IDPlayerMap.containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            IDPlayerMap.get(playerID).setStarterCard(gameModel.drawStarterCard());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Flip a card in the player's hand
     * Handles exceptions for invalid player ID, invalid card ID, card not in player's hand
     * @param playerID ID of the player
     * @param cardID ID of the card
     */
    public void flipCard(int playerID, int cardID){
        try {
            if (!IDPlayerMap.containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            if (!IDPlaceableCardMap.containsKey(cardID)){
                // Invalid card ID
                throw new InvalidCardIDException();
            }

            List<PlaceableCard> hand = IDPlayerMap.get(playerID).getHand();

            if (!hand.contains(IDPlaceableCardMap.get(cardID))){
                // Card not in player's hand
                throw new CardNotInHandException();
            }

            hand.get(hand.indexOf(IDPlaceableCardMap.get(cardID))).flipCard();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void endGameSetUp(){
        gameModel.setGameState(GameState.PLAYING);
    }

    public void endTurn(){
        gameModel.endTurn();
    }

    protected void checkPlayerDrawExceptions(int playerID) throws InvalidGameStateException, InvalidPlayerIDException, NotPlayerTurnException, InvalidPlayerStateException{
        if (gameModel.getGameState() != GameState.PLAYING){
            // Not playing game state
            throw new InvalidGameStateException(gameModel.getGameState().toString(), GameState.PLAYING.toString());
        }

        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        if (gameModel.getCurrPlayerID() != playerID){
            // Not player's turn
            throw new NotPlayerTurnException();
        }

        if (IDPlayerMap.get(playerID).getState() != PlayerState.DRAWING){
            // Not in drawing state
            throw new InvalidPlayerStateException(IDPlayerMap.get(playerID).getState().toString(), PlayerState.DRAWING.toString());
        }
    }

    protected void checkPlayerDrawExceptions(int playerID, int cardID) throws InvalidGameStateException, InvalidPlayerIDException, NotPlayerTurnException, InvalidPlayerStateException, InvalidCardIDException{
        checkPlayerDrawExceptions(playerID);

        if (!IDPlaceableCardMap.containsKey(cardID)){
            // Invalid card ID
            throw new InvalidCardIDException();
        }
    }

    public void checkForEndGame(){
        if (IDPlayerMap.get(gameModel.getCurrPlayerID()).getCurrScore() >= 20){
            gameModel.setGameState(GameState.FINAL_ROUND);
        }

        if (gameModel.getGameState() == GameState.FINAL_ROUND &&
                gameModel.getOrderOfPlayersIDs()[gameModel.getOrderOfPlayersIDs().length] == (gameModel.getCurrPlayerID())){
            gameModel.setGameState(GameState.EXTRA_ROUND);
        }
        else if (gameModel.getGameState().equals(GameState.EXTRA_ROUND) &&
                gameModel.getOrderOfPlayersIDs()[gameModel.getOrderOfPlayersIDs().length] == (gameModel.getCurrPlayerID())){
            gameModel.setGameState(GameState.END_GAME);
        }
    }
}
