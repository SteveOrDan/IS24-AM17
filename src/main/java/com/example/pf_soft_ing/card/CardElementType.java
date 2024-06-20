package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.utils.CC;

public enum CardElementType {
    STARTER("S", CC.YELLOW, CC.YELLOW_BG),
    ANIMAL("A", CC.BLUE, CC.BLUE_BG),
    PLANT("P", CC.GREEN, CC.GREEN_BG),
    FUNGI("F", CC.RED, CC.RED_BG),
    INSECT("I", CC.PURPLE, CC.PURPLE_BG);

    /**
     * string value for TUI output
     */
    private final String str;

    private final String color;

    private final String bgColor;

    CardElementType(String str, String color, String bgColor){
        this.str = str;
        this.color = color;
        this.bgColor = bgColor;
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

    /**
     * @return Background color value to display in TUI
     */
    public String getBgColor() {
        return bgColor;
    }

    /**
     * @param cardElementType String representation of card element type
     * @return CardElementType enum value
     */
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
