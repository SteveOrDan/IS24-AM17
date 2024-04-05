package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectiveCardsDeck {
    private final List<ObjectiveCard> deck;
    private final List<ObjectiveCard> commonObjectives;

    public ObjectiveCardsDeck(List<ObjectiveCard> deck) {
        this.deck = deck;
        commonObjectives = new ArrayList<>();
    }

    public List<ObjectiveCard> getDeck() {
        return deck;
    }

    /**
     * Getter for commonObjectives
     * @return commonObjectives List
     */
    public List<ObjectiveCard> getCommonObjectives(){
        return commonObjectives;
    }

    /**
     * Method to shuffle the deck
     */
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    /**
     * Get the top card in the deck and returns it
     * @return the top card in the deck
     */
    public ObjectiveCard drawCard(){
        try{
            if (!deck.isEmpty()){
                ObjectiveCard retCard = deck.getFirst();

                deck.remove(retCard);

                return retCard;
            }
            else{
                throw new NotEnoughCardsException();
            }
        }
        catch (NotEnoughCardsException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Adds an objective card to the list of common objectives.
     * No more than 2 cards are allowed in the list
     */
    public void setCommonObjectives(){
        while (commonObjectives.size() < 2){
            commonObjectives.add(drawCard());
        }
    }

    /**
     * Changes the values inside the visibleCards list so that it only contains 2 cards
     * @param cardID ID of the card to set visible
     */
    public void setCommonObjective(int cardID){
        if (commonObjectives.size() < 2){
            commonObjectives.add(drawCard(cardID));
        }
    }

    /**
     * Method to draw a specific card from the deck
     * @param cardID ID of the card to draw
     * @return The card that has been drawn
     */
    public ObjectiveCard drawCard(int cardID){
        try{
            if (!deck.isEmpty()){
                ObjectiveCard retCard = getCardByID(cardID);

                deck.remove(retCard);

                return retCard;
            }
            else{
                throw new NotEnoughCardsException();
            }
        }
        catch (NotEnoughCardsException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Method to get a card by its ID
     * @param cardID ID of the card to get
     * @return The card with the given ID
     */
    public ObjectiveCard getCardByID(int cardID){
        for (ObjectiveCard card : deck){
            if (card.getId() == cardID){
                return card;
            }
        }
        return null;
    }
}
