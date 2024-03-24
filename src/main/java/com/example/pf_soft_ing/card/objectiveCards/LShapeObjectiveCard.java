package com.example.pf_soft_ing.card.objectiveCards;

import com.example.pf_soft_ing.Position;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.PlaceableCard;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class LShapeObjectiveCard extends ObjectiveCard {
    private static final int points = 3;
    private final CardElementType mainElementType;
    private final CardElementType secondaryElementType;

    public LShapeObjectiveCard(int id, CardElementType mainElementType, CardElementType secondaryElementType) {
        super(id);

        this.mainElementType = mainElementType;
        this.secondaryElementType = secondaryElementType;
    }

    protected int calculateObjectivePoints(HashMap<Position, PlaceableCard> playArea, int xDirection, int yDirection) {
        ArrayList<Position> usedPositions = new ArrayList<>();

        int objectivePoints = 0;

        for (Position pos : playArea.keySet()){
            if (playArea.get(pos).getElementType().equals(mainElementType) && !usedPositions.contains(pos)){

                // Card adjacent to the main card
                Position diagPos = new Position(pos.getX() + xDirection, pos.getY() + yDirection);
                // Card under or over the other secondary card
                Position farPos = new Position(pos.getX() + xDirection, pos.getY() + 3 * yDirection);

                PlaceableCard diagCard = playArea.get(diagPos);
                PlaceableCard farCard = playArea.get(farPos);

                // Check for:
                //      card != null
                //      card is of the right element
                //      card has not been already used
                if (diagCard != null &&
                        farCard != null &&
                        diagCard.getElementType().equals(secondaryElementType) &&
                        farCard.getElementType().equals(secondaryElementType) &&
                        !usedPositions.contains(diagPos) &&
                        !usedPositions.contains(farPos))
                {
                    objectivePoints += points;
                    usedPositions.add(pos);
                    usedPositions.add(diagPos);
                    usedPositions.add(farPos);
                }
            }
        }

        return objectivePoints;
    }
}
