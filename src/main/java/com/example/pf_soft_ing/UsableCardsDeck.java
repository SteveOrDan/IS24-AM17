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

    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

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

    public void restoreVisibleCards(){
        while (visibleCards.size() < 2){
            visibleCards.add(drawCard());
        }
    }
}
