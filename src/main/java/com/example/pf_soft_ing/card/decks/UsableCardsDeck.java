package com.example.pf_soft_ing.card.decks;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.exceptions.InvalidVisibleCardIndexException;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsableCardsDeck {

    private final List<PlaceableCard> deck;
    private final List<PlaceableCard> visibleCards;

    public UsableCardsDeck(List<PlaceableCard> deck) {
        this.deck = new ArrayList<>(deck);
        visibleCards = new ArrayList<>();
    }

    /**
     * Getter
     * @return List of PlaceableCards in the deck
     */
    public List<PlaceableCard> getDeck() {
        return deck;
    }

    /**
     * Getter
     * @return List of visible PlaceableCards in the deck
     */
    public List<PlaceableCard> getVisibleCards() {
        return visibleCards;
    }

    /**
     * Shuffles the deck
     */
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    /**
     * Moves the first two cards in the deck to the list of visible cards
     */
    public void restoreInitialVisibleCards () {
        PlaceableCard card1 = deck.getFirst();
        deck.remove(card1);
        PlaceableCard card2 = deck.getFirst();
        deck.remove(card2);

        visibleCards.add(card1);
        visibleCards.add(card2);
    }

    /**
     * Gets the top card in the deck, removes it from the deck and returns it
     * @return The top card in the deck
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
     * Returns one of the cards in the visibleCards list based on the index passed as a parameter
     * @param index Index of visible card in the array (either 0 or 1)
     * @return The card that has been drawn
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
     * Moves the first card in the deck to the list of visible cards
     */
    public void restoreVisibleCard() {
        visibleCards.add(deck.getFirst());

        deck.remove(deck.getFirst());
    }

    /**
     * Moves the card passed as a parameter to the list of visible cards
     */
    public void restoreVisibleCardWithOtherDeck(PlaceableCard card) {
        visibleCards.add(card);

        deck.remove(card);
    }

    /**
     * Getter
     * @return True if the deck is empty, false otherwise
     */
    public boolean isDeckEmpty(){
        return deck.isEmpty();
    }
}
