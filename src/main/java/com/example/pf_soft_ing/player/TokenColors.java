package com.example.pf_soft_ing.player;

import javafx.scene.paint.Color;

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

    public static Color getColorFromToken(TokenColors token) {
        return switch (token) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.rgb(235, 196, 0, 1);
            case BLACK -> Color.BLACK;
        };
    }
}
