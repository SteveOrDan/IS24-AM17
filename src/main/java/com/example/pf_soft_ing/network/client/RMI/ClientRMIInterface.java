package com.example.pf_soft_ing.network.client.RMI;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.MVC.model.game.GameState;
import com.example.pf_soft_ing.MVC.model.player.TokenColors;

import java.rmi.*;
import java.util.List;
import java.util.Map;

public interface ClientRMIInterface extends Remote {

    /**
     * Shows the matches available to join
     * @param matchesNicknames Map containing the matches' IDs and the nicknames of the players in the match
     * @param playerID ID of the player that requested the matches
     * @throws RemoteException if there is a problem with the connection
     */
    void showMatches(Map<Integer, List<String>> matchesNicknames, int playerID) throws RemoteException;

    /**
     * Confirms the placement of a starter card
     * @throws RemoteException if there is a problem with the connection
     */
    void placeStarterCard() throws RemoteException;

    /**
     * Confirm the placement of a card
     * @param playerID ID of the player that requested to place the card
     * @param cardID ID of the card to place
     * @param pos Position where to place the card
     * @param chosenSide Side of the card to place
     * @param score Score of the card placed
     * @throws RemoteException if there is a problem with the connection
     */
    void placeCardResult(int playerID, int cardID, Position pos, CardSideType chosenSide, int score) throws RemoteException;

    /**
     * Sends an error to the player
     * @param errorMsg Error message to send
     * @throws RemoteException if there is a problem with the connection
     */
    void sendError(String errorMsg) throws RemoteException;

    /**
     * Confirms that the player has entered the selected match
     * @param matchID ID of the match the player has entered
     * @param nicknames List of the nicknames of the players in the match
     * @throws RemoteException if there is a problem with the connection
     */
    void selectMatchResult(int matchID, List<String> nicknames) throws RemoteException;

    /**
     * Confirms that the player has created a match
     * @param matchID ID of the match the player has created
     * @param hostNickname Nickname of the player that created the match
     * @throws RemoteException if there is a problem with the connection
     */
    void createMatchResult(int matchID, String hostNickname) throws RemoteException;

    /**
     * Confirms that the player has chosen a nickname
     * @param hostNickname Nickname of the player that created the match
     * @throws RemoteException if there is a problem with the connection
     */
    void chooseNicknameResult(String hostNickname) throws RemoteException;

    /**
     * Starts the game
     * @param nickname Nickname of the player that triggered the start of the game
     * @param IDtoNicknameMap Map containing the IDs of the players and their nicknames
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @param starterCardID ID of the starter card
     * @throws RemoteException if there is a problem with the connection
     */
    void startGame(String nickname, Map<Integer, String> IDtoNicknameMap,
                   int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                   int starterCardID) throws RemoteException;

    /**
     * Sends a message to the player regarding the first player's turn
     * @param lastPlayerID ID of the player that triggered the first turn
     * @param playerID ID of the player that has the turn
     * @param playerIDs IDs of the players
     * @param starterCardIDs IDs of the starter cards
     * @param starterCardSides Sides of the starter cards
     * @param tokenColors Token colors of the players
     * @param playerHands Hands of the players
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @throws RemoteException if there is a problem with the connection
     */
    void sendFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs,
                             CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                             int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                             int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) throws RemoteException;

    /**
     * Sends a message to the player regarding the first player's turn
     * @param playerID ID of the player that has the turn
     * @param playerIDs IDs of the players
     * @param starterCardIDs IDs of the starter cards
     * @param starterCardSides Sides of the starter cards
     * @param tokenColors Token colors of the players
     * @param playerHands Hands of the players
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @throws RemoteException if there is a problem with the connection
     */
    void sendFirstPlayerTurn(int playerID, int[] playerIDs, int[] starterCardIDs,
                             CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                             int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                             int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) throws RemoteException;

    /**
     * Sends the missing setup to the player
     * @param resourceCardID1 ID of the first resource card
     * @param resourceCardID2 ID of the second resource card
     * @param goldenCardID ID of the golden card
     * @param tokenColor Token color of the player
     * @param commonObjectiveCardID1 ID of the first common objective card
     * @param commonObjectiveCardID2 ID of the second common objective card
     * @param secretObjectiveCardID1 ID of the first secret objective card
     * @param secretObjectiveCardID2 ID of the second secret objective card
     * @throws RemoteException if there is a problem with the connection
     */
    void setMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                          int commonObjectiveCardID1, int commonObjectiveCardID2,
                          int secretObjectiveCardID1, int secretObjectiveCardID2) throws RemoteException;

    /**
     * Notifies the player that the turn has changed
     * @param drawnCardID ID of the card drawn
     * @param lastPlayerID ID of the player that had the last turn
     * @param newPlayerID ID of the player that has the new turn
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @throws RemoteException if there is a problem with the connection
     */
    void setNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) throws RemoteException;

    /**
     * Shows the player's ranking for the match
     * @param lastPlayerID ID of the player that triggered the ranking
     * @param cardID ID of the card that the last player placed
     * @param pos Position where the card was placed
     * @param side Side of the card that was placed
     * @param deltaScore Points scored by the last player
     * @param nicknames Nicknames of the players
     * @param scores Scores of the players
     * @param numOfSecretObjectives Number of secret objectives completed by the players
     * @throws RemoteException if there is a problem with the connection
     */
    void showRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives) throws RemoteException;

    /**
     * Confirms the choice of a secret objective
     * @throws RemoteException if there is a problem with the connection
     */
    void confirmSecretObjective() throws RemoteException;

    /**
     * Sends a chat message
     * @param senderNickname Nickname of the player that sent the message
     * @param recipientNickname Nickname of the player that will receive the message. It's "all" for global chat.
     * @param message Message to send
     * @throws RemoteException if there is a problem with the connection
     */
    void sendChatMessage(String senderNickname, String recipientNickname, String message) throws RemoteException;

    /**
     * Notifies the player that the turn has changed and the game state has changed
     * @param drawnCardID ID of the card drawn
     * @param lastPlayerID ID of the player that had the last turn
     * @param newPlayerID ID of the player that has the new turn
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @param gameState New game state
     * @throws RemoteException if there is a problem with the connection
     */
    void setNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID,
                                  int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                  int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2, GameState gameState) throws RemoteException;

    /**
     * Notifies the player that the turn has changed when the game is in extra turn state
     * @param cardID ID of the card drawn
     * @param lastPlayerID ID of the player that had the last turn
     * @param pos Position where the card was placed
     * @param side Side of the card that was placed
     * @param newPlayerID ID of the player that has the new turn
     * @param score Points scored by the last player
     * @throws RemoteException if there is a problem with the connection
     */
    void setNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int score) throws RemoteException;

    /**
     * Notifies the player that a player has disconnected
     * @param playerID ID of the player that disconnected
     * @throws RemoteException if there is a problem with the connection
     */
    void sendPlayerDisconnection(int playerID) throws RemoteException;

    /**
     * Notifies the player that a player has disconnected and there is only one player left
     * @param playerID ID of the player that disconnected
     * @throws RemoteException if there is a problem with the connection
     */
    void sendPlayerDisconnectionWithOnePlayerLeft(int playerID) throws RemoteException;

    /**
     * Notifies the player that he has reconnected to the game after disconnecting during the placement of the starter card
     * @param playerID ID of the player that reconnected
     * @param IDToNicknameMap Map containing the IDs of the players and their nicknames
     * @param gameSetupCards IDs of the cards used for the game setup
     * @throws RemoteException if there is a problem with the connection
     */
    void sendReOnStarterPlacement(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards) throws RemoteException;

    /**
     * Notifies the player that he has reconnected to the game after disconnecting during the choice of the secret objective
     * @param playerID ID of the player that reconnected
     * @param IDToNicknameMap Map containing the IDs of the players and their nicknames
     * @param gameSetupCards IDs of the cards used for the game setup
     * @param starterSide Side of the starter card
     * @param tokenColor Token color of the player
     * @throws RemoteException if there is a problem with the connection
     */
    void sendReOnObjectiveChoice(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) throws RemoteException;

    /**
     * Notifies the player that he has reconnected to the game after disconnecting at the end of his setup
     * @param playerID ID of the player that reconnected
     * @param IDToNicknameMap Map containing the IDs of the players and their nicknames
     * @param gameSetupCards IDs of the cards used for the game setup
     * @param starterSide Side of the starter card
     * @param tokenColor Token color of the player
     * @throws RemoteException if there is a problem with the connection
     */
    void sendReAfterSetup(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) throws RemoteException;

    /**
     * Notifies the player that he has reconnected to the game after disconnecting during the game
     * @param playerID ID of the player that reconnected
     * @param playersIDs IDs of the players
     * @param playersNicknames Nicknames of the players
     * @param playersTokenColors Token colors of the players
     * @param playersHands Hands of the players
     * @param playersPlacedCardsPos Positions of the cards placed by the players
     * @param playersPlacedCardsIDs IDs of the cards placed by the players
     * @param playersPlacedCardsSides Sides of the cards placed by the players
     * @param playersPlacedCardsPriorities Priorities of the cards placed by the players
     * @param playersScores Scores of the players
     * @param gameSetupCards IDs of the cards used for the game setup (secret objectives, common objectives and cards in the draw area)
     * @param currPlayerID ID of the player that has the turn
     * @throws RemoteException if there is a problem with the connection
     */
    void sendNormalReconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands,
                             List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                             int[] playersScores, int[] gameSetupCards, int currPlayerID) throws RemoteException;

    /**
     * Notifies the player that a card placement has been undone
     * @param playerID ID of the player that requested the undo
     * @param pos Position where the card was placed
     * @param score Score of the card placed
     * @param nextPlayerID ID of the player that has the turn
     * @throws RemoteException if there is a problem with the connection
     */
    void sendUndoCardPlacement(int playerID, Position pos, int score, int nextPlayerID) throws RemoteException;

    /**
     * Notifies the player that a card placement has been undone and there is only one player left
     * @param playerID ID of the player that requested the undo
     * @param pos Position where the card was placed
     * @param score Score of the card placed
     * @throws RemoteException if there is a problem with the connection
     */
    void sendUndoPlaceWithOnePlayerLeft(int playerID, Position pos, int score) throws RemoteException;

    /**
     * Notifies the player that a player has reconnected
     * @param playerID ID of the player that reconnected
     * @throws RemoteException if there is a problem with the connection
     */
    void sendPlayerReconnection(int playerID) throws RemoteException;

    /**
     * Notifies the player that he is the sole winner of the match
     * @throws RemoteException if there is a problem with the connection
     */
    void sendSoleWinnerMessage() throws RemoteException;

    /**
     * Sends a ping to the player
     * @throws RemoteException if there is a problem with the connection
     */
    void sendPing() throws RemoteException;
}
