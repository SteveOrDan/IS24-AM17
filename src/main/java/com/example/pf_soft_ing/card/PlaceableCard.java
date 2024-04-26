package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.exceptions.CardNotPlacedException;

import java.util.List;

public abstract class PlaceableCard{

    // Todo: set public attributes to private with due corrections

    public String cardType;

    public final int id;

    public final CardElementType elementType;

    private int priority;

    private Side chosenSide = null;
    public final Side front;
    public final Side back;

    public PlaceableCard(CardElementType element, int id, Side front, Side back){
        this.priority = 0;
        this.elementType = element;
        this.id = id;
        this.front = front;
        this.back = back;
    }

    @Override
    public String toString(){
        return "Placeable card";
    }

    /**
     * Getter
     * @return Card's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Getter
     * @return Card's element type
     */
    public CardElementType getElementType(){
        return elementType;
    }

    /**
     * Setter
     * @param newPriority New card's priority
     */
    public void setPriority(int newPriority){
        priority = newPriority;
    }

    /**
     * Getter
     * @return the chosen side of the card for the placement in the play area
     */
    public Side getChosenSide() throws CardNotPlacedException{
        if (chosenSide != null) {
            return chosenSide;
        }
        else {
            throw new CardNotPlacedException();
        }
    }

    /**
     * Setter
     * @param chosenSide New chosen side of the card
     */
    public void setChosenSide(Side chosenSide) {
        this.chosenSide = chosenSide;
    }

    /**
     * Getter
     * @return Card's front side
     */
    public Side getFront() {
        return front;
    }

    /**
     * Getter
     * @return Card's back side
     */
    public Side getBack() {
        return back;
    }

    /**
     * Getter of the resources based on the current side of the card
     * If card side is front gets the values from the corners
     * If card side is back gets the values from a list
     * @return Hash map of resources based on the current side of the card
     */
    public List<ResourceType> getResources(Side chosenSide){
        return chosenSide.getResources();
    }

    public abstract boolean hasEnoughRequiredResources(int[] numOfResourcesArr, Side chosenSide);

    public abstract int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr, Side choesenSide);
}
