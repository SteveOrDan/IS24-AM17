package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class DiagonalObjectiveCard extends ObjectiveCard {

    private static final int points = 2;
    private final CardElementType elementType;

    public DiagonalObjectiveCard(int id, CardElementType elementType) {
        super(id, points);

        this.elementType = elementType;
    }

    /**
     * @return CardElementType of the diagonal shape type objective card
     */
    public CardElementType getElementType() {
        return elementType;
    }

    /**
     * Calculates points given by a diagonal shape type objective card
     * @param playArea Player's play area to get placed cards from
     * @param direction Direction of the diagonal
     * @return Number of points given by the objective card
     */
    protected int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int direction){
        ArrayList<Position> usedPositions = new ArrayList<>();

        int objectivePoints = 0;

        for (Position pos : playArea.keySet()){
            if (playArea.get(pos).getElementType().equals(elementType) && !usedPositions.contains(pos)){
                Position currPos = pos;

                Position newPos = new Position(pos.x() + direction, pos.y() + 1);
                PlaceableCard nextCard = playArea.get(newPos);

                while (nextCard != null && nextCard.getElementType().equals(elementType) && !usedPositions.contains(newPos)){
                    currPos = newPos;

                    newPos = new Position(newPos.x() + direction, newPos.y() + 1);
                    nextCard = playArea.get(newPos);
                }

                // Up to this point I have reached the highest card on that diagonal
                // Now I have to go back down to check if I have 3 in diagonal
                // And I'm also sure that currPos is a valid value (not null)

                // Card adjacent to the curr card
                Position diagPos1 = new Position(currPos.x() - direction, currPos.y() - 1);
                // Card adjacent to diagCard1
                Position diagPos2 = new Position(currPos.x() - 2 * direction, currPos.y() - 2);

                PlaceableCard diagCard1 = playArea.get(diagPos1);
                PlaceableCard diagCard2 = playArea.get(diagPos2);

                // Check for:
                //      card != null
                //      card is of the right element
                //      card has not been already used
                if (diagCard1 != null &&
                        diagCard2 != null &&
                        diagCard1.getElementType().equals(elementType) &&
                        diagCard2.getElementType().equals(elementType) &&
                        !usedPositions.contains(diagPos1) &&
                        !usedPositions.contains(diagPos2))
                {
                    objectivePoints += points;
                    usedPositions.add(currPos);
                    usedPositions.add(diagPos1);
                    usedPositions.add(diagPos2);
                }
            }
        }

        return objectivePoints;
    }
}
