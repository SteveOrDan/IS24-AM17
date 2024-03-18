package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.GoldenCard;
import com.example.pf_soft_ing.card.ObjectiveCard;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.exceptions.MissingResourcesException;
import com.example.pf_soft_ing.exceptions.NoAdjacentCardsException;
import com.example.pf_soft_ing.exceptions.PlacingOnInvalidCornerException;
import com.example.pf_soft_ing.exceptions.PositionAlreadyTakenException;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerModel {
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
     * Checks if all adjacent corners are available or null, else  -->  throw PlacingOnInvalidCornerException
     * Checks if at least one corner is not null (there is at least an adjacent valid corner), else  -->  throw NoAdjacentCardsException
     * Checks if the player has the required resources to place the card, else  -->  throw MissingResourcesException
     * @param card: Card to place in the player's playArea
     * @param pos: Card's position to add to the player's playArea
     */
    public void placeCard(PlaceableCard card, Position pos){
        try {
            if (playArea.containsKey(pos)){
                throw new PositionAlreadyTakenException();
            }

            ArrayList<CardCorner> adjacentCorners = getAdjacentCorners(pos);

            HashMap<ResourceType, Integer> requiredResources = card.getRequiredResources();

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
            for (ResourceType resource : card.getResources().keySet()){
                numOfResourcesArr[resource.getValue()] += card.getResources().get(resource);
            }

            // Add card points
            currScore += card.points;

            if (card.isGolden() && card.points == 0){
                GoldenCard gCard = (GoldenCard) card;

                if (gCard.isPointPerResource()){
                    currScore += numOfResourcesArr[gCard.getPointPerResourceRes().getValue()];
                }
                else {
                    currScore += adjacentCorners.size() * 2;
                }
            }

            // Add card to playArea
            playArea.put(pos, card);
        }
        catch (PositionAlreadyTakenException | PlacingOnInvalidCornerException | MissingResourcesException | NoAdjacentCardsException e){
            System.out.println(e.toString());
        }
    }

    private ArrayList<CardCorner> getAdjacentCorners(Position pos) throws NoAdjacentCardsException, PlacingOnInvalidCornerException {
        ArrayList<CardCorner> adjacentCorners = new ArrayList<>() {{
            PlaceableCard TRCard = playArea.get(new Position(pos.getX() + 1, pos.getY() + 1));
            PlaceableCard BRCard = playArea.get(new Position(pos.getX() + 1, pos.getY() - 1));
            PlaceableCard BLCard = playArea.get(new Position(pos.getX() - 1, pos.getY() - 1));
            PlaceableCard TLCard = playArea.get(new Position(pos.getX() - 1, pos.getY() + 1));

            if (TRCard != null){
                add(TRCard.currSide.getBLCorner());
            }
            if (BRCard != null){
                add(BRCard.currSide.getTLCorner());
            }
            if (BLCard != null){
                add(BLCard.currSide.getTRCorner());
            }
            if (TLCard != null){
                add(TLCard.currSide.getBRCorner());
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
}
