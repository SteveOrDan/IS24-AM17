package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.objectiveCards.*;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.player.PlayerState;
import com.example.pf_soft_ing.player.TokenColors;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

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
        String command = parts[0];

        List<String> legalCommands = stateToCommands.get(playerState);
        if (legalCommands.contains(command)) {
            switch (command) {
                // TODO: add refresh command

                case "CreateMatch" -> {
                    if (parts.length != 3) {
                        System.out.println("Error: CreateMatch takes exactly 2 arguments (num of players, nickname). Please, try again");
                        break;
                    }
                    sender.createMatch(Integer.parseInt(parts[1]), parts[2]);
                }
                case "SelectMatch" -> {
                    if (parts.length != 2) {
                        System.out.println("Error: SelectMatch takes exactly 1 argument (match ID). Please, try again");
                        break;
                    }
                    sender.selectMatch(Integer.parseInt(parts[1]));
                }
                case "ChooseNickname" -> {
                    if (parts.length != 2) {
                        System.out.println("Error: ChooseNickname takes exactly 1 argument (nickname). Please, try again");
                        break;
                    }
                    sender.chooseNickname(parts[1]);
                }
                case "FlipStarterCard" -> {
                    if (parts.length != 1) {
                        System.out.println("Error: FlipStarterCard does not take any arguments. Please, try again");
                        break;
                    }
                    flipStarterCard();
                }
                case "PlaceStarterCard" -> {
                    if (parts.length != 1) {
                        System.out.println("Error: PlaceStarterCard does not take any arguments. Please, try again");
                        break;
                    }
                    sender.placeStarterCard(starterSide);
                }
                case "ChooseSecretObjective" -> {
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
                case "FlipCard" -> {
                    if (parts.length != 2) {
                        System.out.println("Error: FlipCard takes exactly 1 argument (card ID). Please, try again");
                        break;
                    }
                    playerHand.compute(Integer.parseInt(parts[1]), (_, side) -> side == CardSideType.FRONT ? CardSideType.BACK : CardSideType.FRONT);
                }
                case "PlaceCard" -> {
                    if (parts.length != 3) {
                        System.out.println("Error: PlaceCard takes exactly 1 argument (card ID). Please, try again");
                        break;
                    }
                    int cardID = Integer.parseInt(parts[1]);
                    Position pos = posIDToValidPos.get(Integer.parseInt(parts[2]));
                    sender.placeCard(cardID, playerHand.get(cardID), pos);
                }
                case "DrawCard" -> {

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
            System.out.println("To create a new match type: CreateMatch <players_num> <nickname>");
            return;
        }

        System.out.println("Matches:");
        for (Map.Entry<Integer, List<String>> entry : matches.entrySet()) {
            System.out.println("\tMatch with ID " + entry.getKey() + " and players:");
            System.out.println("\t\t- " + entry.getValue());
        }
        System.out.println("To create a new match type: CreateMatch <players_num> <nickname>");
        System.out.println("To join a match type: SelectMatch <matchID>");
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
        System.out.println("To choose the nickname type: ChooseNickname <nickname>");
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
        playerState = PlayerState.PLACING_STARTER;
        System.out.println("Game started.");

        GameResources.initializeAllDecks();

        // Print 4 visible cards, 2 deck top card and starter card
        printDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);

        // Create and print starter card
        printStarterCardChoice(starterCardID);

        System.out.println("To view the other side of the starter card type: FlipStarterCard");
        System.out.println("To place the starter card on the current side type: PlaceStarterCard");
    }

    @Override
    public void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2, int secretObjectiveCardID1, int secretObjectiveCardID2) {
        playerState = PlayerState.CHOOSING_OBJECTIVE;

        // Add cards to player hand
        playerHand.put(resourceCardID1, CardSideType.FRONT);
        playerHand.put(resourceCardID2, CardSideType.FRONT);
        playerHand.put(goldenCardID, CardSideType.FRONT);

        // Create player hand
        printPlayerHand(resourceCardID1, resourceCardID2, goldenCardID, TokenColors.getColorFromToken(tokenColor));

        // Create common objectives
        printCommonObjectives(commonObjectiveCardID1, commonObjectiveCardID2);

        // Render secret objective choice
        printSecretObjectiveChoice(secretObjectiveCardID1, secretObjectiveCardID2);
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
    public void showPlayerTurn(int playerID, String playerNickname) {
        if (playerID == this.playerID) {
            System.out.println("It's your turn.");
            System.out.println("To flip a card type: FlipCard <cardID>");
            System.out.println("To place a card type: PlaceCard <cardID>");
        }
        else {
            System.out.println("It's " + playerNickname + "'s turn.");
            System.out.println("While waiting you can flip a card in your hand by typing: FlipCard <cardID>");
        }
    }

    private void printDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                               int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        System.out.println("Draw area:");

        // Create TUI lines for resource cards
        createResCardLines(resDeckCardID, CardSideType.FRONT);
        createResCardLines(resDeckCardID, CardSideType.BACK);

        createResCardLines(visibleResCardID1, CardSideType.FRONT);
        createResCardLines(visibleResCardID1, CardSideType.BACK);

        createResCardLines(visibleResCardID2, CardSideType.FRONT);
        createResCardLines(visibleResCardID2, CardSideType.BACK);

        // Create TUI lines for gold cards
        createGoldCardLines(goldDeckCardID, CardSideType.FRONT);
        createGoldCardLines(goldDeckCardID, CardSideType.BACK);

        createGoldCardLines(visibleGoldCardID1, CardSideType.FRONT);
        createGoldCardLines(visibleGoldCardID1, CardSideType.BACK);

        createGoldCardLines(visibleGoldCardID2, CardSideType.FRONT);
        createGoldCardLines(visibleGoldCardID2, CardSideType.BACK);

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

        System.out.println("To place the starter card on the current side: PlaceStarterCard");
    }

    private void printPlayerHand(int resourceCardID1, int resourceCardID2, int goldenCardID, Color colorFromToken) {
        System.out.println("Player hand:");

        createResCardLines(resourceCardID1, CardSideType.FRONT);
        createResCardLines(resourceCardID1, CardSideType.BACK);

        createResCardLines(resourceCardID2, CardSideType.FRONT);
        createResCardLines(resourceCardID2, CardSideType.BACK);

        createGoldCardLines(goldenCardID, CardSideType.FRONT);
        createGoldCardLines(goldenCardID, CardSideType.BACK);

        List<String> resCard1Front = cardIDToCardFrontTUILines.get(resourceCardID1);
        List<String> resCard2Front = cardIDToCardFrontTUILines.get(resourceCardID2);
        List<String> goldCardFront = cardIDToCardFrontTUILines.get(goldenCardID);

        System.out.println("Color: " + colorFromToken.toString());

        System.out.println("Your hand:");
        for (int i = 0; i < resCard1Front.size(); i++) {
            System.out.println(resCard1Front.get(i) + "   " + resCard2Front.get(i) + "   " + goldCardFront.get(i));
        }
    }

    private void printCommonObjectives(int commonObjectiveCardID1, int commonObjectiveCardID2) {
        createObjectiveCardLines(commonObjectiveCardID1);

        createObjectiveCardLines(commonObjectiveCardID2);

        List<String> commonObjectiveCard1Front = cardIDToCardFrontTUILines.get(commonObjectiveCardID1);
        List<String> commonObjectiveCard2Front = cardIDToCardFrontTUILines.get(commonObjectiveCardID2);

        System.out.println("Common objectives:");
        for (int i = 0; i < commonObjectiveCard1Front.size(); i++) {
            System.out.println(commonObjectiveCard1Front.get(i) + "   " + commonObjectiveCard2Front.get(i));
        }
    }

    private void printSecretObjectiveChoice(int secretObjectiveCardID1, int secretObjectiveCardID2) {
        this.secretObjectiveCardID1 = secretObjectiveCardID1;
        this.secretObjectiveCardID2 = secretObjectiveCardID2;

        createObjectiveCardLines(secretObjectiveCardID1);
        createObjectiveCardLines(secretObjectiveCardID2);

        List<String> secretObjectiveCard1Front = cardIDToCardFrontTUILines.get(secretObjectiveCardID1);
        List<String> secretObjectiveCard2Front = cardIDToCardFrontTUILines.get(secretObjectiveCardID2);

        System.out.println("Objective 1:");
        for (String s : secretObjectiveCard1Front) {
            System.out.println(s);
        }

        System.out.println("Objective 2:");
        for (String s : secretObjectiveCard2Front) {
            System.out.println(s);
        }

        System.out.println("To choose one of the objectives type: ChooseSecretObjective <1 or 2>.");
    }

    private void updateIlLegalPositions(Position pos, CardSideType sideType){
        ArrayList<Position> newLegalPos = new ArrayList<>();
        ArrayList<Position> newIllegalPos = new ArrayList<>();

        PlaceableCard card = GameResources.getPlaceableCardByID(playArea.get(pos));

        Side currSide = sideType == CardSideType.FRONT ? card.getFront() : card.getBack();

        if (currSide.getBLCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
        }

        if (currSide.getBRCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
        }

        if (currSide.getTLCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
        }

        if (currSide.getTRCorner().isAvailable()){
            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
        }
        else{
            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
        }

        for (Position p : newIllegalPos){
            if (!illegalPosList.contains(p)){
                illegalPosList.add(p);
            }

            legalPosList.remove(p);
        }

        for (Position p : newLegalPos){
            if (!legalPosList.contains(p) && !illegalPosList.contains(p)){
                legalPosList.add(p);
            }
        }

        // Set the new card's position as illegal (a card just got placed)
        legalPosList.remove(pos);

        if (!illegalPosList.contains(pos)){
            illegalPosList.add(pos);
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
        String mainColor = card.getMainElementType().getColor();
        String secColor = card.getSecondaryElementType().getColor();

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
        String mainColor = card.getMainElementType().getColor();
        String secColor = card.getSecondaryElementType().getColor();

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
            String color = card.getResourceType().getColor();

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
            lines.add("┃              " + resType + "  "  + resType + "     ┃");
            lines.add("┃                       ┃");
            lines.add("┃                       ┃");
            lines.add("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
        }

        cardIDToCardFrontTUILines.put(card.getID(), lines);
    }

    private void createTLBRDiagonalObjectiveCardLines(TLBRDiagonalObjectiveCard card) {
        String color = card.getElementType().getColor();

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
        String mainColor = card.getMainElementType().getColor();
        String secColor = card.getSecondaryElementType().getColor();

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
        String color = card.getElementType().getColor();

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
        String mainColor = card.getMainElementType().getColor();
        String secColor = card.getSecondaryElementType().getColor();

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
        stateToCommands.put(PlayerState.MAIN_MENU, new ArrayList<>(Arrays.asList("CreateMatch", "SelectMatch")));
        stateToCommands.put(PlayerState.MATCH_LOBBY, new ArrayList<>());
        stateToCommands.put(PlayerState.CHOOSING_NICKNAME, new ArrayList<>(List.of("ChooseNickname")));
        stateToCommands.put(PlayerState.PLACING_STARTER, new ArrayList<>(Arrays.asList("FlipStarterCard", "PlaceStarterCard")));
        stateToCommands.put(PlayerState.CHOOSING_OBJECTIVE, new ArrayList<>(List.of("ChooseSecretObjective")));
        stateToCommands.put(PlayerState.COMPLETED_SETUP, new ArrayList<>());
        stateToCommands.put(PlayerState.WAITING, new ArrayList<>(List.of("FlipCard")));
        stateToCommands.put(PlayerState.PLACING, new ArrayList<>(Arrays.asList("FlipCard", "PlaceCard")));
        stateToCommands.put(PlayerState.DRAWING, new ArrayList<>(Arrays.asList("FlipCard", "DrawCard")));
    }
}
