package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.mvc.model.game.GameState;
import com.example.pf_soft_ing.mvc.model.player.TokenColors;

import java.util.List;
import java.util.Map;

public interface Sender {

    /**
     * Sends an error message to the client.
     * @param errorMsg Error message
     */
    void sendError(String errorMsg);

    /**
     * Sends the list of matches to the client.
     * @param matches List of matches
     * @param playerID ID of the player
     */
    void sendMatches(Map<Integer, List<String>> matches, int playerID);

    /**
     * Sends the ID of the created match to the client.
     * @param matchID ID of the match
     * @param hostNickname Nickname of the host
     */
    void createMatchResult(int matchID, String hostNickname);

    /**
     * Sends the result of the match selection to the client.
     * @param matchID ID of the match
     * @param nicknames List of nicknames
     */
    void selectMatchResult(int matchID, List<String> nicknames);

    /**
     * Sends the result of the nickname choice to the client.
     * @param nickname Chosen nickname
     */
    void chooseNicknameResult(String nickname);

    /**
     * Sends all the information needed to start the game.
     * @param IDtoOpponentNickname Map of player IDs to nicknames
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @param starterCardID ID of the starter card
     */
    void sendGameStart(Map<Integer, String> IDtoOpponentNickname,
                       int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                       int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                       int starterCardID);

    /**
     * Confirms the placement of the starter card and sends all information needed to complete the setup phase
     * @param resourceCardID1 ID of the first resource card
     * @param resourceCardID2 ID of the second resource card
     * @param goldenCardID ID of the golden card
     * @param tokenColor Token color
     * @param commonObjectiveCardID1 ID of the first common objective card
     * @param commonObjectiveCardID2 ID of the second common objective card
     * @param secretObjectiveCardID1 ID of the first secret objective card
     * @param secretObjectiveCardID2 ID of the second secret objective card
     */
    void sendMissingSetup(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                          int commonObjectiveCardID1, int commonObjectiveCardID2,
                          int secretObjectiveCardID1, int secretObjectiveCardID2);

    /**
     * Confirms the choice of the secret objective card.
     */
    void confirmSecretObjective();

    /**
     * Sends the result of the card placement.
     * @param playerID ID of the player that placed the card
     * @param cardID ID of the card
     * @param pos Position of the card
     * @param chosenSide Side of the card
     * @param score Score of the placement
     */
    void placeCard(int playerID, int cardID, Position pos, CardSideType chosenSide, int score);

    /**
     * Notifies all players of the first turn of the game.
     * @param lastPlayerID ID of the last player to complete the setup phase
     * @param playerID ID of the player that starts the game
     * @param playerIDs IDs of all players in the game
     * @param starterCardIDs IDs of the starter cards of each player
     * @param starterCardSides Sides of the starter cards of each player
     * @param tokenColors Token colors of each player
     * @param playerHands Hands of each player
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     */
    void sendFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs,
                             CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                             int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                             int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);

    /**
     * Notifies all players of the first turn of the game, when the game starts after a player reconnection.
     * @param playerID ID of the player that starts the game
     * @param playerIDs IDs of all players in the game
     * @param starterCardIDs IDs of the starter cards of each player
     * @param starterCardSides Sides of the starter cards of each player
     * @param tokenColors Token colors of each player
     * @param playerHands Hands of each player
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     */
    void sendFirstPlayerTurn(int playerID, int[] playerIDs, int[] starterCardIDs,
                                     CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands,
                                     int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                     int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);

    /**
     * Notifies all players of the new turn of a player.
     * @param drawnCardID ID of the drawn card
     * @param lastPlayerID ID of the last player acting
     * @param newPlayerID ID of the player that starts the new turn
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     */
    void sendNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID,
                           int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                           int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);

    /**
     * Notifies all players of the new turn of a player during the extra turn game phase.
     * @param cardID ID of the placed card
     * @param lastPlayerID ID of the last player acting
     * @param pos Position of the card
     * @param side Side of the card
     * @param newPlayerID ID of the player that starts the new turn
     * @param score Score of the placement
     */
    void sendNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side,
                                int newPlayerID, int score);

    /**
     * Notifies all players of the new turn of a player and the new game state.
     * @param drawnCardID ID of the drawn card
     * @param lastPlayerID ID of the last player acting
     * @param newPlayerID ID of the player that starts the new turn
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @param gameState New game state
     */
    void sendNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID,
                                   int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                                   GameState gameState);

    /**
     * Sends a chat message to the recipient.
     * @param sender Sender of the message
     * @param recipient Recipient of the message
     * @param message Message to send
     */
    void sendChatMessage(String sender, String recipient, String message);

    /**
     * Sends the ranking of the players.
     * @param lastPlayerID ID of the last player acting
     * @param cardID ID of the last card placed
     * @param pos Position of the card
     * @param side Side of the card
     * @param deltaScore Points scored by the last player
     * @param nicknames Nicknames of the players (in order)
     * @param scores Scores of the players
     * @param numOfSecretObjectives Number of secret objectives of the players
     */
    void sendRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives);

    /**
     * Notifies all players of a player disconnection.
     * @param playerID ID of the disconnected player
     */
    void sendPlayerDisconnection(int playerID);

    /**
     * Notifies the only remaining player of a player disconnection.
     * @param playerID ID of the disconnected player
     */
    void sendPlayerDisconnectionWithOnePlayerLeft(int playerID);

    /**
     * Sends the reconnected player the information needed to complete the setup phase (On starter card placement).
     * @param playerID ID of the reconnected player
     * @param IDToNicknameMap Map of player IDs to nicknames
     * @param gameSetupCards IDs of the cards needed for the setup phase
     */
    void sendReOnStarterPlacement(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards);

    /**
     * Sends the reconnected player the information needed to complete the setup phase (On objective card choice).
     * @param playerID ID of the reconnected player
     * @param IDToNicknameMap Map of player IDs to nicknames
     * @param gameSetupCards IDs of the cards needed for the setup phase
     * @param starterSide Side of the starter card
     * @param tokenColor Token color
     */
    void sendReOnObjectiveChoice(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor);

    /**
     * Sends the reconnected player the information needed to complete the setup phase (After completing the setup).
     * @param playerID ID of the reconnected player
     * @param IDToNicknameMap Map of player IDs to nicknames
     * @param gameSetupCards IDs of the cards needed for the setup phase
     * @param starterSide Side of the starter card
     * @param tokenColor Token color
     */
    void sendReAfterSetup(int playerID, Map<Integer, String> IDToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor);

    /**
     * Sends the reconnected player the information needed to complete the setup phase (During normal turn phase).
     * @param playerID ID of the reconnected player
     * @param playersIDs IDs of other players
     * @param playersNicknames Nicknames of other players
     * @param playersTokenColors Token colors of other players
     * @param playersHands Hands of other players
     * @param playersPlacedCardsPos Positions of the placed cards of other players
     * @param playersPlacedCardsIDs IDs of the placed cards of other players
     * @param playersPlacedCardsSides Sides of the placed cards of other players
     * @param playersPlacedCardsPriorities Priorities of the placed cards of other players
     * @param playersScores Scores of other players
     * @param gameSetupCards IDs of the cards needed for the setup phase
     * @param currPlayerID ID of the current player
     */
    void sendNormalReconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands,
                             List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                             int[] playersScores, int[] gameSetupCards, int currPlayerID);

    /**
     * Notifies all players of the request to undo the last card placement.
     * @param playerID ID of the player that requested the undo
     * @param pos Position of the card
     * @param score Score of the placement
     * @param nextPlayerID ID of the player that starts the new turn
     */
    void sendUndoCardPlacement(int playerID, Position pos, int score, int nextPlayerID);

    /**
     * Notifies all players of the request to undo the last card placement with only one player remaining online.
     * @param playerID ID of the player that requested the undo
     * @param pos Position of the card
     * @param score Score of the placement
     */
    void sendUndoPlaceWithOnePlayerLeft(int playerID, Position pos, int score);

    /**
     * Notifies all players of the reconnection of a player.
     * @param playerID ID of the reconnected player
     */
    void sendPlayerReconnection(int playerID);

    /**
     * Sends the only remaining player a messages that the game is over, and he is the only winner.
     */
    void sendSoleWinnerMessage();

    /**
     * Sends a ping message to the client.
     */
    void sendPing();
}
