package com.example.pf_soft_ing.ObserverPattern;

public interface Observable {
    /**
     * Adds listener to the list of listeners
     * @param listener an object that needs to be updated
     */
    void addListener (Listener listener);

    /**
     * Removes listener from the list of listeners
     * @param listener an object that needs to be updated
     */
    void removeListener (Listener listener);

    /**
     * Updates all the listeners
     */
    void updateAll(Object data);
}
