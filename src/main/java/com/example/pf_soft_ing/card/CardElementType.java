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
            case "starter" -> CardElementType.STARTER;
            case "animal" -> CardElementType.ANIMAL;
            case "plant" -> CardElementType.PLANT;
            case "fungi" -> CardElementType.FUNGI;
            case "insect" -> CardElementType.INSECT;
            default -> null;
        };
    }
}
