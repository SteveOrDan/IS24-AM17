package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.GoldenCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.exceptions.MissingResourcesException;
import com.example.pf_soft_ing.exceptions.NoAdjacentCardsException;
import com.example.pf_soft_ing.exceptions.PlacingOnInvalidCornerException;
import com.example.pf_soft_ing.exceptions.PositionAlreadyTakenException;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerModel {
    private static final int pointsPerResource = 1;
    private static final int pointsPerCoveredCorner = 2;

    private final String nickname;
    private final int id;

    private ArrayList<PlaceableCard> hand;
    private ArrayList<ObjectiveCard> objectivesToChoose;
    private ObjectiveCard secretObjective;
    private PlaceableCard starterCard;
    private final int[] numOfResourcesArr = new int[7];
    private int currScore;
    private final HashMap<Position, PlaceableCard> playArea = new HashMap<>();
    private Token token;
    private Token firstPlayerToken;

    public PlayerModel(String nickname, int id) {
        this.nickname = nickname;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    /**
     * Checks if the playArea already has the key "pos"  -->  throw PositionAlreadyTakenException
     * getAdjacentCorner:
     *      Checks if all adjacent corners are available or null, else  -->  throw PlacingOnInvalidCornerException
     *      Checks if at least one corner is not null (there is at least an adjacent valid corner), else  -->  throw NoAdjacentCardsException
     * Removes resources covered by the placed card from the player's possession
     * Adds the placed card's resources to the player's possession
     * Adds points if needed
     * Adds card to play area
     * @param rCard: Resource card to place in the player's playArea
     * @param pos: Card's position to add to the player's playArea
     */
    public void placeCard(ResourceCard rCard, Position pos){
        try {
            if (playArea.containsKey(pos)){
                throw new PositionAlreadyTakenException();
            }

            ArrayList<CardCorner> adjacentCorners = getAdjacentCorners(pos);

            // Remove resources of covered corners
            for (CardCorner corner : adjacentCorners){
                if (corner.getResource() != null){
                    numOfResourcesArr[corner.getResource().getValue()]--;
                }
            }

            // Add resources on placed card's corners
            for (ResourceType resource : rCard.getResources().keySet()){
                numOfResourcesArr[resource.getValue()] += rCard.getResources().get(resource);
            }

            // Add card points
            currScore += rCard.getPoints();

            // Add card to playArea
            playArea.put(pos, rCard);
        }
        catch (PositionAlreadyTakenException | PlacingOnInvalidCornerException | NoAdjacentCardsException e){
            System.out.println(e.getMessage());
        }
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
     * @param gCard: Golden card to place in the player's playArea
     * @param pos: Card's position to add to the player's playArea
     */
    public void placeCard(GoldenCard gCard, Position pos){
        try {
            if (playArea.containsKey(pos)){
                throw new PositionAlreadyTakenException();
            }

            ArrayList<CardCorner> adjacentCorners = getAdjacentCorners(pos);

            HashMap<ResourceType, Integer> requiredResources = gCard.getRequiredResources();

            // Check if player has required resources
            if (requiredResources != null){
                for (ResourceType resource : requiredResources.keySet()){
                    if (numOfResourcesArr[resource.getValue()] < requiredResources.get(resource)){
                        throw new MissingResourcesException();
                    }
                }
            }

            // Remove resources of covered corners
            for (CardCorner corner : adjacentCorners){
                if (corner.getResource() != null){
                    numOfResourcesArr[corner.getResource().getValue()]--;
                }
            }

            // Add resources on placed card's corners
            for (ResourceType resource : gCard.getResources().keySet()){
                numOfResourcesArr[resource.getValue()] += gCard.getResources().get(resource);
            }

            // Add card points
            currScore += gCard.getPoints();

            if (gCard.isPointPerResource()){
                currScore += numOfResourcesArr[gCard.getPointPerResourceRes().getValue()] * pointsPerResource;
            }
            else {
                currScore += adjacentCorners.size() * pointsPerCoveredCorner;
            }

            // Add card to playArea
            playArea.put(pos, gCard);
        }
        catch (PositionAlreadyTakenException | PlacingOnInvalidCornerException | MissingResourcesException | NoAdjacentCardsException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks if all adjacent corners are available or null, else  -->  throw PlacingOnInvalidCornerException
     * Checks if at least one corner is not null (there is at least an adjacent valid corner), else  -->  throw NoAdjacentCardsException
     * @param pos : position of reference card
     * @return list of adjacent corners to be covered by card
     * @throws NoAdjacentCardsException : if there are no adjacent cards to the reference card
     * @throws PlacingOnInvalidCornerException : if a corner is hidden
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


    public void calculateObjectivePoints(ObjectiveCard oCard){

    }
}