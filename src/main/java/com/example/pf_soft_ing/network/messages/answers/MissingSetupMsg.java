package com.example.pf_soft_ing.network.messages.answers;

import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.MVC.model.player.TokenColors;

public class MissingSetupMsg extends Message {

    private final int resourceCardID1, resourceCardID2, goldenCardID, commonObjectiveCardID1, commonObjectiveCardID2, secretObjectiveCardID1, secretObjectiveCardID2;
    private final TokenColors tokenColor;

    public MissingSetupMsg(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                           int commonObjectiveCardID1, int commonObjectiveCardID2,
                           int secretObjectiveCardID1, int secretObjectiveCardID2) {
        this.resourceCardID1 = resourceCardID1;
        this.resourceCardID2 = resourceCardID2;
        this.goldenCardID = goldenCardID;
        this.tokenColor = tokenColor;
        this.commonObjectiveCardID1 = commonObjectiveCardID1;
        this.commonObjectiveCardID2 = commonObjectiveCardID2;
        this.secretObjectiveCardID1 = secretObjectiveCardID1;
        this.secretObjectiveCardID2 = secretObjectiveCardID2;
    }

    /**
     * Getter for the resourceCardID1
     * @return resourceCardID1
     */
    public int getResourceCardID1() {
        return resourceCardID1;
    }

    /**
     * Getter for the resourceCardID2
     * @return resourceCardID2
     */
    public int getResourceCardID2() {
        return resourceCardID2;
    }

    /**
     * Getter for the goldenCardID
     * @return goldenCardID
     */
    public int getGoldenCardID() {
        return goldenCardID;
    }

    /**
     * Getter for the commonObjectiveCardID1
     * @return commonObjectiveCardID1
     */
    public int getCommonObjectiveCardID1() {
        return commonObjectiveCardID1;
    }

    /**
     * Getter for the commonObjectiveCardID2
     * @return commonObjectiveCardID2
     */
    public int getCommonObjectiveCardID2() {
        return commonObjectiveCardID2;
    }

    /**
     * Getter for the secretObjectiveCardID1
     * @return secretObjectiveCardID1
     */
    public int getSecretObjectiveCardID1() {
        return secretObjectiveCardID1;
    }

    /**
     * Getter for the secretObjectiveCardID2
     * @return secretObjectiveCardID2
     */
    public int getSecretObjectiveCardID2() {
        return secretObjectiveCardID2;
    }

    /**
     * Getter for the tokenColor
     * @return tokenColor
     */
    public TokenColors getTokenColor() {
        return tokenColor;
    }

    @Override
    public String toString() {
        return "";
    }
}
