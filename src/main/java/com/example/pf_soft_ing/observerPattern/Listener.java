package com.example.pf_soft_ing.observerPattern;

public interface Listener {
    /**
     * Updates the listener's state based on changes in the observable
     */
    void update(Object data);
}
