package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.*;
import com.example.pf_soft_ing.network.RMI.ServerGameControllerInterface;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MatchController implements ServerGameControllerInterface {

    private final MatchModel matchModel;

    public MatchController(int maxPlayers, int matchID){
        matchModel = new MatchModel(maxPlayers, matchID);
    }

    /**
     * Getter
     * @return IDPlayerMap with keys as player IDs and values as PlayerModel objects
     */
    public HashMap<Integer, PlayerModel> getIDPlayerMap() {
        return matchModel.getIDToPlayerMap();
    }

    /**
     * Getter
     * @return GameModel object
     */
    public MatchModel getMatchModel() {
        return matchModel;
    }

    /**
     * Add player to the game if there isn't already a player with the same ID or nickname or if there are already 4 players
     * Handles exceptions for player ID already exists, game is full, nickname already exists
     * @param nickname Player's nickname
     */
    public Integer addPlayer(String nickname) throws GameFullException, NicknameAlreadyExistsException {
        if (getIDPlayerMap().size() >= matchModel.getMaxPlayers()){
            // Game is full
            throw new GameFullException();
        }

        List<Integer> idList = new ArrayList<>(getIDPlayerMap().keySet());
        Random rand = new Random();

        int id = rand.nextInt(100);
        while (idList.contains(id)){
            id = rand.nextInt(100);
        }

        for (PlayerModel p : getIDPlayerMap().values()){
            if (p.getNickname().equals(nickname)){
                throw new NicknameAlreadyExistsException();
            }
        }

        PlayerModel player = new PlayerModel(nickname, id);

        getIDPlayerMap().put(player.getId(), player);

        matchModel.addPlayer(player);

        player.setState(PlayerState.PRE_GAME);
        //System.out.println("nameOfPlayer: " + nickname + " , id:" + id);

        return id;
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
     * Set the common objectives for the game
     */
    public void setCommonObjectives(){
        matchModel.getObjectiveCardsDeck().setCommonObjectives();
    }

    /**
     * Set the objectives to choose for the player
     * @param playerID ID of the player
     */
    public void setObjectivesToChoose(int playerID){
        try {
            if (!getIDPlayerMap().containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            PlayerModel player = getIDPlayerMap().get(playerID);

            ArrayList<ObjectiveCard> objectives = new ArrayList<>();
            objectives.add(matchModel.drawObjectiveCard());
            objectives.add(matchModel.drawObjectiveCard());

            player.setObjectivesToChoose(objectives);
        } catch (InvalidPlayerIDException e) {
            System.out.println(e.getMessage());
        }
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
    public void placeCard(int playerID, int cardID, Position pos) {
        try {
            if (!getIDPlayerMap().containsKey(playerID)){
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

            if (!getIDPlayerMap().get(playerID).getHand().contains(GameResources.getIDToPlaceableCardMap().get(cardID))){
                // Card not in player's hand
                throw new CardNotInHandException();
            }

            if (matchModel.getGameState() != GameState.PLAYING &&
                    matchModel.getGameState() != GameState.FINAL_ROUND &&
                    matchModel.getGameState() != GameState.EXTRA_ROUND){
                // Not playing game state
                throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.PLAYING + " or " + GameState.FINAL_ROUND + " or " + GameState.EXTRA_ROUND);

            }

            PlayerModel player = getIDPlayerMap().get(playerID);

            player.placeCard(GameResources.getIDToPlaceableCardMap().get(cardID), pos);

            if (matchModel.getGameState() == GameState.EXTRA_ROUND){
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
            if (matchModel.getGameState() != GameState.SET_UP){
                // Not in game set up
                throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.SET_UP.toString());
            }

            if (!getIDPlayerMap().containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            PlayerModel player = getIDPlayerMap().get(playerID);

            player.drawCard(matchModel.drawResourceCard());
            player.drawCard(matchModel.drawResourceCard());
            player.drawCard(matchModel.drawGoldenCard());
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

            getIDPlayerMap().get(playerID).drawCard(matchModel.drawResourceCard());

            getIDPlayerMap().get(playerID).setState(PlayerState.WAITING);

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

            getIDPlayerMap().get(playerID).drawCard(matchModel.drawVisibleResourceCard(index));

            matchModel.restoreVisibleResourceCard();

            getIDPlayerMap().get(playerID).setState(PlayerState.WAITING);

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

            getIDPlayerMap().get(playerID).drawCard(matchModel.drawGoldenCard());

            getIDPlayerMap().get(playerID).setState(PlayerState.WAITING);

            endTurn();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Draw a visible golden card
     * Handles exceptions for invalid game state, invalid player ID, not player's turn, not in drawing state, card not visible
     * @param playerID ID of the player
     * @param index index of the visible golden card (either 0 or 1)
     */
    public void drawVisibleGoldenCard(int playerID, int index){
        try {
            checkPlayerDrawExceptions(playerID);

            getIDPlayerMap().get(playerID).drawCard(matchModel.drawVisibleGoldenCard(index));

            matchModel.restoreVisibleGoldenCard();

            getIDPlayerMap().get(playerID).setState(PlayerState.WAITING);

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
            if (matchModel.getGameState() != GameState.SET_UP){
                // Not in game set up
                throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.SET_UP.toString());
            }

            if (!getIDPlayerMap().containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            getIDPlayerMap().get(playerID).setStarterCard(matchModel.drawStarterCard());
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
            if (!getIDPlayerMap().containsKey(playerID)){
                // Invalid player ID
                throw new InvalidPlayerIDException();
            }

            if (!GameResources.getIDToPlaceableCardMap().containsKey(cardID)){
                // Invalid card ID
                throw new InvalidCardIDException();
            }

            List<PlaceableCard> hand = getIDPlayerMap().get(playerID).getHand();

            if (!hand.contains(GameResources.getIDToPlaceableCardMap().get(cardID))){
                // Card not in player's hand
                throw new CardNotInHandException();
            }

            hand.get(hand.indexOf(GameResources.getIDToPlaceableCardMap().get(cardID))).flipCard();
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
    protected void checkPlayerDrawExceptions(int playerID) throws InvalidGameStateException, InvalidPlayerIDException, NotPlayerTurnException, InvalidPlayerStateException{
        if (matchModel.getGameState() != GameState.PLAYING &&
                matchModel.getGameState() != GameState.FINAL_ROUND){
            // Not playing game state
            throw new InvalidGameStateException(matchModel.getGameState().toString(), GameState.PLAYING + " or " + GameState.FINAL_ROUND);
        }

        if (!getIDPlayerMap().containsKey(playerID)){
            // Invalid player ID
            throw new InvalidPlayerIDException();
        }

        if (matchModel.getCurrPlayerID() != playerID){
            // Not player's turn
            throw new NotPlayerTurnException();
        }

        if (getIDPlayerMap().get(playerID).getState() != PlayerState.DRAWING){
            // Not in drawing state
            throw new InvalidPlayerStateException(getIDPlayerMap().get(playerID).getState().toString(), PlayerState.DRAWING.toString());
        }
    }
}
