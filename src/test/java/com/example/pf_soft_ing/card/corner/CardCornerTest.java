package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.card.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardCornerTest {
    CardCorner emptyCorner = new EmptyCorner();
    CardCorner hiddenCorner = new HiddenCorner();
    CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);

    @DisplayName("Corner availability test")
    @Test
    void isAvailable() {
        assertTrue(emptyCorner.isAvailable());
        assertTrue(aCorner.isAvailable());
        assertTrue(fCorner.isAvailable());
        assertTrue(iCorner.isAvailable());
        assertTrue(pCorner.isAvailable());
        assertTrue(kCorner.isAvailable());
        assertTrue(mCorner.isAvailable());
        assertTrue(qCorner.isAvailable());
        assertFalse(hiddenCorner.isAvailable());
    }
    @DisplayName("Resource getter method test")
    @Test
    void getResource() {
        assertNull(emptyCorner.getResource());
        assertNull(hiddenCorner.getResource());
        assertEquals(ResourceType.ANIMAL, aCorner.getResource());
        assertEquals(ResourceType.FUNGI, fCorner.getResource());
        assertEquals(ResourceType.INSECT, iCorner.getResource());
        assertEquals(ResourceType.PLANT, pCorner.getResource());
        assertEquals(ResourceType.INKWELL, kCorner.getResource());
        assertEquals(ResourceType.MANUSCRIPT, mCorner.getResource());
        assertEquals(ResourceType.QUILL, qCorner.getResource());
    }
}