package com.example.pf_soft_ing;

public enum ResourceType {
    ANIMAL(0, "A"),
    PLANT(1, "P"),
    FUNGI(2, "F"),
    INSECT(3, "I"),
    QUILL(4, "Q"),
    INKWELL(5, "K"),
    MANUSCRIPT(6, "M");

    /**
     * index for playerModel resource array
     */
    private final int value;

    /**
     * string value for TUI output
     */
    private final String str;

    /**
     * Constructor for ResourceType enum
     * @param value Initialize value as resource arr index
     * @param str Initialize value as TUI string output
     */
    ResourceType(int value, String str){
        this.value = value;
        this.str = str;
    }

    /**
     * @return Index value
     */
    public int getValue(){
        return value;
    }

    /**
     * @return String value to display in TUI
     */
    @Override
    public String toString() {
        return str;
    }

    /**
     * @param str String to convert to ResourceType
     * @return ResourceType enum corresponding to input string
     */
    public static ResourceType resourceTypeFromString(String str){
        str = str.toLowerCase();

        return switch (str) {
            case "a", "animal" -> ANIMAL;
            case "p", "plant" -> PLANT;
            case "f", "fungi" -> FUNGI;
            case "i", "insect" -> INSECT;
            case "q", "quill" -> QUILL;
            case "k", "inkwell" -> INKWELL;
            case "m", "manuscript" -> MANUSCRIPT;
            default -> throw new IllegalArgumentException("Invalid resource type: " + str);
        };
    }
}
