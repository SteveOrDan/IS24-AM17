package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.card.PlaceableCard;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameResources {

    private final static String resourceCardsFilename = "src/main/resources/JsonFiles/CardsData/ResourceCards.json";
    private final static String goldenCardsFilename = "src/main/resources/JsonFiles/CardsData/GoldenCards.json";
    private final static String starterCardsFilename = "src/main/resources/JsonFiles/CardsData/StarterCards.json";
    private final static String objectiveCardsFilename = "src/main/resources/JsonFiles/CardsData/ObjectiveCards.json";

    private static List<PlaceableCard> resourcesDeck;
    private static List<PlaceableCard> goldenDeck;
    private static List<PlaceableCard> starterDeck;
    private static List<ObjectiveCard> objectiveDeck;

    private static Map<Integer, PlaceableCard> IDToPlaceableCardMap;
    private static Map<Integer, ObjectiveCard> IDToObjectiveCardMap;

    public static CardCorner deserializeCorner() {
        try {
            JsonReader cornerReader = new JsonReader(new FileReader("src/main/resources/JsonFiles/Exports/Corner.json"));
            CornerDeserializer cornerDeserializer = new CornerDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(CardCorner.class, cornerDeserializer)
                    .create();

            return gson.fromJson(cornerReader, CardCorner.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public static Side deserializeFront() {
        try {
            JsonReader frontReader = new JsonReader(new FileReader("src/main/resources/JsonFiles/Exports/Front.json"));
            FrontDeserializer frontDeserializer = new FrontDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Front.class, frontDeserializer)
                    .create();

            return gson.fromJson(frontReader, Front.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public static Side deserializeBack() {
        try {
            JsonReader backReader = new JsonReader(new FileReader("src/main/resources/JsonFiles/Exports/Back.json"));
            BackDeserializer backDeserializer = new BackDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Back.class, backDeserializer)
                    .create();

            return gson.fromJson(backReader, Back.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public static PlaceableCard deserializeResourceCard() {
        try {
            JsonReader resourceCardReader = new JsonReader(new FileReader("src/main/resources/JsonFiles/Exports/ResourceCard.json"));
            ResourceCardDeserializer resourceCardDeserializer = new ResourceCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, resourceCardDeserializer)
                    .create();

            return gson.fromJson(resourceCardReader, PlaceableCard.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public static PlaceableCard deserializeGoldenCard() {
        try {
            JsonReader goldenCardReader = new JsonReader(new FileReader("src/main/resources/JsonFiles/Exports/GoldenCard.json"));
            GoldenCardDeserializer goldenCardDeserializer = new GoldenCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, goldenCardDeserializer)
                    .create();

            return gson.fromJson(goldenCardReader, PlaceableCard.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public static PlaceableCard deserializeStarterCard() {
        try {
            JsonReader starterCardReader = new JsonReader(new FileReader("src/main/resources/JsonFiles/Exports/StarterCard.json"));
            StarterCardDeserializer starterCardDeserializer = new StarterCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, starterCardDeserializer)
                    .create();

            return gson.fromJson(starterCardReader, PlaceableCard.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    public static ObjectiveCard deserializeObjectiveCard() {
        try {
            JsonReader objectiveCardReader = new JsonReader(new FileReader("src/main/resources/JsonFiles/Exports/ObjectiveCard.json"));
            ObjectiveCardDeserializer objectiveCardDeserializer = new ObjectiveCardDeserializer();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ObjectiveCard.class, objectiveCardDeserializer)
                    .create();

            return gson.fromJson(objectiveCardReader, ObjectiveCard.class);
        }
        catch (FileNotFoundException e) {
            return null;
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

    public static void fillCardMaps(){
        IDToPlaceableCardMap = new HashMap<>();
        IDToObjectiveCardMap = new HashMap<>();

        for (PlaceableCard card : resourcesDeck) {
            IDToPlaceableCardMap.put(card.getID(), card);
        }

        for (PlaceableCard card : goldenDeck) {
            IDToPlaceableCardMap.put(card.getID(), card);
        }

        for (PlaceableCard card : starterDeck) {
            IDToPlaceableCardMap.put(card.getID(), card);
        }

        for (ObjectiveCard card : objectiveDeck) {
            IDToObjectiveCardMap.put(card.getID(), card);
        }
    }

    public static void initializeAllDecks() {
        initializeResourceDeck();
        initializeGoldenDeck();
        initializeStarterDeck();
        initializeObjectiveDeck();

        fillCardMaps();
    }

    public static PlaceableCard getPlaceableCardByID(int ID) {
        return IDToPlaceableCardMap.get(ID);
    }

    public static ObjectiveCard getObjectiveCardByID(int ID) {
        return IDToObjectiveCardMap.get(ID);
    }

    /**
     * Getter
     * @return Resource cards deck list
     */
    public static List<PlaceableCard> getResourcesDeck() {
        return resourcesDeck;
    }

    /**
     * Getter
     * @return Gold cards deck list
     */
    public static List<PlaceableCard> getGoldenDeck() {
        return goldenDeck;
    }

    /**
     * Getter
     * @return Starter cards deck list
     */
    public static List<PlaceableCard> getStarterDeck() {
        return starterDeck;
    }

    /**
     * Getter
     * @return Objective cards deck list
     */
    public static List<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    /**
     * Getter
     * @return Map of IDs to PlaceableCards
     */
    public static Map<Integer, PlaceableCard> getIDToPlaceableCardMap() {
        return IDToPlaceableCardMap;
    }

    /**
     * Getter
     * @return Map of IDs to ObjectiveCards
     */
    public static Map<Integer, ObjectiveCard> getIDToObjectiveCardMap() {
        return IDToObjectiveCardMap;
    }
}
