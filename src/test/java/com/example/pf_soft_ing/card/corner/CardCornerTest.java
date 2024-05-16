package com.example.pf_soft_ing.card.corner;

import com.example.pf_soft_ing.card.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardCornerTest {
    private final CardCorner emptyCorner = new EmptyCorner();
    private final CardCorner hiddenCorner = new HiddenCorner();
    private final CardCorner aCorner = new ResourceCorner(ResourceType.ANIMAL);
    private final CardCorner fCorner = new ResourceCorner(ResourceType.FUNGI);
    private final CardCorner iCorner = new ResourceCorner(ResourceType.INSECT);
    private final CardCorner pCorner = new ResourceCorner(ResourceType.PLANT);
    private final CardCorner kCorner = new ResourceCorner(ResourceType.INKWELL);
    private final CardCorner mCorner = new ResourceCorner(ResourceType.MANUSCRIPT);
    private final CardCorner qCorner = new ResourceCorner(ResourceType.QUILL);

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