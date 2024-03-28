package com.example.pf_soft_ing.card;

public enum CardElementType {
    STARTER,
    ANIMAL,
    PLANT,
    FUNGI,
    INSECT;

    public static CardElementType cardElementTypeFromString(String cardElementType) {

        cardElementType = cardElementType.toLowerCase();

        return switch (cardElementType) {
            case "s", "starter" -> CardElementType.STARTER;
            case "a", "animal" -> CardElementType.ANIMAL;
            case "p", "plant" -> CardElementType.PLANT;
            case "f", "fungi" -> CardElementType.FUNGI;
            case "i", "insect" -> CardElementType.INSECT;
            default -> null;
        };
    }
}
