package com.example.pf_soft_ing.card.decks;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;

import java.util.Collections;
import java.util.List;

public class StarterCardsDeck {

    private final List<PlaceableCard> deck;

    public StarterCardsDeck(List<PlaceableCard> deck) {
        this.deck = deck;
    }

    public List<PlaceableCard> getDeck() {
        return deck;
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
}
