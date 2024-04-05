package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;

import java.util.ArrayList;
import java.util.HashMap;

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
     * @param player PlayerModel object to add
     */
    public void addPlayer(PlayerModel player){
        if (IDPlayerMap.containsKey(player.getId()) || IDPlayerMap.size() >= 4){
            return;
        }

        for (PlayerModel p : IDPlayerMap.values()){
            if (p.getNickname().equals(player.getNickname())){
                return;
            }
        }

        IDPlayerMap.put(player.getId(), player);

        gameModel.addPlayer(player);
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
     * Set visible cards for the game
     * @param resourceCard1 ID of the first resource card
     * @param resourceCard2 ID of the second resource card
     * @param goldenCard1 ID of the first golden card
     * @param goldenCard2 ID of the second golden card
     */
    public void setVisibleCards(int resourceCard1, int resourceCard2, int goldenCard1, int goldenCard2){
        gameModel.setVisibleCards(resourceCard1, resourceCard2, goldenCard1, goldenCard2);
    }

    /**
     * Set visible cards for the game
     * @param resourceCardID ID of the resource card
     */
    public void setResourceVisibleCard(int resourceCardID){
        gameModel.setResourceVisibleCard(resourceCardID);
    }

    /**
     * Set visible cards for the game
     * @param goldenCardID ID of the golden card
     */
    public void setGoldenVisibleCard(int goldenCardID){
        gameModel.setGoldenVisibleCard(goldenCardID);
    }

    /**
     * Draw a visible resource card
     * @param playerID ID of the player
     * @param index Index of the visible resource card
     */
    public void drawVisibleResourceCard(int playerID, int index){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        IDPlayerMap.get(playerID).drawCard(gameModel.drawVisibleResourceCard(index));
    }

    /**
     * Draw a visible golden card
     * @param playerID ID of the player
     * @param index Index of the visible golden card
     */
    public void drawVisibleGoldenCard(int playerID, int index){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        IDPlayerMap.get(playerID).drawCard(gameModel.drawVisibleGoldenCard(index));
    }

    /**
     * Set the visible cards for the game
     */
    public void setCommonObjectives(){
        gameModel.getObjectiveCardsDeck().setCommonObjectives();
    }

    /**
     * Set the common objective for the game
     * @param objective ID of the common objective
     */
    public void setCommonObjective(int objective){
        gameModel.getObjectiveCardsDeck().setCommonObjective(objective);
    }

    /**
     * Set the objectives to choose for the player
     * @param playerID ID of the player
     */
    public void setObjectiveToChoose(int playerID){
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
     * Set the objectives to choose for the player
     * @param playerID ID of the player
     * @param objective1 ID of the first objective
     * @param objective2 ID of the second objective
     */
    public void setObjectivesToChoose(int playerID, int objective1, int objective2){
        if (!IDPlayerMap.containsKey(playerID)){
            return;
        }

        PlayerModel player = IDPlayerMap.get(playerID);

        ArrayList<ObjectiveCard> objectives = new ArrayList<>();
        objectives.add(gameModel.drawObjectiveCard(objective1));
        objectives.add(gameModel.drawObjectiveCard(objective2));

        player.setObjectivesToChoose(objectives);
    }

    /**
     * Set the first player randomly
     */
    public void setRandomFirstPlayer(){
        gameModel.setRandomFirstPlayer();
    }

    /**
     * Set the first player
     * @param playerID ID of the player
     */
    public void setFirstPlayer(int playerID){
        gameModel.setFirstPlayer(playerID);
    }

    /**
     * Set the order of the players
     */
    public void setOrderOfPlayers(){
        gameModel.setOrderOfPlayers();
    }

    /**
     * Place a card on player's play area
     * @param playerID ID of the player
     * @param cardID ID of the card
     * @param pos Position to place the card
     */
    public void placeCard(int playerID, int cardID, Position pos){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        if (!IDPlaceableCardMap.containsKey(cardID)){
            // Invalid card ID
            return;
        }

        if (!IDPlayerMap.get(playerID).getHand().contains(IDPlaceableCardMap.get(cardID))){
            // Card not in player's hand
            return;
        }

        PlayerModel player = IDPlayerMap.get(playerID);

        player.placeCard(IDPlaceableCardMap.get(cardID), pos);
    }

    /**
     * Draw a resource card from the deck and add to the player's hand
     * @param playerID ID of the player
     */
    public void drawResourceCard(int playerID){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        IDPlayerMap.get(playerID).drawCard(gameModel.drawResourceCard());
    }

    /**
     * Draw a resource card from the deck and add to the player's hand
     * @param playerID ID of the player
     * @param cardID ID of the card
     */
    public void drawResourceCard(int playerID, int cardID){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        if (!IDPlaceableCardMap.containsKey(cardID)){
            // Invalid card ID
            return;
        }

        IDPlayerMap.get(playerID).drawCard(gameModel.drawResourceCard(cardID));
    }

    /**
     * Draw a golden card from the deck and add to the player's hand
     * @param playerID ID of the player
     */
    public void drawGoldenCard(int playerID){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        IDPlayerMap.get(playerID).drawCard(gameModel.drawGoldenCard());
    }

    /**
     * Draw a golden card from the deck and add to the player's hand
     * @param playerID ID of the player
     * @param cardID ID of the card
     */
    public void drawGoldenCard(int playerID, int cardID){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        if (!IDPlaceableCardMap.containsKey(cardID)){
            // Invalid card ID
            return;
        }

        IDPlayerMap.get(playerID).drawCard(gameModel.drawGoldenCard(cardID));
    }

    /**
     * Draw a starter card from the deck and set it as the player's starter card
     * @param playerID ID of the player
     */
    public void drawStarterCard(int playerID){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        IDPlayerMap.get(playerID).setStarterCard(gameModel.drawStarterCard());
    }

    /**
     * Draw a starter card from the deck and set it as the player's starter card
     * @param playerID ID of the player
     * @param cardID ID of the card
     */
    public void drawStarterCard(int playerID, int cardID){
        if (!IDPlayerMap.containsKey(playerID)){
            // Invalid player ID
            return;
        }

        if (!IDPlaceableCardMap.containsKey(cardID)){
            // Invalid card ID
            return;
        }

        IDPlayerMap.get(playerID).setStarterCard(gameModel.drawStarterCard(cardID));
    }

    public void endTurn(){
        gameModel.endTurn();
    }
}
