package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.exceptions.NotEnoughCardsException;

import java.util.Collections;
import java.util.List;

public class StarterCardsDeck {
    private final List<StarterCard> deck;

    public StarterCardsDeck(List<StarterCard> deck) {
        this.deck = deck;
    }

    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public StarterCard drawCard(){
        try{
            if (!deck.isEmpty()){
                int randIndex = (int)(Math.random() * (deck.size() - 1));

                StarterCard retCard = deck.get(randIndex);

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
