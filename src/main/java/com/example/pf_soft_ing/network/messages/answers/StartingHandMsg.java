package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.player.TokenColors;

public class StartingHandMsg extends Message {

    private final int resourceCardID1;
    private final int resourceCardID2;
    private final int goldenCardID;

    private final TokenColors tokenColor;

    private final int commonObjectiveCardID1;
    private final int commonObjectiveCardID2;
    private final int secretObjectiveCardID1;
    private final int secretObjectiveCardID2;

    public StartingHandMsg(int resourceCardID1, int resourceCardID2, int goldenCardID,
                           TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2,
                           int secretObjectiveCardID1, int secretObjectiveCardID2){
        this.resourceCardID1 = resourceCardID1;
        this.resourceCardID2 = resourceCardID2;
        this.goldenCardID = goldenCardID;

        this.tokenColor = tokenColor;

        this.commonObjectiveCardID1 = commonObjectiveCardID1;
        this.commonObjectiveCardID2 = commonObjectiveCardID2;
        this.secretObjectiveCardID1 = secretObjectiveCardID1;
        this.secretObjectiveCardID2 = secretObjectiveCardID2;
    }

    public int getResourceCardID1(){
        return resourceCardID1;
    }

    public int getResourceCardID2(){
        return resourceCardID2;
    }

    public int getGoldenCardID(){
        return goldenCardID;
    }

    public TokenColors getTokenColor() {
        return tokenColor;
    }

    public int getCommonObjectiveCardID1() {
        return commonObjectiveCardID1;
    }

    public int getCommonObjectiveCardID2() {
        return commonObjectiveCardID2;
    }

    public int getSecretObjectiveCardID1() {
        return secretObjectiveCardID1;
    }

    public int getSecretObjectiveCardID2() {
        return secretObjectiveCardID2;
    }

    @Override
    public String toString() {
        return "";
    }
}
