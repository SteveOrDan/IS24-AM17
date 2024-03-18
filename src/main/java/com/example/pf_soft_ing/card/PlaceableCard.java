package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;
import java.util.List;

public abstract class PlaceableCard extends Card{
    public Side currSide;
    public int points;
    public int priority;
    public Side front;
    public Side back;
    public HashMap<ResourceType, Integer> requiredResources;

    public PlaceableCard(int points, int priority, int id, Side front, Side back, HashMap<ResourceType, Integer> requiredResources){
        super(id);
        this.points = points;
        this.priority = priority;
        this.front = front;
        this.back = back;
        this.requiredResources = requiredResources;
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

    public int getPoints() {
        return points;
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

    public HashMap<ResourceType, Integer> getRequiredResources(){
        return requiredResources;
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

    public abstract boolean isGolden();
}
