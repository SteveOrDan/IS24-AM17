package com.example.pf_soft_ing.player;

public enum TokenColors {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    BLACK;

    public static TokenColors getColorFromInt(int n) {
        for (TokenColors color : TokenColors.values()) {
            if (color.ordinal() == n) {
                return color;
            }
        }
        return null;
    }
}
