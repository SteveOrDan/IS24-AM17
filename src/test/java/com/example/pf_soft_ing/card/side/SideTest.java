package com.example.pf_soft_ing.card.side;

import com.example.pf_soft_ing.ResourceType;
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
    CardCorner emptyCorner = new EmptyCorner();
    CardCorner hiddenCorner = new HiddenCorner();
    CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);
    Side front = new Front(emptyCorner, hiddenCorner, aCorner, kCorner);
    List<ResourceType> allResourceTypesFront= new ArrayList<>() {{
        add(ResourceType.ANIMAL);
        add(ResourceType.INKWELL);
    }};

    List<ResourceType> starterPermResourceTypesBack = new ArrayList<>() {{
        add(ResourceType.PLANT);
        add(ResourceType.FUNGI);
    }};

    Side starterBack = new Back(hiddenCorner, hiddenCorner, pCorner, fCorner, starterPermResourceTypesBack);

    List<ResourceType> allStarterResourceTypesBack = new ArrayList<>() {{
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