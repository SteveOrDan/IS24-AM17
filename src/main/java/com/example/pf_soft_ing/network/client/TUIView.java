package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.objectiveCards.*;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TUIView implements View {

    private ClientSender sender;
    private BufferedReader stdIn;

    private final List<PlayerViewModel> opponents = new ArrayList<>();

    private int playerID;
    private String playerNickname;
    private int matchID;
    private TokenColors tokenColor;
    private int score = 0;

    private final Map<Integer, List<String>> cardIDToCardFrontTUILines = new HashMap<>();
    private final Map<Integer, List<String>> cardIDToCardBackTUILines = new HashMap<>();

    private int commonObjectiveCardID1;
    private int commonObjectiveCardID2;

    private int resDeckCardID;
    private int visibleResCardID1;
    private int visibleResCardID2;

    private int goldDeckCardID;
    private int visibleGoldCardID1;
    private int visibleGoldCardID2;

    private PlaceableCard starterCard;

    private int secretObjectiveCardID1;
    private int secretObjectiveCardID2;

    private int secretObjectiveCardID;

    private int priority;

    private PlayerState playerState;

    private final List<Position> legalPosList = new ArrayList<>();
    private final List<Position> illegalPosList = new ArrayList<>();
    private final Map<Position, PlaceableCard> playArea = new HashMap<>();

    private final Map<Integer, Position> posIDToValidPos = new HashMap<>();

    private final List<PlaceableCard> playerHand = new ArrayList<>();

    private final Map<PlayerState, List<String>> stateToCommands = new HashMap<>();

    private final List<String> chat = new ArrayList<>();

    private static final int COUNT_DEC_PERIOD = 1000;
    private static final int MAX_PACKET_LOSS = 3;
    private int packetLoss = 0;
    private final Object packetLossLock = new Object();

    private final Timer timer = new Timer();
    private TimerTask packetLossTask;

    /**
     * Launches the game with text interface
     * @param args The arguments required to start the game
     */
    public void start(String[] args) {
        createStateToCommandsMap();
        playerState = PlayerState.MAIN_MENU;
        System.out.println("Welcome.\nThis is the application to play \"Codex Naturalis\".");

        stdIn = new BufferedReader(new InputStreamReader(System.in));
        boolean connected = false;
        int portNumber = 0;

        while (!connected){
            try {
                String connectionType = args[1];

                System.out.println("\nEnter the port number for " + connectionType + " connection:");
                boolean hasPort = false;

                while (!hasPort) {
                    try {
                        portNumber = Integer.parseInt(stdIn.readLine());
                        hasPort = true;
                    }
                    catch (Exception e) {
                        System.out.println("\nPlease insert a valid number.");
                    }
                }

                if (connectionType.equals("socket")) {
                    sender = ClientMain.startSocket(args[0], portNumber, this);
                }
                else {
                    sender = ClientMain.startRMI(args[0], portNumber, this);
                }
                connected = true;
            }
            catch (Exception e) {
                System.out.println("\nError: " + e.getMessage());
            }
        }

        sender.connect();

        startInputReading();
    }

    /**
     * Starts the thread that reads the input from the user
     */
    private void startInputReading() {
        new Thread(() -> {
            while (true) {
                try {
                    String userInput = stdIn.readLine();

                    if (userInput != null) {
                        interpretInput(userInput);
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Interprets the input from the user and calls the appropriate method
     * @param userInput The input from the user
     */
    private void interpretInput(String userInput) {
        String[] parts = userInput.split(" ");
        String command = parts[0].toLowerCase();

        List<String> legalCommands = stateToCommands.get(playerState);

        if (playerState == PlayerState.DISCONNECTED) {
            makeSpace();
            System.out.println("You are disconnected. Please, restart the game.");
            return;
        }

        if (legalCommands.contains(command)) {
            switch (command) {
                case "rml" -> { // RefreshMatchesList
                    if (parts.length != 1) {
                        System.out.println("\nError: RefreshMatchesList does not take any arguments. Please, try again");
                        break;
                    }

                    sender.getMatches();
                }

                case "cm" -> { // CreateMatch
                    if (parts.length != 3) {
                        System.out.println("\nError: CreateMatch takes exactly 2 arguments (num of players, nickname). Please, try again");
                        break;
                    }
                    try {
                        sender.createMatch(Integer.parseInt(parts[1]), parts[2]);
                    }
                    catch (NumberFormatException e){
                        System.out.println("\nError: " + parts[1] + " is not a valid number. Please, try again");
                    }
                }
                case "sm" -> { // SelectMatch
                    if (parts.length != 2) {
                        System.out.println("\nError: SelectMatch takes exactly 1 argument (match ID). Please, try again");
                        break;
                    }
                    try {
                        sender.selectMatch(Integer.parseInt(parts[1]));
                    }
                    catch (NumberFormatException e){
                        System.out.println("\nError: " + parts[1] + " is not a valid number. Please, try again");
                    }
                }
                case "cn" -> { // ChooseNickname
                    if (parts.length != 2) {
                        System.out.println("\nError: ChooseNickname takes exactly 1 argument (nickname). Please, try again");
                        break;
                    }

                    sender.chooseNickname(parts[1]);
                }
                case "reconnect" -> { // ReconnectToMatch
                    if (parts.length != 3) {
                        System.out.println("\nError: Reconnect takes exactly 2 arguments (match ID and nickname). Please, try again");
                        break;
                    }

                    try {
                        sender.reconnectToMatch(Integer.parseInt(parts[1]), parts[2]);
                    }
                    catch (NumberFormatException e){
                        System.out.println("\nError: " + parts[1] + " is not a valid number. Please, try again");
                    }
                }
                case "fsc" -> { // FlipStarterCard
                    if (parts.length != 1) {
                        System.out.println("\nError: FlipStarterCard does not take any arguments. Please, try again");
                        break;
                    }

                    flipStarterCard();
                }
                case "psc" -> { // PlaceStarterCard
                    if (parts.length != 1) {
                        System.out.println("\nError: PlaceStarterCard does not take any arguments. Please, try again");
                        break;
                    }

                    sender.placeStarterCard(playerID, starterCard.getCurrSideType());
                }
                case "cso" -> { // ChooseSecretObjective
                    if (parts.length != 2) {
                        System.out.println("\nError: ChooseSecretObjective takes exactly 1 argument (card ID). Please, try again");
                        break;
                    }

                    try {
                        int choice = Integer.parseInt(parts[1]);
                        if (choice != 1 && choice != 2) {
                            System.out.println("\nError: " + parts[1] + " is not a valid choice. Please choose either 1 or 2.");
                            break;
                        }

                        secretObjectiveCardID = choice == 1 ? secretObjectiveCardID1 : secretObjectiveCardID2;

                        sender.chooseSecretObjective(playerID, secretObjectiveCardID);
                    }
                    catch (Exception e) {
                        System.out.println("\nError: " + parts[1] + " is not a valid choice. Please choose either 1 or 2.");
                    }
                }
                case "fc" -> { // FlipCard
                    if (parts.length != 2) {
                        System.out.println("\nError: FlipCard takes exactly 1 argument (card ID). Please, try again");
                        break;
                    }

                    try {
                        for (PlaceableCard card : playerHand) {
                            if (card.getID() == Integer.parseInt(parts[1])) {
                                card.setCurrSideType(card.getCurrSideType() == CardSideType.FRONT ? CardSideType.BACK : CardSideType.FRONT);
                                break;
                            }
                        }
                        printPlayerHand();
                    }
                    catch (NumberFormatException e){
                        System.out.println("\nError: " + parts[1] + " is not a valid number. Please, try again");
                    }
                }
                case "pc" -> { // PlaceCard
                    if (parts.length != 3) {
                        System.out.println("\nError: PlaceCard takes exactly 2 arguments (card ID and position ID). Please, try again");
                        break;
                    }

                    try {
                        PlaceableCard placingCard = GameResources.getPlaceableCardByID(Integer.parseInt(parts[1]));
                        Position placingCardPos = posIDToValidPos.get(Integer.parseInt(parts[2]));

                        if (placingCard == null) {
                            System.out.println("\nError: " + parts[1] + " is not a valid card ID. Please, try again");
                            break;
                        }
                        else if (placingCardPos == null) {
                            System.out.println("\nError: " + parts[2] + " is not a valid position ID. Please, try again");
                            break;
                        }
                        sender.placeCard(playerID, placingCard.getID(), placingCard.getCurrSideType(), placingCardPos);
                    }
                    catch (NumberFormatException e){
                        System.out.println("\nError: " + parts[1] + " or " + parts[2] + " is not a valid number. Please, try again");
                    }
                }
                case "ddr" -> { // DrawDeckResourceCard
                    if (parts.length != 1) {
                        System.out.println("\nError: DrawDeckResourceCard does not take any arguments. Please, try again");
                        break;
                    }

                    sender.drawResourceCard(playerID);
                }
                case "dvr" -> { // DrawVisibleResourceCard
                    if (parts.length != 2) {
                        System.out.println("\nError: DrawResourceCard takes exactly 1 argument (card index 0 or 1). Please, try again");
                        break;
                    }

                    try {
                        sender.drawVisibleResourceCard(playerID, Integer.parseInt(parts[1]));
                    }
                    catch (NumberFormatException e){
                        System.out.println("\nError: " + parts[1] + " is not a valid number. Please, try again");
                    }
                }
                case "ddg" -> { // DrawDeckGoldCard
                    if (parts.length != 1) {
                        System.out.println("\nError: DrawDeckGoldenCard does not take any arguments. Please, try again");
                        break;
                    }

                    sender.drawGoldenCard(playerID);
                }
                case "dvg" -> { // DrawVisibleGoldCard
                    if (parts.length != 2) {
                        System.out.println("\nError: DrawVisibleGoldenCard takes exactly 1 argument (card index 0 or 1). Please, try again");
                        break;
                    }
                    try {
                        int choice = Integer.parseInt(parts[1]);
                        if (choice != 0 && choice != 1) {
                            System.out.println("\nError: " + parts[1] + " is not a valid choice. Please choose either 0 or 1.");
                            break;
                        }
                        sender.drawVisibleGoldenCard(playerID, choice);
                    }
                    catch (NumberFormatException e){
                        System.out.println("\nError: " + parts[1] + " is not a valid number. Please, try again");
                    }
                }
                case "gh" -> { // GetHand
                    if (parts.length != 1) {
                        System.out.println("\nError: GetHand does not take any arguments. Please, try again");
                        break;
                    }

                    printPlayerHand();
                }
                case "opa" -> { // PrintOpponentPlayArea
                    if (parts.length != 2) {
                        System.out.println("\nError: OpponentPlayArea takes exactly 1 argument (opponent nickname). Please, try again");
                        break;
                    }
                    if (parts[1].equals(playerNickname)) {
                        System.out.println("\nError: OpponentPlayArea takes exactly 1 argument (opponent nickname, not your name). Please, try again");
                        break;
                    }

                    printOpponentPlayArea(parts[1]);
                }
                case "pa" -> { // PrintPlayArea
                    if (parts.length != 1) {
                        System.out.println("\nError: PlayArea does not take any arguments. Please, try again");
                        break;
                    }

                    printPlayArea();
                }
                case "chat" -> { // Write a message in the chat
                    if (parts.length < 3) {
                        System.out.println("\nError: Chat takes at least 2 argument (recipient nickname (or all) and message). Please, try again");
                        break;
                    }

                    List<String> partsArray = new ArrayList<>(Arrays.asList(parts));

                    sender.sendChatMessage(playerID, partsArray.get(1), String.join(" ", partsArray.subList(2, partsArray.size())));
                }
                case "gmc" -> { // GetMatchChat
                    if (parts.length != 1) {
                        System.out.println("\nError: GetMatchChat does not take any arguments. Please, try again");
                        break;
                    }

                    printChat();
                }
                case "gmi" -> { //GetMatchID
                    if (parts.length != 1) {
                        System.out.println("\nError: GetMatchID does not take any arguments. Please, try again");
                        break;
                    }

                    System.out.println("\nMatch ID: " + matchID);
                    makeSpace();
                }
                case "gs" -> { // GetScore
                    if (parts.length != 1) {
                        System.out.println("\nError: GetScore does not take any arguments. Please, try again");
                        break;
                    }

                    printAllScores();
                }
                case "mm" -> { // Main Menu
                    if (parts.length != 1) {
                        System.out.println("\nError: MainMenu does not take any arguments. Please, try again");
                        break;
                    }
                    resetPlayerModel();
                    playerState = PlayerState.MAIN_MENU;
                    sender.getMatches();
                }
            }
        }
        else if (command.equals("exit") || command.equals("quit")) {
            System.exit(0);
        }
        else {
            System.out.println("\nError: " + command + " is not a valid command. Please, try again");
        }
    }

    @Override
    public void setID(int playerID) {
        sender.setPlayerID(playerID);
        this.playerID = playerID;
    }

    @Override
    public void showMatches(Map<Integer, List<String>> matches) {
        makeSpace();
        if (matches.isEmpty()) {
            System.out.println("""
                    No matches available.
                    To create a new match, type: cm <players_num> <nickname>
                    To refresh the matches list, type: rml""");
            return;
        }

        System.out.println("Matches:");
        for (Map.Entry<Integer, List<String>> entry : matches.entrySet()) {
            System.out.println("\tMatch with ID " + entry.getKey() + " and players:");
            System.out.println("\t\t- " + entry.getValue());
        }
        System.out.println("""
                \nTo create a new match, type: cm <players_num> <nickname>
                To join a match, type: sm <matchID>
                To refresh the matches list, type: rml
                To reconnect to a match, type: reconnect <matchID> <nickname>""");
    }

    @Override
    public void createMatch(int matchID, String hostNickname) {
        startConnectionCheck();

        playerState = PlayerState.MATCH_LOBBY;
        playerNickname = hostNickname;
        this.matchID = matchID;

        makeSpace();
        System.out.println("Match created with ID: " + matchID + " and host nickname: " + hostNickname);
        System.out.println("""
                Waiting for players to join...
                To view the match ID during the game, type: gmi""");
    }

    @Override
    public void selectMatch(int matchID, List<String> nicknames) {
        startConnectionCheck();

        playerState = PlayerState.CHOOSING_NICKNAME;
        this.matchID = matchID;

        makeSpace();
        System.out.println("Match selected with ID: " + matchID);
        System.out.println("""
                To choose the nickname, type: cn <nickname>
                To view the match ID during the game, type: gmi""");
    }

    @Override
    public void chooseNickname(String nickname) {
        playerState = PlayerState.MATCH_LOBBY;
        playerNickname = nickname;

        makeSpace();
        System.out.println("Joined match with nickname: " + nickname);
    }

    @Override
    public void startGame(Map<Integer, String> IDToNicknameMap,
                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) {
        createInvalidCardLines();

        makeSpace();
        System.out.println("Game started.");

        for (Map.Entry<Integer, String> entry : IDToNicknameMap.entrySet()) {
            if (entry.getKey() != playerID){
                PlayerViewModel opponent = new PlayerViewModel();
                opponent.setPlayerID(entry.getKey());
                opponent.setNickname(entry.getValue());
                opponents.add(opponent);
            }
            else {
                playerNickname = IDToNicknameMap.get(playerID);
            }
        }

        playerState = PlayerState.PLACING_STARTER;

        GameResources.initializeAllDecks();

        // Update draw area cards
        setDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);

        // Print 4 visible cards, 2 deck top card and starter card
        printDrawArea();

        // Create and print starter card
        printStarterCardChoice(starterCardID);

        // Print available commands
        System.out.println("""
                To view the other side of the starter card, type: fsc
                To place the starter card on the current side, type: psc""");
    }

    /**
     * Creates the lines for an invalid card and adds them to the cardIDToCardFrontTUILines and cardIDToCardBackTUILines maps.
     */
    private void createInvalidCardLines() {
        List<String> invalidCardLines = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            invalidCardLines.add("                         ");
        }

        cardIDToCardFrontTUILines.put(-1, invalidCardLines);
        cardIDToCardBackTUILines.put(-1, invalidCardLines);
    }

    @Override
    public void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                                int commonObjectiveCardID1, int commonObjectiveCardID2,
                                int secretObjectiveCardID1, int secretObjectiveCardID2) {
        // Place the starter card in the play area and update all legal and illegal positions
        starterCard.setPriority(priority);
        playArea.put(new Position(0, 0), starterCard);
        priority++;

        updatePlacementPositions(new Position(0, 0));

        // Set missing setup
        playerState = PlayerState.CHOOSING_OBJECTIVE;

        // Set token color
        setTokenColor(tokenColor);

        // Add cards to player hand ====================================================================================
        createPlayerHand(resourceCardID1, resourceCardID2, goldenCardID);

        // Print player hand
        printPlayerHand();

        // Create common objectives ====================================================================================
        setCommonObjectives(commonObjectiveCardID1, commonObjectiveCardID2);

        // Print common objectives
        printCommonObjectives();


        // Set secret objective ========================================================================================
        setSecretObjectives(secretObjectiveCardID1, secretObjectiveCardID2);

        // Print secret objective choice
        printSecretObjectiveChoice();
    }

    @Override
    public void confirmSecretObjective() {
        playerState = PlayerState.COMPLETED_SETUP;

        int otherSecretObjectiveCardID = secretObjectiveCardID == secretObjectiveCardID1 ? secretObjectiveCardID2 : secretObjectiveCardID1;

        cardIDToCardFrontTUILines.remove(otherSecretObjectiveCardID);

        makeSpace();
        System.out.println("Secret objective chosen:");

        List<String> secretObjectiveCardFront = cardIDToCardFrontTUILines.get(secretObjectiveCardID);

        for (String s : secretObjectiveCardFront) {
            System.out.println(s);
        }

        System.out.println("Waiting for other players to end their setup.");
    }

    @Override
    public void errorMessage(String message) {
        System.out.println(message);
    }


    @Override
    public void showFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs,
                                    int[] starterCardIDs, CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands) {
        if (this.playerID == lastPlayerID) {
            confirmSecretObjective();
        }

        // Add starter card to opponents' play areas
        for (int i = 0; i < playerIDs.length; i++){
            if (playerIDs[i] != this.playerID) {
                getOpponentByID(playerIDs[i]).setTokenColor(tokenColors[i]);
                getOpponentByID(playerIDs[i]).placeCardInPlayArea(starterCardIDs[i], starterCardSides[i], new Position(0, 0), 0);

                for (int cardID : playerHands[i]) {
                    getOpponentByID(playerIDs[i]).drawCard(cardID);
                }
            }
            else {
                setTokenColor(tokenColors[i]);
            }
        }

        makeSpace();
        System.out.println("""
                Now you can use the chat.
                To view the match chat, type: gmc
                To send a message in the chat, type: chat <recipient nickname> <message>
                    recipient nickname can be 'all' to send a message to all players.""");

        if (playerID == this.playerID) {
            playerState = PlayerState.PLACING;
            System.out.println("\nIt's your turn.");

            // Print player hand
            printPlayerHand();

            // Print play area
            printPlayArea();

            // Print available commands
            System.out.println("""
                    These are the available commands:
                    Check your hand: gh
                    Flip a card: fc <cardID>
                    Place a card: pc <cardID> <posID>
                    Check actual score: gs
                    Check opponent's play area: opa <opponent nickname>
                    Check your own play area: pa""");
        }
        else {
            playerState = PlayerState.WAITING;
            String playerNickname = getPlayerNickname(playerID);

            System.out.println("\nIt's " + playerNickname + "'s turn.\n" +
                    "While waiting you can: \n" +
                    "\t- Flip a card in your hand by typing: fc <cardID>\n" +
                    "\t- Check your hand by typing: gh");
        }
    }

    @Override
    public void placeCard(int playerID, int cardID, Position pos, CardSideType side, int deltaScore) {
        makeSpace();
        if (this.playerID == playerID) {
            playerState = PlayerState.DRAWING;

            this.score += deltaScore;

            PlaceableCard placingCard = GameResources.getPlaceableCardByID(cardID);
            placingCard.setPriority(priority);
            priority++;

            // Add card to play area
            playArea.put(pos, placingCard);

            // Update legal and illegal positions
            updatePlacementPositions(pos);

            // Remove card from player hand
            for (PlaceableCard card : playerHand) {
                if (card.getID() == placingCard.getID()) {
                    playerHand.remove(card);
                    break;
                }
            }

            // Print play area
            printPlayArea();

            // Print draw area
            printDrawArea();

            // Print player scored points
            System.out.println("You scored " + deltaScore + " points");

            // Print available commands
            System.out.println("""
                To draw from resource deck, type: ddr
                To draw a visible resource card, type: dvr <0 or 1>
                To draw from gold deck, type: ddg
                To draw a visible gold card, type: dvg <0 or 1>""");
        }
        else {
            // Place card for opponent
            PlayerViewModel opponent = getOpponentByID(playerID);
            opponent.placeCardInPlayArea(cardID, side, pos, deltaScore);

            System.out.println("Opponent " + opponent.getNickname() + " scored " + deltaScore + " points");
        }
    }

    @Override
    public void showNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID) {
        makeSpace();
        String playerNickname = getPlayerNickname(newPlayerID);

        if (lastPlayerID == playerID) {
            playerState = PlayerState.WAITING;

            PlaceableCard drawnCard = GameResources.getPlaceableCardByID(drawnCardID);

            playerHand.add(drawnCard);

            System.out.println("You drew a new card (ID: " + drawnCardID + "):");
            createPlaceableCardLines(drawnCardID, CardSideType.FRONT);
            createPlaceableCardLines(drawnCardID, CardSideType.BACK);

            for (String s : cardIDToCardFrontTUILines.get(drawnCardID)) {
                System.out.println(s);
            }
            System.out.println("It's " + playerNickname + "'s turn.");
            System.out.println("""
                    While waiting you can flip a card in your hand by typing: fc <cardID>
                    To check your hand, type: gh
                    To check actual score, type: gs
                    To check opponents play area: opa <opponent Nickname>""");
        }
        else {
            if (playerID == newPlayerID) {
                playerState = PlayerState.PLACING;

                System.out.println("It's your turn.");

                // Print player hand
                printPlayerHand();

                // Print play area
                printPlayArea();

                // Print available commands
                System.out.println("""
                        To check your hand, type: gh
                        To flip a card, type: fc <cardID>
                        To place a card, type: pc <cardID> <posID>""");
            }
            else {
                playerState = PlayerState.WAITING;

                // Print available commands
                System.out.println("It's " + playerNickname + "'s turn.\n" +
                        "While waiting you can flip a card in your hand by typing: fc <cardID>\n" +
                        "To check your hand, type: gh\n" +
                        "To check opponents play area: opa <opponent nickname>");
            }

            getOpponentByID(lastPlayerID).drawCard(drawnCardID);
        }
    }

    @Override
    public void updateDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        setDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);

        printDrawArea();
    }

    /**
     * Sets the class attributes for the 6 cards in the draw area.
     * Creates the TUI lines (front and back) for the given cards if they are not already created.
     * @param resDeckCardID The ID of the resource deck card.
     * @param visibleResCardID1 The ID of the first visible resource card.
     * @param visibleResCardID2 The ID of the second visible resource card.
     * @param goldDeckCardID The ID of the gold deck card.
     * @param visibleGoldCardID1 The ID of the first visible gold card.
     * @param visibleGoldCardID2 The ID of the second visible gold card.
     */
    private void setDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        this.resDeckCardID = resDeckCardID;
        this.visibleResCardID1 = visibleResCardID1;
        this.visibleResCardID2 = visibleResCardID2;

        this.goldDeckCardID = goldDeckCardID;
        this.visibleGoldCardID1 = visibleGoldCardID1;
        this.visibleGoldCardID2 = visibleGoldCardID2;

        // Create TUI lines for resource cards
        if (!cardIDToCardFrontTUILines.containsKey(resDeckCardID)) {
            createResCardLines(resDeckCardID, CardSideType.FRONT);
            createResCardLines(resDeckCardID, CardSideType.BACK);
        }

        if (!cardIDToCardFrontTUILines.containsKey(visibleResCardID1)) {
            createResCardLines(visibleResCardID1, CardSideType.FRONT);
            createResCardLines(visibleResCardID1, CardSideType.BACK);
        }

        if (!cardIDToCardFrontTUILines.containsKey(visibleResCardID2)) {
            createResCardLines(visibleResCardID2, CardSideType.FRONT);
            createResCardLines(visibleResCardID2, CardSideType.BACK);
        }

        // Create TUI lines for gold cards
        if (!cardIDToCardFrontTUILines.containsKey(goldDeckCardID)) {
            createGoldCardLines(goldDeckCardID, CardSideType.FRONT);
            createGoldCardLines(goldDeckCardID, CardSideType.BACK);
        }

        if (!cardIDToCardFrontTUILines.containsKey(visibleGoldCardID1)) {
            createGoldCardLines(visibleGoldCardID1, CardSideType.FRONT);
            createGoldCardLines(visibleGoldCardID1, CardSideType.BACK);
        }

        if (!cardIDToCardFrontTUILines.containsKey(visibleGoldCardID2)) {
            createGoldCardLines(visibleGoldCardID2, CardSideType.FRONT);
            createGoldCardLines(visibleGoldCardID2, CardSideType.BACK);
        }
    }

    /**
     * Prints the draw area.
     * The draw area consists of 6 cards divided in 2 areas:
     *  - 3 resource cards (1 deck card and 2 visible cards)
     *  - 3 golden cards (1 deck card and 2 visible cards)
     */
    private void printDrawArea() {
        makeSpace();
        System.out.println("Draw area:");

        // Print TUI lines for cards in the draw area
        List<String> resDeckCard = cardIDToCardBackTUILines.get(resDeckCardID);
        List<String> visibleResCard1 = cardIDToCardFrontTUILines.get(visibleResCardID1);
        List<String> visibleResCard2 = cardIDToCardFrontTUILines.get(visibleResCardID2);

        System.out.println("Resource cards:");
        for (int i = 0; i < resDeckCard.size(); i++) {
            System.out.println(resDeckCard.get(i) + "   " + visibleResCard1.get(i) + "   " + visibleResCard2.get(i));
        }

        List<String> goldDeckCard = cardIDToCardBackTUILines.get(goldDeckCardID);
        List<String> visibleGoldCard1 = cardIDToCardFrontTUILines.get(visibleGoldCardID1);
        List<String> visibleGoldCard2 = cardIDToCardFrontTUILines.get(visibleGoldCardID2);

        System.out.println("Golden cards:");
        for (int i = 0; i < goldDeckCard.size(); i++) {
            System.out.println(goldDeckCard.get(i) + "   " + visibleGoldCard1.get(i) + "   " + visibleGoldCard2.get(i));
        }
    }

    /**
     * Prints the message for the choice of the starter card
     * @param starterCardID ID of the starter card to print
     */
    private void printStarterCardChoice(int starterCardID) {
        makeSpace();
        starterCard = GameResources.getPlaceableCardByID(starterCardID);
        starterCard.setCurrSideType(CardSideType.FRONT);

        createStarterCardLines(starterCardID, CardSideType.FRONT);
        createStarterCardLines(starterCardID, CardSideType.BACK);

        List<String> starterCardFront = cardIDToCardFrontTUILines.get(starterCardID);

        System.out.println("Starter card:");
        for (String s : starterCardFront) {
            System.out.println(s);
        }
    }

    /**
     * Flips the starter card and prints the new side
     */
    private void flipStarterCard() {
        List<String> starterCardLines;

        if (starterCard.getCurrSideType().equals(CardSideType.FRONT)) {
            starterCard.setCurrSideType(CardSideType.BACK);

            starterCardLines = cardIDToCardBackTUILines.get(starterCard.getID());
        }
        else {
            starterCard.setCurrSideType(CardSideType.FRONT);

            starterCardLines = cardIDToCardFrontTUILines.get(starterCard.getID());
        }

        System.out.println("\nCurrent starter card side:");
        for (String s : starterCardLines) {
            System.out.println(s);
        }

        System.out.println("To view the other side of the starter card, type: fsc\n" +
                "To place the starter card on the current side, type: psc");
    }

    /**
     * Setter
     * @param resourceCardID1 ID of the first resource card
     * @param resourceCardID2 ID of the second resource card
     * @param goldenCardID ID of the golden card
     */
    private void createPlayerHand(int resourceCardID1, int resourceCardID2, int goldenCardID) {
        makeSpace();
        PlaceableCard resourceCard1 = GameResources.getPlaceableCardByID(resourceCardID1);
        PlaceableCard resourceCard2 = GameResources.getPlaceableCardByID(resourceCardID2);
        PlaceableCard goldenCard = GameResources.getPlaceableCardByID(goldenCardID);

        playerHand.add(resourceCard1);
        playerHand.add(resourceCard2);
        playerHand.add(goldenCard);

        // Create player hand
        createResCardLines(resourceCardID1, CardSideType.FRONT);
        createResCardLines(resourceCardID1, CardSideType.BACK);

        createResCardLines(resourceCardID2, CardSideType.FRONT);
        createResCardLines(resourceCardID2, CardSideType.BACK);

        createGoldCardLines(goldenCardID, CardSideType.FRONT);
        createGoldCardLines(goldenCardID, CardSideType.BACK);
    }

    /**
     * Prints the player's hand
     */
    private void printPlayerHand() {
        List<List<String>> handToPrint = new ArrayList<>();
        StringBuilder cardIDs = new StringBuilder("Your hand:");

        for (PlaceableCard card : playerHand) {
            cardIDs.append("\t").append(card.getID());

            if (card.getCurrSideType().equals(CardSideType.FRONT)) {
                handToPrint.add(cardIDToCardFrontTUILines.get(card.getID()));
            }
            else {
                handToPrint.add(cardIDToCardBackTUILines.get(card.getID()));
            }
        }

        System.out.println(cardIDs);
        for (int i = 0; i < handToPrint.getFirst().size(); i++) {
            StringBuilder line = new StringBuilder();

            for (List<String> card : handToPrint) {
                line.append(card.get(i)).append("   ");
            }

            System.out.println(line);
        }
    }

    /**
     * Setter
     * @param commonObjectiveCardID1 ID of the first common objective card
     * @param commonObjectiveCardID2 ID of the second common objective card
     */
    private void setCommonObjectives(int commonObjectiveCardID1, int commonObjectiveCardID2) {
        this.commonObjectiveCardID1 = commonObjectiveCardID1;
        this.commonObjectiveCardID2 = commonObjectiveCardID2;

        createObjectiveCardLines(commonObjectiveCardID1);
        createObjectiveCardLines(commonObjectiveCardID2);
    }

    /**
     * Prints the common objectives
     */
    private void printCommonObjectives() {
        List<String> commonObjectiveCard1Front = cardIDToCardFrontTUILines.get(commonObjectiveCardID1);
        List<String> commonObjectiveCard2Front = cardIDToCardFrontTUILines.get(commonObjectiveCardID2);

        System.out.println("Common objectives:");
        for (int i = 0; i < commonObjectiveCard1Front.size(); i++) {
            System.out.println(commonObjectiveCard1Front.get(i) + "   " + commonObjectiveCard2Front.get(i));
        }
    }

    /**
     * Prints the player's secret objective
     */
    private void printSecretObjective() {
        List<String> secretObjectiveCardFront = cardIDToCardFrontTUILines.get(secretObjectiveCardID);

        System.out.println("Secret objective:");
        for (String s : secretObjectiveCardFront) {
            System.out.println(s);
        }
    }

    /**
     * Setter
     * @param secretObjectiveCardID1 ID of the first secret objective card
     * @param secretObjectiveCardID2 ID of the second secret objective card
     */
    private void setSecretObjectives(int secretObjectiveCardID1, int secretObjectiveCardID2) {
        this.secretObjectiveCardID1 = secretObjectiveCardID1;
        this.secretObjectiveCardID2 = secretObjectiveCardID2;

        createObjectiveCardLines(secretObjectiveCardID1);
        createObjectiveCardLines(secretObjectiveCardID2);
    }

    /**
     * Prints the choice of the secret objective
     */
    private void printSecretObjectiveChoice() {
        List<String> secretObjectiveCard1Front = cardIDToCardFrontTUILines.get(secretObjectiveCardID1);
        List<String> secretObjectiveCard2Front = cardIDToCardFrontTUILines.get(secretObjectiveCardID2);

        System.out.println("Secret objectives:");

        for (int i = 0; i < secretObjectiveCard1Front.size(); i++) {
            System.out.println(secretObjectiveCard1Front.get(i) + "   " + secretObjectiveCard2Front.get(i));
        }

        System.out.println("To choose one of the secret objectives, type: cso <1 or 2>.");
    }

    /**
     * Create the matrix containing al characters representing the play area
     * @param playArea The map containing the cards in the play area
     * @return The matrix calculated
     */
    private String[][] createPlayerArr(Map<Position, PlaceableCard> playArea) {
        if (playArea.isEmpty()) {
            return null;
        }

        // Get the max and min values of x and y
        int maxX = playArea.keySet().stream().map(Position::getX).max(Integer::compareTo).orElse(0);
        int minX = playArea.keySet().stream().map(Position::getX).min(Integer::compareTo).orElse(0);
        int maxY = playArea.keySet().stream().map(Position::getY).max(Integer::compareTo).orElse(0);
        int minY = playArea.keySet().stream().map(Position::getY).min(Integer::compareTo).orElse(0);

        // +3 because of the starter card and the 1 extra space on each border (for the validPosIDs)
        PlaceableCard[][] cardArr = new PlaceableCard[maxY - minY + 3][maxX - minX + 3];
        String[][] playAreaArr = new String[3 * (maxY - minY + 3) + 2][3 * (maxX - minX + 3) + 2];

        // The position of the starter card is new Position(-minX + 1, maxY + 1);
        // And the array's Y is inverted, so the starter card's position in the array is [xPos - minX + 1][-yPos + maxY + 1]

        // Fill the array with elements from the playArea map
        for (Position pos : playArea.keySet()) {
            cardArr[-pos.getY() + maxY + 1][pos.getX() - minX + 1] = playArea.get(pos);
        }

        // Fill array of characters to print
        for (int y = 0; y < cardArr.length; y++) {
            for (int x = 0; x < cardArr[y].length; x++) {
                if (x > 0 && x < cardArr[y].length - 1 &&
                        y > 0 && y < cardArr.length - 1) {
                    PlaceableCard card = cardArr[y][x];

                    if (card != null) {
                        // region Set permanent resources and constant card areas
                        String PR1, PR2, PR3;

                        Side currSide = card.getCurrSide();

                        if (card.getCurrSideType().equals(CardSideType.BACK)) {
                            List<ResourceType> permanentRes = card.getCurrSide().getResources();

                            List<ResourceType> cornerResources = new ArrayList<>(){{
                                add(currSide.getTLCorner().getResource());
                                add(currSide.getTRCorner().getResource());
                                add(currSide.getBLCorner().getResource());
                                add(currSide.getBRCorner().getResource());
                            }};

                            for (ResourceType cornerRes : cornerResources) {
                                if (cornerRes != null){
                                    permanentRes.remove(cornerRes);
                                }
                            }

                            if (permanentRes.size() == 1) {
                                PR1 = " ";
                                PR2 = currSide.getResources().getFirst().toString();
                                PR3 = " ";
                            }
                            else if (permanentRes.size() == 2) {
                                PR1 = currSide.getResources().getFirst().toString();
                                PR2 = " ";
                                PR3 = currSide.getResources().get(1).toString();
                            }
                            else { // size == 3
                                PR1 = currSide.getResources().getFirst().toString();
                                PR2 = currSide.getResources().get(1).toString();
                                PR3 = currSide.getResources().get(2).toString();
                            }
                        }
                        else {
                            PR1 = " ";
                            PR2 = " ";
                            PR3 = " ";
                        }

                        String cardColor = card.getElementType().getColor();

                        playAreaArr[3 * y][3 * x + 2] = cardColor + "─" + CC.RESET;
                        playAreaArr[3 * y + 1][3 * x + 2] = " ";
                        playAreaArr[3 * y + 2][3 * x + 2] = PR2;
                        playAreaArr[3 * y + 3][3 * x + 2] = " ";
                        playAreaArr[3 * y + 4][3 * x + 2] = cardColor + "─" + CC.RESET;

                        playAreaArr[3 * y + 2][3 * x] = cardColor + "│" + CC.RESET;
                        playAreaArr[3 * y + 2][3 * x + 1] = PR1;
                        playAreaArr[3 * y + 2][3 * x + 3] = PR3;
                        playAreaArr[3 * y + 2][3 * x + 4] = cardColor + "│" + CC.RESET;
                        // endregion

                        PlaceableCard TLCard = cardArr[y - 1][x - 1];
                        PlaceableCard TRCard = cardArr[y - 1][x + 1];
                        PlaceableCard BLCard = cardArr[y + 1][x - 1];
                        PlaceableCard BRCard = cardArr[y + 1][x + 1];

                        // Check if there is a card in the TL corner
                        if (playAreaArr[3 * y][3 * x] == null) {
                            if (TLCard != null) {
                                if (TLCard.getPriority() > card.getPriority()){
                                    String TLColor = TLCard.getElementType().getColor();

                                    playAreaArr[3 * y + 1][3 * x + 1] = TLColor + "┘" + CC.RESET;
                                    playAreaArr[3 * y + 1][3 * x] = TLColor + "┬" + CC.RESET;
                                    playAreaArr[3 * y][3 * x + 1] = TLColor + "├" + CC.RESET;
                                    ResourceType TLRes = TLCard.getCurrSide().getBRCorner().getResource();
                                    playAreaArr[3 * y][3 * x] = TLRes == null ? " " : TLRes.toString();
                                }
                                else {
                                    playAreaArr[3 * y][3 * x] = cardColor + "┌" + CC.RESET;
                                    playAreaArr[3 * y][3 * x + 1] = cardColor + "┴" + CC.RESET;
                                    playAreaArr[3 * y + 1][3 * x] = cardColor + "┤" + CC.RESET;
                                    ResourceType TLRes = card.getCurrSide().getTLCorner().getResource();
                                    playAreaArr[3 * y + 1][3 * x + 1] = TLRes == null ? " " : TLRes.toString();
                                }
                            }
                            else {
                                playAreaArr[3 * y][3 * x] = cardColor + "┌" + CC.RESET;
                                playAreaArr[3 * y][3 * x + 1] = cardColor + "─" + CC.RESET;
                                playAreaArr[3 * y + 1][3 * x] = cardColor + "│" + CC.RESET;
                                ResourceType TLRes = card.getCurrSide().getTLCorner().getResource();
                                playAreaArr[3 * y + 1][3 * x + 1] = TLRes == null ? " " : TLRes.toString();
                            }
                        }

                        // Check if there is a card in the TR corner
                        if (playAreaArr[3 * y][3 * x + 3] == null) {
                            if (TRCard != null) {
                                if (TRCard.getPriority() > card.getPriority()){
                                    String TRColor = TRCard.getElementType().getColor();

                                    playAreaArr[3 * y + 1][3 * x + 4] = TRColor + "┬" + CC.RESET;
                                    playAreaArr[3 * y + 1][3 * x + 3] = TRColor + "└" + CC.RESET;
                                    playAreaArr[3 * y][3 * x + 3] = TRColor + "┤" + CC.RESET;
                                    ResourceType TRRes = TRCard.getCurrSide().getBLCorner().getResource();
                                    playAreaArr[3 * y][3 * x + 4] = TRRes == null ? " " : TRRes.toString();
                                }
                                else {
                                    playAreaArr[3 * y][3 * x + 3] = cardColor + "┴" + CC.RESET;
                                    playAreaArr[3 * y][3 * x + 4] = cardColor + "┐" + CC.RESET;
                                    playAreaArr[3 * y + 1][3 * x + 4] = cardColor + "├" + CC.RESET;
                                    ResourceType TRRes = card.getCurrSide().getTRCorner().getResource();
                                    playAreaArr[3 * y + 1][3 * x + 3] = TRRes == null ? " " : TRRes.toString();
                                }
                            }
                            else {
                                playAreaArr[3 * y][3 * x + 4] = cardColor + "┐" + CC.RESET;
                                playAreaArr[3 * y][3 * x + 3] = cardColor + "─" + CC.RESET;
                                playAreaArr[3 * y + 1][3 * x + 4] = cardColor + "│" + CC.RESET;
                                ResourceType TRRes = card.getCurrSide().getTRCorner().getResource();
                                playAreaArr[3 * y + 1][3 * x + 3] = TRRes == null ? " " : TRRes.toString();
                            }
                        }

                        // Check if there is a card in the BL corner
                        if (playAreaArr[3 * y + 3][3 * x] == null){
                            if (BLCard != null) {
                                if (BLCard.getPriority() > card.getPriority()){
                                    String BLColor = BLCard.getElementType().getColor();

                                    playAreaArr[3 * y + 3][3 * x] = BLColor + "┴" + CC.RESET;
                                    playAreaArr[3 * y + 4][3 * x + 1] = BLColor + "├" + CC.RESET;
                                    playAreaArr[3 * y + 3][3 * x + 1] = BLColor + "┐" + CC.RESET;
                                    ResourceType BLRes = BLCard.getCurrSide().getTRCorner().getResource();
                                    playAreaArr[3 * y + 4][3 * x] = BLRes == null ? " " : BLRes.toString();
                                }
                                else {
                                    playAreaArr[3 * y + 4][3 * x + 1] = cardColor + "┬" + CC.RESET;
                                    playAreaArr[3 * y + 3][3 * x] = cardColor + "┤" + CC.RESET;
                                    playAreaArr[3 * y + 4][3 * x] = cardColor + "└" + CC.RESET;
                                    ResourceType BLRes = card.getCurrSide().getBLCorner().getResource();
                                    playAreaArr[3 * y + 3][3 * x + 1] = BLRes == null ? " " : BLRes.toString();
                                }
                            }
                            else {
                                playAreaArr[3 * y + 4][3 * x + 1] = cardColor + "─" + CC.RESET;
                                playAreaArr[3 * y + 3][3 * x] = cardColor + "│" + CC.RESET;
                                playAreaArr[3 * y + 4][3 * x] = cardColor + "└" + CC.RESET;
                                ResourceType BLRes = card.getCurrSide().getBLCorner().getResource();
                                playAreaArr[3 * y + 3][3 * x + 1] = BLRes == null ? " " : BLRes.toString();
                            }
                        }

                        // Check if there is a card in the BR corner
                        if (playAreaArr[3 * y + 3][3 * x + 3] == null){
                            if (BRCard != null) {
                                if (BRCard.getPriority() > card.getPriority()) {
                                    String BRColor = BRCard.getElementType().getColor();

                                    playAreaArr[3 * y + 3][3 * x + 3] = BRColor + "┌" + CC.RESET;
                                    playAreaArr[3 * y + 3][3 * x + 4] = BRColor + "┴" + CC.RESET;
                                    playAreaArr[3 * y + 4][3 * x + 3] = BRColor + "┤" + CC.RESET;
                                    ResourceType BRRes = BRCard.getCurrSide().getTLCorner().getResource();
                                    playAreaArr[3 * y + 4][3 * x + 4] = BRRes == null ? " " : BRRes.toString();
                                }
                                else {
                                    playAreaArr[3 * y + 4][3 * x + 4] = cardColor + "┘" + CC.RESET;
                                    playAreaArr[3 * y + 3][3 * x + 4] = cardColor + "├" + CC.RESET;
                                    playAreaArr[3 * y + 4][3 * x + 3] = cardColor + "┬" + CC.RESET;
                                    ResourceType BRRes = card.getCurrSide().getBRCorner().getResource();
                                    playAreaArr[3 * y + 3][3 * x + 3] = BRRes == null ? " " : BRRes.toString();
                                }
                            }
                            else {
                                playAreaArr[3 * y + 4][3 * x + 4] = cardColor + "┘" + CC.RESET;
                                playAreaArr[3 * y + 3][3 * x + 4] = cardColor + "│" + CC.RESET;
                                playAreaArr[3 * y + 4][3 * x + 3] = cardColor + "─" + CC.RESET;
                                ResourceType BRRes = card.getCurrSide().getBRCorner().getResource();
                                playAreaArr[3 * y + 3][3 * x + 3] = BRRes == null ? " " : BRRes.toString();
                            }
                        }
                    }
                }
            }
        }

        return playAreaArr;
    }

    /**
     * Prints the play area of the player.
     */
    private void printPlayArea() {
        String[][] playAreaArr = createPlayerArr(playArea);

        // Print the token color and score
        System.out.println("Your token color: " + tokenColor + "\n" +
                "Your score: " + score + "\n");

        if (playAreaArr == null) {
            System.out.println("No cards in play area");
            return;
        }

        int minX = playArea.keySet().stream().map(Position::getX).min(Integer::compareTo).orElse(0);
        int maxY = playArea.keySet().stream().map(Position::getY).max(Integer::compareTo).orElse(0);

        // Fill the validPosIDs
        for (int i : posIDToValidPos.keySet()) {
            Position pos = posIDToValidPos.get(i);
            Position arrPos = new Position(3 * (pos.getX() - minX + 1), 3 * (-pos.getY() + maxY + 1));

            if (i < 10) {
                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 2] = String.valueOf(i);
            }
            else {
                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 1] = String.valueOf(i / 10);
                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 2] = String.valueOf(i % 10);
            }
        }

        System.out.println("Your play area:");

        // Print the play area
        for (String[] strings : playAreaArr) {
            StringBuilder line = new StringBuilder();

            for (String s : strings) {
                line.append(Objects.requireNonNullElse(s, " "));
            }
            System.out.println(line);
        }
    }

    /**
     * Prints the play area of the opponent with the given nickname.
     * @param opponentNick The nickname of the opponent.
     */
    private void printOpponentPlayArea(String opponentNick) {
        // Check if the opponent nickname exists
        if (!nicknameExists(opponentNick)) {
            System.out.println("No opponent with nickname " + opponentNick + " found. Please, try again.");
            return;
        }

        // Get the opponent's ID
        int opponentID = getOpponentID(opponentNick);
        Map<Position, PlaceableCard> oppPlayArea = getOpponentByID(opponentID).getPlayArea();

        String[][] playAreaArr = createPlayerArr(oppPlayArea);

        // Print the token color and score
        System.out.println("\n" + opponentNick + "'s token color: " + getOpponentByID(opponentID).getTokenColor() + "\n"
        + opponentNick + "'s score: " + getOpponentByID(opponentID).getScore() + "\n");

        if (playAreaArr == null) {
            System.out.println("No cards in " + opponentNick + "'s play area");
            return;
        }

        System.out.println(opponentNick + "'s play area:");

        // Print the play area
        for (String[] strings : playAreaArr) {
            StringBuilder line = new StringBuilder();

            for (String s : strings) {
                line.append(Objects.requireNonNullElse(s, " "));
            }
            System.out.println(line);
        }

        // Print opponent hand
        List<PlaceableCard> playerHand = getOpponentByID(opponentID).getPlayerHand();

        List<List<String>> handToPrint = new ArrayList<>();

        for (PlaceableCard card : playerHand) {
            if (!cardIDToCardBackTUILines.containsKey(card.getID())) {
                // Create TUI lines for the card
                createPlaceableCardLines(card.getID(), CardSideType.BACK);
            }

            handToPrint.add(cardIDToCardBackTUILines.get(card.getID()));
        }

        System.out.println(opponentNick + "'s hand:");
        for (int i = 0; i < handToPrint.getFirst().size(); i++) {
            StringBuilder line = new StringBuilder();

            for (List<String> card : handToPrint) {
                line.append(card.get(i)).append("   ");
            }

            System.out.println(line);
        }
        makeSpace();
    }

    /**
     * Updates the legal and illegal positions for the placement of a card.
     * @param pos The position of the card that was just placed.
     */
    private void updatePlacementPositions(Position pos) {
        ArrayList<Position> newLegalPos = new ArrayList<>();
        ArrayList<Position> newIllegalPos = new ArrayList<>();

        PlaceableCard card = playArea.get(pos);

        Side currSide = card.getCurrSide();

        if (currSide.getBLCorner().isAvailable()) {
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }
        else {
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }

        if (currSide.getBRCorner().isAvailable()) {
            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }
        else {
            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }

        if (currSide.getTLCorner().isAvailable()) {
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }
        else {
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }

        if (currSide.getTRCorner().isAvailable()) {
            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
        }
        else {
            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
        }

        for (Position p : newIllegalPos) {
            if (!illegalPosList.contains(p)) {
                illegalPosList.add(p);
            }

            legalPosList.remove(p);
        }

        for (Position p : newLegalPos) {
            if (!legalPosList.contains(p) && !illegalPosList.contains(p)) {
                legalPosList.add(p);
            }
        }

        // Set the new card's position as illegal (a card just got placed)
        legalPosList.remove(pos);
        illegalPosList.add(pos);

        // Update the posIDToValidPos map
        posIDToValidPos.clear();

        for (int i = 0; i < legalPosList.size(); i++) {
            posIDToValidPos.put(i, legalPosList.get(i));
        }
    }

    /**
     * Creates the TUI lines for a placeable card.
     * @param cardID The ID of the card to create the TUI lines for.
     * @param sideType The side of the card to create the TUI lines for.
     */
    private void createPlaceableCardLines(int cardID, CardSideType sideType){
        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);

        if (card instanceof ResourceCard){
            createResCardLines(cardID, sideType);
        }
        else if (card instanceof GoldenCard){
            createGoldCardLines(cardID, sideType);
        }
        else {
            createStarterCardLines(cardID, sideType);
        }
    }

    /**
     * Creates the TUI lines for a resource card.
     * @param cardID The ID of the card to create the TUI lines for.
     * @param sideType The side of the card to create the TUI lines for.
     */
    private void createResCardLines(int cardID, CardSideType sideType) {
        ResourceCard card = (ResourceCard) GameResources.getPlaceableCardByID(cardID);

        String A1, A2, A3, B1, B2, B3, C, D, E1, E2, E3, F1, F2, F3;

        Side cardSide;
        String cardColor = card.getElementType().getColor();

        if (sideType.equals(CardSideType.FRONT)){
            cardSide = card.getFront();
        }
        else {
            cardSide = card.getBack();
        }

        if (cardSide.getTLCorner().isAvailable()) {
            A1 = "┌─────┬";
            String r = cardSide.getTLCorner().getResource() != null ?
                    cardSide.getTLCorner().getResource().getColor() + cardSide.getTLCorner().getResource().toString() + CC.RESET : " ";
            A2 = cardColor + "│  " + CC.RESET + r + cardColor + "  │" + CC.RESET;
            A3 = cardColor + "├─────┘" + CC.RESET;
        }
        else{
            A1 = "┌──────";
            A2 = cardColor + "│      " + CC.RESET;
            A3 = cardColor + "│      " + CC.RESET;
        }

        if (cardSide.getTRCorner().isAvailable()) {
            B1 = "┬─────┐";
            String r = cardSide.getTRCorner().getResource() != null ?
                    cardSide.getTRCorner().getResource().getColor() + cardSide.getTRCorner().getResource().toString() + CC.RESET : " ";
            B2 = cardColor + "│  " + CC.RESET + r + cardColor + "  │" + CC.RESET;
            B3 = cardColor + "└─────┤" + CC.RESET;
        }
        else{
            B1 = "──────┐";
            B2 = cardColor + "      │" + CC.RESET;
            B3 = cardColor + "      │" + CC.RESET;
        }

        C = (sideType.equals(CardSideType.FRONT) && card.getPoints() != 0) ? String.valueOf(card.getPoints()) : " ";

        D = " ";
        if (sideType.equals(CardSideType.BACK)){
            switch (card.getElementType()){
                case FUNGI -> D = "F";
                case ANIMAL -> D = "A";
                case PLANT -> D = "P";
                case INSECT -> D = "I";
            }
        }

        if (cardSide.getBLCorner().isAvailable()) {
            E1 = cardColor + "├─────┐" + CC.RESET;
            String r = cardSide.getBLCorner().getResource() != null ?
                    cardSide.getBLCorner().getResource().getColor() + cardSide.getBLCorner().getResource().toString() + CC.RESET : " ";
            E2 = cardColor + "│  " + CC.RESET + r + cardColor + "  │" + CC.RESET;
            E3 = "└─────┴";
        }
        else {
            E1 = cardColor + "│      " + CC.RESET;
            E2 = cardColor + "│      " + CC.RESET;
            E3 = "└──────";
        }

        if (cardSide.getBRCorner().isAvailable()) {
            F1 = cardColor + "┌─────┤" + CC.RESET;
            String r = cardSide.getBRCorner().getResource() != null ?
                    cardSide.getBRCorner().getResource().getColor() + cardSide.getBRCorner().getResource().toString() + CC.RESET : " ";
            F2 = cardColor + "│  " + CC.RESET + r + cardColor + "  │" + CC.RESET;
            F3 = "┴─────┘";
        }
        else {
            F1 = cardColor + "      │" + CC.RESET;
            F2 = cardColor + "      │" + CC.RESET;
            F3 = "──────┘";
        }

        List<String> lines = new ArrayList<>();

        lines.add(cardColor + A1 + "───────────" + B1 + CC.RESET);
        lines.add(A2 + "     " + C + "     " + B2);
        lines.add(A3 + "           " + B3);
        lines.add(cardColor + "│           " + CC.RESET + D + cardColor + "           │" + CC.RESET);
        lines.add(E1 + "           " + F1);
        lines.add(E2 + "           " + F2);
        lines.add(cardColor + E3 + "───────────" + F3 + CC.RESET);

        if (sideType.equals(CardSideType.FRONT)){
            cardIDToCardFrontTUILines.put(cardID, lines);
        }
        else {
            cardIDToCardBackTUILines.put(cardID, lines);
        }
    }

    /**
     * Creates the TUI lines for a golden card.
     * @param cardID The ID of the card to create the TUI lines for.
     * @param sideType The side of the card to create the TUI lines for.
     */
    private void createGoldCardLines(int cardID, CardSideType sideType) {
        GoldenCard card = (GoldenCard) GameResources.getPlaceableCardByID(cardID);

        String A1, A2, A3, B1, B2, B3, C, D, E1, E2, E3, F1, F2, F3;

        StringBuilder G;

        Side cardSide;
        String cardColor = card.getElementType().getColor();

        if (sideType.equals(CardSideType.FRONT)){
            cardSide = card.getFront();
        }
        else {
            cardSide = card.getBack();
        }

        if (cardSide.getTLCorner().isAvailable()) {
            A1 = "╔═════╦";
            String r = cardSide.getTLCorner().getResource() != null ?
                    cardSide.getTLCorner().getResource().getColor() + cardSide.getTLCorner().getResource().toString() + CC.RESET : " ";
            A2 = cardColor + "║  " + r + cardColor + "  ║" + CC.RESET;
            A3 = "╠═════╝";
        }
        else{
            A1 = "╔══════";
            A2 = cardColor + "║      " + CC.RESET;
            A3 = "║      ";
        }

        if (cardSide.getTRCorner().isAvailable()) {
            B1 = "╦═════╗";
            String r = cardSide.getTRCorner().getResource() != null ?
                    cardSide.getTRCorner().getResource().getColor() + cardSide.getTRCorner().getResource().toString() + CC.RESET : " ";
            B2 = cardColor + "║  " + r + cardColor + "  ║" + CC.RESET;
            B3 = "╚═════╣";
        }
        else{
            B1 = "══════╗";
            B2 = cardColor + "      ║" + CC.RESET;
            B3 = "      ║";
        }

        if (sideType.equals(CardSideType.FRONT)) {
            if (card.getPoints() == 0) {
                if (card.isPointPerResource()) {
                    ResourceType res = card.getPointPerResourceRes();
                    C = "1 " + res.getColor() + res + CC.RESET;
                }
                else {
                    C = "2 C";
                }
            }
            else {
                C = " " + card.getPoints() + " ";
            }
        }
        else {
            C = "   ";
        }

        D = " ";
        if (sideType.equals(CardSideType.BACK)){
            switch (card.getElementType()){
                case FUNGI -> D = "F";
                case ANIMAL -> D = "A";
                case PLANT -> D = "P";
                case INSECT -> D = "I";
            }
        }

        if (cardSide.getBLCorner().isAvailable()) {
            E1 = "╠═════╗";
            String r = cardSide.getBLCorner().getResource() != null ?
                    cardSide.getBLCorner().getResource().getColor() + cardSide.getBLCorner().getResource().toString() + CC.RESET : " ";
            E2 = cardColor + "║  " + r + cardColor + "  ║" + CC.RESET;
            E3 = "╚═════╩";
        }
        else {
            E1 = "║      ";
            E2 = cardColor + "║      " + CC.RESET;
            E3 = "╚══════";
        }

        if (cardSide.getBRCorner().isAvailable()) {
            F1 = "╔═════╣";
            String r = cardSide.getBRCorner().getResource() != null ?
                    cardSide.getBRCorner().getResource().getColor() + cardSide.getBRCorner().getResource().toString() + CC.RESET : " ";
            F2 = cardColor + "║  " + r + cardColor + "  ║" + CC.RESET;
            F3 = "╩═════╝";
        }
        else {
            F1 = "      ║";
            F2 = cardColor + "      ║" + CC.RESET;
            F3 = "══════╝";
        }

        G = new StringBuilder();
        HashMap<ResourceType, Integer> requiredResources = card.getRequiredResources();

        int size = 0;
        for (ResourceType res : requiredResources.keySet()) {
            size += requiredResources.get(res);
        }

        if (sideType.equals(CardSideType.FRONT)) {
            if (size == 3) {
                G.append(" ");

                for (ResourceType res : requiredResources.keySet()) {
                    for (int i = 0; i < requiredResources.get(res); i++) {
                        G.append(res.getColor()).append(res).append(CC.RESET);
                    }
                }

                G.append(" ");
            }
            if (size == 4) {

                int checkedNum = 0;
                for (ResourceType res : requiredResources.keySet()) {
                    for (int i = 0; i < requiredResources.get(res); i++) {
                        if (checkedNum == 2) {
                            G.append(" ");
                        }
                        G.append(res.getColor()).append(res).append(CC.RESET);
                        checkedNum++;
                    }
                }
            }
            if (size == 5) {
                for (ResourceType res : requiredResources.keySet()) {
                    for (int i = 0; i < requiredResources.get(res); i++) {
                        G.append(res.getColor()).append(res).append(CC.RESET);
                    }
                }
            }
        }
        else {
            G = new StringBuilder("     ");
        }

        List<String> lines = new ArrayList<>();

        lines.add(cardColor + A1 + "═══════════" + B1 + CC.RESET);
        lines.add(A2 + "    " + C + "    " + B2);
        lines.add(cardColor + A3 + "           " + B3 + CC.RESET);
        lines.add(cardColor + "║           " + CC.RESET + D + cardColor + "           ║" + CC.RESET);
        lines.add(cardColor + E1 + "           " + F1 + CC.RESET);
        lines.add(E2 + "   " + G + "   " + F2);
        lines.add(cardColor + E3 + "═══════════" + F3 + CC.RESET);

        if (sideType.equals(CardSideType.FRONT)){
            cardIDToCardFrontTUILines.put(cardID, lines);
        }
        else {
            cardIDToCardBackTUILines.put(cardID, lines);
        }
    }

    /**
     * Creates the TUI lines for a starter card.
     * @param cardID The ID of the card to create the TUI lines for.
     * @param sideType The side of the card to create the TUI lines for.
     */
    private void createStarterCardLines(int cardID, CardSideType sideType) {
        StarterCard card = (StarterCard) GameResources.getPlaceableCardByID(cardID);

        String A1, A2, A3, B1, B2, B3, D1, D2, D3, E1, E2, E3, F1, F2, F3;

        Side cardSide;

        if (sideType.equals(CardSideType.FRONT)){
            cardSide = card.getFront();
        }
        else {
            cardSide = card.getBack();
        }

        List<ResourceType> cornerResources = new ArrayList<>();

        if (cardSide.getTLCorner().isAvailable()) {
            A1 = "┌─────┬";

            String r = " ";
            ResourceType currResource = cardSide.getTLCorner().getResource();
            if(currResource != null){
                r = currResource.getColor() + currResource + CC.RESET;
                cornerResources.add(currResource);
            }

            A2 = "│  " + r + "  │";
            A3 = "├─────┘";
        }
        else{
            A1 = "┌─────";
            A2 = "│     ";
            A3 = "│     ";
        }

        if (cardSide.getTRCorner().isAvailable()) {
            B1 = "┬─────┐";

            String r = " ";
            ResourceType currResource = cardSide.getTRCorner().getResource();
            if(currResource != null){
                r = currResource.getColor() + currResource + CC.RESET ;
                cornerResources.add(currResource);
            }

            B2 = "│  " + r + "  │";
            B3 = "└─────┤";
        }
        else{
            B1 = "──────┐";
            B2 = "      │";
            B3 = "      │";
        }

        if (cardSide.getBLCorner().isAvailable()) {
            E1 = "├─────┐";

            String r = " ";
            ResourceType currResource = cardSide.getBLCorner().getResource();
            if(currResource != null){
                r = currResource.getColor() + currResource + CC.RESET;
                cornerResources.add(currResource);
            }

            E2 = "│  " + r + "  │";
            E3 = "└─────┴";

        }
        else {
            E1 = "│      ";
            E2 = "│      ";
            E3 = "└──────";
        }

        if (cardSide.getBRCorner().isAvailable()) {
            F1 = "┌─────┤";

            String r = " ";
            ResourceType currResource = cardSide.getBRCorner().getResource();
            if(currResource != null){
                r = currResource.getColor() + currResource + CC.RESET;
                cornerResources.add(currResource);
            }

            F2 = "│  " + r + "  │";
            F3 = "┴─────┘";
        }
        else {
            F1 = "      │";
            F2 = "      │";
            F3 = "──────┘";
        }

        if (sideType.equals(CardSideType.BACK)) {
            List<ResourceType> permanentRes = cardSide.getResources();

            for (ResourceType cornerRes : cornerResources) {
                permanentRes.remove(cornerRes);
            }

            if (permanentRes.size() == 1) {
                D1 = " ";
                D2 = cardSide.getResources().getFirst().toString();
                D3 = " ";
            }
            else if (permanentRes.size() == 2) {
                D1 = cardSide.getResources().getFirst().toString();
                D2 = " ";
                D3 = cardSide.getResources().get(1).toString();
            }
            else { // size == 3
                D1 = cardSide.getResources().getFirst().toString();
                D2 = cardSide.getResources().get(1).toString();
                D3 = cardSide.getResources().get(2).toString();
            }
        }
        else {
            D1 = " ";
            D2 = " ";
            D3 = " ";
        }

        List<String> lines = new ArrayList<>();

        lines.add(A1 + "───────────" + B1);
        lines.add(A2 + "           " + B2);
        lines.add(A3 + "     " + D1 + "     " + B3);
        lines.add("│           " + D2 + "           │");
        lines.add(E1 + "     " + D3 +"     " + F1);
        lines.add(E2 + "           " + F2);
        lines.add(E3 + "───────────" + F3);

        if (sideType.equals(CardSideType.FRONT)){
            cardIDToCardFrontTUILines.put(cardID, lines);
        }
        else {
            cardIDToCardBackTUILines.put(cardID, lines);
        }
    }

    /**
     * Creates the TUI lines for an objective card.
     * @param cardID The ID of the card to create the TUI lines for.
     */
    private void createObjectiveCardLines(int cardID) {
        ObjectiveCard card = GameResources.getObjectiveCardByID(cardID);

        switch (card){
            case BLLShapeObjectiveCard castedCard -> createBLLShapeObjectiveCardLines(castedCard);

            case BRLShapeObjectiveCard castedCard -> createBRLShapeObjectiveCardLines(castedCard);

            case ResourcesCountObjectiveCard castedCard -> createResourcesCountObjectiveCardLines(castedCard);

            case TLBRDiagonalObjectiveCard castedCard -> createTLBRDiagonalObjectiveCardLines(castedCard);

            case TLLShapeObjectiveCard castedCard -> createTLLShapeObjectiveCardLines(castedCard);

            case TRBLDiagonalObjectiveCard castedCard -> createTRBLDiagonalObjectiveCardLines(castedCard);

            case TrinityObjectiveCard castedCard -> createTrinityObjectiveCardLines(castedCard);

            case TRLShapeObjectiveCard castedCard -> createTRLShapeObjectiveCardLines(castedCard);

            default -> throw new IllegalStateException("Unexpected value: " + card);
        }
    }

    /**
     * Creates the TUI lines for a BL L objective card.
     * @param card The BL L objective card to create the TUI lines for.
     */
    private void createBLLShapeObjectiveCardLines(BLLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┌───────────────────────┐");
        lines.add("│               "+ card.getPoints() + "       │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│           " + mainColor + "   " + CC.RESET + "         │");
        lines.add("│                       │");
        lines.add("└───────────────────────┘");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the TUI lines for a BR L objective card.
     * @param card The BR L objective card to create the TUI lines for.
     */
    private void createBRLShapeObjectiveCardLines(BRLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┌───────────────────────┐");
        lines.add("│               " + card.getPoints() + "       │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│                 " + mainColor + "   " + CC.RESET + "   │");
        lines.add("│                       │");
        lines.add("└───────────────────────┘");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the TUI lines for a resources count objective card.
     * @param card The resources count objective card to create the TUI lines for.
     */
    private void createResourcesCountObjectiveCardLines(ResourcesCountObjectiveCard card) {
        List<String> lines = new ArrayList<>();

        if (card.getID() >= 94 && card.getID() <= 97) {
            String r = card.getResourceType().getColor() + card.getResourceType() + CC.RESET;

            lines.add("┌───────────────────────┐");
            lines.add("│               " + card.getPoints() + "       │");
            lines.add("│                       │");
            lines.add("│               " + r + "       │");
            lines.add("│            " + r + "     " + r + "    │");
            lines.add("│                       │");
            lines.add("└───────────────────────┘");
        }
        else {
            String resType = card.getResourceType().toString();

            lines.add("┌───────────────────────┐");
            lines.add("│               "+ card.getPoints()+ "       │");
            lines.add("│                       │");
            lines.add("│              " + CC.YELLOW + resType + "  "  + resType + CC.RESET + "     │");
            lines.add("│                       │");
            lines.add("│                       │");
            lines.add("└───────────────────────┘");
        }

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the TUI lines for a TLBR diagonal objective card.
     * @param card The TLBR diagonal objective card to create the TUI lines for.
     */
    private void createTLBRDiagonalObjectiveCardLines(TLBRDiagonalObjectiveCard card) {
        String color = card.getElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┌───────────────────────┐");
        lines.add("│               " + card.getPoints() + "       │");
        lines.add("│           " + color + "   " + CC.RESET + "         │");
        lines.add("│              " + color + "   " + CC.RESET + "      │");
        lines.add("│                 " + color + "   " + CC.RESET + "   │");
        lines.add("│                       │");
        lines.add("└───────────────────────┘");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the TUI lines for a TL L shape objective card.
     * @param card The TL L shape objective card to create the TUI lines for.
     */
    private void createTLLShapeObjectiveCardLines(TLLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┌───────────────────────┐");
        lines.add("│               " + card.getPoints() + "       │");
        lines.add("│           " + mainColor + "   " + CC.RESET + "         │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│                       │");
        lines.add("└───────────────────────┘");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the TUI lines for a TRBL diagonal objective card.
     * @param card The TRBL diagonal objective card to create the TUI lines for.
     */
    private void createTRBLDiagonalObjectiveCardLines(TRBLDiagonalObjectiveCard card) {
        String color = card.getElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┌───────────────────────┐");
        lines.add("│               " + card.getPoints() + "       │");
        lines.add("│                 " + color + "   " + CC.RESET + "   │");
        lines.add("│              " + color + "   " + CC.RESET + "      │");
        lines.add("│           " + color + "   " + CC.RESET + "         │");
        lines.add("│                       │");
        lines.add("└───────────────────────┘");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the TUI lines for a trinity objective card.
     * @param card The trinity objective card to create the TUI lines for.
     */
    private void createTrinityObjectiveCardLines(TrinityObjectiveCard card) {
        List<String> lines = new ArrayList<>();

        lines.add("┌───────────────────────┐");
        lines.add("│               "+ card.getPoints()+ "       │");
        lines.add("│                       │");
        lines.add("│            " + CC.YELLOW + "Q  K  M" + CC.RESET + "    │");
        lines.add("│                       │");
        lines.add("│                       │");
        lines.add("└───────────────────────┘");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the TUI lines for a TR L shape objective card.
     * @param card The TR L shape objective card to create the TUI lines for.
     */
    private void createTRLShapeObjectiveCardLines(TRLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┌───────────────────────┐");
        lines.add("│               "+ card.getPoints()+ "       │");
        lines.add("│                 " + mainColor + "   " + CC.RESET + "   │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│              " + secColor + "   " + CC.RESET + "      │");
        lines.add("│                       │");
        lines.add("└───────────────────────┘");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    /**
     * Creates the map of allowed commands for each player state.
     */
    private void createStateToCommandsMap() {
        stateToCommands.put(PlayerState.MAIN_MENU,
                new ArrayList<>(Arrays.asList("cm", "sm", "rml", "reconnect")));

        stateToCommands.put(PlayerState.MATCH_LOBBY,
                new ArrayList<>(List.of("gmi")));

        stateToCommands.put(PlayerState.CHOOSING_NICKNAME,
                new ArrayList<>(List.of("cn", "gmi")));

        stateToCommands.put(PlayerState.PLACING_STARTER,
                new ArrayList<>(Arrays.asList("fsc", "psc", "gmi")));

        stateToCommands.put(PlayerState.CHOOSING_OBJECTIVE,
                new ArrayList<>(List.of("cso", "gmi")));

        stateToCommands.put(PlayerState.COMPLETED_SETUP,
                new ArrayList<>(List.of("chat", "gmc", "gs", "opa", "gmi", "pa")));

        stateToCommands.put(PlayerState.WAITING,
                new ArrayList<>(List.of("fc", "gh", "chat", "gmc", "gs", "opa", "gmi", "pa")));

        stateToCommands.put(PlayerState.PLACING,
                new ArrayList<>(Arrays.asList("fc", "gh", "pc", "chat", "gmc", "gs", "opa", "gmi", "pa")));

        stateToCommands.put(PlayerState.DRAWING,
                new ArrayList<>(Arrays.asList("fc", "gh", "ddr", "dvr", "ddg", "dvg", "chat", "gmc", "gs", "opa", "gmi", "pa")));

        stateToCommands.put(PlayerState.GAME_OVER,
                new ArrayList<>(Arrays.asList("opa", "pa", "mm")));
    }

    @Override
    public void receiveChatMessage(String senderNickname, String recipientNickname, String message) {
        makeSpace();
        String fullMessage;
        String actualSender = senderNickname;

        if (actualSender.equals(playerNickname)){
            actualSender = "(You)";
        }

        if (recipientNickname.equals("all")){
            fullMessage = actualSender + ": " + message;
        }
        else {
            fullMessage = actualSender + " -> " + recipientNickname + ": " + message;
        }

        System.out.println(fullMessage);

        synchronized (chat) {
            chat.add(fullMessage);
        }
        makeSpace();
    }

    @Override
    public void showRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfObjectives) {
        if (playerID == lastPlayerID){
            playerState = PlayerState.WAITING;

            this.score += deltaScore;

            PlaceableCard placingCard = GameResources.getPlaceableCardByID(cardID);
            placingCard.setPriority(priority);
            priority++;

            // Add card to play area
            playArea.put(pos, placingCard);

            // Update legal and illegal positions
            updatePlacementPositions(pos);

            // Remove card from player hand
            for (PlaceableCard card : playerHand) {
                if (card.getID() == placingCard.getID()) {
                    playerHand.remove(card);
                    break;
                }
            }

            // Print play area
            printPlayArea();

            // Print player score
            System.out.println("You scored " + deltaScore + " points");
        }
        else {
            // Place card for opponent
            PlayerViewModel opponent = getOpponentByID(lastPlayerID);
            opponent.placeCardInPlayArea(cardID, side, pos, deltaScore);

            System.out.println("Opponent " + opponent.getNickname() + " scored " + deltaScore + " points");
        }

        System.out.println("The final ranking is:");

        for (int i = 0; i < nicknames.length; i++) {
            System.out.println((i + 1) + ". " + nicknames[i] + " with " + scores[i] + " points and " + numOfObjectives[i] + " objectives completed.");
        }
        makeSpace();
    }

    @Override
    public void showNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID, GameState gameState) {
        makeSpace();
        if (gameState == GameState.FINAL_ROUND) {
            System.out.println("The game entered the final round state.");
        }
        else {
            System.out.println("The game entered the extra round state.");
        }

        showNewPlayerTurn(drawnCardID, lastPlayerID, newPlayerID);
    }

    @Override
    public void showNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int deltaScore) {
        makeSpace();
        if (playerID == lastPlayerID){
            playerState = PlayerState.WAITING;

            this.score += deltaScore;

            PlaceableCard placingCard = GameResources.getPlaceableCardByID(cardID);
            placingCard.setPriority(priority);
            priority++;

            // Add card to play area
            playArea.put(pos, placingCard);

            // Update legal and illegal positions
            updatePlacementPositions(pos);

            // Remove card from player hand
            for (PlaceableCard card : playerHand) {
                if (card.getID() == placingCard.getID()) {
                    playerHand.remove(card);
                    break;
                }
            }

            // Print play area
            printPlayArea();

            // Print player score
            System.out.println("You scored " + deltaScore + " points");
            System.out.println("It's " + getPlayerNickname(newPlayerID) + "'s turn.\n" +
                    "While waiting you can flip a card in your hand by typing: fc <cardID>\n" +
                    "To check your hand, type: gh\n" +
                    "To check actual score, type: gs\n" +
                    "To check opponents play area: opa <opponentNickname>");
        }
        else {
            // Place card for opponent
            PlayerViewModel opponent = getOpponentByID(lastPlayerID);
            opponent.placeCardInPlayArea(cardID, side, pos, deltaScore);

            System.out.println("Opponent " + opponent.getNickname() + " scored " + deltaScore + " points");

            if (playerID == newPlayerID) {
                playerState = PlayerState.PLACING;

                System.out.println("It's your turn.");

                // Print player hand
                printPlayerHand();

                // Print play area
                printPlayArea();

                // Print available commands
                System.out.println("""
                        To check your hand, type: gh
                        To flip a card, type: fc <cardID>
                        To place a card, type: pc <cardID> <posID>""");
            }
            else {
                playerState = PlayerState.WAITING;
                String newPlayerNickname = getPlayerNickname(newPlayerID);

                // Print available commands
                System.out.println("It's " + newPlayerNickname + "'s turn.\n" +
                        "While waiting you can flip a card in your hand by typing: fc <cardID>\n" +
                        "To check your hand, type: gh\n" +
                        "To check actual score, type: gs\n" +
                        "To check opponents play area: opa <opponentNickname>");
            }
        }
    }

    @Override
    public void showPlayerDisconnection(int playerID) {
        makeSpace();
        System.out.println(getPlayerNickname(playerID) + " has disconnected.");
        makeSpace();
    }

    @Override
    public void showPlayerDisconnectionWithOnePlayerLeft(int playerID) {
        makeSpace();
        playerState = PlayerState.ALONE;

        System.out.println(getPlayerNickname(playerID) + " has disconnected.");
        System.out.println("""
                    You are the only player left in the game.
                    
                    While waiting for the others to reconnect you can flip a card in your hand by typing: fc <cardID>
                    To check your hand, type: gh
                    To check actual score, type: gs
                    To check opponents play area: opa <opponentNickname>""");
    }

    @Override
    public void reconnectOnStarterPlacement(int playerID, Map<Integer, String> IDToOpponentNickname, int[] gameSetupCards) {
        makeSpace();
        startConnectionCheck();

        setID(playerID);
        setTokenColor(tokenColor);
        playerState = PlayerState.PLACING_STARTER;
        createInvalidCardLines();

        System.out.println("You have successfully reconnected to the match.");

        for (Map.Entry<Integer, String> entry : IDToOpponentNickname.entrySet()) {
            if (entry.getKey() != playerID){
                PlayerViewModel opponent = new PlayerViewModel();
                opponent.setPlayerID(entry.getKey());
                opponent.setNickname(entry.getValue());
                opponents.add(opponent);
            }
            else {
                playerNickname = entry.getValue();
            }
        }

        GameResources.initializeAllDecks();

        // Update draw area cards
        setDrawArea(gameSetupCards[0], gameSetupCards[1], gameSetupCards[2],
                gameSetupCards[3], gameSetupCards[4], gameSetupCards[5]);

        // Print 4 visible cards, 2 deck top card and starter card
        printDrawArea();

        // Create and print starter card
        printStarterCardChoice(gameSetupCards[6]);

        // Print available commands
        System.out.println("To view the other side of the starter card, type: fsc \n" +
                "To place the starter card on the current side, type: psc");
    }

    @Override
    public void reconnectOnObjectiveChoice(int playerID, Map<Integer, String> IDToOpponentNickname, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {
        makeSpace();
        startConnectionCheck();

        setID(playerID);
        setTokenColor(tokenColor);
        playerState = PlayerState.CHOOSING_OBJECTIVE;
        createInvalidCardLines();

        System.out.println("You have successfully reconnected to the match.");

        for (Map.Entry<Integer, String> entry : IDToOpponentNickname.entrySet()) {
            if (entry.getKey() != playerID){
                PlayerViewModel opponent = new PlayerViewModel();
                opponent.setPlayerID(entry.getKey());
                opponent.setNickname(entry.getValue());
                opponents.add(opponent);
            }
            else {
                playerNickname = entry.getValue();
            }
        }

        GameResources.initializeAllDecks();

        // Update draw area cards
        setDrawArea(gameSetupCards[0], gameSetupCards[1], gameSetupCards[2],
                gameSetupCards[3], gameSetupCards[4], gameSetupCards[5]);

        // Print 4 visible cards, 2 deck top card
        printDrawArea();

        starterCard = GameResources.getPlaceableCardByID(gameSetupCards[13]);
        starterCard.setCurrSideType(starterSide);

        // Place the starter card in the play area and update all legal and illegal positions
        starterCard.setPriority(priority);
        playArea.put(new Position(0, 0), starterCard);
        priority++;

        updatePlacementPositions(new Position(0, 0));

        // Add cards to player hand ====================================================================================
        createPlayerHand(gameSetupCards[6], gameSetupCards[7], gameSetupCards[8]);

        // Print player hand
        printPlayerHand();

        // Create common objectives ====================================================================================
        setCommonObjectives(gameSetupCards[9], gameSetupCards[10]);

        // Print common objectives
        printCommonObjectives();

        // Set secret objective ========================================================================================
        setSecretObjectives(gameSetupCards[11], gameSetupCards[12]);

        // Print secret objective choice
        printSecretObjectiveChoice();
    }

    @Override
    public void reconnectAfterSetup(int playerID, Map<Integer, String> idToNicknameMap, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {
        makeSpace();
        startConnectionCheck();

        setID(playerID);
        setTokenColor(tokenColor);
        playerState = PlayerState.COMPLETED_SETUP;
        createInvalidCardLines();

        System.out.println("You have successfully reconnected to the match.");

        for (Map.Entry<Integer, String> entry : idToNicknameMap.entrySet()) {
            if (entry.getKey() != playerID){
                PlayerViewModel opponent = new PlayerViewModel();
                opponent.setPlayerID(entry.getKey());
                opponent.setNickname(entry.getValue());
                opponents.add(opponent);
            }
            else {
                playerNickname = entry.getValue();
            }
        }

        GameResources.initializeAllDecks();

        // Update draw area cards
        setDrawArea(gameSetupCards[0], gameSetupCards[1], gameSetupCards[2],
                gameSetupCards[3], gameSetupCards[4], gameSetupCards[5]);

        // Print 4 visible cards, 2 deck top card
        printDrawArea();

        starterCard = GameResources.getPlaceableCardByID(gameSetupCards[12]);
        starterCard.setCurrSideType(starterSide);

        // Place the starter card in the play area and update all legal and illegal positions
        starterCard.setPriority(priority);
        playArea.put(new Position(0, 0), starterCard);
        priority++;

        updatePlacementPositions(new Position(0, 0));

        // Add cards to player hand ====================================================================================
        createPlayerHand(gameSetupCards[6], gameSetupCards[7], gameSetupCards[8]);

        // Print player hand
        printPlayerHand();

        // Create common objectives ====================================================================================
        setCommonObjectives(gameSetupCards[9], gameSetupCards[10]);

        // Print common objectives
        printCommonObjectives();

        // Set secret objective ========================================================================================
        secretObjectiveCardID = gameSetupCards[11];
        createObjectiveCardLines(secretObjectiveCardID);

        // Print secret objective
        printSecretObjective();

        // Print available commands
        System.out.println("""
                To chat with other players, type: chat <message>
                To get the match info, type: gmi
                To see the actual score, type: gs
                To see the opponents play area, type: opa <opponentNickname>""");
    }

    @Override
    public void reconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors, int[][] playersHands,
                          List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs, List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                          int[] playersScores, int[] gameSetupCards, int currPlayerID) {
        makeSpace();
        GameResources.initializeAllDecks();

        // Set the player ID and state
        setID(playerID);
        playerState = PlayerState.WAITING;
        int numOfPlayers = playersIDs.length;

        // For all players in the match
        for (int i = 0; i < numOfPlayers; i++) {
            // If the ID is the same as the player's ID
            if (playersIDs[i] == playerID) {
                // Set the player's nickname
                playerNickname = playersNicknames[i];
                // Set the player's token color
                setTokenColor(playersTokenColors[i]);

                // Set the player's hand
                for (int cardID : playersHands[i]) {
                    PlaceableCard card = GameResources.getPlaceableCardByID(cardID);
                    createPlaceableCardLines(cardID, CardSideType.FRONT);
                    createPlaceableCardLines(cardID, CardSideType.BACK);
                    this.playerHand.add(card);
                }

                priority = 0;
                // Set the player's play area
                for (int j = 0; j < playersPlacedCardsIDs.get(i).length; j++) {
                    PlaceableCard card = GameResources.getPlaceableCardByID(playersPlacedCardsIDs.get(i)[j]);
                    createPlaceableCardLines(playersPlacedCardsIDs.get(i)[j], CardSideType.FRONT);
                    createPlaceableCardLines(playersPlacedCardsIDs.get(i)[j], CardSideType.BACK);
                    card.setCurrSideType(playersPlacedCardsSides.get(i)[j]);
                    card.setPriority(playersPlacedCardsPriorities.get(i)[j]);
                    playArea.put(playersPlacedCardsPos.get(i)[j], card);
                    priority++;
                    updatePlacementPositions(playersPlacedCardsPos.get(i)[j]);
                }
                // Set the player's score
                score = playersScores[i];

                // Set the secret objective, the common objectives and the draw area
                secretObjectiveCardID = gameSetupCards[0];
                createObjectiveCardLines(secretObjectiveCardID);
                createObjectiveCardLines(secretObjectiveCardID);
                setCommonObjectives(gameSetupCards[1], gameSetupCards[2]);
                setDrawArea(gameSetupCards[3], gameSetupCards[4], gameSetupCards[5], gameSetupCards[6], gameSetupCards[7], gameSetupCards[8]);
            }
            else {
                PlayerViewModel opponent = new PlayerViewModel();
                // Set the opponent's ID, nickname and token color
                opponent.setPlayerID(playersIDs[i]);
                opponent.setNickname(playersNicknames[i]);
                opponent.setTokenColor(playersTokenColors[i]);

                // Set the opponent's hand
                for (int cardID : playersHands[i]) {
                    PlaceableCard card = GameResources.getPlaceableCardByID(cardID);
                    createPlaceableCardLines(cardID, CardSideType.FRONT);
                    createPlaceableCardLines(cardID, CardSideType.BACK);
                    opponent.getPlayerHand().add(card);
                }

                // Set the opponent's play area
                for (int j = 0; j < playersPlacedCardsIDs.get(i).length; j++) {
                    PlaceableCard card = GameResources.getPlaceableCardByID(playersPlacedCardsIDs.get(i)[j]);
                    card.setCurrSideType(playersPlacedCardsSides.get(i)[j]);
                    card.setPriority(playersPlacedCardsPriorities.get(i)[j]);
                    opponent.addCardToPlayArea(card, playersPlacedCardsPos.get(i)[j]);
                }
                // Set the opponent's score
                opponent.setScore(playersScores[i]);

                // Add the opponent to the list of opponents
                opponents.add(opponent);
            }
        }

        System.out.println("You have successfully reconnected to the match.");

        // Print the secret objective, common objectives, draw area and the player's hand
        printSecretObjective();
        System.out.println();
        printCommonObjectives();
        printDrawArea();
        printPlayerHand();
        System.out.println("It's " + getPlayerNickname(currPlayerID) + "'s turn.");
    }

    @Override
    public void showPlayerReconnection(int playerID) {
        makeSpace();
        if (playerState == PlayerState.ALONE) {
            playerState = PlayerState.PLACING;
            System.out.println(getPlayerNickname(playerID) + " has reconnected.");
            System.out.println("It's your turn.");

            // Print player hand
            printPlayerHand();

            // Print play area
            printPlayArea();

            // Print available commands
            System.out.println("""
                        To check your hand, type: gh
                        To flip a card, type: fc <cardID>
                        To place a card, type: pc <cardID> <posID>""");
        }
        else {
            System.out.println(getPlayerNickname(playerID) + " has reconnected.");
        }
    }

    @Override
    public void undoCardPlacement(int playerID, Position pos, int score, int nextPlayerID) {
        makeSpace();
        System.out.println(getPlayerNickname(playerID) + " has disconnected.");

        if (this.playerID == nextPlayerID) {
            playerState = PlayerState.PLACING;
            System.out.println("It's your turn.");
        }
        else if (nextPlayerID == -1) {
            playerState = PlayerState.ALONE;
            System.out.println("""
                    You are the only player left in the game.
                    
                    While waiting for the others to reconnect you can flip a card in your hand by typing: fc <cardID>
                    To check your hand, type: gh
                    To check actual score, type: gs
                    To check opponents play area: opa <opponentNickname>""");
        }
        else {
            System.out.println("It's " + getPlayerNickname(nextPlayerID) + "'s turn.");
            System.out.println("""
                    While waiting you can flip a card in your hand by typing: fc <cardID>
                    To check your hand, type: gh
                    To check actual score, type: gs
                    To check opponents play area: opa <opponentNickname>""");
        }

        getOpponentByID(playerID).removeCard(pos, score);
    }

    @Override
    public void showSoleWinnerMessage() {
        makeSpace();
        packetLossTask.cancel();
        playerState = PlayerState.GAME_OVER;

        System.out.println("""
                Congratulations! You are the winner of the match.
                You can view your opponents' play areas by typing: opa <opponentNickname>
                To return to the main menu, type: mm""");
    }

    @Override
    public void receivePing() {
        synchronized (packetLossLock) {
            packetLoss = 0;
        }
        sender.sendPong(playerID);
    }

    /**
     * Prints the chat.
     */
    private void printChat() {
        List<String> chatCopy;

        synchronized (chat) {
            chatCopy = new ArrayList<>(chat);
        }

        if (!chatCopy.isEmpty()) {
            System.out.println("Chat:");
            for (String message : chatCopy) {
                System.out.println(message);
            }
        }
        else {
            System.out.println("No messages in chat.");
        }
        makeSpace();
    }

    /**
     * Prints the scores of all players.
     */
    private void printAllScores() {
        System.out.println("Your score: " + score);

        for (PlayerViewModel p : opponents){
            System.out.println(p.getNickname() + "'s score: " + p.getScore());
        }
        makeSpace();
    }

    /**
     * Gets the nickname of the player with the given ID.
     * @param playerID The ID of the player.
     * @return The nickname of the player.
     */
    private String getPlayerNickname(int playerID){
        for (PlayerViewModel p : opponents){
            if (p.getPlayerID() == playerID){
                return p.getNickname();
            }
        }

        return playerNickname;
    }

    /**
     * Gets the ID of the opponent with the given nickname.
     * @param nickname The nickname of the opponent.
     * @return The ID of the opponent.
     */
    private int getOpponentID(String nickname){
        for (PlayerViewModel p : opponents){
            if (p.getNickname().equals(nickname)){
                return p.getPlayerID();
            }
        }

        return -1;
    }

    /**
     * Gets the PlayerViewModel with the given ID.
     * @param playerID The ID of the opponent.
     * @return The PlayerViewModel of the opponent.
     */
    private PlayerViewModel getOpponentByID(int playerID){
        return opponents.stream()
                .filter(opponent -> opponent.getPlayerID() == playerID)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a nickname exists in the list of opponents.
     * @param nickname The nickname to check.
     * @return True if the nickname exists, false otherwise.
     */
    private boolean nicknameExists(String nickname){
        for (PlayerViewModel p : opponents){
            if (p.getNickname().equals(nickname) && p.getPlayerID() != playerID){
                return true;
            }
        }

        return false;
    }

    /**
     * Starts to check if the player has lost connection to the server.
     */
    private void startConnectionCheck() {
        packetLossTask = new TimerTask() {
            @Override
            public void run() {
                synchronized (packetLossLock) {
                    if (packetLoss >= MAX_PACKET_LOSS) {
                        playerState = PlayerState.DISCONNECTED;
                        showConnectionLoss();
                        cancel();
                    }
                    packetLoss++;
                }
            }
        };
        timer.scheduleAtFixedRate(packetLossTask, 0, COUNT_DEC_PERIOD);
    }

    /**
     * Notifies the player that he has lost connection to the server.
     */
    private void showConnectionLoss() {
        makeSpace();
        System.out.println("Connection lost. Please restart the game to reconnect.");
    }

    /**
     * Resets the attributes of the player.
     */
    private void resetPlayerModel() {
        playerHand.clear();
        playArea.clear();
        opponents.clear();
        chat.clear();
        score = 0;
        priority = 0;
    }

    /**
     * Sets the Token Color of the player.
     * @param tokenColor The Token Color to set.
     */
    private void setTokenColor(TokenColors tokenColor) {
        this.tokenColor = tokenColor;
    }

    /**
     * Prints a "divider" to make the output more readable.
     */
    private void makeSpace(){
        System.out.println("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }
}
