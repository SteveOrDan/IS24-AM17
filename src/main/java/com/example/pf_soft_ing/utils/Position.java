package com.example.pf_soft_ing.utils;

import java.io.Serializable;

public record Position(int x, int y) implements Serializable {

    /**
     * Getter
     *
     * @return X value of position
     */
    @Override
    public int x() {
        return x;
    }

    /**
     * Getter
     *
     * @return Y value of position
     */
    @Override
    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

}
