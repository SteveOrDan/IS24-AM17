package com.example.pf_soft_ing.ServerConnection;


import com.example.pf_soft_ing.network.RMI.ClientRMI;
import com.example.pf_soft_ing.network.RMI.ClientRMIInterface;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.rmi.RemoteException;
import java.util.List;

public class RMISender extends Encoder {

    ClientRMIInterface client;

    public RMISender(ClientRMIInterface client){
        this.client = client;
    }


    /**
     * Method to send ID to the Player
     * @param id Player's ID
     */
    public void sendID(int id){
        try {
            client.sendID(id);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets the state for the Player
     */
    public void setState(PlayerState state){
        try {
            client.setState(state);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets the score for the Player
     */
    public void setCurrScore(int score){
        try {
            client.setCurrScore(score);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the list of IDs of objectives to choose for the Player
     */

    protected void setObjectivesToChooseEncoded(List<Integer> objectiveIDs){
        try {
            client.setObjectivesToChoose(objectiveIDs);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the color of the token for the first Player
     */

    protected void setFirstPlayerTokenEncoded(TokenColors color){
        try {
            client.setFirstPlayerToken(color);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets the token's color for the Player
     */
    protected void setTokenEncoded(TokenColors color){
        try {
            client.setToken(color);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Method to add a cardID to Player hand
     * @param id CardID to add
     */
    protected void addCardToPlayerHandEncoded(int id){
        try {
            client.addCardToPlayerHand(id);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the id of the secret objective card for the Player
     */

    protected void setSecretObjectiveEncoded(int id){
        try {
            client.setSecretObjective(id);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets the starter card for the Player
     */
    protected void setStarterCardEncoded(int id){
        try {
            client.setStarterCard(id);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to place starter card
     * @param placed boolean
     */
    public void placeStarterCard(boolean placed){
        try {
            client.placeStarterCard(placed);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to place a card
     * @param placed boolean
     */
    public void placeCard(boolean placed){
        try {
            client.placeCard(placed);
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to requestError to the Player
     */
    public void requestError(){
    }

}
