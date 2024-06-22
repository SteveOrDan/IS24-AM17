package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.utils.CC;

public enum ResourceType {
    ANIMAL(0, "A", CC.BLUE),
    PLANT(1, "P", CC.GREEN),
    FUNGI(2, "F", CC.RED),
    INSECT(3, "I", CC.PURPLE),
    QUILL(4, "Q", CC.YELLOW),
    INKWELL(5, "K", CC.YELLOW),
    MANUSCRIPT(6, "M", CC.YELLOW);

    private final int value;

    private final String str;

    private final String color;

    ResourceType(int value, String str, String color){
        this.value = value;
        this.str = str;
        this.color = color;
    }

    /**
     * Getter
     * @return Index for playerModel resource array
     */
    public int getValue(){
        return value;
    }

    /**
     * Getter
     * @return String value for TUI output
     */
    @Override
    public String toString() {
        return str;
    }

    /**
     * Getter
     * @return Color value to display in TUI
     */
    public String getColor() {
        return color;
    }

    /**
     * @param str String to convert to ResourceType
     * @return ResourceType enum corresponding to input string
     */
    public static ResourceType resourceTypeFromString(String str){
        str = str.toLowerCase();

        return switch (str) {
            case "animal" -> ANIMAL;
            case "plant" -> PLANT;
            case "fungi" -> FUNGI;
            case "insect" -> INSECT;
            case "quill" -> QUILL;
            case "inkwell" -> INKWELL;
            case "manuscript" -> MANUSCRIPT;
            default -> throw new IllegalArgumentException("Invalid resource type: " + str);
        };
    }
}
