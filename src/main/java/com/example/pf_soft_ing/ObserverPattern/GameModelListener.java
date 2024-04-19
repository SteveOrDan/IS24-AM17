package com.example.pf_soft_ing.ObserverPattern;

public class GameModelListener implements Listener{
    private int[] decks = new int[6];
    private int[] commonObjectives = new int[2];

    @Override
    public void update(Object data) {
        int[] values = (int[]) data;
        if (values.length == 6){
            decks = values;
            //Inform other clients about the change
        } else if (values.length == 2){
            commonObjectives = values;
            //Inform other clients about the change
        }
    }
}
