package com.example.pf_soft_ing.card;

public enum ResourceType {
    ANIMAL(0, "A", CC.BLUE, CC.BLUE_BG),
    PLANT(1, "P", CC.GREEN, CC.GREEN_BG),
    FUNGI(2, "F", CC.RED, CC.RED_BG),
    INSECT(3, "I", CC.PURPLE, CC.PURPLE_BG),
    QUILL(4, "Q", CC.YELLOW, CC.YELLOW_BG),
    INKWELL(5, "K", CC.YELLOW, CC.YELLOW_BG),
    MANUSCRIPT(6, "M", CC.YELLOW, CC.YELLOW_BG);

    /**
     * index for playerModel resource array
     */
    private final int value;

    /**
     * string value for TUI output
     */
    private final String str;

    private final String color;

    private final String bgColor;

    /**
     * Constructor for ResourceType enum
     * @param value Initialize value as resource arr index
     * @param str Initialize value as TUI string output
     */
    ResourceType(int value, String str, String color, String bgColor){
        this.value = value;
        this.str = str;
        this.color = color;
        this.bgColor = bgColor;
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
     * @return Color value to display in TUI
     */
    public String getColor() {
        return color;
    }

    /**
     * @return Bg color to display in TUI
     */
    public String getBgColor() {
        return bgColor;
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
