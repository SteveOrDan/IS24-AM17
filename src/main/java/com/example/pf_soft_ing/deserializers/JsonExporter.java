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
    static CardCorner emptyCorner = new EmptyCorner();
    static CardCorner hiddenCorner = new HiddenCorner();
    static CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    static CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    static CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    static CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    static CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    static CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    static CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);

    static Side resourceFront1 = new Front(emptyCorner, hiddenCorner, iCorner, kCorner);
    static Side resourceBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.INSECT);}});
    static PlaceableCard resourceCard1 = new ResourceCard(0, CardElementType.INSECT, 0, resourceFront1, resourceBack1);

    static Side goldenFront1 = new Front(emptyCorner, hiddenCorner, qCorner, mCorner);
    static Side goldenBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.ANIMAL);}});
    static PlaceableCard goldenCard1 = new GoldenCard(CardElementType.ANIMAL, 1, goldenFront1, goldenBack1, 3, new HashMap<>(){{put(ResourceType.ANIMAL, 2); put(ResourceType.INSECT, 1);}}, false, null);

    static Side starterFront1 = new Front(aCorner, pCorner, iCorner, fCorner);
    static Side starerBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.FUNGI);}});
    static PlaceableCard starterCard1 = new StarterCard(2, starterFront1, starerBack1);

    static ObjectiveCard objectiveCard1 = new ResourcesCountObjectiveCard(3, ResourceType.PLANT, 3);

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
