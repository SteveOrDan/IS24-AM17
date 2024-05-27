package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.List;

public abstract class PlaceableCard {

    private String cardType;

    private final int ID;

    private final CardElementType elementType;

    private int priority;

    private CardSideType currSide = CardSideType.FRONT;
    private final Side front;
    private final Side back;

    public PlaceableCard(CardElementType element, int ID, Side front, Side back){
        this.priority = 0;
        this.elementType = element;
        this.ID = ID;
        this.front = front;
        this.back = back;
    }

    @Override
    public String toString(){
        return "Placeable card";
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * Getter
     * @return Card's ID
     */
    public int getID() {
        return ID;
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
     * @return Card's priority
     */
    public int getPriority(){
        return priority;
    }

    /**
     * Getter
     * @return the chosen side of the card for the placement in the play area
     */
    public CardSideType getCurrSideType() {
        return currSide;
    }

    /**
     * Getter
     * @param sideType Type of the side to return
     * @return The side based on the parameter
     */
    public Side getSideFromType(CardSideType sideType) {
        return sideType.equals(CardSideType.FRONT) ? front : back;
    }

    /**
     * Returns the correct side of the card based on the chosen side
     * @return Current side of the card
     */
    public Side getCurrSide(){
        return currSide == CardSideType.FRONT ? front : back;
    }

    /**
     * Setter
     * @param newSide New side of the card
     */
    public void setCurrSideType(CardSideType newSide) {
        this.currSide = newSide;
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

    public abstract boolean hasEnoughRequiredResources(int[] numOfResourcesArr, CardSideType chosenSide);

    public abstract int calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr, CardSideType chosenSide);
}
