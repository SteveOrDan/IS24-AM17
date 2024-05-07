package com.example.pf_soft_ing.network.messages.requests;

import com.example.pf_soft_ing.network.messages.Message;

public class ChooseSecretObjMsg extends Message {

    private final int cardID;

    public ChooseSecretObjMsg(int cardID){
        this.cardID = cardID;
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
