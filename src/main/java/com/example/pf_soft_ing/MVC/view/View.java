package com.example.pf_soft_ing.MVC.view;

import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.MVC.model.game.GameState;
import com.example.pf_soft_ing.MVC.model.player.TokenColors;

import java.util.List;
import java.util.Map;

public interface View {

    /**
     * Shows the list of matches.
     * @param matches Map of match IDs to list of nicknames
     */
    void showMatches(Map<Integer, List<String>> matches);

    /**
     * Shows the result of creating a match.
     * @param matchID ID of the match
     * @param hostNickname Nickname of the host
     */
    void createMatch(int matchID, String hostNickname);

    /**
     * Shows the result of joining a match.
     * @param matchID ID of the match
     * @param nicknames List of nicknames of the players in the match
     */
    void selectMatch(int matchID, List<String> nicknames);

    /**
     * Shows the result of choosing a nickname.
     * @param nickname Chosen nickname
     */
    void chooseNickname(String nickname);

    /**
     * Starts the game.
     * @param IDToNicknameMap Map of player IDs to nicknames
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @param starterCardID ID of the starter card
     */
    void startGame(Map<Integer, String> IDToNicknameMap,
                   int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                   int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                   int starterCardID);

    /**
     * Sends the information missing for completing the setup phase.
     * @param resourceCardID1 ID of the first resource card
     * @param resourceCardID2 ID of the second resource card
     * @param goldenCardID ID of the gold card
     * @param tokenColor Token color of the player
     * @param commonObjectiveCardID1 ID of the first common objective card
     * @param commonObjectiveCardID2 ID of the second common objective card
     * @param secretObjectiveCardID1 ID of the first secret objective card
     * @param secretObjectiveCardID2 ID of the second secret objective card
     */
    void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID,
                         TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2,
                         int secretObjectiveCardID1, int secretObjectiveCardID2);

    /**
     * Confirms the choice of the secret objective card.
     */
    void confirmSecretObjective();

    /**
     * Shows an error message.
     * @param errorMessage Error message
     */
    void errorMessage(String errorMessage);

    /**
     * Shows the first player's turn.
     * @param lastPlayerID ID of the last player
     * @param playerID ID of the player
     * @param playerIDs IDs of the players
     * @param starterCardIDs IDs of the starter cards
     * @param starterCardSides Sides of the starter cards
     * @param tokenColors Token colors of the players
     * @param playerHands Hands of the players
     */
    void showFirstPlayerTurn(int lastPlayerID, int playerID,
                             int[] playerIDs, int[] starterCardIDs, CardSideType[] starterCardSides,
                             TokenColors[] tokenColors, int[][] playerHands);

    /**
     * Shows the first player's turn after a reconnection
     * @param playerID ID of the player who reconnected
     * @param playerIDs IDs of the players
     * @param starterCardIDs IDs of the starter cards
     * @param starterCardSides Sides of the starter cards
     * @param tokenColors Token colors of the players
     * @param playerHands Hands of the players
     */
    void showFirstPlayerTurnAfterRec(int playerID, int[] playerIDs,
                                     int[] starterCardIDs, CardSideType[] starterCardSides,
                                     TokenColors[] tokenColors, int[][] playerHands);

    /**
     * Shows the new player's turn.
     * @param drawnCardID ID of the drawn card
     * @param lastPlayerID ID of the last player
     * @param newPlayerID ID of the new player
     */
    void showNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID);

    /**
     * Shows the new player's extra turn.
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     */
    void updateDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                        int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2);

    /**
     * Sets the player's ID.
     * @param playerID Player ID
     */
    void setID(int playerID);

    /**
     * Shows the result of placing a card.
     * @param playerId Player ID of the player who placed the card
     * @param cardID Card ID of the placed card
     * @param pos Position of the card
     * @param side Side of the card
     * @param deltaScore Delta score
     */
    void placeCard(int playerId, int cardID, Position pos, CardSideType side, int deltaScore);

    /**
     * Send chat message.
     * @param senderNickname Sender nickname
     * @param recipientNickname Recipient nickname
     * @param message Message
     */
    void receiveChatMessage(String senderNickname, String recipientNickname, String message);

    /**
     * Shows the rankings of the players.
     * @param lastPlayerID ID of the last player
     * @param cardID Card ID
     * @param pos Position of the card
     * @param side Side of the card
     * @param deltaScore Points scored by the last player
     * @param nicknames Nicknames of the players
     * @param scores Scores of the players
     * @param numOfSecretObjectives Number of secret objectives
     */
    void showRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfSecretObjectives);

    /**
     * Shows the new player's turn with the new state.
     * @param drawnCardID ID of the drawn card
     * @param lastPlayerID ID of the last player
     * @param newPlayerID ID of the new player
     * @param gameState Game state
     */
    void showNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID, GameState gameState);

    /**
     * Shows the new player when the game is in extra turn state.
     * @param cardID ID of the placed card
     * @param lastPlayerID ID of the player who played the card
     * @param pos Position of the card
     * @param side Side of the card
     * @param newPlayerID ID of the new player
     * @param deltaScore Points scored by the last player
     */
    void showNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int deltaScore);

    /**
     * Shows the player disconnection.
     * @param playerID ID of the disconnected player
     */
    void showPlayerDisconnection(int playerID);

    /**
     * Shows the player disconnection when only one player is left.
     * @param playerID ID of the disconnected player
     */
    void showPlayerDisconnectionWithOnePlayerLeft(int playerID);

    /**
     * Reconnects the player to the match during the starter card placement.
     * @param playerID ID of the player to reconnect
     * @param IDToOpponentNickname Map of player IDs to nicknames
     * @param gameSetupCards IDs of the cards needed for the setup
     */
    void reconnectOnStarterPlacement(int playerID, Map<Integer, String> IDToOpponentNickname, int[] gameSetupCards);

    /**
     * Reconnects the player to the match during the secret objective card choice.
     * @param playerID ID of the player to reconnect
     * @param IDToOpponentNickname Map of player IDs to nicknames
     * @param gameSetupCards IDs of the cards needed for the setup
     * @param starterSide Side of the starter card
     * @param tokenColor Token color of the player
     */
    void reconnectOnObjectiveChoice(int playerID, Map<Integer, String> IDToOpponentNickname, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor);

    /**
     * Reconnects the player to the match after the setup phase.
     * @param playerID ID of the player to reconnect
     * @param idToNicknameMap Map of player IDs to nicknames
     * @param gameSetupCards IDs of the cards needed for the setup
     * @param starterSide Side of the starter card
     * @param tokenColor Token color of the player
     */
    void reconnectAfterSetup(int playerID, Map<Integer, String> idToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor);

    /**
     * Reconnects the player during the match.
     * @param playerID ID of the player to reconnect
     * @param playersIDs IDs of the other players
     * @param playersNicknames Nicknames of the players
     * @param playersTokenColors Token colors of the players
     * @param playersHands Hands of the players
     * @param playersPlacedCardsPos Positions of the placed cards of each player
     * @param playersPlacedCardsIDs IDs of the placed cards of each player
     * @param playersPlacedCardsSides Sides of the placed cards of each player
     * @param playersPlacedCardsPriorities Priorities of the placed cards of each player
     * @param playersScores Scores of the players
     * @param gameSetupCards IDs of the cards needed for the setup
     * @param currPlayerID ID of the current player
     */
    void reconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands,
                   List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                   int[] playersScores, int[] gameSetupCards, int currPlayerID);

    /**
     * Shows the player reconnection.
     * @param playerID ID of the player who reconnected
     */
    void showPlayerReconnection(int playerID);

    /**
     * Undoes the card placement.
     * @param playerID ID of the player who placed the card
     * @param pos Position of the card
     * @param score Score of the player
     * @param nextPlayerID ID of the next player
     */
    void undoCardPlacement(int playerID, Position pos, int score, int nextPlayerID);

    /**
     * Shows the message to the sole winner.
     */
    void showSoleWinnerMessage();

    /**
     * Sends a ping message.
     */
    void receivePing();
}
