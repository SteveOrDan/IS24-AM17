package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Side;

import java.util.List;

public abstract class PlaceableCard extends Card{
    public Side currSide;
    public int points;
    public int priority;
    public Side front;
    public Side back;

    public PlaceableCard(int points, int priority, int id, Side front, Side back){
        super(id);
        this.points = points;
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
    public List<ResourceType> getResources(){
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
}
