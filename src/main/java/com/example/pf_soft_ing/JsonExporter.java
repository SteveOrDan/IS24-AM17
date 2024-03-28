package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    static PlaceableCard goldenCard1 = new GoldenCard(CardElementType.ANIMAL, 1, goldenFront1, goldenBack1, 3, new HashMap<>(){{put(ResourceType.ANIMAL, 3);}}, false, null);

    static Side starterFront1 = new Front(aCorner, pCorner, iCorner, fCorner);
    static Side starerBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.FUNGI);}});
    static PlaceableCard starterCard1 = new StarterCard(2, starterFront1, starerBack1);

    public static void main(String[] args){
        // exportCard(resourceCard1);
        // {"points":0,"id":0,"elementType":"INSECT","priority":0,"currSide":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"INSECT"},"TRCorner":{"resourceType":"INKWELL"}},"front":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"INSECT"},"TRCorner":{"resourceType":"INKWELL"}},"back":{"permanentResources":["INSECT"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}}

        exportCard(goldenCard1);
        // {"isPointPerResource":false,"requiredResources":{"A":3},"points":3,"id":1,"elementType":"ANIMAL","priority":0,"currSide":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"QUILL"},"TRCorner":{"resourceType":"MANUSCRIPT"}},"front":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"QUILL"},"TRCorner":{"resourceType":"MANUSCRIPT"}},"back":{"permanentResources":["ANIMAL"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}}

        // exportCard(starterCard1);
        // {"id":2,"elementType":"STARTER","priority":0,"currSide":{"BLCorner":{"resourceType":"ANIMAL"},"BRCorner":{"resourceType":"PLANT"},"TLCorner":{"resourceType":"INSECT"},"TRCorner":{"resourceType":"FUNGI"}},"front":{"BLCorner":{"resourceType":"ANIMAL"},"BRCorner":{"resourceType":"PLANT"},"TLCorner":{"resourceType":"INSECT"},"TRCorner":{"resourceType":"FUNGI"}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}}
    }

    public static void exportCard(PlaceableCard card){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("Card.json")){
            gson.toJson(card, writer);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void exportCardList(List<PlaceableCard> cards){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("ResourceCards.json")){
            gson.toJson(cards, writer);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

//[
//  {"points":0,"type":"ResourceCard","id":0,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{"resourceType":"FUNGI"},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{}},"front":{"BLCorner":{"resourceType":"FUNGI"},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":0,"type":"ResourceCard","id":1,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{"resourceType":"FUNGI"}},"front":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{"resourceType":"FUNGI"}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":0,"type":"ResourceCard","id":2,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{"resourceType":"FUNGI"},"BRCorner":{"resourceType":"FUNGI"},"TLCorner":{},"TRCorner":{}},"front":{"BLCorner":{"resourceType":"FUNGI"},"BRCorner":{"resourceType":"FUNGI"},"TLCorner":{},"TRCorner":{}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":0,"type":"ResourceCard","id":3,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{},"BRCorner":{"resourceType":"FUNGI"},"TLCorner":{},"TRCorner":{"resourceType":"FUNGI"}},"front":{"BLCorner":{},"BRCorner":{"resourceType":"FUNGI"},"TLCorner":{},"TRCorner":{"resourceType":"FUNGI"}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":0,"type":"ResourceCard","id":4,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{"resourceType":"PLANT"},"BRCorner":{"resourceType":"FUNGI"},"TLCorner":{},"TRCorner":{"resourceType":"QUILL"}},"front":{"BLCorner":{"resourceType":"PLANT"},"BRCorner":{"resourceType":"FUNGI"},"TLCorner":{},"TRCorner":{"resourceType":"QUILL"}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":0,"type":"ResourceCard","id":5,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{},"BRCorner":{"resourceType":"ANIMAL"},"TLCorner":{"resourceType":"INKWELL"},"TRCorner":{"resourceType":"FUNGI"}},"front":{"BLCorner":{},"BRCorner":{"resourceType":"ANIMAL"},"TLCorner":{"resourceType":"INKWELL"},"TRCorner":{"resourceType":"FUNGI"}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":0,"type":"ResourceCard","id":6,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{"resourceType":"MANUSCRIPT"},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{"resourceType":"INSECT"}},"front":{"BLCorner":{"resourceType":"MANUSCRIPT"},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{"resourceType":"INSECT"}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":1,"type":"ResourceCard","id":7,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{"resourceType":"FUNGI"}},"front":{"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{"resourceType":"FUNGI"}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":1,"type":"ResourceCard","id":8,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{}},"front":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}},
//  {"points":1,"type":"ResourceCard","id":9,"elementType":"FUNGI","priority":0,"currSide":{"BLCorner":{"resourceType":"FUNGI"},"BRCorner":{},"TLCorner":{},"TRCorner":{}},"front":{"BLCorner":{},"BRCorner":{},"TLCorner":{"resourceType":"FUNGI"},"TRCorner":{}},"back":{"permanentResources":["FUNGI"],"BLCorner":{},"BRCorner":{},"TLCorner":{},"TRCorner":{}}}
//]
