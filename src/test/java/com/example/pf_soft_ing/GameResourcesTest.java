package com.example.pf_soft_ing;

import static org.junit.jupiter.api.Assertions.*;

import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.card.GoldenCard;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.objectiveCards.ResourcesCountObjectiveCard;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.Test;

class GameResourcesTest {
    @Test
    void cornerDeserialization() {
        CardCorner testCorner = GameResources.deserializeCorner();

        assertInstanceOf(ResourceCorner.class, testCorner);
    }

    @Test
    void FrontDeserialization() {
        Side testFront = GameResources.deserializeFront();

        assertInstanceOf(Front.class, testFront);
    }

    @Test
    void BackDeserialization() {
        Side testBack = GameResources.deserializeBack();

        assertInstanceOf(Back.class, testBack);
    }

    @Test
    void ResourceCardDeserialization() {
        PlaceableCard testResourceCard = GameResources.deserializeResourceCard();

        assertInstanceOf(ResourceCard.class, testResourceCard);
    }

    @Test
    void GoldenCardDeserialization() {
        PlaceableCard testGoldenCard = GameResources.deserializeGoldenCard();

        assertInstanceOf(GoldenCard.class, testGoldenCard);
    }

    @Test
    void StarterCardDeserialization() {
        PlaceableCard testStarterCard = GameResources.deserializeStarterCard();

        assertInstanceOf(StarterCard.class, testStarterCard);
    }

    @Test
    void ObjectiveCardDeserialization() {
        ObjectiveCard testObjectiveCard = GameResources.deserializeObjectiveCard();

        assertInstanceOf(ResourcesCountObjectiveCard.class, testObjectiveCard);
    }

    @Test
    void resourceCardDeckDeserialization() {
        GameResources.initializeResourceDeck();

        assertNotNull(GameResources.getResourcesDeck());
        assertEquals(40, GameResources.getResourcesDeck().size());
    }

    @Test
    void goldenCardDeckDeserialization() {
        GameResources.initializeGoldenDeck();

        assertNotNull(GameResources.getGoldenDeck());
        assertEquals(40, GameResources.getGoldenDeck().size());
    }

    @Test
    void starterCardDeckDeserialization() {
        GameResources.initializeStarterDeck();

        assertNotNull(GameResources.getStarterDeck());
        assertEquals(6, GameResources.getStarterDeck().size());
    }

    @Test
    void objectiveCardDeckDeserialization() {
        GameResources.initializeObjectiveDeck();

        assertNotNull(GameResources.getObjectiveDeck());
        assertEquals(16, GameResources.getObjectiveDeck().size());
    }

    @Test
    void testCardsBack() {
        GameResources.initializeAllDecks();

        for (PlaceableCard card : GameResources.getResourcesDeck()) {
            assertEquals(EmptyCorner.class, card.getBack().getTLCorner().getClass());
            assertEquals(EmptyCorner.class, card.getBack().getTRCorner().getClass());
            assertEquals(EmptyCorner.class, card.getBack().getBLCorner().getClass());
            assertEquals(EmptyCorner.class, card.getBack().getBRCorner().getClass());
        }

        for (PlaceableCard card : GameResources.getGoldenDeck()) {
            assertEquals(EmptyCorner.class, card.getBack().getTLCorner().getClass());
            assertEquals(EmptyCorner.class, card.getBack().getTRCorner().getClass());
            assertEquals(EmptyCorner.class, card.getBack().getBLCorner().getClass());
            assertEquals(EmptyCorner.class, card.getBack().getBRCorner().getClass());
        }
    }
}