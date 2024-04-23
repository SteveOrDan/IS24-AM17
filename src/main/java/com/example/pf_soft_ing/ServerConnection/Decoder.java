package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.PlayerModel;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.Token;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

import static com.example.pf_soft_ing.game.GameResources.getPlaceableCardByID;

public abstract class Decoder {

    private final MatchController matchController;

    private final PlayerModel playerModel;

    public Decoder(MatchController matchController, PlayerModel playerModel) {
        this.matchController = matchController;
        this.playerModel = playerModel;
    }

    public void placeCard(int id, Position pos){
        placeCardDecoded(getPlaceableCardByID(id), pos);
    }

    protected void placeCardDecoded(PlaceableCard card, Position pos){
        int playerId = 0; //Provvisorio
        matchController.placeCard(playerId, playerId, pos);
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
        playerModel.requestError();
    }
}
