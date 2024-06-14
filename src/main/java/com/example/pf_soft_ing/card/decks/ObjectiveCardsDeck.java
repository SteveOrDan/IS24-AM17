package com.example.pf_soft_ing.card.decks;

import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectiveCardsDeck {

    private final List<ObjectiveCard> deck;
    private final List<ObjectiveCard> commonObjectives;

    public ObjectiveCardsDeck(List<ObjectiveCard> deck) {
        this.deck = new ArrayList<>(deck);
        commonObjectives = new ArrayList<>();
    }

    /**
     * Getter
     * @return List of cards in the deck
     */
    public List<ObjectiveCard> getDeck() {
        return deck;
    }

    /**
     * Getter
     * @return List of common objectives
     */
    public List<ObjectiveCard> getCommonObjectives(){
        return commonObjectives;
    }

    /**
     * Shuffles the deck
     */
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    /**
     * Gets the top card in the deck, removes it from the deck and returns it
     * @return The top card in the deck
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
}
