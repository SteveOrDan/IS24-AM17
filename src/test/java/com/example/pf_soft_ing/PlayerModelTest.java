package com.example.pf_soft_ing;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.Test;

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
    Side starterFront1 = new Front(aCorner, pCorner, iCorner, fCorner);
    Side starerBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.FUNGI);}});
    PlaceableCard starterCard1 = new StarterCard(2, starterFront1, starerBack1);

    PlayerModel playerModel = new PlayerModel("John Smith", 34);
    @Test
    void placeStarterCard() {
        playerModel.setStarterCard(starterCard1);
        starterCard1.flipCard();
        starterCard1.flipCard();
        playerModel.placeStarterCard();
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.ANIMAL.getValue()]);
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.INSECT.getValue()]);
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.PLANT.getValue()]);
        assertEquals(1, playerModel.getNumOfResourcesArr()[ResourceType.FUNGI.getValue()]);
        assertEquals(1, playerModel.getCurrMaxPriority());
        assertTrue(playerModel.getPlayArea().containsValue(starterCard1) && playerModel.getPlayArea().containsKey(new Position(0,0)));
    }

    @Test
    void placeCard() {
    }

    @Test
    void testPlaceCard() {
    }
}