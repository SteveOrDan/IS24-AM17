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

    public List<ObjectiveCard> getCommonObjectives(){
        return commonObjectives;
    }

    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public ObjectiveCard drawCard(){
        try{
            if (!deck.isEmpty()){
                int randIndex = (int)(Math.random() * (deck.size() - 1));

                ObjectiveCard retCard = deck.get(randIndex);

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

    public void setCommonObjectives(){
        while (commonObjectives.size() < 2){
            commonObjectives.add(drawCard());
        }
    }
}
