package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.card.side.Side;

import java.util.HashMap;
import java.util.List;

public abstract class PlaceableCard{
    private final int id;
    private final CardElementType elementType;
    private int priority;
    private Side currSide;
    private final Side front;
    private final Side back;

    public PlaceableCard(CardElementType element, int id, Side front, Side back){
        this.priority = 0;
        this.elementType = element;
        this.id = id;
        this.front = front;
        this.back = back;
    }

    @Override
    public String toString(){
        return "Placeable card";
    }

    /**
     * Getter of the resources based on the current side of the card
     * If card side is front gets the values from the corners
     * If card side is back gets the values from a list
     * @return Hash map of resources based on the current side of the card
     */
    public List<ResourceType> getResources(){
        return currSide.getResources();
    }

    /**
     * Getter
     * @return Card's priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Setter
     * @param newPriority New card's priority
     */
    public void setPriority(int newPriority){
        priority = newPriority;
    }

    /**
     * Getter
     * @return Card's element type
     */
    public CardElementType getElementType(){
        return elementType;
    }

    /**
     * Getter
     * @return Card's back side
     */
    public Side getBack() {
        return back;
    }

    /**
     * Getter
     * @return Card's front side
     */
    public Side getFront() {
        return front;
    }

    /**
     * Getter
     * @return Card's current side
     */
    public Side getCurrSide() {
        return currSide;
    }

    /**
     * Switches the card's current side from front and back and viceversa
     */
    public void flipCard(){
        if (currSide.equals(front)){
            currSide = back;
        }
        else{
            currSide = front;
        }
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

    /**
     * Getter
     * @return Card's ID
     */
    public int getId() {
        return id;
    }
}