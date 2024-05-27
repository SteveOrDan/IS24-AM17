package com.example.pf_soft_ing.player;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerModel {

    private String nickname = "";
    private final int ID;
    private int matchID = -1;

    private final List<PlaceableCard> hand = new ArrayList<>();

    private ObjectiveCard secretObjective;

    private PlaceableCard starterCard;

    private final HashMap<Position, PlaceableCard> playArea = new HashMap<>();
    private final int[] numOfResourcesArr = new int[7];

    private int currScore = 0;
    private int numOfCompletedObjectives = 0;

    private Token token;
    private Token firstPlayerToken;

    private PlayerState state = PlayerState.MAIN_MENU;

    private int currMaxPriority = 0;

    private final Sender sender;

    public PlayerModel(int ID, Sender sender) {
        this.ID = ID;
        this.sender = sender;
    }

    /**
     * Getter
     * @return Player's sender
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Setter
     * @param nickname Player's nickname as a string
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter
     * @return player's nickname as a string
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter
     * @return Player's ID as an int
     */
    public int getID() {
        return ID;
    }

    /**
     * Getter
     * @return Player's match ID
     */
    public int getMatchID() {
        return matchID;
    }

    /**
     * Setter
     * @param matchID Player's match ID
     */
    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    /**
     * Getter
     * @return List of cards in the player's hand
     */
    public List<PlaceableCard> getHand() {
        return hand;
    }

    /**
     * Getter
     * @return List of objective cards to choose from
     */
    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    /**
     * Sets the player's secret objective card
     * @param cardID ID of the card to set as the secret objective
     */
    public void setSecretObjective(int cardID) throws InvalidObjectiveCardIDException {
        List<Integer> objectiveIDs = GameResources.getObjectiveDeck().stream().map(ObjectiveCard::getID).toList();

        if (!objectiveIDs.contains(cardID)){
            throw new InvalidObjectiveCardIDException();
        }
        secretObjective = GameResources.getObjectiveCardByID(cardID);
    }

    /**
     * Getter
     * @return Player's starter card
     */
    public PlaceableCard getStarterCard() throws StarterCardNotSetException {
        if (starterCard != null) {
            return starterCard;
        }
        else {
            throw new StarterCardNotSetException();
        }
    }

    public void setStarterCard(PlaceableCard sCard){
        starterCard = sCard;
    }

    /**
     * Getter
     * @return Player's play area
     */
    public HashMap<Position, PlaceableCard> getPlayArea() {
        return playArea;
    }

    /**
     * Getter
     * @return Player's resources array
     */
    public int[] getNumOfResourcesArr(){
        return numOfResourcesArr;
    }

    /**
     * Getter
     * @return Current player's score (int)
     */
    public int getCurrScore(){
        return currScore;
    }

    /**
     * Setter
     * @param score New score to set for the player
     */
    public void setCurrScore(int score){
        currScore = score;
    }

    /**
     * Getter
     * @return Number of completed objectives
     */
    public int getNumOfCompletedObjectives() {
        return numOfCompletedObjectives;
    }

    /**
     * Setter
     * @param tokenColor Player's token color
     */
    public void setToken(TokenColors tokenColor){
        this.token = new Token(tokenColor);
    }

    /**
     * Getter
     * @return If the player is the first player
     */
    public boolean isFirstPlayer(){
        return firstPlayerToken != null;
    }

    /**
     * Setter
     * @param isFirstPlayer Boolean to check if the player is the first player
     */
    public void setAsFirstPlayer(boolean isFirstPlayer){
        if (isFirstPlayer){
            firstPlayerToken = new Token(TokenColors.BLACK);
        }
        else{
            firstPlayerToken = null;
        }
    }

    /**
     * Getter
     * @return Player's current state
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Setter
     * @param state New state of the player
     */
    public void setState(PlayerState state){
        this.state = state;
    }

    /**
     * Getter
     * @return Player's current max priority for card placement
     */
    public int getCurrMaxPriority(){
        return currMaxPriority;
    }

    /**
     * Places the starter card at the position (0, 0)
     * Adds resources on the card's side
     * No requirements are needed for positioning this card
     */
    public void placeStarterCard(CardSideType chosenSide){
        if (playArea.containsKey(new Position(0,0))){
            System.out.println(new StarterCardAlreadyPlacedException().getMessage());
            return;
        }

        starterCard.setCurrSideType(chosenSide);

        for (ResourceType resource : starterCard.getResources(starterCard.getCurrSide())){
            numOfResourcesArr[resource.getValue()]++;
        }

        starterCard.setPriority(currMaxPriority);

        currMaxPriority++;

        playArea.put(new Position(0,0), starterCard);
    }

    /**
     * Checks if the playArea already has the key "pos"  -->  throw PositionAlreadyTakenException
     * getAdjacentCorner:
     *      Checks if all adjacent corners are available or null, else  -->  throw PlacingOnInvalidCornerException
     *      Checks if at least one corner is not null (there is at least an adjacent valid corner), else  -->  throw NoAdjacentCardsException
     * Checks if the player has the required resources to place the card, else  -->  throw MissingResourcesException
     * Removes resources covered by the placed card from the player's possession
     * Adds the placed card's resources to the player's possession
     * Adds either normal points or objective points based on the card
     * Adds card to play area
     * @param card Golden card to place in the player's playArea
     * @param pos Card's position to add to the player's playArea
     */
    public void placeCard(PlaceableCard card, Position pos, CardSideType chosenSide)
            throws PositionAlreadyTakenException, MissingResourcesException,
            NoAdjacentCardsException, PlacingOnInvalidCornerException, InvalidPlayerStateException {

        if (playArea.containsKey(pos)){
            throw new PositionAlreadyTakenException();
        }

        if(!card.hasEnoughRequiredResources(numOfResourcesArr, chosenSide)){
            throw new MissingResourcesException();
        }

        if (state != PlayerState.PLACING){
            throw new InvalidPlayerStateException(state.name(), PlayerState.PLACING.name());
        }

        ArrayList<CardCorner> adjacentCorners = getAdjacentCorners(pos);

        // Set chosen side
        card.setCurrSideType(chosenSide);

        // Remove resources of covered corners
        for (CardCorner corner : adjacentCorners){
            if (corner.getResource() != null){
                numOfResourcesArr[corner.getResource().getValue()]--;
            }
        }

        // Add resources on placed card's corners
        for (ResourceType resource : card.getSideFromType(chosenSide).getResources()){
            numOfResourcesArr[resource.getValue()]++;
        }

        // Add card points
        currScore += card.calculatePlacementPoints(adjacentCorners.size(), numOfResourcesArr, card.getCurrSideType());

        // Set card priority
        card.setPriority(currMaxPriority);

        currMaxPriority++;

        // Add card to playArea
        playArea.put(pos, card);

        // Remove card from hand
        hand.remove(card);

        state = PlayerState.DRAWING;
    }

    /**
     * Checks if all adjacent corners are available or null, else  -->  throw PlacingOnInvalidCornerException
     * Checks if at least one corner is not null (there is at least an adjacent valid corner), else  -->  throw NoAdjacentCardsException
     * @param pos Position of reference card
     * @return List of adjacent corners to be covered by card
     * @throws NoAdjacentCardsException If there are no adjacent cards to the reference card
     * @throws PlacingOnInvalidCornerException If a corner is hidden
     */
    private ArrayList<CardCorner> getAdjacentCorners(Position pos) throws NoAdjacentCardsException, PlacingOnInvalidCornerException {
        ArrayList<CardCorner> adjacentCorners = new ArrayList<>() {{
            PlaceableCard TRCard = playArea.get(new Position(pos.getX() + 1, pos.getY() + 1));
            PlaceableCard BRCard = playArea.get(new Position(pos.getX() + 1, pos.getY() - 1));
            PlaceableCard BLCard = playArea.get(new Position(pos.getX() - 1, pos.getY() - 1));
            PlaceableCard TLCard = playArea.get(new Position(pos.getX() - 1, pos.getY() + 1));

            if (TRCard != null) {
                Side currSide = TRCard.getCurrSide();
                add(currSide.getBLCorner());
            }
            if (BRCard != null) {
                Side currSide = BRCard.getCurrSide();
                add(currSide.getTLCorner());
            }
            if (BLCard != null) {
                Side currSide = BLCard.getCurrSide();
                add(currSide.getTRCorner());
            }
            if (TLCard != null) {
                Side currSide = TLCard.getCurrSide();
                add(currSide.getBRCorner());
            }
        }};

        // Check if card has any adjacent card
        if (adjacentCorners.isEmpty()){
            throw new NoAdjacentCardsException();
        }

        // Check if all not null corners are available for placement
        for (CardCorner corner : adjacentCorners){
            if (!corner.isAvailable()){
                throw new PlacingOnInvalidCornerException();
            }
        }
        return adjacentCorners;
    }

    /**
     * Adds the points given an objective card to the player's score
     * Objective card could be the player's secret objective or one of the common objectives
     * The score is capped to a maximum of 29
     * @param oCard Card to use for points calculations
     */
    public void calculateObjectivePoints(ObjectiveCard oCard) {
        int points = oCard.calculateObjectivePoints(playArea, numOfResourcesArr);
        currScore += points;
        if (currScore > 29){
            currScore = 29;
        }
        numOfCompletedObjectives += points/oCard.getPoints();
    }

    /**
     * Adds a card to the player's hand
     * @param card Card to add to the player's hand
     */
    public void drawCard(PlaceableCard card){
        hand.add(card);
    }
}
