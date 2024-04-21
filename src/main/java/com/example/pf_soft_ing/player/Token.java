package com.example.pf_soft_ing.player;

public class Token {
    private final TokenColors color;

    public Token(TokenColors color) {
        this.color = color;
    }

    /**
     * Getter
     * @return Token color
     */
    public TokenColors getColor() {
        return color;
    }
}
