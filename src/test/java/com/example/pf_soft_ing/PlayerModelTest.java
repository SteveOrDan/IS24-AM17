package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.exceptions.NoAdjacentCardsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerModelTest {
    CardCorner emptyCorner = new EmptyCorner();
    CardCorner hiddenCorner = new HiddenCorner();
    CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);
    Side starterFront1 = new Front(hiddenCorner, pCorner, iCorner, fCorner);
    Side starerBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.FUNGI);}});
    PlaceableCard starterCard1 = new StarterCard(2, starterFront1, starerBack1);
    int resourceCardPoints = 2;
    Side normalFront = new Front(aCorner, kCorner, mCorner, qCorner);
    Side normalBack = new Back(emptyCorner, emptyCorner, emptyCorner,emptyCorner, new ArrayList<>(){{add(ResourceType.ANIMAL);}});
    ResourceCard normalCard = new ResourceCard(resourceCardPoints, CardElementType.ANIMAL, 3, normalFront, normalBack);
    PlayerModel playerModel = new PlayerModel("John Smith", 34);

    @DisplayName("Test for placing a starter card in the player area")
    @Test
    void placeStarterCard() {
        playerModel.setStarterCard(starterCard1);
        starterCard1.flipCard();
        starterCard1.flipCard();
        playerModel.placeStarterCard();
        assertEquals(0, playerModel.getCurrScore());
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.INSECT.getValue()]);
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.PLANT.getValue()]);
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.FUNGI.getValue()]);
        assertEquals(1, playerModel.getCurrMaxPriority());
        assertTrue(playerModel.getPlayArea().containsValue(starterCard1) && playerModel.getPlayArea().containsKey(new Position(0,0)));
    }

    @DisplayName("Test for placing a card in a valid position")
    @Test
    void placeCardInValidPos() {
        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard();
        assertEquals(0, playerModel.getCurrScore());
        //Now place a card in a valid position
        Position pos = new Position(1,1);
        playerModel.placeCard(normalCard, pos);
        assertTrue(playerModel.getPlayArea().containsValue(normalCard) &&
                playerModel.getPlayArea().containsKey(pos) &&
                playerModel.getCurrScore() == resourceCardPoints
                );
    }

    @DisplayName("Test for placing a card in an invalid position")
    @Test
    void placeCardInInvalidPos() {
        //Start by placing a starter card in the play area
        playerModel.setStarterCard(starterCard1);
        playerModel.placeStarterCard();
        assertEquals(0, playerModel.getCurrScore());
        //Now place a card in a valid position
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(1,0);
        Position pos3 = new Position(-1,-1);
        assertDoesNotThrow(() -> playerModel.placeCard(normalCard, pos1));
        assertDoesNotThrow(() -> playerModel.placeCard(normalCard, pos2));
        assertDoesNotThrow(() -> playerModel.placeCard(normalCard, pos3));
    }
}