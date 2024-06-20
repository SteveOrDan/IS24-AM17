package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;

public interface ClientSender {

    /**
     * Connects to the server.
     */
    void connect();

    /**
     * Sets the player ID.
     * @param playerID New player ID
     */
    void setPlayerID(int playerID);

    /**
     * Requests the list of matches.
     */
    void getMatches();

    /**
     * Requests the creation of a new match.
     * @param numberOfPlayers Number of players in the match
     * @param nickname Nickname of the player
     */
    void createMatch(int numberOfPlayers, String nickname);

    /**
     * Requests to join a match.
     * @param matchID ID of the match
     */
    void selectMatch(int matchID);

    /**
     * Requests to choose a nickname.
     * @param nickname Nickname to choose
     */
    void chooseNickname(String nickname);

    /**
     * Requests to reconnect to a match.
     * @param matchID ID of the match
     * @param nickname Nickname of the player
     */
    void reconnectToMatch(int matchID, String nickname);

    /**
     * Requests to place the starter card
     * @param playerID Player ID
     * @param side Side of the card
     */
    void placeStarterCard(int playerID, CardSideType side);

    /**
     * Requests to choose the secret objective card.
     * @param playerID Player ID
     * @param cardID Card ID
     */
    void chooseSecretObjective(int playerID, int cardID);

    /**
     * Requests to place a card.
     * @param playerID Player ID
     * @param cardID Card ID
     * @param side Side of the card
     * @param pos Position to place the card
     */
    void placeCard(int playerID, int cardID, CardSideType side, Position pos);

    /**
     * Requests to draw a resource card from the deck.
     * @param playerID Player ID
     */
    void drawResourceCard(int playerID);

    /**
     * Requests to draw a visible resource card.
     * @param playerID Player ID
     * @param index Index of the card to draw
     */
    void drawVisibleResourceCard(int playerID, int index);

    /**
     * Requests to draw a golden card from the deck.
     * @param playerID Player ID
     */
    void drawGoldenCard(int playerID);

    /**
     * Requests to draw a visible golden card.
     * @param playerID Player ID
     * @param index Index of the card to draw
     */
    void drawVisibleGoldenCard(int playerID, int index);

    /**
     * Requests to send a chat message.
     * @param playerID Player ID
     * @param recipient Recipient of the message
     * @param message Message to send
     */
    void sendChatMessage(int playerID, String recipient, String message);

    /**
     * Sends a pong message.
     * @param playerID Player ID
     */
    void sendPong(int playerID);
}
