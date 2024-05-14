package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.objectiveCards.*;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TUIView implements View {

    private ClientSender sender;
    private BufferedReader stdIn;

    private int playerID;

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

    private CardSideType starterSide;
    private int starterCardID;

    private int secretObjectiveCardID1;
    private int secretObjectiveCardID2;

    private int secretObjectiveCardID;

    private PlayerState playerState;

    private final List<Position> legalPosList = new ArrayList<>();
    private final List<Position> illegalPosList = new ArrayList<>();
    private final Map<Position, Integer> playArea = new HashMap<>();

    private final Map<Integer, Position> posIDToValidPos = new HashMap<>();

    private final Map<Integer, CardSideType> playerHand = new HashMap<>();

    private final Map<PlayerState, List<String>> stateToCommands = new HashMap<>();

    private int placingCardID;
    private Position placingCardPos;

    private Map<Integer, String> IDtoOpponentNickname = new HashMap<>();

    private Map<Integer, Map<Position, Integer>> IDtoOpponentPlayerArea = new HashMap<>();

    private final List<MessageModel> matchChatList = new ArrayList<>();

    private final Map<Integer, List<MessageModel>> opponentIDtoPrivateChatList = new HashMap<>();

    public void start(String[] args) {
        createStateToCommandsMap();
        playerState = PlayerState.MAIN_MENU;
        System.out.println("Welcome.\nThis is the application to play \"Codex Naturalis\".");

        stdIn = new BufferedReader(new InputStreamReader(System.in));
        boolean connected = false;
        int portNumber = 0;

        while (!connected){
            System.out.println("Choose connection type:\n\t-'1' for Socket;\n\t-'2' for RMI;");

            try {
                String connectionType = stdIn.readLine();
                while (!connectionType.equals("1") && !connectionType.equals("2")) {
                    System.out.println("Please insert a valid choice (either 1 or 2).");
                    connectionType = stdIn.readLine();
                }
                System.out.println("Enter the port number:");
                boolean hasPort = false;

                while (!hasPort) {
                    try {
                        portNumber = Integer.parseInt(stdIn.readLine());
                        hasPort = true;
                    }
                    catch (Exception e) {
                        System.out.println("Please insert a valid number.");
                    }
                }

                if (connectionType.equals("1")) {
                    sender = ClientMain.startSocket(args[0], portNumber, this);
                }
                else {
                    sender = ClientMain.startRMI(args[0], portNumber, this);
                }
                connected = true;
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sender.getMatches();

        startInputReading();
    }

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

    private void interpretInput(String userInput) {
        String[] parts = userInput.split(" ");
        String command = parts[0].toLowerCase();

        List<String> legalCommands = stateToCommands.get(playerState);

            if (legalCommands.contains(command)) {
                switch (command) {
                    // TODO: add refresh command

                    case "cm" -> { // CreateMatch
                        if (parts.length != 3) {
                            System.out.println("Error: CreateMatch takes exactly 2 arguments (num of players, nickname). Please, try again");
                            break;
                        }
                        try {
                            sender.createMatch(Integer.parseInt(parts[1]), parts[2]);
                        } catch (NumberFormatException e){
                            System.out.println("Error: " + parts[1] + " is not a valid number. Please, try again");
                        }
                    }
                    case "sm" -> { // SelectMatch
                        if (parts.length != 2) {
                            System.out.println("Error: SelectMatch takes exactly 1 argument (match ID). Please, try again");
                            break;
                        }
                        try {
                            sender.selectMatch(Integer.parseInt(parts[1]));
                        } catch (NumberFormatException e){
                            System.out.println("Error: " + parts[1] + " is not a valid number. Please, try again");
                        }
                    }
                    case "cn" -> { // ChooseNickname
                        if (parts.length != 2) {
                            System.out.println("Error: ChooseNickname takes exactly 1 argument (nickname). Please, try again");
                            break;
                        }

                        sender.chooseNickname(parts[1]);
                    }
                    case "fsc" -> { // FlipStarterCard
                        if (parts.length != 1) {
                            System.out.println("Error: FlipStarterCard does not take any arguments. Please, try again");
                            break;
                        }

                        flipStarterCard();
                    }
                    case "psc" -> { // PlaceStarterCard
                        if (parts.length != 1) {
                            System.out.println("Error: PlaceStarterCard does not take any arguments. Please, try again");
                            break;
                        }

                        sender.placeStarterCard(starterSide);
                    }
                    case "cso" -> { // ChooseSecretObjective
                        if (parts.length != 2) {
                            System.out.println("Error: ChooseSecretObjective takes exactly 1 argument (card ID). Please, try again");
                            break;
                        }

                        try {
                            int choice = Integer.parseInt(parts[1]);

                            secretObjectiveCardID = choice == 1 ? secretObjectiveCardID1 : secretObjectiveCardID2;

                            sender.chooseSecretObjective(secretObjectiveCardID);
                        }
                        catch (Exception e) {
                            System.out.println("Error: " + parts[1] + " is not a valid choice. Please choose either 1 or 2.");
                        }
                    }
                    case "fc" -> { // FlipCard
                        if (parts.length != 2) {
                            System.out.println("Error: FlipCard takes exactly 1 argument (card ID). Please, try again");
                            break;
                        }

                        try {
                            playerHand.compute(Integer.parseInt(parts[1]), (_, side) -> side == CardSideType.FRONT ? CardSideType.BACK : CardSideType.FRONT);
                            printPlayerHand();
                        } catch (NumberFormatException e){
                            System.out.println("Error: " + parts[1] + " is not a valid number. Please, try again");
                        }
                    }
                    case "pc" -> { // PlaceCard
                        if (parts.length != 3) {
                            System.out.println("Error: PlaceCard takes exactly 2 arguments (card ID and position ID). Please, try again");
                            break;
                        }

                        try {
                            placingCardID = Integer.parseInt(parts[1]);
                            placingCardPos = posIDToValidPos.get(Integer.parseInt(parts[2]));

                            sender.placeCard(placingCardID, playerHand.get(placingCardID), placingCardPos);
                        } catch (NumberFormatException e){
                            System.out.println("Error: " + parts[1] + " or " + parts[2] + " is not a valid number. Please, try again");
                        }

                    }
                    case "ddr" -> { // DrawDeckResourceCard
                        if (parts.length != 1) {
                            System.out.println("Error: DrawDeckResourceCard does not take any arguments. Please, try again");
                            break;
                        }

                        sender.drawResourceCard(playerID);
                    }
                    case "dvr" -> { // DrawVisibleResourceCard
                        if (parts.length != 2) {
                            System.out.println("Error: DrawResourceCard takes exactly 1 argument (card index 0 or 1). Please, try again");
                            break;
                        }

                        try {
                            sender.drawVisibleResourceCard(playerID, Integer.parseInt(parts[1]));
                        } catch (NumberFormatException e){
                            System.out.println("Error: " + parts[1] + " is not a valid number. Please, try again");
                        }
                    }
                    case "ddg" -> { // DrawDeckGoldCard
                        if (parts.length != 1) {
                            System.out.println("Error: DrawDeckGoldenCard does not take any arguments. Please, try again");
                            break;
                        }

                        sender.drawGoldenCard(playerID);
                    }
                    case "dvg" -> { // DrawVisibleGoldCard
                        if (parts.length != 2) {
                            System.out.println("Error: DrawVisibleGoldenCard takes exactly 1 argument (card index 0 or 1). Please, try again");
                            break;
                        }
                        try {
                            sender.drawVisibleGoldenCard(playerID, Integer.parseInt(parts[1]));
                        } catch (NumberFormatException e){
                            System.out.println("Error: " + parts[1] + " is not a valid number. Please, try again");
                        }
                    }
                    case "gh" -> { // GetHand
                        if (parts.length != 1) {
                            System.out.println("Error: GetHand does not take any arguments. Please, try again");
                            break;
                        }

                        printPlayerHand();
                    }
                    case "opa" -> { // PrintOpponentPlayArea
                        if (parts.length != 2) {
                            System.out.println("Error: OpponentPlayArea takes exactly 1 argument (opponent nickname). Please, try again");
                            break;
                        }

                    boolean opponentFound = false;
                    // Search opponent ID
                    for (Integer opponentID : IDtoOpponentNickname.keySet()){
                        if (IDtoOpponentNickname.get(opponentID).equals(parts[1])){
                            printOpponentPlayArea(opponentID);
                        }
                    }
                    if (!opponentFound) {
                        System.out.println("Error: invalid nickname. Please, try again");
                    }
                }
                case "wmm" -> { // Write a message in the match chat
                    if (parts.length <= 1) {
                        System.out.println("Error: OpponentPlayArea takes at least 1 argument. Please, try again");
                        break;
                    }

                    ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts));

                    //Remove "wmm" from the message
                    partsArray.remove("wmm");

                    //convert che String[] in String
                    String message = String.join(" ", partsArray);

                    sender.sendMatchMessage(message);
                }
                case "wpm" -> { // Write a message in the private chat
                    if (parts.length <= 2) {
                        System.out.println("Error: OpponentPlayArea takes at least 2 argument (recipient nickname and message). Please, try again");
                        break;
                    }

                    boolean opponentFound = false;
                    // Search recipient ID
                    for (Integer recipientID : IDtoOpponentNickname.keySet()){
                        if (IDtoOpponentNickname.get(recipientID).equals(parts[1])){
                            ArrayList<String> partsArray = new ArrayList<>(Arrays.asList(parts));

                            //Remove "wpm" from the message
                            partsArray.remove("wpm");

                            //Remove recipient nickname from the message
                            partsArray.remove(IDtoOpponentNickname.get(recipientID));

                            //convert che String[] in String
                            String message = String.join(" ", partsArray);

                            sender.sendPrivateMessage(recipientID, message);
                            opponentFound = true;
                        }
                    }
                    if (!opponentFound) {
                        System.out.println("Error: invalid nickname. Please, try again");
                    }
                }
                case "rmc" -> { // Print match chat
                    if (parts.length != 1) {
                        System.out.println("Error: ReadMatchChat does not take any arguments. Please, try again");
                        break;
                    }

                    printMatchChat();
                }
                case "rpc" -> { // Print private chat
                    if (parts.length != 2) {
                        System.out.println("Error: ReadPrivateChat takes exactly 1 argument (opponent nickname). Please, try again");
                        break;
                    }

                    printOpponentChat(parts[1]);
                }
            }
        }
        else if (command.equals("exit") || command.equals("quit")) {
            System.err.println("Disconnecting...");
        }
        else {
            System.out.println("Error: " + command + " is not a valid command. Please, try again");
        }
    }

    @Override
    public void setID(int playerID) {
        sender.setPlayerID(playerID);
        this.playerID = playerID;
    }

    @Override
    public void showMatches(Map<Integer, List<String>> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matches available.");
            System.out.println("To create a new match type: cm <players_num> <nickname>");
            return;
        }

        System.out.println("Matches:");
        for (Map.Entry<Integer, List<String>> entry : matches.entrySet()) {
            System.out.println("\tMatch with ID " + entry.getKey() + " and players:");
            System.out.println("\t\t- " + entry.getValue());
        }
        System.out.println("To create a new match type: cm <players_num> <nickname>");
        System.out.println("To join a match type: sm <matchID>");
    }

    @Override
    public void createMatch(int matchID, String hostNickname) {
        playerState = PlayerState.MATCH_LOBBY;

        System.out.println("Match created with ID: " + matchID + " and host nickname: " + hostNickname);
        System.out.println("Waiting for players to join...");
    }

    @Override
    public void selectMatch(int matchID, List<String> nicknames) {
        playerState = PlayerState.CHOOSING_NICKNAME;

        System.out.println("Match selected with ID: " + matchID);
        System.out.println("To choose the nickname type: cn <nickname>");
    }

    @Override
    public void chooseNickname(String nickname) {
        playerState = PlayerState.MATCH_LOBBY;

        System.out.println("Joined match with nickname: " + nickname);
    }

    @Override
    public void startGame(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) {
        System.out.println("Game started.");

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
        System.out.println("To view the other side of the starter card type: fsc");
        System.out.println("To place the starter card on the current side type: psc");
    }

    @Override
    public void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                                int commonObjectiveCardID1, int commonObjectiveCardID2,
                                int secretObjectiveCardID1, int secretObjectiveCardID2) {
        // Place the starter card in the play area and update all legal and illegal positions
        playArea.put(new Position(0, 0), starterCardID);

        updatePlacementPositions(new Position(0, 0), starterSide);

        // Set missing setup
        playerState = PlayerState.CHOOSING_OBJECTIVE;

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
    public void showNewPlayer(String nicknames) {
        System.out.println("New player joined: " + nicknames);
        System.out.println("Waiting for players to join...");
    }

    @Override
    public void showFirstPlayerTurn(int playerID, String playerNickname, Map<Integer, String> IDtoOpponentNickname, Map<Integer, Map<Position, Integer>> IDtoOpponentPlayArea) {
        System.out.println("Now you can use the chat.");
        System.out.println("To send a message in the match chat type: wmm <message>");
        System.out.println("To send a message in the private chat type: wpm <recipient nickname> <message>");
        System.out.println("To read the match chat type: rmc");
        System.out.println("To read the private chat type: rpc <opponent nickname>");
        this.IDtoOpponentNickname = IDtoOpponentNickname;
        this.IDtoOpponentPlayerArea = IDtoOpponentPlayArea;
        for(Integer opponentID : IDtoOpponentNickname.keySet()){
            opponentIDtoPrivateChatList.put(opponentID, new ArrayList<>());
        }
        if (playerID == this.playerID) {
            playerState = PlayerState.PLACING;
            System.out.println("It's your turn.");

            // Print player hand
            printPlayerHand();

            // Print play area
            printPlayArea();

            // Print available commands
            System.out.println("To check your hand type: gh");
            System.out.println("To flip a card type: fc <cardID>");
            System.out.println("To place a card type: pc <cardID> <posID>");
        }
        else {
            playerState = PlayerState.WAITING;
            System.out.println("It's " + playerNickname + "'s turn.");
            System.out.println("While waiting you can: ");
            System.out.println("\t- Flip a card in your hand by typing: fc <cardID>");
            System.out.println("\t- Check your hand by typing: gh");
        }
    }

    @Override
    public void placeCard() {
        playerState = PlayerState.DRAWING;

        // Add card to play area
        playArea.put(placingCardPos, placingCardID);

        // Update legal and illegal positions
        updatePlacementPositions(placingCardPos, playerHand.get(placingCardID));

        // Remove card from player hand
        playerHand.remove(placingCardID);

        // Print draw area
        printDrawArea();

        // Print available commands
        System.out.println("To draw from resource deck, type: ddr");
        System.out.println("To draw a visible resource card, type: dvr <0 or 1>");
        System.out.println("To draw from gold deck, type: ddg");
        System.out.println("To draw a visible gold card, type: dvg <0 or 1>");
    }

    @Override
    public void opponentPlaceCard(int playerId, int cardID, Position pos, CardSideType side) {
        IDtoOpponentPlayerArea.get(playerId).put(pos, cardID);
        GameResources.getPlaceableCardByID(cardID).setCurrSideType(side);
    }

    @Override
    public void showNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID, String playerNickname) {
        if (lastPlayerID == playerID) {
            playerState = PlayerState.WAITING;
            playerHand.put(drawnCardID, CardSideType.FRONT);

            System.out.println("You drew a new card (ID: " + drawnCardID + "):");
            createPlaceableCardLines(drawnCardID, CardSideType.FRONT);
            createPlaceableCardLines(drawnCardID, CardSideType.BACK);

            for (String s : cardIDToCardFrontTUILines.get(drawnCardID)) {
                System.out.println(s);
            }
            System.out.println("It's " + IDtoOpponentNickname.get(newPlayerID) + " turn.");
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
                System.out.println("To check your hand type: gh");
                System.out.println("To flip a card type: fc <cardID>");
                System.out.println("To place a card type: pc <cardID> <posID>");
            }
            else {
                playerState = PlayerState.WAITING;

                System.out.println("It's " + playerNickname + "'s turn.");
                System.out.println("While waiting you can flip a card in your hand by typing: fc <cardID>");
                System.out.println("To check your hand type: gh");
                System.out.println("To check opponents play area: opa <opponentNickname>");
            }
        }
    }

    @Override
    public void updateDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        setDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);

        printDrawArea();
    }

    /**
     * Set the class attributes for the 6 cards in the draw area.
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

    private void printStarterCardChoice(int starterCardID) {
        this.starterCardID = starterCardID;
        starterSide = CardSideType.FRONT;

        createStarterCardLines(starterCardID, CardSideType.FRONT);
        createStarterCardLines(starterCardID, CardSideType.BACK);

        List<String> starterCardFront = cardIDToCardFrontTUILines.get(starterCardID);

        System.out.println("Starter card:");
        for (String s : starterCardFront) {
            System.out.println(s);
        }
    }

    private void flipStarterCard() {
        List<String> starterCard;

        if (starterSide.equals(CardSideType.FRONT)) {
            starterSide = CardSideType.BACK;

            starterCard = cardIDToCardBackTUILines.get(starterCardID);
        }
        else {
            starterSide = CardSideType.FRONT;

            starterCard = cardIDToCardFrontTUILines.get(starterCardID);
        }

        System.out.println("Current starter card side:");
        for (String s : starterCard) {
            System.out.println(s);
        }

        System.out.println("To view the other side of the starter card type: fsc");
        System.out.println("To place the starter card on the current side type: psc");
    }

    private void createPlayerHand(int resourceCardID1, int resourceCardID2, int goldenCardID) {
        playerHand.put(resourceCardID1, CardSideType.FRONT);
        playerHand.put(resourceCardID2, CardSideType.FRONT);
        playerHand.put(goldenCardID, CardSideType.FRONT);

        // Create player hand
        createResCardLines(resourceCardID1, CardSideType.FRONT);
        createResCardLines(resourceCardID1, CardSideType.BACK);

        createResCardLines(resourceCardID2, CardSideType.FRONT);
        createResCardLines(resourceCardID2, CardSideType.BACK);

        createGoldCardLines(goldenCardID, CardSideType.FRONT);
        createGoldCardLines(goldenCardID, CardSideType.BACK);
    }

    private void printPlayerHand() {
        List<List<String>> handToPrint = new ArrayList<>();
        StringBuilder cardIDs = new StringBuilder("Your hand:");

        for (Map.Entry<Integer, CardSideType> entry : playerHand.entrySet()) {
            cardIDs.append("\t").append(entry.getKey());

            if (entry.getValue().equals(CardSideType.FRONT)) {
                handToPrint.add(cardIDToCardFrontTUILines.get(entry.getKey()));
            }
            else {
                handToPrint.add(cardIDToCardBackTUILines.get(entry.getKey()));
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

    private void setCommonObjectives(int commonObjectiveCardID1, int commonObjectiveCardID2) {
        this.commonObjectiveCardID1 = commonObjectiveCardID1;
        this.commonObjectiveCardID2 = commonObjectiveCardID2;

        createObjectiveCardLines(commonObjectiveCardID1);
        createObjectiveCardLines(commonObjectiveCardID2);
    }

    private void printCommonObjectives() {
        List<String> commonObjectiveCard1Front = cardIDToCardFrontTUILines.get(commonObjectiveCardID1);
        List<String> commonObjectiveCard2Front = cardIDToCardFrontTUILines.get(commonObjectiveCardID2);

        System.out.println("Common objectives:");
        for (int i = 0; i < commonObjectiveCard1Front.size(); i++) {
            System.out.println(commonObjectiveCard1Front.get(i) + "   " + commonObjectiveCard2Front.get(i));
        }
    }

    private void setSecretObjectives(int secretObjectiveCardID1, int secretObjectiveCardID2) {
        this.secretObjectiveCardID1 = secretObjectiveCardID1;
        this.secretObjectiveCardID2 = secretObjectiveCardID2;

        createObjectiveCardLines(secretObjectiveCardID1);
        createObjectiveCardLines(secretObjectiveCardID2);
    }

    private void printSecretObjectiveChoice() {
        List<String> secretObjectiveCard1Front = cardIDToCardFrontTUILines.get(secretObjectiveCardID1);
        List<String> secretObjectiveCard2Front = cardIDToCardFrontTUILines.get(secretObjectiveCardID2);

        System.out.println("Secret objectives:");

        for (int i = 0; i < secretObjectiveCard1Front.size(); i++) {
            System.out.println(secretObjectiveCard1Front.get(i) + "   " + secretObjectiveCard2Front.get(i));
        }

        System.out.println("To choose one of the secret objectives type: cso <1 or 2>.");
    }

    private void printPlayArea() {
        if (playArea.isEmpty()) {
            System.out.println("No cards in play area");
            return;
        }

        // Get the max and min values of x and y
        int maxX = playArea.keySet().stream().map(Position::getX).max(Integer::compareTo).orElse(0);
        int minX = playArea.keySet().stream().map(Position::getX).min(Integer::compareTo).orElse(0);
        int maxY = playArea.keySet().stream().map(Position::getY).max(Integer::compareTo).orElse(0);
        int minY = playArea.keySet().stream().map(Position::getY).min(Integer::compareTo).orElse(0);

        // +3 because of the starter card and the 1 extra space on each border (for the validPosIDs)
        int[][] cardIDArr = new int[maxY - minY + 3][maxX - minX + 3];
        char[][] playAreaArr = new char[3 * (maxY - minY + 3) + 2][3 * (maxX - minX + 3) + 2];

        // The position of the starter card is new Position(-minX + 1, maxY + 1);
        // And the array's Y is inverted, so the starter card's position in the array is [xPos - minX + 1][-yPos + maxY + 1]

        for (int[] IDs : cardIDArr) {
            Arrays.fill(IDs, -1);
        }

        // Fill the array with elements from the playArea map
        for (Position pos : playArea.keySet()) {
            cardIDArr[-pos.getY() + maxY + 1][pos.getX() - minX + 1] = playArea.get(pos);
        }

        // Fill array of characters to print
        for (int y = 0; y < cardIDArr.length; y++) {
            for (int x = 0; x < cardIDArr[y].length; x++) {
                if (x > 0 && x < cardIDArr[y].length - 1 &&
                        y > 0 && y < cardIDArr.length - 1) {
                    PlaceableCard card = GameResources.getPlaceableCardByID(cardIDArr[y][x]);

                    if (card != null) {
                        // region Set permanent resources and constant card areas
                        char PR1, PR2, PR3;

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
                                PR1 = ' ';
                                PR2 = currSide.getResources().getFirst().toString().charAt(0);
                                PR3 = ' ';
                            }
                            else if (permanentRes.size() == 2) {
                                PR1 = currSide.getResources().getFirst().toString().charAt(0);
                                PR2 = ' ';
                                PR3 = currSide.getResources().get(1).toString().charAt(0);
                            }
                            else { // size == 3
                                PR1 = currSide.getResources().getFirst().toString().charAt(0);
                                PR2 = currSide.getResources().get(1).toString().charAt(0);
                                PR3 = currSide.getResources().get(2).toString().charAt(0);
                            }
                        }
                        else {
                            PR1 = ' ';
                            PR2 = ' ';
                            PR3 = ' ';
                        }

                        playAreaArr[3 * y][3 * x + 2] = '─';
                        playAreaArr[3 * y + 1][3 * x + 2] = ' ';
                        playAreaArr[3 * y + 2][3 * x + 2] = PR2;
                        playAreaArr[3 * y + 3][3 * x + 2] = ' ';
                        playAreaArr[3 * y + 4][3 * x + 2] = '─';

                        playAreaArr[3 * y + 2][3 * x] = '│';
                        playAreaArr[3 * y + 2][3 * x + 1] = PR1;
                        playAreaArr[3 * y + 2][3 * x + 3] = PR3;
                        playAreaArr[3 * y + 2][3 * x + 4] = '│';
                        // endregion

                        PlaceableCard TLCard = GameResources.getPlaceableCardByID(cardIDArr[y - 1][x - 1]);
                        PlaceableCard TRCard = GameResources.getPlaceableCardByID(cardIDArr[y - 1][x + 1]);
                        PlaceableCard BLCard = GameResources.getPlaceableCardByID(cardIDArr[y + 1][x - 1]);
                        PlaceableCard BRCard = GameResources.getPlaceableCardByID(cardIDArr[y + 1][x + 1]);

                        // Check if there is a card in the TL corner
                        if (playAreaArr[3 * y][3 * x] == '\u0000') {
                            if (TLCard != null) {
                                if (TLCard.getPriority() > card.getPriority()){
                                    playAreaArr[3 * y + 1][3 * x + 1] = '┘';
                                    playAreaArr[3 * y + 1][3 * x] = '┬';
                                    playAreaArr[3 * y][3 * x + 1] = '├';
                                    ResourceType TLRes = TLCard.getCurrSide().getBRCorner().getResource();
                                    playAreaArr[3 * y][3 * x] = TLRes == null ? ' ' : TLRes.toString().charAt(0);
                                }
                                else {
                                    playAreaArr[3 * y][3 * x] = '┌';
                                    playAreaArr[3 * y][3 * x + 1] = '┴';
                                    playAreaArr[3 * y + 1][3 * x] = '┤';
                                    ResourceType TLRes = card.getCurrSide().getTLCorner().getResource();
                                    playAreaArr[3 * y + 1][3 * x + 1] = TLRes == null ? ' ' : TLRes.toString().charAt(0);
                                }
                            }
                            else {
                                playAreaArr[3 * y][3 * x] = '┌';
                                playAreaArr[3 * y][3 * x + 1] = '─';
                                playAreaArr[3 * y + 1][3 * x] = '│';
                                ResourceType TLRes = card.getCurrSide().getTLCorner().getResource();
                                playAreaArr[3 * y + 1][3 * x + 1] = TLRes == null ? ' ' : TLRes.toString().charAt(0);
                            }
                        }

                        // Check if there is a card in the TR corner
                        if (playAreaArr[3 * y][3 * x + 3] == '\u0000') {
                            if (TRCard != null) {
                                if (TRCard.getPriority() > card.getPriority()){
                                    playAreaArr[3 * y + 1][3 * x + 4] = '┬';
                                    playAreaArr[3 * y + 1][3 * x + 3] = '└';
                                    playAreaArr[3 * y][3 * x + 3] = '┤';
                                    ResourceType TRRes = TRCard.getCurrSide().getBLCorner().getResource();
                                    playAreaArr[3 * y][3 * x + 4] = TRRes == null ? ' ' : TRRes.toString().charAt(0);
                                }
                                else {
                                    playAreaArr[3 * y][3 * x + 3] = '┴';
                                    playAreaArr[3 * y][3 * x + 4] = '┐';
                                    playAreaArr[3 * y + 1][3 * x + 4] = '├';
                                    ResourceType TRRes = card.getCurrSide().getTRCorner().getResource();
                                    playAreaArr[3 * y + 1][3 * x + 3] = TRRes == null ? ' ' : TRRes.toString().charAt(0);
                                }
                            }
                            else {
                                playAreaArr[3 * y][3 * x + 4] = '┐';
                                playAreaArr[3 * y][3 * x + 3] = '─';
                                playAreaArr[3 * y + 1][3 * x + 4] = '│';
                                ResourceType TRRes = card.getCurrSide().getTRCorner().getResource();
                                playAreaArr[3 * y + 1][3 * x + 3] = TRRes == null ? ' ' : TRRes.toString().charAt(0);
                            }
                        }

                        // Check if there is a card in the BL corner
                        if (playAreaArr[3 * y + 3][3 * x] == '\u0000'){
                            if (BLCard != null) {
                                if (BLCard.getPriority() > card.getPriority()){
                                    playAreaArr[3 * y + 3][3 * x] = '┴';
                                    playAreaArr[3 * y + 4][3 * x + 1] = '├';
                                    playAreaArr[3 * y + 3][3 * x + 1] = '┐';
                                    ResourceType BLRes = BLCard.getCurrSide().getTRCorner().getResource();
                                    playAreaArr[3 * y + 4][3 * x] = BLRes == null ? ' ' : BLRes.toString().charAt(0);
                                }
                                else {
                                    playAreaArr[3 * y + 4][3 * x + 1] = '┬';
                                    playAreaArr[3 * y + 3][3 * x] = '┤';
                                    playAreaArr[3 * y + 4][3 * x] = '└';
                                    ResourceType BLRes = card.getCurrSide().getBLCorner().getResource();
                                    playAreaArr[3 * y + 3][3 * x + 1] = BLRes == null ? ' ' : BLRes.toString().charAt(0);
                                }
                            }
                            else {
                                playAreaArr[3 * y + 4][3 * x + 1] = '─';
                                playAreaArr[3 * y + 3][3 * x] = '│';
                                playAreaArr[3 * y + 4][3 * x] = '└';
                                ResourceType BLRes = card.getCurrSide().getBLCorner().getResource();
                                playAreaArr[3 * y + 3][3 * x + 1] = BLRes == null ? ' ' : BLRes.toString().charAt(0);
                            }
                        }

                        // Check if there is a card in the BR corner
                        if (playAreaArr[3 * y + 3][3 * x + 3] == '\u0000'){
                            if (BRCard != null) {
                                if (BRCard.getPriority() > card.getPriority()) {
                                    playAreaArr[3 * y + 3][3 * x + 3] = '┌';
                                    playAreaArr[3 * y + 3][3 * x + 4] = '┴';
                                    playAreaArr[3 * y + 4][3 * x + 3] = '┤';
                                    ResourceType BRRes = BRCard.getCurrSide().getTLCorner().getResource();
                                    playAreaArr[3 * y + 4][3 * x + 4] = BRRes == null ? ' ' : BRRes.toString().charAt(0);
                                }
                                else {
                                    playAreaArr[3 * y + 4][3 * x + 4] = '┘';
                                    playAreaArr[3 * y + 3][3 * x + 4] = '├';
                                    playAreaArr[3 * y + 4][3 * x + 3] = '┬';
                                    ResourceType BRRes = card.getCurrSide().getBRCorner().getResource();
                                    playAreaArr[3 * y + 3][3 * x + 3] = BRRes == null ? ' ' : BRRes.toString().charAt(0);
                                }
                            }
                            else {
                                playAreaArr[3 * y + 4][3 * x + 4] = '┘';
                                playAreaArr[3 * y + 3][3 * x + 4] = '│';
                                playAreaArr[3 * y + 4][3 * x + 3] = '─';
                                ResourceType BRRes = card.getCurrSide().getBRCorner().getResource();
                                playAreaArr[3 * y + 3][3 * x + 3] = BRRes == null ? ' ' : BRRes.toString().charAt(0);
                            }
                        }
                    }
                }
            }
        }

        // Fill the validPosIDs
        for (int i : posIDToValidPos.keySet()) {
            Position pos = posIDToValidPos.get(i);
            Position arrPos = new Position(3 * (pos.getX() - minX + 1), 3 * (-pos.getY() + maxY + 1));
            if (i < 10) {
                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 2] = (char) (i + 48);
            }
            else {
                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 1] = (char) (i / 10 + 48);
                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 2] = (char) (i % 10 + 48);
            }
        }

        // Print the play area
        for (char[] chars : playAreaArr) {
            StringBuilder line = new StringBuilder();

            for (char c : chars) {
                if (c == '\u0000') {
                    line.append(' ');
                } else {
                    line.append(c);
                }
            }
            System.out.println(line);
        }
    }

    private void printOpponentPlayArea(int opponentID) {
        // TODO: Implement
    }

    private void updatePlacementPositions(Position pos, CardSideType sideType) {
        ArrayList<Position> newLegalPos = new ArrayList<>();
        ArrayList<Position> newIllegalPos = new ArrayList<>();

        PlaceableCard card = GameResources.getPlaceableCardByID(playArea.get(pos));

        Side currSide = sideType == CardSideType.FRONT ? card.getFront() : card.getBack();

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

        if (!illegalPosList.contains(pos)) {
            illegalPosList.add(pos);
        }

        // Update the posIDToValidPos map
        posIDToValidPos.clear();

        for (int i = 0; i < legalPosList.size(); i++) {
            posIDToValidPos.put(i, legalPosList.get(i));
        }
    }

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

    private void createResCardLines(int cardID, CardSideType sideType) {
        ResourceCard card = (ResourceCard) GameResources.getPlaceableCardByID(cardID);

        String A1, A2, A3, B1, B2, B3, C, D, E1, E2, E3, F1, F2, F3;

        Side cardSide;

        if (sideType.equals(CardSideType.FRONT)){
            cardSide = card.getFront();
        }
        else {
            cardSide = card.getBack();
        }

        if (cardSide.getTLCorner().isAvailable()) {
            A1 = "┏━━━━━┳";
            String r = cardSide.getTLCorner().getResource() != null ?
                    cardSide.getTLCorner().getResource().getColor() + cardSide.getTLCorner().getResource().toString() + CC.RESET : " ";
            A2 = "┃  " + r + "  ┃";
            A3 = "┣━━━━━┛";
        }
        else{
            A1 = "┏━━━━━━";
            A2 = "┃      ";
            A3 = "┃      ";
        }

        if (cardSide.getTRCorner().isAvailable()) {
            B1 = "┳━━━━━┓";
            String r = cardSide.getTRCorner().getResource() != null ?
                    cardSide.getTRCorner().getResource().getColor() + cardSide.getTRCorner().getResource().toString() + CC.RESET : " ";
            B2 = "┃  " + r + "  ┃";
            B3 = "┗━━━━━┫";
        }
        else{
            B1 = "━━━━━━┓";
            B2 = "      ┃";
            B3 = "      ┃";
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
            E1 = "┣━━━━━┓";
            String r = cardSide.getBLCorner().getResource() != null ?
                    cardSide.getBLCorner().getResource().getColor() + cardSide.getBLCorner().getResource().toString() + CC.RESET : " ";
            E2 = "┃  " + r + "  ┃";
            E3 = "┗━━━━━┻";
        }
        else {
            E1 = "┃      ";
            E2 = "┃      ";
            E3 = "┗━━━━━━";
        }

        if (cardSide.getBRCorner().isAvailable()) {
            F1 = "┏━━━━━┫";
            String r = cardSide.getBRCorner().getResource() != null ?
                    cardSide.getBRCorner().getResource().getColor() + cardSide.getBRCorner().getResource().toString() + CC.RESET : " ";
            F2 = "┃  " + r + "  ┃";
            F3 = "┻━━━━━┛";
        }
        else {
            F1 = "      ┃";
            F2 = "      ┃";
            F3 = "━━━━━━┛";
        }

        List<String> lines = new ArrayList<>();

        lines.add(A1 + "━━━━━━━━━━━" + B1);
        lines.add(A2 + "     " + C + "     " + B2);
        lines.add(A3 + "           " + B3);
        lines.add("┃           " + D + "           ┃");
        lines.add(E1 + "           " + F1);
        lines.add(E2 + "           " + F2);
        lines.add(E3 + "━━━━━━━━━━━" + F3);

        if (sideType.equals(CardSideType.FRONT)){
            cardIDToCardFrontTUILines.put(cardID, lines);
        }
        else {
            cardIDToCardBackTUILines.put(cardID, lines);
        }
    }

    private void createGoldCardLines(int cardID, CardSideType sideType) {
        GoldenCard card = (GoldenCard) GameResources.getPlaceableCardByID(cardID);

        String A1, A2, A3, B1, B2, B3, C, D, E1, E2, E3, F1, F2, F3;

        StringBuilder G;

        Side cardSide;

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
            A2 = "║  " + r + "  ║";
            A3 = "╠═════╝";
        }
        else{
            A1 = "╔══════";
            A2 = "║      ";
            A3 = "║      ";
        }

        if (cardSide.getTRCorner().isAvailable()) {
            B1 = "╦═════╗";
            String r = cardSide.getTRCorner().getResource() != null ?
                    cardSide.getTRCorner().getResource().getColor() + cardSide.getTRCorner().getResource().toString() + CC.RESET : " ";
            B2 = "║  " + r + "  ║";
            B3 = "╚═════╣";
        }
        else{
            B1 = "══════╗";
            B2 = "      ║";
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
            E2 = "║  " + r + "  ║";
            E3 = "╚═════╩";
        }
        else {
            E1 = "║      ";
            E2 = "║      ";
            E3 = "╚══════";
        }

        if (cardSide.getBRCorner().isAvailable()) {
            F1 = "╔═════╣";
            String r = cardSide.getBRCorner().getResource() != null ?
                    cardSide.getBRCorner().getResource().getColor() + cardSide.getBRCorner().getResource().toString() + CC.RESET : " ";
            F2 = "║  " + r + "  ║";
            F3 = "╩═════╝";
        }
        else {
            F1 = "      ║";
            F2 = "      ║";
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

        lines.add(A1 + "═══════════" + B1);
        lines.add(A2 + "    " + C + "    " + B2);
        lines.add(A3 + "           " + B3);
        lines.add("║           " + D + "           ║");
        lines.add(E1 + "           " + F1);
        lines.add(E2 + "   " + G + "   " + F2);
        lines.add(E3 + "═══════════" + F3);

        if (sideType.equals(CardSideType.FRONT)){
            cardIDToCardFrontTUILines.put(cardID, lines);
        }
        else {
            cardIDToCardBackTUILines.put(cardID, lines);
        }
    }

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

    private void createBLLShapeObjectiveCardLines(BLLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        lines.add("┃               "+ card.getPoints() + "       ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃           " + mainColor + "   " + CC.RESET + "         ┃");
        lines.add("┃                       ┃");
        lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createBRLShapeObjectiveCardLines(BRLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        lines.add("┃               "+ card.getPoints()+ "       ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃                 " + mainColor + "   " + CC.RESET + "   ┃");
        lines.add("┃                       ┃");
        lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createResourcesCountObjectiveCardLines(ResourcesCountObjectiveCard card) {
        List<String> lines = new ArrayList<>();

        if (card.getID() >= 94 && card.getID() <= 97) {
            String color = card.getResourceType().getBgColor();

            lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
            lines.add("┃               " + card.getPoints() + "       ┃");
            lines.add("┃                       ┃");
            lines.add("┃              " + color + "   " + CC.RESET + "      ┃");
            lines.add("┃           " + color + "   " + CC.RESET + "   " + color + "   " + CC.RESET + "   ┃");
            lines.add("┃                       ┃");
            lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
        }
        else {
            String resType = card.getResourceType().toString();

            lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
            lines.add("┃               "+ card.getPoints()+ "       ┃");
            lines.add("┃                       ┃");
            lines.add("┃              " + CC.YELLOW + resType + "  "  + resType + CC.RESET + "     ┃");
            lines.add("┃                       ┃");
            lines.add("┃                       ┃");
            lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
        }

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createTLBRDiagonalObjectiveCardLines(TLBRDiagonalObjectiveCard card) {
        String color = card.getElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        lines.add("┃               "+ card.getPoints()+ "       ┃");
        lines.add("┃           " + color + "   " + CC.RESET + "         ┃");
        lines.add("┃              " + color + "   " + CC.RESET + "      ┃");
        lines.add("┃                 " + color + "   " + CC.RESET + "   ┃");
        lines.add("┃                       ┃");
        lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createTLLShapeObjectiveCardLines(TLLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        lines.add("┃               "+ card.getPoints()+ "       ┃");
        lines.add("┃           " + mainColor + "   " + CC.RESET + "         ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃                       ┃");
        lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createTRBLDiagonalObjectiveCardLines(TRBLDiagonalObjectiveCard card) {
        String color = card.getElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        lines.add("┃               "+ card.getPoints()+ "       ┃");
        lines.add("┃                 " + color + "   " + CC.RESET + "   ┃");
        lines.add("┃              " + color + "   " + CC.RESET + "      ┃");
        lines.add("┃           " + color + "   " + CC.RESET + "         ┃");
        lines.add("┃                       ┃");
        lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createTrinityObjectiveCardLines(TrinityObjectiveCard card) {
        List<String> lines = new ArrayList<>();

        lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        lines.add("┃               "+ card.getPoints()+ "       ┃");
        lines.add("┃                       ┃");
        lines.add("┃            " + CC.YELLOW + "Q  K  M" + CC.RESET + "    ┃");
        lines.add("┃                       ┃");
        lines.add("┃                       ┃");
        lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createTRLShapeObjectiveCardLines(TRLShapeObjectiveCard card) {
        String mainColor = card.getMainElementType().getBgColor();
        String secColor = card.getSecondaryElementType().getBgColor();

        List<String> lines = new ArrayList<>();

        lines.add("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        lines.add("┃               "+ card.getPoints()+ "       ┃");
        lines.add("┃                 " + mainColor + "   " + CC.RESET + "   ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃              " + secColor + "   " + CC.RESET + "      ┃");
        lines.add("┃                       ┃");
        lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createStateToCommandsMap() {
        stateToCommands.put(PlayerState.MAIN_MENU,
                new ArrayList<>(Arrays.asList("cm", "sm")));

        stateToCommands.put(PlayerState.MATCH_LOBBY,
                new ArrayList<>());

        stateToCommands.put(PlayerState.CHOOSING_NICKNAME,
                new ArrayList<>(List.of("cn")));

        stateToCommands.put(PlayerState.PLACING_STARTER,
                new ArrayList<>(Arrays.asList("fsc", "psc")));

        stateToCommands.put(PlayerState.CHOOSING_OBJECTIVE,
                new ArrayList<>(List.of("cso", "rmc")));

        stateToCommands.put(PlayerState.COMPLETED_SETUP,
                new ArrayList<>(List.of("rmc", "rpc", "wmm", "wpm")));

        stateToCommands.put(PlayerState.WAITING,
                new ArrayList<>(List.of("fc", "gh", "rmc", "rpc", "wmm", "wpm")));

        stateToCommands.put(PlayerState.PLACING,
                new ArrayList<>(Arrays.asList("fc", "gh", "pc", "rmc", "rpc", "wmm", "wpm")));

        stateToCommands.put(PlayerState.DRAWING,
                new ArrayList<>(Arrays.asList("fc", "gh", "ddr", "dvr", "ddg", "dvg", "rmc", "rpc", "wmm", "wpm")));
    }

    @Override
    public void confirmPrivateMessage(int recipientID, String message, int senderID) {
        String senderNickname;
        if (senderID == playerID){
            senderNickname = "Me";
        }
        else {
            senderNickname = IDtoOpponentNickname.get(senderID);
        }
        opponentIDtoPrivateChatList.get(recipientID).add(new MessageModel(senderNickname, message));
        System.out.println("Message to " + IDtoOpponentNickname.get(recipientID) + " successfully delivered.");
    }

    @Override
    public void receivingPrivateMessage(String message, int senderID) {
        String senderNickname;
        if (senderID == playerID){
            senderNickname = "Me";
        }
        else {
            senderNickname = IDtoOpponentNickname.get(senderID);
        }
        MessageModel newMessage = new MessageModel(senderNickname, message);
        opponentIDtoPrivateChatList.get(senderID).add(newMessage);
        System.out.println("New private message:");
        System.out.println(newMessage.printMessage());
    }

    @Override
    public void receivingMatchMessage(String message, int senderID) {
        if (senderID == playerID){
            matchChatList.add(new MessageModel("Me", message));
            System.out.println("Message successfully delivered.");
        }
        else {
            MessageModel newMessage = new MessageModel(IDtoOpponentNickname.get(senderID), message);
            matchChatList.add(newMessage);
            System.out.println("New match message:");
            System.out.println(newMessage.printMessage());
        }
    }

    @Override
    public void recipientNotFound(int recipientID) {
        System.out.println("Message delivery to " + IDtoOpponentNickname.get(recipientID) + " failed.");
    }

    private void printMatchChat() {
        if (matchChatList.isEmpty()){
            System.out.println("The match chat is empty. To write a match message type: wmm <message>.");
        }
        else {
            System.out.println("This is the match chat:");
            for (MessageModel messageModel : matchChatList){
                System.out.println(messageModel.printMessage());
            }
        }
    }

    private void printOpponentChat(String opponent) {
        boolean opponentFound = false;
        for (Integer opponentID : IDtoOpponentNickname.keySet()) {
            if (IDtoOpponentNickname.get(opponentID).equals(opponent)){
                if (opponentIDtoPrivateChatList.get(opponentID).isEmpty()){
                    System.out.println("The private chat with " + opponent + " is empty. To write a private message type: wpm <opponent nickname> <message>.");
                }
                else {
                    System.out.println("This is the match chat:");
                    for (MessageModel messageModel : opponentIDtoPrivateChatList.get(opponentID)){
                        System.out.println(messageModel.printMessage());
                    }
                }
                opponentFound = true;
            }
        }
        if (!opponentFound){
            System.out.println("Opponent not found.");
        }

    }
}
