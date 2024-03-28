package com.example.pf_soft_ing;

import static org.junit.jupiter.api.Assertions.*;

import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import org.junit.jupiter.api.Test;

class GameResourcesTest {
    @Test
    void cornerDeserialization() {
        GameResources gameResources = new GameResources();

        GameResources.deserializeCorner();

        assertInstanceOf(ResourceCorner.class, GameResources.testCorner);
    }

    @Test
    void FrontDeserialization() {
        GameResources gameResources = new GameResources();

        GameResources.deserializeFront();

        assertInstanceOf(Front.class, GameResources.testFront);
    }

    @Test
    void BackDeserialization() {
        GameResources gameResources = new GameResources();

        GameResources.deserializeBack();

        assertInstanceOf(Back.class, GameResources.testBack);
    }

    @Test
    void SideDeserialization() {
        GameResources gameResources = new GameResources();

        GameResources.deserializeSide();

        assertInstanceOf(Back.class, GameResources.testBack);
    }

    @Test
    void ResourceCardDeserialization() {
        GameResources gameResources = new GameResources();

        GameResources.deserializeResourceCard();

        assertInstanceOf(ResourceCard.class, GameResources.testResourceCard);
    }

    @Test
    void resourceCardDeckDeserialization() {
        GameResources gameResources = new GameResources();

        GameResources.initializeResourceDeck();

        assertNotNull(gameResources.getResourcesDeck());
        assertEquals(2, gameResources.getResourcesDeck().size());
    }
}