package com.example.pf_soft_ing;

public enum TokenColors {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    BLACK;

    private final int value;

    TokenColors() {
        this.value = this.ordinal();
    }

    public int getValue() {
        return value;
    }

    public static TokenColors getColorFromInt(int n) {
        for (TokenColors color : TokenColors.values()) {
            if (color.ordinal() == n) {
                return color;
            }
        }
        return null;
    }
}
