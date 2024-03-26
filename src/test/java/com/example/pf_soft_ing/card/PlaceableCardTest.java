package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlaceableCardTest {
    CardCorner emptyCorner = new EmptyCorner();
    CardCorner hiddenCorner = new HiddenCorner();
    CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);
    Side resourceFront1 = new Front(emptyCorner, hiddenCorner, iCorner, kCorner);
    Side resourceBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.INSECT);}});
    PlaceableCard resourceCard1 = new ResourceCard(0, CardElementType.INSECT, 0, resourceFront1, resourceBack1);

    Side goldenFront1 = new Front(emptyCorner, hiddenCorner, qCorner, mCorner);
    Side goldenBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.ANIMAL);}});
    PlaceableCard goldenCard1 = new GoldenCard(CardElementType.ANIMAL, 1, goldenFront1, goldenBack1, 3, new HashMap<>(){{put(ResourceType.ANIMAL, 3);}});

    Side starterFront1 = new Front(aCorner, pCorner, iCorner, fCorner);
    Side starerBack1 = new Back(emptyCorner, emptyCorner, emptyCorner, emptyCorner, new ArrayList<>(){{add(ResourceType.FUNGI);}});
    PlaceableCard starterCard1 = new StarterCard(2, starterFront1, starerBack1);

    @Test
    void flipCard() {
    }
}