package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.deserializers.corner.CornerDeserializer;
import com.example.pf_soft_ing.deserializers.objective_card.ObjectiveCardDeserializer;
import com.example.pf_soft_ing.deserializers.placeable_card.GoldenCardDeserializer;
import com.example.pf_soft_ing.deserializers.placeable_card.ResourceCardDeserializer;
import com.example.pf_soft_ing.deserializers.placeable_card.StarterCardDeserializer;
import com.example.pf_soft_ing.deserializers.side.*;
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
    private final static String resourceCardsFilename = "JsonFiles/CardsData/ResourceCards.json";
    private final static String goldenCardsFilename = "JsonFiles/CardsData/GoldenCards.json";
    private final static String starterCardsFilename = "JsonFiles/CardsData/StarterCards.json";
    private final static String objectiveCardsFilename = "JsonFiles/CardsData/ObjectiveCards.json";

    private static List<PlaceableCard> resourcesDeck;
    private static List<PlaceableCard> goldenDeck;
    private static List<PlaceableCard> starterDeck;
    private static List<ObjectiveCard> objectiveDeck;

    public static CardCorner testCorner;
    public static Side testFront;
    public static Side testBack;
    public static PlaceableCard testResourceCard;
    public static PlaceableCard testGoldenCard;
    public static PlaceableCard testStarterCard;
    public static ObjectiveCard testObjectiveCard;

    private List<Token> tokensList;

    public static void main(String[] args) {
//        deserializeCorner();
//        deserializeFront();
//        deserializeBack();
//        deserializeSide();
//        deserializeResourceCard();
//        initializeResourceDeck();
//        deserializeGoldenCard();
//        deserializeObjectiveCard();
//        deserializeStarterCard();
    }

    public static void deserializeCorner() {
        try {
            JsonReader cornerReader = new JsonReader(new FileReader("JsonFiles/Exports/Corner.json"));
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
            JsonReader frontReader = new JsonReader(new FileReader("JsonFiles/Exports/Front.json"));
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
            JsonReader backReader = new JsonReader(new FileReader("JsonFiles/Exports/Back.json"));
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
            JsonReader sideReader = new JsonReader(new FileReader("JsonFiles/Exports/Back.json"));
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
            JsonReader resourceCardReader = new JsonReader(new FileReader("JsonFiles/Exports/ResourceCard.json"));
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
            JsonReader goldenCardReader = new JsonReader(new FileReader("JsonFiles/Exports/GoldenCard.json"));
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

    public static void deserializeStarterCard() {
        try {
            JsonReader starterCardReader = new JsonReader(new FileReader("JsonFiles/Exports/StarterCard.json"));
            StarterCardDeserializer starterCardDeserializer = new StarterCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, starterCardDeserializer)
                    .create();

            testStarterCard = gson.fromJson(starterCardReader, PlaceableCard.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deserializeObjectiveCard() {
        try {
            JsonReader objectiveCardReader = new JsonReader(new FileReader("JsonFiles/Exports/ObjectiveCard.json"));
            ObjectiveCardDeserializer objectiveCardDeserializer = new ObjectiveCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ObjectiveCard.class, objectiveCardDeserializer)
                    .create();

            testObjectiveCard = gson.fromJson(objectiveCardReader, ObjectiveCard.class);
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

    public static void initializeStarterDeck() {
        try {
            JsonReader starterCardsReader = new JsonReader(new FileReader(starterCardsFilename));

            StarterCardDeserializer starterCardDeserializer = new StarterCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<PlaceableCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, starterCardDeserializer)
                    .create();

            starterDeck = gson.fromJson(starterCardsReader, listOfClassObject);
        }
        catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeObjectiveDeck() {
        try {
            JsonReader objectiveCardsReader = new JsonReader(new FileReader(objectiveCardsFilename));

            ObjectiveCardDeserializer objectiveCardDeserializer = new ObjectiveCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<ObjectiveCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ObjectiveCard.class, objectiveCardDeserializer)
                    .create();

            objectiveDeck = gson.fromJson(objectiveCardsReader, listOfClassObject);
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
    public List<PlaceableCard> getStarterDeck() {
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