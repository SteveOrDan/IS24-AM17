package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.exceptions.InvalidCardIDException;
import com.example.pf_soft_ing.exceptions.InvalidVisibleCardIndexException;
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

    public void restoreInitialVisibleCards () {
        PlaceableCard card1 = deck.getFirst();
        deck.remove(card1);
        PlaceableCard card2 = deck.getFirst();
        deck.remove(card2);

        visibleCards.add(card1);
        visibleCards.add(card2);
    }

    /**
     * Get the top card in the deck and returns it
     * @return the top card in the deck
     */
    public PlaceableCard drawCard() throws NotEnoughCardsException {
        if (!deck.isEmpty()){
            PlaceableCard retCard = deck.getFirst();

            deck.remove(retCard);

            return retCard;
        }
        else{
            throw new NotEnoughCardsException();
        }
    }

    /**
     * The method returns one of the cards in the visibleCards list based on the ID passed as a parameter
     * @param index index of visible card in the array (either 0 or 1)
     * @return the card that has been drawn
     */
    public PlaceableCard drawVisibleCard(int index) throws InvalidVisibleCardIndexException{
        if (!visibleCards.isEmpty() && index >= 0 && index < visibleCards.size()) {
            PlaceableCard retCard = visibleCards.get(index);
            visibleCards.remove(index);
            return retCard;
        }
        throw new InvalidVisibleCardIndexException(index);
    }

    /**
     * Changes the values inside the visibleCards list so that it only contains 2 cards
     */
    public void restoreVisibleCard() throws NotEnoughCardsException {
        visibleCards.add(drawCard());
    }


    /**
     * Changes the values inside the visibleCards list so that it only contains 2 cards
     */
    public void restoreVisibleCardWithOtherDeck(PlaceableCard card) throws NotEnoughCardsException {
        visibleCards.add(card);
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

    /**
     * Method to get a visible card by its ID
     * @param cardID ID of the card to get
     * @return The card with the given ID
     */
    public PlaceableCard getVisibleCardByID(int cardID){
        for (PlaceableCard card : visibleCards){
            if (card.getId() == cardID){
                return card;
            }
        }
        return null;
    }
}
