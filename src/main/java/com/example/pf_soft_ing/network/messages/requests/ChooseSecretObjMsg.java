package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class ChooseSecretObjMsg extends Message {

    private final int playerID;
    private final int cardID;

    public ChooseSecretObjMsg(int playerID, int cardID){
        this.playerID = playerID;
        this.cardID = cardID;
    }

    /**
     * Getter
     * @return ID of the player
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Getter
     * @return ID of the chosen objective
     */
    public int getCardID(){
        return cardID;
    }

    @Override
    public String toString() {
        return "";
    }
}
