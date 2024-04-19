package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameController implements ServerGameControllerInterface {

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

    /**
     * Getter
     * @return IDPlaceableCardMap with keys as card IDs and values as PlaceableCard objects
     */
    public HashMap<Integer, PlaceableCard> getIDPlaceableCardMap() {
        return IDPlaceableCardMap;
    }

    /**
     * Getter
     * @return IDPlayerMap with keys as player IDs and values as PlayerModel objects
     */
    public HashMap<Integer, PlayerModel> getIDPlayerMap() {
        return IDPlayerMap;
    }

    /**
     * Getter
     * @return GameModel object
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Add player to the game if there isn't already a player with the same ID or nickname or if there are already 4 players
     * Handles exceptions for player ID already exists, game is full, nickname already exists
     * @param nickname Player's nickname
     */
    public Integer addPlayer(String nickname){
        List<Integer> idList = new ArrayList<>(IDPlayerMap.keySet());
        Random rand = new Random();

        int id = rand.nextInt(10);
        while (idList.contains(id)){
            id = rand.nextInt(10);
        }

        PlayerModel player = new PlayerModel(nickname, id);

        try {
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
            //System.out.println("nameOfPlayer: " + nickname + " , id:" + id);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
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
        try {
            if (!IDPlayerMap.containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            PlayerModel player = IDPlayerMap.get(playerID);

            ArrayList<ObjectiveCard> objectives = new ArrayList<>();
            objectives.add(gameModel.drawObjectiveCard());
            objectives.add(gameModel.drawObjectiveCard());

            player.setObjectivesToChoose(objectives);
        } catch (InvalidPlayerIDException e) {
            System.out.println(e.getMessage());
        }
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
    public void placeCard(int playerID, int cardID, Position pos) {
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

            if (gameModel.getGameState() != GameState.PLAYING &&
                    gameModel.getGameState() != GameState.FINAL_ROUND &&
                    gameModel.getGameState() != GameState.EXTRA_ROUND){
                // Not playing game state
                throw new InvalidGameStateException(gameModel.getGameState().toString(), GameState.PLAYING + " or " + GameState.FINAL_ROUND + " or " + GameState.EXTRA_ROUND);

            }

            PlayerModel player = IDPlayerMap.get(playerID);

            player.placeCard(IDPlaceableCardMap.get(cardID), pos);

            if (gameModel.getGameState() == GameState.EXTRA_ROUND){
                endTurn();
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
            checkPlayerDrawExceptions(playerID);

            IDPlayerMap.get(playerID).drawCard(gameModel.drawVisibleResourceCard(index));

            gameModel.restoreVisibleResourceCard();

            IDPlayerMap.get(playerID).setState(PlayerState.WAITING);

            endTurn();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            checkPlayerDrawExceptions(playerID);

            IDPlayerMap.get(playerID).drawCard(gameModel.drawVisibleGoldenCard(index));

            gameModel.restoreVisibleGoldenCard();

            IDPlayerMap.get(playerID).setState(PlayerState.WAITING);

            endTurn();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            //System.out.println("DrawStarterCard invoked");
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
            //System.out.println("Card flipped");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * End the game set up and set the game state to "playing"
     */
    public void endGameSetUp(){
        gameModel.setGameState(GameState.PLAYING);
    }

    /**
     * End the current player's turn
     */
    public void endTurn(){
        gameModel.endTurn();
    }

    /**
     * Checks for any exceptions when a player is drawing a card
     */
    protected void checkPlayerDrawExceptions(int playerID) throws InvalidGameStateException, InvalidPlayerIDException, NotPlayerTurnException, InvalidPlayerStateException{
        if (gameModel.getGameState() != GameState.PLAYING &&
                gameModel.getGameState() != GameState.FINAL_ROUND){
            // Not playing game state
            throw new InvalidGameStateException(gameModel.getGameState().toString(), GameState.PLAYING + " or " + GameState.FINAL_ROUND);
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
}
