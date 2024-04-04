package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.GoldenCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerModel {
    private final String nickname;
    private final int id;

    private List<PlaceableCard> hand;
    private List<ObjectiveCard> objectivesToChoose;
    private ObjectiveCard secretObjective;
    private PlaceableCard starterCard;
    private final int[] numOfResourcesArr = new int[7];
    private int currScore;
    private final HashMap<Position, PlaceableCard> playArea = new HashMap<>();
    private Token token;
    private Token firstPlayerToken;

    private int currMaxPriority;

    public PlayerModel(String nickname, int id) {
        this.nickname = nickname;
        this.id = id;
        currMaxPriority = 0;
        currScore = 0;
        hand = new ArrayList<>();
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
    public int getId() {
        return id;
    }

    public List<PlaceableCard> getHand() {
        return hand;
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public PlaceableCard getStarterCard() {
        return starterCard;
    }

    public int[] getNumOfResourcesArr(){
        return numOfResourcesArr;
    }

    public int getCurrMaxPriority(){
        return currMaxPriority;
    }

    public HashMap<Position, PlaceableCard> getPlayArea() {
        return playArea;
    }

    /**
     * Getter
     * @return Current player's score (int)
     */
    public int getCurrScore(){
        return currScore;
    }

    /**
     * Places the starter card at the position (0, 0)
     * Adds resources on the card's side
     * No requirements are needed for positioning this card
     */
    public void placeStarterCard(){
        for (ResourceType resource : starterCard.getResources()){
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
    public void placeCard(PlaceableCard card, Position pos){
        try {
            if (playArea.containsKey(pos)){
                throw new PositionAlreadyTakenException();
            }

            if(!card.hasEnoughRequiredResources(numOfResourcesArr)){
                throw new MissingResourcesException();
            }

            ArrayList<CardCorner> adjacentCorners = getAdjacentCorners(pos);

            // Remove resources of covered corners
            for (CardCorner corner : adjacentCorners){
                if (corner.getResource() != null){
                    numOfResourcesArr[corner.getResource().getValue()]--;
                }
            }

            // Add resources on placed card's corners
            for (ResourceType resource : card.getResources()){
                numOfResourcesArr[resource.getValue()]++;
            }

            // Add card points
            currScore += card.calculatePlacementPoints(adjacentCorners.size(), numOfResourcesArr);



            // Set card priority
            card.setPriority(currMaxPriority);

            currMaxPriority++;

            // Add card to playArea
            playArea.put(pos, card);
        }
        catch (PositionAlreadyTakenException | PlacingOnInvalidCornerException | MissingResourcesException | NoAdjacentCardsException e){
            System.out.println(e.getMessage());
        }
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

            if (TRCard != null){
                add(TRCard.getCurrSide().getBLCorner());
            }
            if (BRCard != null){
                add(BRCard.getCurrSide().getTLCorner());
            }
            if (BLCard != null){
                add(BLCard.getCurrSide().getTRCorner());
            }
            if (TLCard != null){
                add(TLCard.getCurrSide().getBRCorner());
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

    public void setStarterCard(PlaceableCard sCard){
        starterCard = sCard;
    }

    /**
     * Sets the player's secret objective card choices
     * @param objectives List of 2 objective cards to choose from
     */
    public void setObjectivesToChoose(List<ObjectiveCard> objectives){
        objectivesToChoose = objectives;
    }

    /**
     * Sets the player's secret objective card
     * @param index Index of the objective card in the list of objectives to choose from
     */
    public void setSecretObjective(int index) {
        try{
            if (index < 0 || index >= objectivesToChoose.size()){
                throw new InvalidIndexException();
            }
            secretObjective = objectivesToChoose.get(index);
        }
        catch (InvalidIndexException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds the points given by the secret objective card to the player's score
     */
    public void calculateSecretObjectivePoints() {
        try {
            if (secretObjective == null){
                throw new SecretObjectiveNotSetException();
            }
            currScore += secretObjective.calculateObjectivePoints(playArea, numOfResourcesArr);
        }
        catch (SecretObjectiveNotSetException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds the points given an objective card to the player's score
     * Objective card could be the player's secret objective or one of the common objectives
     * @param oCard Card to use for points calculations
     */
    public void calculateObjectivePoints(ObjectiveCard oCard) {
        currScore += oCard.calculateObjectivePoints(playArea, numOfResourcesArr);
    }

    /**
     * Flips the starter card
     */
    public void flipStarterCard() {
        if (starterCard != null){
            starterCard.flipCard();
        }
    }

    public void drawCard(PlaceableCard card){
        if (card != null && hand.size() < 3){
            hand.add(card);
        }
    }
}