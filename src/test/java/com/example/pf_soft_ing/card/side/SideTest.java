package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SideTest {
    private final CardCorner emptyCorner = new EmptyCorner();
    private final CardCorner hiddenCorner = new HiddenCorner();
    private final CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    private final CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    private final CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    private final CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    private final Side front = new Front(emptyCorner, hiddenCorner, aCorner, kCorner);
    private final List<ResourceType> allResourceTypesFront= new ArrayList<>() {{
        add(ResourceType.ANIMAL);
        add(ResourceType.INKWELL);
    }};

    private final List<ResourceType> starterPermResourceTypesBack = new ArrayList<>() {{
        add(ResourceType.PLANT);
        add(ResourceType.FUNGI);
    }};

    private final Side starterBack = new Back(hiddenCorner, hiddenCorner, pCorner, fCorner, starterPermResourceTypesBack);

    private final List<ResourceType> allStarterResourceTypesBack = new ArrayList<>() {{
        add(ResourceType.PLANT);
        add(ResourceType.FUNGI);
        add(ResourceType.FUNGI);
        add(ResourceType.PLANT);
    }};

    @DisplayName("Resource getter test")
    @Test
    void getResources() {
        assertTrue(allResourceTypesFront.containsAll(front.getResources()) && front.getResources().containsAll(allResourceTypesFront));
        assertTrue(allStarterResourceTypesBack.containsAll(starterBack.getResources()) && starterBack.getResources().containsAll(allStarterResourceTypesBack));
    }
}