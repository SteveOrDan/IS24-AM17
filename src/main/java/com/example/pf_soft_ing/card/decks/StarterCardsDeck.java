package com.example.pf_soft_ing.card.decks;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.exceptions.cards.NotEnoughCardsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record StarterCardsDeck(List<PlaceableCard> deck) {

    public StarterCardsDeck(List<PlaceableCard> deck) {
        this.deck = new ArrayList<>(deck);
    }

    /**
     * Getter
     * @return List of cards in the deck
     */
    @Override
    public List<PlaceableCard> deck() {
        return deck;
    }

    /**
     * Shuffles the deck
     */
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Gets the top card in the deck, removes it from the deck and returns it
     * @return The top card in the deck
     */
    public PlaceableCard drawCard() {
        try {
            if (!deck.isEmpty()) {
                int randIndex = (int) (Math.random() * (deck.size() - 1));

                PlaceableCard retCard = deck.get(randIndex);

                deck.remove(retCard);

                return retCard;
            } else {
                throw new NotEnoughCardsException();
            }
        } catch (NotEnoughCardsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
