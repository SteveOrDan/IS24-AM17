package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;
import java.util.List;

public abstract class PlaceableCard{
    private final int id;
    private Side currSide;
    private int priority;
    private final Side front;
    private final Side back;

    public PlaceableCard(int priority, int id, Side front, Side back){
        this.id = id;
        this.priority = priority;
        this.front = front;
        this.back = back;
    }

    @Override
    public String toString(){
        return "Placeable card";
    }

    /**
     * getter of resources
     */
    public HashMap<ResourceType, Integer> getResources(){
        return null;
    }

    public int getPriority() {
        return priority;
    }

    public Side getBack() {
        return back;
    }

    public Side getFront() {
        return front;
    }

    public Side getCurrSide() {
        return currSide;
    }

//    public void exportCard(){
//        Gson gson = new Gson();
//
////        String myJson = gson.toJson(card);
////        System.out.println(myJson);
//
//        try (FileWriter writer = new FileWriter("test.json")){
//            gson.toJson(this, writer);
//        }
//        catch (IOException e){
//            throw new RuntimeException(e);
//        }
//    }

    public int getId() {
        return id;
    }
}