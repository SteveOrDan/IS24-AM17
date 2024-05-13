package com.example.pf_soft_ing.card;

public enum CardElementType {
    STARTER("S", CC.YELLOW),
    ANIMAL("A", CC.BLUE),
    PLANT("P", CC.GREEN),
    FUNGI("F", CC.RED),
    INSECT("I", CC.PURPLE);

    /**
     * string value for TUI output
     */
    private final String str;

    private final String color;

    CardElementType(String str, String color){
        this.str = str;
        this.color = color;
    }

    @Override
    public String toString() {
        return str;
    }

    /**
     * @return Color value to display in TUI
     */
    public String getColor() {
        return color;
    }

    public static CardElementType cardElementTypeFromString(String cardElementType) {

        cardElementType = cardElementType.toLowerCase();

        return switch (cardElementType) {
            case "starter", "s" -> CardElementType.STARTER;
            case "animal", "a" -> CardElementType.ANIMAL;
            case "plant", "p" -> CardElementType.PLANT;
            case "fungi", "f" -> CardElementType.FUNGI;
            case "insect", "i" -> CardElementType.INSECT;
            default -> null;
        };
    }
}
