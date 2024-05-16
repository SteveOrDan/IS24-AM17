package com.example.pf_soft_ing.deserializers;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.objectiveCards.ResourcesCountObjectiveCard;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonExporter {
    private final static CardCorner emptyCorner = new EmptyCorner();
    private final static CardCorner hiddenCorner = new HiddenCorner();
    private final static CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    private final static CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    private final static CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    private final static CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    private final static CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    private final static CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    private final static CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);

    private final static Side resourceFront1 = new Front(emptyCorner, hiddenCorner, iCorner, kCorner);
    private final static Side resourceBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.INSECT);}});
    private final static PlaceableCard resourceCard1 = new ResourceCard(0, CardElementType.INSECT, 0, resourceFront1, resourceBack1);

    private final static Side goldenFront1 = new Front(emptyCorner, hiddenCorner, qCorner, mCorner);
    private final static Side goldenBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.ANIMAL);}});
    private final static PlaceableCard goldenCard1 = new GoldenCard(CardElementType.ANIMAL, 1, goldenFront1, goldenBack1, 3, new HashMap<>(){{put(ResourceType.ANIMAL, 2); put(ResourceType.INSECT, 1);}}, false, null);

    private final static Side starterFront1 = new Front(aCorner, pCorner, iCorner, fCorner);
    private final static Side starerBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.FUNGI);}});
    private final static PlaceableCard starterCard1 = new StarterCard(2, starterFront1, starerBack1);

    private final static ObjectiveCard objectiveCard1 = new ResourcesCountObjectiveCard(3, ResourceType.PLANT, 3);

    public static void main(String[] args){
         exportCard(resourceCard1);
//         exportCard(goldenCard1);
//         exportCard(starterCard1);
//        exportObjectiveCard(objectiveCard1);
    }

    public static void exportCard(PlaceableCard card){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("JsonFiles/Exports/ResourceCard.json")){
            gson.toJson(card, writer);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void exportObjectiveCard(ObjectiveCard card){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("JsonFiles/Exports/ObjectiveCard.json")){
            gson.toJson(card, writer);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
