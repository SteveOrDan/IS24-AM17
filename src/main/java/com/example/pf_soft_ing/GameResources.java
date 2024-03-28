package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.deserializers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameResources {
    private final static String resourceCardsFilename = "ResourceCards.json";
    private final static String goldenCardsFilename = "ResourceCards.json";
    private final static String starterCardsFilename = "ResourceCards.json";
    private final static String objectiveCardsFilename = "ResourceCards.json";

    private static List<PlaceableCard> goldenDeck;
    private static List<ObjectiveCard> objectiveDeck;
    private static List<StarterCard> starterDeck;
    private static List<PlaceableCard> resourcesDeck;

    public static CardCorner testCorner;
    public static Side testFront;
    public static Side testBack;
    public static PlaceableCard testResourceCard;
    public static PlaceableCard testGoldenCard;

    private List<Token> tokensList;

    public static void main(String[] args) {
//        deserializeCorner();
//        deserializeFront();
//        deserializeBack();
//        deserializeSide();
//        deserializeResourceCard();
//        initializeResourceDeck();
        deserializeGoldenCard();
    }

    public static void deserializeCorner() {
        try {
            JsonReader cornerReader = new JsonReader(new FileReader("Corner.json"));
            CornerDeserializer cornerDeserializer = new CornerDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CardCorner.class, cornerDeserializer)
                    .create();

            testCorner = gson.fromJson(cornerReader, CardCorner.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserializeFront() {
        try {
            JsonReader frontReader = new JsonReader(new FileReader("Front.json"));
            FrontDeserializer frontDeserializer = new FrontDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Front.class, frontDeserializer)
                    .create();

            testFront = gson.fromJson(frontReader, Front.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserializeBack() {
        try {
            JsonReader backReader = new JsonReader(new FileReader("Back.json"));
            BackDeserializer backDeserializer = new BackDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Back.class, backDeserializer)
                    .create();

            testBack = gson.fromJson(backReader, Back.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserializeSide(){
        try {
            JsonReader sideReader = new JsonReader(new FileReader("Back.json"));
            SideDeserializer sideDeserializer = new SideDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Side.class, sideDeserializer)
                    .create();

            testBack = gson.fromJson(sideReader, Side.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserializeResourceCard() {
        try {
            JsonReader resourceCardReader = new JsonReader(new FileReader("ResourceCard.json"));
            ResourceCardDeserializer resourceCardDeserializer = new ResourceCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, resourceCardDeserializer)
                    .create();

            testResourceCard = gson.fromJson(resourceCardReader, PlaceableCard.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserializeGoldenCard() {
        try {
            JsonReader goldenCardReader = new JsonReader(new FileReader("GoldenCard.json"));
            GoldenCardDeserializer goldenCardDeserializer = new GoldenCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, goldenCardDeserializer)
                    .create();

            testGoldenCard = gson.fromJson(goldenCardReader, PlaceableCard.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initializeResourceDeck() {
        try {
            JsonReader resourceCardsReader = new JsonReader(new FileReader(resourceCardsFilename));

            ResourceCardDeserializer resourceCardDeserializer = new ResourceCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<PlaceableCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, resourceCardDeserializer)
                    .create();

            resourcesDeck = gson.fromJson(resourceCardsReader, listOfClassObject);
        }
        catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeGoldenDeck() {
        try {
            JsonReader goldenCardsReader = new JsonReader(new FileReader(goldenCardsFilename));

            GoldenCardDeserializer goldenCardDeserializer = new GoldenCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<PlaceableCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, goldenCardDeserializer)
                    .create();

            goldenDeck = gson.fromJson(goldenCardsReader, listOfClassObject);
        }
        catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Getter
     * @return Gold cards deck list
     */
    public List<PlaceableCard> getGoldenDeck() {
        return goldenDeck;
    }

    /**
     * Getter
     * @return Objective cards deck list
     */
    public List<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    /**
     * Getter
     * @return Starter cards deck list
     */
    public List<StarterCard> getStarterDeck() {
        return starterDeck;
    }

    /**
     * Getter
     * @return Resource cards deck list
     */
    public List<PlaceableCard> getResourcesDeck() {
        return resourcesDeck;
    }
}