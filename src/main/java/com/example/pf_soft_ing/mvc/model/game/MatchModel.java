package com.example.pf_soft_ing.mvc.model.game;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.decks.ObjectiveCardsDeck;
import com.example.pf_soft_ing.card.decks.StarterCardsDeck;
import com.example.pf_soft_ing.card.decks.UsableCardsDeck;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.exceptions.cards.InvalidVisibleCardIndexException;
import com.example.pf_soft_ing.exceptions.cards.NotEnoughCardsException;
import com.example.pf_soft_ing.exceptions.cards.StarterCardNotSetException;
import com.example.pf_soft_ing.exceptions.match.GameIsFullException;
import com.example.pf_soft_ing.exceptions.player.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.exceptions.player.NicknameNotInMatch;
import com.example.pf_soft_ing.exceptions.player.SpecifiedPlayerNotDisconnected;
import com.example.pf_soft_ing.network.server.socket.Decoder;
import com.example.pf_soft_ing.network.server.rmi.RMIReceiver;
import com.example.pf_soft_ing.network.server.Sender;
import com.example.pf_soft_ing.mvc.model.player.PlayerModel;
import com.example.pf_soft_ing.utils.PlayerRanker;
import com.example.pf_soft_ing.mvc.model.player.PlayerState;
import com.example.pf_soft_ing.mvc.model.player.TokenColors;

import java.util.*;

public class MatchModel {

    private final int maxPlayers;
    private int currPlayers = 0;
    private int playersReady = 0;
    private final int matchID;

    private final HashMap<Integer, PlayerModel> IDToPlayerMap = new HashMap<>();

    private UsableCardsDeck resourceCardsDeck;
    private UsableCardsDeck goldenCardsDeck;
    private ObjectiveCardsDeck objectiveCardsDeck;
    private StarterCardsDeck starterCardsDeck;

    private int currPlayerID = -1;
    private int firstPlayerID;
    private int[] orderOfPlayersIDs;

    private GameState gameState = GameState.PREGAME;

    private final List<TokenColors> tokenColors = new ArrayList<>(List.of(TokenColors.values()));

    private String[] nicknamesRanked;
    private int[] scoresRanked;
    private int[] numOfCompletedObjectivesRanked;

    private final Timer timer = new Timer();
    private TimerTask announceSoleWinner;
    private final static int SOLE_WINNER_ANNOUNCE_DELAY = 30000;

    public MatchModel(int maxPlayers, int matchID){
        this.maxPlayers = maxPlayers;
        this.matchID = matchID;
    }

    /**
     * Selects a random token color
     * @return Token color
     */
    public TokenColors getTokenColor(){
        Random rand = new Random();
        int n = rand.nextInt(tokenColors.size());

        while (tokenColors.get(n) == TokenColors.BLACK){
            n = rand.nextInt(tokenColors.size());
        }

        TokenColors tokenColor = tokenColors.get(n);
        tokenColors.remove(n);
        return tokenColor;
    }

    /**
     * Getter
     * @return Map of player IDs to player models
     */
    public HashMap<Integer, PlayerModel> getIDToPlayerMap() {
        return IDToPlayerMap;
    }

    /**
     * Getter
     * @return All players' nicknames
     */
    public List<String> getNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (PlayerModel playerModel : IDToPlayerMap.values()){
            nicknames.add(playerModel.getNickname());
        }
        return nicknames;
    }

    /**
     * Getter
     * @return Number of maximum players in the match
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Getter
     * @return Number of players ready in the match
     */
    public int getPlayersReady() {
        return playersReady;
    }

    /**
     * Getter
     * @return ID of the match
     */
    public int getMatchID() {
        return matchID;
    }

    /**
     * Getter
     * @return Resource cards deck
     */
    public UsableCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * Getter
     * @return Golden cards deck
     */
    public UsableCardsDeck getGoldenCardsDeck() {
        return goldenCardsDeck;
    }

    /**
     * Getter
     * @return Objective cards deck
     */
    public ObjectiveCardsDeck getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    /**
     * Getter
     * @return Starter cards deck
     */
    public StarterCardsDeck getStarterCardsDeck() {
        return starterCardsDeck;
    }

    /**
     * Getter
     * @return ID of the player that is currently playing his turn
     */
    public int getCurrPlayerID() {
        return currPlayerID;
    }

    /**
     * Getter
     * @return Current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Setter
     * @param gameState New game state
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Adds the first player to the match as the host
     * @param playerModel PlayerModel of the host
     */
    public void addHost(PlayerModel playerModel){
        IDToPlayerMap.put(playerModel.getID(), playerModel);

        currPlayers++;
        playersReady++;
    }

    /**
     * Increase by one the number of players ready in the match
     */
    public void addReadyPlayer(){
        playersReady++;
    }

    /**
     * Increase by one the number of current players in the match
     */
    public void addCurrPlayer(PlayerModel playerModel) throws GameIsFullException {
        if (currPlayers >= maxPlayers){
            throw new GameIsFullException();
        }
        IDToPlayerMap.put(playerModel.getID(), playerModel);

        currPlayers++;
    }

    /**
     * Copies the deck from the game resources
     */
    public void initializeDecks(){
        GameResources.initializeAllDecks();

        resourceCardsDeck = new UsableCardsDeck(GameResources.getResourcesDeck());
        goldenCardsDeck = new UsableCardsDeck(GameResources.getGoldenDeck());
        objectiveCardsDeck = new ObjectiveCardsDeck(GameResources.getObjectiveDeck());
        starterCardsDeck = new StarterCardsDeck(GameResources.getStarterDeck());
    }

    /**
     * Shuffles all decks
     */
    public void shuffleAllDecks(){
        resourceCardsDeck.shuffleDeck();
        goldenCardsDeck.shuffleDeck();
        objectiveCardsDeck.shuffleDeck();
        starterCardsDeck.shuffleDeck();
    }

    /**
     * Sets the visible cards list in the golden and resource card decks
     */
    public void setVisibleCards () {
        resourceCardsDeck.restoreInitialVisibleCards();
        goldenCardsDeck.restoreInitialVisibleCards();
    }

    /**
     * Getter
     * @return Visible resource cards
     */
    public List<PlaceableCard> getVisibleResourceCards(){
        return resourceCardsDeck.getVisibleCards();
    }

    /**
     * Getter
     * @return Visible golden cards
     */
    public List<PlaceableCard> getVisibleGoldenCards(){
        return goldenCardsDeck.getVisibleCards();
    }

    /**
     * Getter
     * @return Nicknames of the players ranked by their final scores
     */
    public String[] getNicknamesRanked() {
        return nicknamesRanked;
    }

    /**
     * Getter
     * @return Final scores of the players ranked
     */
    public int[] getScoresRanked() {
        return scoresRanked;
    }

    /**
     * Getter
     * @return Array that contains the number of completed objectives of the players ranked
     */
    public int[] getNumOfCompletedObjectivesRanked() {
        return numOfCompletedObjectivesRanked;
    }

    /**
     * Draws the first resource card of the deck
     * @return First resource card of the deck
     */
    public PlaceableCard drawResourceCard() throws NotEnoughCardsException {
        return resourceCardsDeck.drawCard();
    }

    /**
     * Draws the first golden card of the deck
     * @return First golden card of the deck
     */
    public PlaceableCard drawGoldenCard() throws NotEnoughCardsException {
        return goldenCardsDeck.drawCard();
    }

    /**
     * Draws the first starter card of the deck
     * @return First starter card of the deck
     */
    public PlaceableCard drawStarterCard(){
        return starterCardsDeck.drawCard();
    }

    /**
     * Draws the first objective card of the deck
     * @return First objective card of the deck
     */
    public ObjectiveCard drawObjectiveCard(){
        return objectiveCardsDeck.drawCard();
    }

    /**
     * Draws the visible resource card of the deck with the given ID
     * @param index index of the card to draw (either 0 or 1)
     * @return Visible resource card of the deck with the given ID
     */
    public PlaceableCard drawVisibleResourceCard(int index) throws InvalidVisibleCardIndexException {
        return resourceCardsDeck.drawVisibleCard(index);
    }

    /**
     * Draws the visible golden card of the deck with the given ID
     * @param index index of the card to draw (either 0 or 1)
     * @return Visible golden card of the deck with the given ID
     */
    public PlaceableCard drawVisibleGoldenCard(int index) throws InvalidVisibleCardIndexException {
        return goldenCardsDeck.drawVisibleCard(index);
    }

    /**
     * Restore the visible resource cards
     */
    public void restoreVisibleResourceCard (){
        if (!resourceCardsDeck.isDeckEmpty()){
            resourceCardsDeck.restoreVisibleCard();
        }
        else { // if (!goldenCardsDeck.isDeckEmpty())
            try {
                resourceCardsDeck.restoreVisibleCardWithOtherDeck(goldenCardsDeck.drawCard());
            } catch (NotEnoughCardsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Restore the visible golden cards
     */
    public void restoreVisibleGoldenCard (){
        if (!goldenCardsDeck.isDeckEmpty()){
            goldenCardsDeck.restoreVisibleCard();
        }
        else { // if (!resourceCardsDeck.isDeckEmpty())
            try {
                goldenCardsDeck.restoreVisibleCardWithOtherDeck(resourceCardsDeck.drawCard());
            } catch (NotEnoughCardsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Randomly chooses the first player and gives him the black token
     */
    public void setRandomFirstPlayer(){
        for (Integer i : IDToPlayerMap.keySet()){
            IDToPlayerMap.get(i).setAsFirstPlayer(false);
            IDToPlayerMap.get(i).setState(PlayerState.WAITING);
        }

        List<Integer> playerIDList = new ArrayList<>(IDToPlayerMap.keySet());

        firstPlayerID = playerIDList.get((int) Math.round(Math.random() * (playerIDList.size() - 1)));
        currPlayerID = firstPlayerID;

        PlayerModel firstPlayer = IDToPlayerMap.get(firstPlayerID);
        firstPlayer.setAsFirstPlayer(true);
        firstPlayer.setState(PlayerState.PLACING);
    }

    /**
     * Calculates the order of the players based on the first player
     */
    public void calculateOrderOfPlayers() {
        orderOfPlayersIDs = new int[IDToPlayerMap.size()];

        orderOfPlayersIDs[0] = firstPlayerID;

        int pos = 1;
        for (Integer i : IDToPlayerMap.keySet()){
            if (i != firstPlayerID){
                orderOfPlayersIDs[pos] = i;
                pos++;
            }
        }
    }

    /**
     * Ends the turn of the current player and passes the turn to
     * the next player in the order
     */
    public void endTurn(){
        if (IDToPlayerMap.get(currPlayerID).getState() != PlayerState.DISCONNECTED) {
            IDToPlayerMap.get(currPlayerID).setState(PlayerState.WAITING);
        }

        int currPlayerIndex = 0;
        for (int i = 0; i < orderOfPlayersIDs.length; i++){
            if (orderOfPlayersIDs[i] == currPlayerID){
                currPlayerIndex = i;
                break;
            }
        }

        if (gameState == GameState.PLAYING){
            checkForEndGame();
        }

        boolean hasLooped = false;
        boolean foundNextPlayer = false;
        while (!foundNextPlayer){
            if (currPlayerIndex == orderOfPlayersIDs.length - 1){
                currPlayerIndex = 0;
                hasLooped = true;
            }
            else {
                currPlayerIndex++;
            }

            if (IDToPlayerMap.get(orderOfPlayersIDs[currPlayerIndex]).getState() != PlayerState.DISCONNECTED){
                foundNextPlayer = true;
            }
        }

        if (hasLooped){
            manageEndGame();
        }
        currPlayerID = orderOfPlayersIDs[currPlayerIndex];

        if (gameState != GameState.END_GAME){
            IDToPlayerMap.get(currPlayerID).setState(PlayerState.PLACING);
        }
    }

    /**
     * Manages game state transitions
     */
    public void checkForEndGame(){
        if (IDToPlayerMap.get(currPlayerID).getCurrScore() >= 20 || (resourceCardsDeck.isDeckEmpty() && goldenCardsDeck.isDeckEmpty())){
            gameState = GameState.FINAL_ROUND;
        }
    }

    /**
     * Manages the final rounds of the game
     */
    public void manageEndGame() {
        if (gameState == GameState.FINAL_ROUND){
            gameState = GameState.EXTRA_ROUND;
        }
        else if (gameState == GameState.EXTRA_ROUND){
            gameState = GameState.END_GAME;
            determineRanking();
        }
    }

    /**
     * Determines the ranking of the players based on their final scores
     */
    public void determineRanking() {
        List<PlayerModel> players = new LinkedList<>(IDToPlayerMap.values());
        nicknamesRanked = new String[players.size()];
        scoresRanked = new int[players.size()];
        numOfCompletedObjectivesRanked = new int[players.size()];

        for (PlayerModel player : IDToPlayerMap.values()) {
            player.calculateObjectivePoints(player.getSecretObjective());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().getFirst());
            player.calculateObjectivePoints(objectiveCardsDeck.getCommonObjectives().get(1));
        }

        players.sort(new PlayerRanker());

        for (PlayerModel player : players) {
            nicknamesRanked[players.indexOf(player)] = player.getNickname();
            scoresRanked[players.indexOf(player)] = player.getCurrScore();
            numOfCompletedObjectivesRanked[players.indexOf(player)] = player.getNumOfCompletedObjectives();
            stopPinging(player);
        }
    }

    /**
     * Checks if all players have completed their setup
     * @return true if all players have completed their setup, false otherwise
     */
    public boolean checkForTurnOrderPhase() {
        for (PlayerModel player : IDToPlayerMap.values()){
            if (player.getState() != PlayerState.COMPLETED_SETUP){
                return false;
            }
        }
        return true;
    }

    /**
     * Starts the turn order phase of the game
     */
    public void startTurnOrderPhase() {
        for (PlayerModel player : IDToPlayerMap.values()){
            player.setState(PlayerState.WAITING);
        }

        setGameState(GameState.PLAYING);

        setRandomFirstPlayer();

        calculateOrderOfPlayers();
    }

    /**
     * Checks if the nickname is already taken by another player
     * @param nickname Nickname to check
     * @throws NicknameAlreadyExistsException if the nickname is already taken or "all"
     */
    public void checkNickname(String nickname) throws NicknameAlreadyExistsException {
        if (nickname.equals("all")){
            throw new NicknameAlreadyExistsException();
        }

        for (String otherNickname : getNicknames()) {
            if (otherNickname.equals(nickname)) {
                throw new NicknameAlreadyExistsException();
            }
        }
    }

    /**
     * Getter
     * @return Map of player IDs to their nicknames
     */
    public Map<Integer, String> getIDtoNicknameMap() {
        Map<Integer, String> IDToNickname = new HashMap<>();

        for (PlayerModel player : IDToPlayerMap.values()){
            IDToNickname.put(player.getID(), player.getNickname());
        }
        return IDToNickname;
    }

    /**
     * Removes the player with the given ID from the match
     * @param playerID ID of the player to remove
     */
    public void removePlayer(int playerID) {
        IDToPlayerMap.remove(playerID);
        currPlayers--;
        playersReady--;
    }

    /**
     * Handles the reconnection of a player that was disconnected during the match
     * @param nickname Nickname of the player to reconnect
     * @param newSender New sender of the player
     * @return ID of the player that reconnected
     * @throws SpecifiedPlayerNotDisconnected If the player is not disconnected
     * @throws NicknameNotInMatch If the nickname is not in the match
     */
    public int reconnectPlayer(String nickname, Sender newSender) throws SpecifiedPlayerNotDisconnected, NicknameNotInMatch {

        for (PlayerModel player : IDToPlayerMap.values()){
            if (player.getNickname().equals(nickname)){

                // If there is a disconnected player with the same nickname, reconnect him
                if (player.getState() == PlayerState.DISCONNECTED){
                    player.setSender(newSender);

                    if (gameState == GameState.SET_UP) {
                        if (player.getLastPlayerState() == PlayerState.PLACING_STARTER) {
                            reconnectOnStarterPlacement(player);
                        }
                        else if (player.getLastPlayerState() == PlayerState.CHOOSING_OBJECTIVE) {
                            reconnectOnObjectiveChoice(player);
                        }
                        else if (player.getLastPlayerState() == PlayerState.COMPLETED_SETUP) {
                            player.setState(PlayerState.COMPLETED_SETUP);

                            // Check if all players have completed their setup
                            if (checkForTurnOrderPhase()) {
                                reconnectPlayerAtGameStart(player);
                            }
                            else // If not, reconnect the player after the setup phase (other players are still setting up)
                            {
                                reconnectPlayerAfterSetup(player);
                            }
                        }
                    }
                    else if (gameState == GameState.PLAYING || gameState == GameState.FINAL_ROUND || gameState == GameState.EXTRA_ROUND) {
                        if (announceSoleWinner != null){
                            announceSoleWinner.cancel();
                            announceSoleWinner = null;
                            IDToPlayerMap.get(getCurrPlayerID()).setState(PlayerState.PLACING);
                        }
                        reconnect(player);
                    }
                    else if (gameState == GameState.END_GAME){
                        player.getSender().sendError("The match has already ended. You cannot reconnect.");
                    }

                    return player.getID();
                }
                else {
                    throw new SpecifiedPlayerNotDisconnected();
                }
            }
        }
        throw new NicknameNotInMatch();
    }

    /**
     * Reconnects a player that has completed his setup given that other players already finished theirs while he was disconnected
     * @param player Player to reconnect
     */
    private void reconnectPlayerAtGameStart(PlayerModel player) {
        try {
            int playerID = player.getID();

            startGame(playerID, true);
        }
        // This exception should never be thrown
        catch (StarterCardNotSetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the game after all players have completed their setup
     * @param playerID ID of the player that reconnected or was last to complete his setup
     * @param isReconnecting True if the player is reconnecting, false otherwise
     * @throws StarterCardNotSetException If the starter card of the player is not set
     */
    public void startGame(int playerID, boolean isReconnecting) throws StarterCardNotSetException {
        PlayerModel player = IDToPlayerMap.get(playerID);

        startTurnOrderPhase();

        int currPlayerID = getCurrPlayerID();

        List<PlaceableCard> visibleResCards = getVisibleResourceCards();
        List<PlaceableCard> visibleGoldCards = getVisibleGoldenCards();

        PlaceableCard resDeckCardID = getResourceCardsDeck().getDeck().getFirst();
        PlaceableCard goldDeckCardID = getGoldenCardsDeck().getDeck().getFirst();

        int[] playerIDs = new int[IDToPlayerMap.size()];
        int[] starterCardIDs = new int[IDToPlayerMap.size()];
        CardSideType[] starterCardSides = new CardSideType[IDToPlayerMap.size()];
        TokenColors[] tokenColors = new TokenColors[IDToPlayerMap.size()];
        int[][] playerHands = new int[IDToPlayerMap.size()][3];

        int i = 0;
        for (int id : IDToPlayerMap.keySet()) {
            playerIDs[i] = IDToPlayerMap.get(id).getID();
            starterCardIDs[i] = IDToPlayerMap.get(id).getStarterCard().getID();
            starterCardSides[i] = IDToPlayerMap.get(id).getStarterCard().getCurrSideType();
            tokenColors[i] = IDToPlayerMap.get(id).getTokenColor();
            for (int j = 0; j < 3; j++) {
                playerHands[i][j] = IDToPlayerMap.get(id).getHand().get(j).getID();
            }

            i++;
        }

        if (isReconnecting) {
            for (Integer ID : IDToPlayerMap.keySet()) {
                if (ID != playerID) {
                    IDToPlayerMap.get(ID).getSender().sendFirstPlayerTurn(currPlayerID, playerIDs, starterCardIDs, starterCardSides,
                            tokenColors, playerHands,
                            resDeckCardID.getID(), visibleResCards.getFirst().getID(), visibleResCards.get(1).getID(),
                            goldDeckCardID.getID(), visibleGoldCards.getFirst().getID(), visibleGoldCards.get(1).getID());
                }
                else {
                    reconnect(player);
                }
            }
        }
        else {
            for (Integer ID : getIDToPlayerMap().keySet()) {
                IDToPlayerMap.get(ID).getSender().sendFirstPlayerTurn(playerID, currPlayerID, playerIDs, starterCardIDs, starterCardSides,
                        tokenColors, playerHands,
                        resDeckCardID.getID(), visibleResCards.getFirst().getID(), visibleResCards.get(1).getID(),
                        goldDeckCardID.getID(), visibleGoldCards.getFirst().getID(), visibleGoldCards.get(1).getID());
            }
        }
    }

    /**
     * Checks if the match has no players
     * @return True if the match has no players, false otherwise
     */
    public boolean hasNoPlayers() {
        return currPlayers == 0;
    }

    /**
     * Checks if all players are disconnected
     * @return True if the match has no players online, false otherwise
     */
    public boolean hasNoPlayersOnline() {
        return IDToPlayerMap.values().stream().allMatch(player -> player.getState() == PlayerState.DISCONNECTED);
    }

    /**
     * Undo the last card placement of the player with the given ID
     * @param playerID ID of the player to undo the card placement
     */
    public void undoCardPlacement(int playerID) {
        IDToPlayerMap.get(playerID).undoCardPlacement();
        endTurn();
    }

    /**
     * Checks if there is only one player left in the match
     * and announce him as the sole winner after a delay
     */
    public boolean checkForLastPlayerStanding() {
        int playersOnline = 0;

        for (PlayerModel player : IDToPlayerMap.values()){
            if (player.getState() != PlayerState.DISCONNECTED){
                playersOnline++;
            }
        }

        if (playersOnline == 1) {
            announceSoleWinner = new TimerTask() {
                @Override
                public void run() {
                    int playersOnline = 0;
                    int lastPlayerID = -1;

                    for (PlayerModel player : IDToPlayerMap.values()) {
                        if (player.getState() != PlayerState.DISCONNECTED) {
                            playersOnline++;
                            lastPlayerID = player.getID();
                        }
                    }

                    if (playersOnline == 1) {
                        IDToPlayerMap.get(lastPlayerID).getSender().sendSoleWinnerMessage();
                        gameState = GameState.END_GAME;
                        IDToPlayerMap.get(lastPlayerID).setState(PlayerState.MAIN_MENU);
                        stopPinging(IDToPlayerMap.get(lastPlayerID));
                        GameModel.removeMatch(matchID);
                    }
                }
            };
            // Announce the sole winner after a delay
            timer.schedule(announceSoleWinner, SOLE_WINNER_ANNOUNCE_DELAY);
            return true;
        }

        return false;
    }

    /**
     * Checks if the match is over
     * @return True if the match is over, false otherwise
     */
    public boolean isOver() {
        return gameState == GameState.END_GAME;
    }

    /**
     * Stop the pinging process of the given player
     * @param player Player to stop pinging
     */
    private static void stopPinging(PlayerModel player) {
        Decoder.finishedMatch(player.getID());
        RMIReceiver.finishedMatch(player.getID());
    }

    /**
     * Reconnects a player that was disconnected during the match
     * @param player Player to reconnect
     */
    private void reconnect(PlayerModel player) {
        player.setState(PlayerState.WAITING);

        int[] playersIDs = new int[IDToPlayerMap.size()];
        String[] playersNicknames = new String[IDToPlayerMap.size()];
        TokenColors[] playersTokenColors = new TokenColors[IDToPlayerMap.size()];
        int[][] playersHands = new int[IDToPlayerMap.size()][];
        int[] playersScores = new int[IDToPlayerMap.size()];

        List<Position[]> playersPlacedCardsPos = new ArrayList<>();
        List<int[]> playersPlacedCardsIDs = new ArrayList<>();
        List<CardSideType[]> playersPlacedCardsSides = new ArrayList<>();
        List<int[]> playersPlacedCardsPriorities = new ArrayList<>();

        int i = 0;

        // For each player in the match
        for (PlayerModel otherPlayer : IDToPlayerMap.values()){
            // Add the player's ID, nickname and token color to the arrays
            playersIDs[i] = otherPlayer.getID();
            playersNicknames[i] = otherPlayer.getNickname();
            playersTokenColors[i] = otherPlayer.getTokenColor();

            // Add the player's cards' IDs to the array
            List<PlaceableCard> hand = otherPlayer.getHand();
            playersHands[i] = new int[hand.size()];

            for (int j = 0; j < hand.size(); j++) {
                playersHands[i][j] = hand.get(j).getID();
            }

            int playAreaSize = otherPlayer.getPlayArea().size();
            Position[] playerPlacedCardsPos = new Position[playAreaSize];
            int[] playerPlacedCardsIDs = new int[playAreaSize];
            CardSideType[] playerPlacedCardsSides = new CardSideType[playAreaSize];
            int[] playerPlacedCardsPriorities = new int[playAreaSize];
            int k = 0;

            // Add the player's placed cards' positions, IDs, sides and priority to the arrays
            for (Map.Entry<Position, PlaceableCard> entry : otherPlayer.getPlayArea().entrySet()) {
                playerPlacedCardsPos[k] = entry.getKey();
                playerPlacedCardsIDs[k] = entry.getValue().getID();
                playerPlacedCardsSides[k] = entry.getValue().getCurrSideType();
                playerPlacedCardsPriorities[k] = entry.getValue().getPriority();
                k++;
            }
            playersPlacedCardsPos.add(playerPlacedCardsPos);
            playersPlacedCardsIDs.add(playerPlacedCardsIDs);
            playersPlacedCardsSides.add(playerPlacedCardsSides);
            playersPlacedCardsPriorities.add(playerPlacedCardsPriorities);

            // Add the player's score to the array
            playersScores[i] = otherPlayer.getCurrScore();

            i++;
        }
        int[] gameSetupCards = new int[9];
        int currPlayerID = getCurrPlayerID();

        // Add the IDs of the player's secret objective card and the common objective cards to the array
        gameSetupCards[0] = player.getSecretObjective().getID();
        gameSetupCards[1] = objectiveCardsDeck.getCommonObjectives().getFirst().getID();
        gameSetupCards[2] = objectiveCardsDeck.getCommonObjectives().get(1).getID();

        // Add the IDs of the draw area's cards to the array
        gameSetupCards[3] = getResourceCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[4] = getVisibleResourceCards().getFirst().getID();
        gameSetupCards[5] = getVisibleResourceCards().get(1).getID();
        gameSetupCards[6] = getGoldenCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[7] = getVisibleGoldenCards().getFirst().getID();
        gameSetupCards[8] = getVisibleGoldenCards().get(1).getID();

        player.getSender().sendNormalReconnect(player.getID(), playersIDs, playersNicknames,playersTokenColors, playersHands,
                playersPlacedCardsPos, playersPlacedCardsIDs, playersPlacedCardsSides, playersPlacedCardsPriorities,
                playersScores, gameSetupCards, currPlayerID);
    }

    /**
     * Reconnects a player that was disconnected during the starter card placement phase
     * @param player Player to reconnect
     */
    private void reconnectOnStarterPlacement(PlayerModel player) {
        int[] gameSetupCards = new int[7];

        player.setState(PlayerState.PLACING_STARTER);

        // Add the IDs of the deck's cards to the array
        gameSetupCards[0] = getResourceCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[1] = getVisibleResourceCards().getFirst().getID();
        gameSetupCards[2] = getVisibleResourceCards().get(1).getID();
        gameSetupCards[3] = getGoldenCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[4] = getVisibleGoldenCards().getFirst().getID();
        gameSetupCards[5] = getVisibleGoldenCards().get(1).getID();
        // Add the ID of the player's starter card to the array
        try {
            gameSetupCards[6] = player.getStarterCard().getID();
        }
        // This exception should never be thrown
        catch (StarterCardNotSetException e) {
            throw new RuntimeException(e);
        }
        player.getSender().sendReOnStarterPlacement(player.getID(), getIDtoNicknameMap(), gameSetupCards);
    }

    /**
     * Reconnects a player that was disconnected during the objective choice phase
     * @param player Player to reconnect
     */
    private void reconnectOnObjectiveChoice(PlayerModel player) {
        int[] gameSetupCards = new int[14];

        player.setState(PlayerState.CHOOSING_OBJECTIVE);

        // Add the IDs of the deck's cards to the array
        gameSetupCards[0] = getResourceCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[1] = getVisibleResourceCards().getFirst().getID();
        gameSetupCards[2] = getVisibleResourceCards().get(1).getID();
        gameSetupCards[3] = getGoldenCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[4] = getVisibleGoldenCards().getFirst().getID();
        gameSetupCards[5] = getVisibleGoldenCards().get(1).getID();

        // Add the IDs of the player's cards to the array
        gameSetupCards[6] = player.getHand().getFirst().getID();
        gameSetupCards[7] = player.getHand().get(1).getID();
        gameSetupCards[8] = player.getHand().get(2).getID();

        // Add the IDs of the common objective cards to the array
        gameSetupCards[9] = getObjectiveCardsDeck().getCommonObjectives().getFirst().getID();
        gameSetupCards[10] = getObjectiveCardsDeck().getCommonObjectives().get(1).getID();

        int[] objToChoose = player.getSecretObjectiveCardIDs();

        // Add the IDs of the secret objective cards to choose from to the array
        gameSetupCards[11] = objToChoose[0];
        gameSetupCards[12] = objToChoose[1];

        try {
            gameSetupCards[13] = player.getStarterCard().getID();
            player.getSender().sendReOnObjectiveChoice(player.getID(), getIDtoNicknameMap(), gameSetupCards, player.getStarterCard().getCurrSideType(), player.getTokenColor());
        }
        // This exception should never be thrown
        catch (StarterCardNotSetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reconnects a player that was disconnected after the setup phase
     * @param player Player to reconnect
     */
    private void reconnectPlayerAfterSetup(PlayerModel player) {
        int[] gameSetupCards = new int[13];

        // Add the IDs of the deck's cards to the array
        gameSetupCards[0] = getResourceCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[1] = getVisibleResourceCards().getFirst().getID();
        gameSetupCards[2] = getVisibleResourceCards().get(1).getID();
        gameSetupCards[3] = getGoldenCardsDeck().getDeck().getFirst().getID();
        gameSetupCards[4] = getVisibleGoldenCards().getFirst().getID();
        gameSetupCards[5] = getVisibleGoldenCards().get(1).getID();

        // Add the IDs of the player's cards to the array
        gameSetupCards[6] = player.getHand().getFirst().getID();
        gameSetupCards[7] = player.getHand().get(1).getID();
        gameSetupCards[8] = player.getHand().get(2).getID();

        // Add the IDs of the common objective cards to the array
        gameSetupCards[9] = getObjectiveCardsDeck().getCommonObjectives().getFirst().getID();
        gameSetupCards[10] = getObjectiveCardsDeck().getCommonObjectives().get(1).getID();

        // Add the ID of the player's secret objective card to the array
        gameSetupCards[11] = player.getSecretObjective().getID();

        // Add the ID of the player's starter card to the array
        try {
            gameSetupCards[12] = player.getStarterCard().getID();
            player.getSender().sendReAfterSetup(player.getID(), getIDtoNicknameMap(), gameSetupCards, player.getStarterCard().getCurrSideType(), player.getTokenColor());
        }
        // This exception should never be thrown
        catch (StarterCardNotSetException e) {
            throw new RuntimeException(e);
        }
    }
}
