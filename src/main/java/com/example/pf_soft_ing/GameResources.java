package com.example.pf_soft_ing;

import java.util.List;

import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;

public class GameResources {
    private List<PlaceableCard> goldenDeck;
    private List<ObjectiveCard> objectiveDeck;
    private List<StarterCard> starterDeck;
    private List<PlaceableCard> resourcesDeck;

    private List<Token> tokensList;


    public static void main(String[] args){
//        ArrayList<ResourceType> res = new ArrayList<>();
//
//        res.add(ResourceType.ANIMAL);
//        res.add(ResourceType.INSECT);
//
//        GoldenCard card = new GoldenCard(1,1,1, new Front(), new Back(res));
//
//        card.exportCard();
    }

    public List<PlaceableCard> getGoldenDeck() {
        return goldenDeck;
    }

    public List<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    public List<StarterCard> getStarterDeck() {
        return starterDeck;
    }

    public List<PlaceableCard> getResourcesDeck() {
        return resourcesDeck;
    }
}