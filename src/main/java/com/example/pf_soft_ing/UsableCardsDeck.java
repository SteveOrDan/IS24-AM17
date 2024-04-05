package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsableCardsDeck {
    private final List<PlaceableCard> deck;
    private final List<PlaceableCard> visibleCards;

    public UsableCardsDeck(List<PlaceableCard> deck) {
        this.deck = deck;
        visibleCards = new ArrayList<>();
    }

    public List<PlaceableCard> getDeck() {
        return deck;
    }

    public List<PlaceableCard> getVisibleCards() {
        return visibleCards;
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
    public PlaceableCard drawCard(){
        try{
            if (!deck.isEmpty()){
                int randIndex = (int)(Math.random() * (deck.size() - 1));

                PlaceableCard retCard = deck.get(randIndex);

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
     * The method returns one of the cards in the visibleCards list based on the index passed as a parameter
     * @param index Index of visible card in the list (either 0 or 1)
     * @return the card that has been drawn
     */
    public PlaceableCard drawVisibleCard(int index){
        try{
            if (!visibleCards.isEmpty()){
                PlaceableCard retCard = visibleCards.get(index);

                visibleCards.remove(retCard);

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
     * Changes the values inside the visibleCards list so that it only contains 2 cards
     */
    public void restoreVisibleCards(){
        while (visibleCards.size() < 2){
            visibleCards.add(drawCard());
        }
    }

    /**
     * Changes the values inside the visibleCards list so that it only contains 2 cards
     * @param cardID ID of the card to set visible
     */
    public void restoreVisibleCard(int cardID){
        if (visibleCards.size() < 2){
            visibleCards.add(drawCard(cardID));
        }
    }

    /**
     * Method to draw a specific card from the deck
     * @param cardID ID of the card to draw
     * @return The card that has been drawn
     */
    public PlaceableCard drawCard(int cardID){
        try{
            if (!deck.isEmpty()){
                PlaceableCard retCard = getCardByID(cardID);

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
    public PlaceableCard getCardByID(int cardID){
        for (PlaceableCard card : deck){
            if (card.getId() == cardID){
                return card;
            }
        }
        return null;
    }
}
