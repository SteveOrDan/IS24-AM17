package com.example.pf_soft_ing.network.server.RMI;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.client.RMI.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIReceiverInterface extends Remote {

    /**
     * Connects the client to the server.
     * @param client Client to connect
     * @throws RemoteException If an error occurs during the connection
     */
    void connect(ClientRMIInterface client) throws RemoteException;

    /**
     * Requests the list of matches.
     * @param playerID ID of the player that sent the request
     * @throws RemoteException If an error occurs during the request
     */
    void getMatches(int playerID) throws RemoteException;

    /**
     * Requests the creation of a new match.
     * @param playerID ID of the player that sent the request
     * @param numberOfPlayers Number of players in the match
     * @param nickname Nickname of the player
     * @throws RemoteException If an error occurs during the request
     */
    void createMatch(int playerID, int numberOfPlayers, String nickname) throws RemoteException;

    /**
     * Requests to join a match.
     * @param playerID ID of the player that sent the request
     * @param matchID ID of the match
     * @throws RemoteException If an error occurs during the request
     */
    void selectMatch(int playerID, int matchID) throws RemoteException;

    /**
     * Requests to choose a nickname.
     * @param playerID ID of the player that sent the request
     * @param nickname Chosen nickname
     * @throws RemoteException If an error occurs during the request
     */
    void chooseNickname(int playerID, String nickname) throws RemoteException;

    /**
     * Requests to reconnect to a match.
     * @param playerID ID of the player that sent the request
     * @param nickname Nickname of the player in the match
     * @param matchID ID of the match
     * @throws RemoteException If an error occurs during the request
     */
    void reconnectToMatch(int playerID, String nickname, int matchID) throws RemoteException;

    /**
     * Requests to place the starter card.
     * @param playerID ID of the player that sent the request
     * @param side Side of the card
     * @throws RemoteException If an error occurs during the request
     */
    void placeStarterCard(int playerID, CardSideType side) throws RemoteException;

    /**
     * Requests to choose the secret objective card.
     * @param playerID ID of the player that sent the request
     * @param cardID ID of the card
     * @throws RemoteException If an error occurs during the request
     */
    void chooseSecretObj(int playerID, int cardID) throws RemoteException;

    /**
     * Requests to place a card.
     * @param playerID ID of the player that sent the request
     * @param cardID ID of the card
     * @param side Side of the card
     * @param pos Position to place the card
     * @throws RemoteException If an error occurs during the request
     */
    void placeCard(int playerID, int cardID, CardSideType side, Position pos) throws RemoteException;

    /**
     * Requests to draw a resource card.
     * @param playerID ID of the player that sent the request
     * @throws RemoteException If an error occurs during the request
     */
    void drawResourceCard(int playerID) throws RemoteException;

    /**
     * Requests to draw a visible resource card.
     * @param playerID ID of the player that sent the request
     * @param index Index of the card to draw. (Either 0 or 1)
     * @throws RemoteException If an error occurs during the request
     */
    void drawVisibleResourceCard(int playerID, int index) throws RemoteException;

    /**
     * Requests to draw a golden card.
     * @param playerID ID of the player that sent the request
     * @throws RemoteException If an error occurs during the request
     */
    void drawGoldenCard(int playerID) throws RemoteException;

    /**
     * Requests to draw a visible golden card.
     * @param playerID ID of the player that sent the request
     * @param index Index of the card to draw. (Either 0 or 1)
     * @throws RemoteException If an error occurs during the request
     */
    void drawVisibleGoldenCard(int playerID, int index) throws RemoteException;

    /**
     * Requests to send a chat message.
     * @param playerID ID of the player that sent the request
     * @param recipientNickname Nickname of the recipient. It's "all" if the message is for everyone
     * @param message Message to send
     * @throws RemoteException If an error occurs during the request
     */
    void sendChatMessage(int playerID, String recipientNickname, String message) throws RemoteException;

    /**
     * Requests to send a pong.
     * @param playerID ID of the player that sent the request
     * @throws RemoteException If an error occurs during the request
     */
    void sendPong(int playerID) throws RemoteException;
}
