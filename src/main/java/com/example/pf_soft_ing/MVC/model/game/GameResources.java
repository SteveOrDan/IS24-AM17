package com.example.pf_soft_ing.MVC.model.game;

import com.example.pf_soft_ing.Main;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;

import com.example.pf_soft_ing.JSONDeserializers.corner.CornerDeserializer;
import com.example.pf_soft_ing.JSONDeserializers.objective_card.ObjectiveCardDeserializer;
import com.example.pf_soft_ing.JSONDeserializers.placeable_card.GoldenCardDeserializer;
import com.example.pf_soft_ing.JSONDeserializers.placeable_card.ResourceCardDeserializer;
import com.example.pf_soft_ing.JSONDeserializers.placeable_card.StarterCardDeserializer;
import com.example.pf_soft_ing.JSONDeserializers.side.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class GameResources {

    private final static String resourceCardsFilename = "/JsonFiles/CardsData/ResourceCards.json"; // src/main/resources
    private final static String goldenCardsFilename = "/JsonFiles/CardsData/GoldenCards.json";
    private final static String starterCardsFilename = "/JsonFiles/CardsData/StarterCards.json";
    private final static String objectiveCardsFilename = "/JsonFiles/CardsData/ObjectiveCards.json";

    private static List<PlaceableCard> resourcesDeck;
    private static List<PlaceableCard> goldenDeck;
    private static List<PlaceableCard> starterDeck;
    private static List<ObjectiveCard> objectiveDeck;

    private static Map<Integer, PlaceableCard> IDToPlaceableCardMap;
    private static Map<Integer, ObjectiveCard> IDToObjectiveCardMap;

    /**
     * Deserialize the corner of a card
     * @return CardCorner object
     */
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

    /**
     * Deserialize the front side of a card
     * @return Front object as Side
     */
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

    /**
     * Deserialize the back side of a card
     * @return Back object as Side
     */
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

    /**
     * Deserialize a resource card
     * @return PlaceableCard object
     */
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

    /**
     * Deserialize a golden card
     * @return PlaceableCard object
     */
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

    /**
     * Deserialize a starter card
     * @return PlaceableCard object
     */
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

    /**
     * Deserialize an objective card
     * @return ObjectiveCard object
     */
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

    /**
     * Initializes the resource deck
     */
    public static void initializeResourceDeck() {
        try {
            JsonReader resourceCardsReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream(resourceCardsFilename))));

            ResourceCardDeserializer resourceCardDeserializer = new ResourceCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<PlaceableCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, resourceCardDeserializer)
                    .create();

            resourcesDeck = gson.fromJson(resourceCardsReader, listOfClassObject);
        }
        catch (JsonIOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initializes the golden deck
     */
    public static void initializeGoldenDeck() {
        try {
            JsonReader goldenCardsReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream(goldenCardsFilename))));

            GoldenCardDeserializer goldenCardDeserializer = new GoldenCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<PlaceableCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, goldenCardDeserializer)
                    .create();

            goldenDeck = gson.fromJson(goldenCardsReader, listOfClassObject);
        }
        catch (JsonIOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initializes the starter deck
     */
    public static void initializeStarterDeck() {
        try {
            JsonReader starterCardsReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream(starterCardsFilename))));

            StarterCardDeserializer starterCardDeserializer = new StarterCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<PlaceableCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(PlaceableCard.class, starterCardDeserializer)
                    .create();

            starterDeck = gson.fromJson(starterCardsReader, listOfClassObject);
        }
        catch (JsonIOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initializes the objective deck
     */
    public static void initializeObjectiveDeck() {
        try {
            JsonReader objectiveCardsReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream(objectiveCardsFilename))));

            ObjectiveCardDeserializer objectiveCardDeserializer = new ObjectiveCardDeserializer();

            Type listOfClassObject = new TypeToken<ArrayList<ObjectiveCard>>() {}.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ObjectiveCard.class, objectiveCardDeserializer)
                    .create();

            objectiveDeck = gson.fromJson(objectiveCardsReader, listOfClassObject);
        }
        catch (JsonIOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Fills the maps with IDs to cards
     */
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

    /**
     * Initializes all the decks and fills the maps
     */
    public static void initializeAllDecks() {
        initializeResourceDeck();
        initializeGoldenDeck();
        initializeStarterDeck();
        initializeObjectiveDeck();

        fillCardMaps();
    }

    /**
     * Getter
     * @param ID ID of the card
     * @return PlaceableCard with the given ID
     */
    public static PlaceableCard getPlaceableCardByID(int ID) {
        return IDToPlaceableCardMap.get(ID);
    }

    /**
     * Getter
     * @param ID ID of the card
     * @return ObjectiveCard with the given ID
     */
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
}
