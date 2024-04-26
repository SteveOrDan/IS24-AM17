package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.game.MatchController;

import static com.example.pf_soft_ing.game.GameResources.getPlaceableCardByID;

public class Decoder {

    private final MatchController matchController;
    private final int playerId;

    public Decoder(MatchController matchController, int playerId) {
        this.matchController = matchController;
        this.playerId = playerId;
    }

    public void placeCard(int id, Position pos){
        placeCardDecoded(getPlaceableCardByID(id), pos);
    }

    protected void placeCardDecoded(PlaceableCard card, Position pos){
        int playerId = 0; //Provvisorio
        //matchController.placeCard(playerId, playerId, pos);
//        matchController.placeCard(playerModel, card, pos);
    }

    public void drawResourceCard(){
        int playerId = 0; //Provvisorio
        matchController.drawResourceCard(playerId);
//        matchController.drawResourceCard(playerModel);
    }

    public void drawVisibleResourceCard(int playerID, int index){}

    public void drawGoldenCard(int playerID){}

    public void drawVisibleGoldenCard(int playerID, int index){}

    public void requestError(){
        //matchController.requestError(playerId);
        System.out.println("Error Request");
    }
}
