package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.player.TokenColors;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class GUIView implements View {

    private final List<Position> legalPosList = new ArrayList<>();
    private final List<Position> illegalPosList = new ArrayList<>();
    private final Map<Position, Integer> playArea = new HashMap<>();

    private final Map<Position, Pane> validPosToButtonPane = new HashMap<>();

    private final List<Integer> playerHand = new ArrayList<>();

    private int matchID;

    private CardSideType starterCardSide;
    private int starterCardID;

    private int secretObjectiveCardID;

    private String connectionType;

    private int selectedCardID = -1;
    private double lastPlacedCardX;
    private double lastPlacedCardY;
    private double selectedCardX;
    private double selectedCardY;
    private Pane selectedCardPane;

    private boolean pressingCtrl = false;
    private double zoomLevel = 1;
    private final static double minZoom = 0.5;
    private final static double maxZoom = 1.5;

    private final static double barOffset = 40;
    private final static double defaultElementsOffset = 10;

    private double stageWidth;
    private double stageHeight;

    private double cardWidth;
    private double cardHeight;

    private double playerFieldRectWidth;
    private double playerFieldRectHeight;

    private double gridCellWidth;
    private double gridCellHeight;

    private double gridWidth;
    private double gridHeight;

    private int gridRows;
    private int gridColumns;

    private static final double selectedCardOffset = 25;

    // Card corner 267 x 326 out of 1232 x 815
    // --> 0.215 x 0.4
    private final static double cardCornerWidthProportion = 0.215;
    private final static double cardCornerHeightProportion = 0.4;

    private Group root;
    private GridPane playerFieldGrid;
    private AnchorPane playerField;
    private AnchorPane playerHandPane;
    private AnchorPane tempChoicePane;

    private final String ip;
    private ClientSender sender;

    private final Stage stage;

    public GUIView(Stage stage, String ip) {
        this.stage = stage;
        this.ip = ip;

        launchApp();
    }

    private void launchApp(){
        stage.setTitle("Codex naturalis");
        root = new Group();
        stage.setScene(new Scene(root));
        stage.setWidth(1721);
        stage.setHeight(926);
        stage.show();

        stageWidth = stage.getWidth() - 15; // Don't ask me why 15
        stageHeight = stage.getHeight() - barOffset;

        stage.setMaximized(true);

        drawConnectionChoiceScene();
    }

    // Game flow:
    // 1. Place shuffled decks and 4 visible cards
    // 2. Place starter card
    // 3. Choose token
    // 4. Fill hand
    // 5. Place common objectives
    // 6. Choose secret objective

    private void drawConnectionChoiceScene(){
        // Create connection choice scene
        AnchorPane connectionChoice = new AnchorPane();
        connectionChoice.setPrefSize(stageWidth, stageHeight);
        connectionChoice.setLayoutX(0);
        connectionChoice.setLayoutY(0);

        // Create buttons for connection type choice
        Button socketButton = new Button("Socket");
        socketButton.setPrefSize(200, 100);
        socketButton.setLayoutX((stageWidth - 200) * 0.5 - 150);
        socketButton.setLayoutY((stageHeight - 350) * 0.5);


        Button rmiButton = new Button("RMI");
        rmiButton.setPrefSize(200, 100);
        rmiButton.setLayoutX((stageWidth - 200) * 0.5 + 150);
        rmiButton.setLayoutY((stageHeight - 350) * 0.5);

        socketButton.setOnAction((_) -> {
            connectionType = "socket";
            socketButton.setEffect(new ColorAdjust(0.7, 0.2, 0, 0));
            rmiButton.setEffect(new ColorAdjust(0, 0, 0, 0));
        });
        rmiButton.setOnAction((_) -> {
            connectionType = "rmi";
            rmiButton.setEffect(new ColorAdjust(0.7, 0.2, 0, 0));
            socketButton.setEffect(new ColorAdjust(0, 0, 0, 0));
        });

        // Create text field for IP
        TextField IPField = new TextField();
        IPField.setPrefSize(200, 50);
        IPField.setLayoutX((stageWidth - 200) * 0.5);
        IPField.setLayoutY((stageHeight - 50) * 0.5);
        IPField.setPromptText("Enter port number...");

        // Create confirm button
        Button confirmIP = new Button("Confirm");
        confirmIP.setPrefSize(200, 50);
        confirmIP.setLayoutX((stageWidth - 200) * 0.5);
        confirmIP.setLayoutY((stageHeight + 75) * 0.5);
        confirmIP.setDefaultButton(true);
        confirmIP.setOnAction((_) -> tryConnect(IPField.getText()));

        connectionChoice.getChildren().add(IPField);
        connectionChoice.getChildren().add(confirmIP);
        connectionChoice.getChildren().add(socketButton);
        connectionChoice.getChildren().add(rmiButton);

        root.getChildren().add(connectionChoice);
    }

    private void tryConnect(String port){
        boolean connected = false;
        String errorMessage = "";
        try {
            int portNumber = Integer.parseInt(port);

            if (connectionType.equals("socket")){
                sender = ClientMain.startSocket(ip, portNumber, this);
                connected = true;
            }
            else if (connectionType.equals("rmi")){
                sender = ClientMain.startRMI(ip, portNumber, this);
                connected = true;
            }
            else {
                errorMessage = "Please choose a connection type!";
            }
        }
        catch (Exception e) {
            errorMessage = "Please enter a valid port number!";
        }

        if (connected) {
            sender.getMatches();
        }
        else {
            showError(errorMessage);
        }
    }

    private void drawMainMenuScene(Map<Integer, List<String>> matches){
        root.getChildren().clear();

        // Create main menu scene
        AnchorPane mainMenu = new AnchorPane();
        mainMenu.setPrefSize(stageWidth, stageHeight);
        mainMenu.setLayoutX(0);
        mainMenu.setLayoutY(0);

        // Create scroll rect
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(stageWidth - 50, stageHeight - 150);
        scrollPane.setLayoutX(25);
        scrollPane.setLayoutY(25);

        scrollPane.setPannable(true);

        int spacesNum = 0;

        for (Map.Entry<Integer, List<String>> match : matches.entrySet()){
            spacesNum += match.getValue().size() + 1;
        }

        VBox matchList = new VBox();
        matchList.setPrefSize(stageWidth - 50, spacesNum * 50);
        matchList.setLayoutX(0);
        matchList.setLayoutY(0);

        if (matches.isEmpty()) {
            Label noMatches = new Label("No matches available");
            noMatches.setPrefSize(stageWidth - 50, 50);
            noMatches.setFont(new Font(Font.getDefault().getName(), 24));
            noMatches.setLayoutX(0);
            noMatches.setLayoutY(0);

            matchList.getChildren().add(noMatches);
        }
        else{
            for (Map.Entry<Integer, List<String>> match : matches.entrySet()){
                Pane matchPane = createMatchPane(match.getKey(), match.getValue());
                matchList.getChildren().add(matchPane);
            }
        }

        // Set content of scroll pane (at the end)
        scrollPane.setContent(matchList);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        mainMenu.getChildren().add(scrollPane);

        // Create add match button
        Button addMatchButton = new Button("+ Add Match");
        addMatchButton.setPrefSize(200, 50);
        addMatchButton.setLayoutX((stageWidth - 200) * 0.5);
        addMatchButton.setLayoutY(stageHeight - 100);

        addMatchButton.setOnAction((_) -> { // event
            openCreateMatchWindow();
        });

        mainMenu.getChildren().add(addMatchButton);

        root.getChildren().add(mainMenu);
    }

    private Pane createMatchPane(int matchID, List<String> players){
        Pane matchPane = new Pane();
        matchPane.setPrefSize(stageWidth - 50, (players.size() + 1) * 50);

        VBox matchInfo = new VBox();
        matchInfo.setPrefSize(stageWidth - 100, (players.size() + 1) * 50);
        matchInfo.setLayoutX(50);

        Label matchName = new Label("Match " + matchID);
        matchName.setPrefSize(stageWidth - 100, 50);
        matchName.setFont(new Font(Font.getDefault().getName(), 24));

        matchInfo.getChildren().add(matchName);

        for (String player : players){
            Label playerLabel = new Label(player);
            playerLabel.setPrefSize(stageWidth - 100, 50);
            playerLabel.setFont(new Font(Font.getDefault().getName(), 18));

            matchInfo.getChildren().add(playerLabel);
        }

        Button selectButton = new Button();
        selectButton.setPrefSize(stageWidth - 50, (players.size() + 1) * 50);
        selectButton.setOpacity(0.1);

        selectButton.setOnAction((_) -> { // event
            sender.selectMatch(matchID);
        });

        matchPane.getChildren().add(matchInfo);
        matchPane.getChildren().add(selectButton);

        return matchPane;
    }

    private void openCreateMatchWindow(){
        root.getChildren().clear();

        AnchorPane createPane = new AnchorPane();
        createPane.setPrefSize(stageWidth, stageHeight);
        createPane.setLayoutX(0);
        createPane.setLayoutY(0);

        // Create text field for number of players
        TextField playersField = new TextField();
        playersField.setPrefSize(300, 50);
        playersField.setLayoutX((stageWidth - 300) * 0.5);
        playersField.setLayoutY(100);
        playersField.setPromptText("Enter number of players...");

        // Create text field for nickname
        TextField nameField = new TextField();
        nameField.setPrefSize(300, 50);
        nameField.setLayoutX((stageWidth - 300) * 0.5);
        nameField.setLayoutY(200);
        nameField.setPromptText("Enter nickname...");

        // Create confirm button
        Button confirmMatch = new Button("Create");
        confirmMatch.setPrefSize(150, 50);
        confirmMatch.setLayoutX((stageWidth - 150) * 0.5);
        confirmMatch.setLayoutY(300);
        confirmMatch.setDefaultButton(true);
        confirmMatch.setOnAction((_) -> {
            int numOfPlayers = playersField.getText().isEmpty() ? 0 : Integer.parseInt(playersField.getText());
            String nickname = nameField.getText();

            sender.createMatch(numOfPlayers, nickname);
        });

        createPane.getChildren().add(playersField);
        createPane.getChildren().add(nameField);
        createPane.getChildren().add(confirmMatch);

        root.getChildren().add(createPane);
    }

    private void openSelectNickWindow(List<String> players){
        root.getChildren().clear();

        AnchorPane joinPane = new AnchorPane();
        joinPane.setPrefSize(stageWidth, stageHeight);
        joinPane.setLayoutX(0);
        joinPane.setLayoutY(0);

        // Create text field for nickname
        TextField nameField = new TextField();
        nameField.setPrefSize(200, 50);
        nameField.setLayoutX((stageWidth - 200) * 0.5 - 50);
        nameField.setLayoutY(100);

        // Create confirm button
        Button confirmNick = new Button("Join");
        confirmNick.setPrefSize(150, 50);
        confirmNick.setLayoutX((stageWidth + 200) * 0.5 + 100);
        confirmNick.setLayoutY(100);
        confirmNick.setDefaultButton(true);
        confirmNick.setOnAction((_) -> { // event
            sender.chooseNickname(nameField.getText());
        });

        // Create IP label
        Label IPLabel = new Label("IP: 127.0.0.1");
        IPLabel.setPrefSize(200, 50);
        IPLabel.setLayoutX(10);
        IPLabel.setLayoutY(stageHeight - 60);

        // Create table of current players
        VBox playerList = new VBox();
        playerList.setPrefSize(stageWidth - 100, stageHeight - 300);
        playerList.setLayoutX(50);
        playerList.setLayoutY(150);

        for (String player : players){
            Label playerLabel = new Label(player);
            playerLabel.setPrefSize(stageWidth - 100, 50);
            playerLabel.setFont(new Font(Font.getDefault().getName(), 24));

            playerList.getChildren().add(playerLabel);
        }

        joinPane.getChildren().add(nameField);
        joinPane.getChildren().add(confirmNick);
        joinPane.getChildren().add(IPLabel);
        joinPane.getChildren().add(playerList);

        root.getChildren().add(joinPane);
    }

    private void selectNickname(String nick){
        root.getChildren().clear();

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(stageWidth, stageHeight);
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);

        Label waitingLabel = new Label("Waiting for other players...\nEntering match " + matchID + " with nick " + nick);
        waitingLabel.setPrefSize(stageWidth, stageHeight);
        waitingLabel.setLayoutX(50);
        waitingLabel.setLayoutY(50);
        waitingLabel.setFont(new Font(Font.getDefault().getName(), 32));

        anchorPane.getChildren().add(waitingLabel);

        root.getChildren().add(anchorPane);
    }

    private void drawGameStart(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                               int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                               int starterCardID){
        GameResources.initializeAllDecks();
        root.getChildren().clear();

        // Create player field
        setPlayArea();

        // Add common cards to the stage
        renderCommonCards(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);

        // Add board to the stage
        drawBoard();

        // Add vertical and horizontal lines to separate player field, hand and common view
        drawSeparationLines();

        // Draw starter card side choice
        renderStarterCardChoice(starterCardID);
    }

    private void setPlayArea(){
        cardWidth = (stageWidth - (9 * defaultElementsOffset)) / 7;
        cardHeight = cardWidth / 1.5;

        playerFieldRectWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
        playerFieldRectHeight = stageHeight - (2 * cardHeight) - (3 * defaultElementsOffset);

        // Calculate grid cell dimensions
        gridCellWidth = cardWidth - (cardCornerWidthProportion * cardWidth);
        gridCellHeight = cardHeight - (cardCornerHeightProportion * cardHeight);

        // Calculate number of rows and columns. I need odd numbers so that starter card is in the middle
        gridRows = (int) Math.floor(Math.ceil(playerFieldRectHeight / gridCellHeight) / 2) * 2 + 1;
        gridColumns = (int) Math.floor(Math.ceil(playerFieldRectWidth / gridCellWidth) / 2) * 2 + 1;

        // Calculate total grid dimensions
        gridWidth = gridCellWidth * gridColumns + 2 * cardCornerWidthProportion * cardWidth;
        gridHeight = gridCellHeight * gridRows + 2 * cardCornerHeightProportion * cardHeight;


        // Create scroll pane for player field
        ScrollPane playerFieldScroll = new ScrollPane();
        playerFieldScroll.setPrefSize(playerFieldRectWidth, playerFieldRectHeight);
        playerFieldScroll.setPannable(true);
        playerFieldScroll.setLayoutX(3 * (defaultElementsOffset + cardWidth) + 10);
        playerFieldScroll.setLayoutY(0);


        // Create player field anchor pane
        double playerFieldWidth = gridCellWidth * gridColumns + 2 * cardCornerWidthProportion * cardWidth;
        double playerFieldHeight = gridCellHeight * gridRows + 2 * cardCornerHeightProportion * cardHeight;

        playerField = new AnchorPane();
        playerField.setPrefSize(playerFieldWidth, playerFieldHeight);
        playerField.setLayoutX(0);
        playerField.setLayoutY(0);


        // Create grid for player field
        playerFieldGrid = new GridPane();

        playerFieldGrid.setPrefSize(gridWidth, gridHeight);
        playerFieldGrid.setLayoutX(cardCornerWidthProportion * cardWidth);
        playerFieldGrid.setLayoutY(cardCornerHeightProportion * cardHeight);

        playerFieldGrid.getColumnConstraints().add(new ColumnConstraints(gridCellWidth));
        playerFieldGrid.getRowConstraints().add(new RowConstraints(gridCellHeight));

        // I have to add an element on each column and row to make the grid "visible"
        for (int i = 0; i < gridColumns - 1; i++){
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefSize(gridCellWidth, gridCellHeight);

            playerFieldGrid.add(anchorPane, i, i);
        }

        // Create player hand's pane
        playerHandPane = new AnchorPane();

        double playerHandWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
        double playerHandHeight = (2 * cardHeight) + (3 * defaultElementsOffset);

        playerHandPane.setPrefSize(playerHandWidth, playerHandHeight);
        playerHandPane.setLayoutX(3 * (defaultElementsOffset + cardWidth) + 10);
        playerHandPane.setLayoutY(playerFieldRectHeight);

        // Create a temporary pane for starter card and secret objective choice
        tempChoicePane = new AnchorPane();
        tempChoicePane.setPrefSize(stageWidth, stageHeight);
        tempChoicePane.setLayoutX(0);
        tempChoicePane.setLayoutY(0);

        // Fill hierarchy of the scene
        playerField.getChildren().add(playerFieldGrid);

        playerFieldScroll.setContent(playerField);

        root.getChildren().add(playerFieldScroll);
        root.getChildren().add(playerHandPane);
    }

    private void renderStarterCardChoice(int starterCardID){
        this.starterCardID = starterCardID;

        Rectangle paneRect = new Rectangle();
        paneRect.setWidth(stageWidth);
        paneRect.setHeight(stageHeight);
        paneRect.setFill(Color.rgb(0, 0, 0, 0.3));

        // Create white background anchor pane
        AnchorPane backgroundPane = new AnchorPane();
        backgroundPane.setPrefSize(stageWidth * 0.5, stageHeight * 0.5);
        backgroundPane.setLayoutX(stageWidth * 0.25);
        backgroundPane.setLayoutY(stageHeight * 0.25);
        backgroundPane.setStyle("-fx-background-color: white;");
        backgroundPane.setOpacity(0.9);

        // Render starter card
        Pane starterCardPane = createCardPane(starterCardID, CardSideType.FRONT, (stageWidth - cardWidth) * 0.5, (stageHeight - cardHeight) * 0.5 - 100, 1);
        starterCardSide = CardSideType.FRONT;

        // Render a button to flip the card
        Button flipButton = new Button("Flip");
        flipButton.setPrefSize(100, 50);
        flipButton.setLayoutX((stageWidth - 100) * 0.5);
        flipButton.setLayoutY((stageHeight + cardHeight) * 0.5 - 75);
        flipButton.setOnAction((_) -> {
            // Flip the card
            flipStarterCard(starterCardPane);
        });

        // Render a button to place the starter card
        Button placeButton = new Button("Place");
        placeButton.setPrefSize(100, 50);
        placeButton.setLayoutX((stageWidth - 100) * 0.5);
        placeButton.setLayoutY((stageHeight + cardHeight) * 0.5);
        placeButton.setOnAction((_) -> {
            // Send the choice to the server
            sender.placeStarterCard(starterCardSide);
        });

        // Add all elements to the root
        tempChoicePane.getChildren().add(paneRect);
        tempChoicePane.getChildren().add(backgroundPane);
        tempChoicePane.getChildren().add(starterCardPane);
        tempChoicePane.getChildren().add(flipButton);
        tempChoicePane.getChildren().add(placeButton);

        root.getChildren().add(tempChoicePane);
    }

    private void flipStarterCard(Pane starterCardPane){
        if (starterCardSide.equals(CardSideType.BACK)){
            starterCardSide = CardSideType.FRONT;
        }
        else {
            starterCardSide = CardSideType.BACK;
        }

        Pane newPane = createCardPane(starterCardID, starterCardSide, starterCardPane.getLayoutX(), starterCardPane.getLayoutY(), 1);
        tempChoicePane.getChildren().add(newPane);

        tempChoicePane.getChildren().remove(starterCardPane);
    }

    private void drawBoard(){
        String boardName = "Board.png";
        Image boardImg = new Image(boardName);
        ImageView boardImgV = new ImageView(boardImg);

        boardImgV.setFitHeight(stageHeight - (2 * cardHeight) - (4 * defaultElementsOffset));
        boardImgV.setFitWidth(boardImgV.getFitHeight() * 0.5);

        boardImgV.setX(defaultElementsOffset);
        boardImgV.setY(defaultElementsOffset);

        root.getChildren().add(boardImgV);
    }

    private void renderCommonCards(int resDeckCardID, int resVisibleCard1ID, int resVisibleCard2ID,
                                   int goldDeckCardID, int goldVisibleCard1ID, int goldVisibleCard2ID){
        double resourceCardsY = stageHeight - (2 * cardHeight) - 2 * defaultElementsOffset;
        double goldenCardsY = stageHeight - cardHeight - defaultElementsOffset;

        Pane resDeckPane = createCardPane(resDeckCardID, CardSideType.BACK, defaultElementsOffset, resourceCardsY, 1);
        Pane resVisible1Pane = createCardPane(resVisibleCard1ID, CardSideType.FRONT, 2 * defaultElementsOffset + cardWidth + xOffsetByScale(0.8), resourceCardsY + yOffsetByScale(0.8), 0.8);
        Pane resVisible2Pane = createCardPane(resVisibleCard2ID, CardSideType.FRONT, 3 * defaultElementsOffset + 2 * cardWidth + xOffsetByScale(0.8), resourceCardsY + yOffsetByScale(0.8), 0.8);

        Pane goldDeckPane = createCardPane(goldDeckCardID, CardSideType.BACK, defaultElementsOffset, goldenCardsY, 1);
        Pane goldVisible1Pane = createCardPane(goldVisibleCard1ID, CardSideType.FRONT, 2 * defaultElementsOffset + cardWidth + xOffsetByScale(0.8), goldenCardsY + yOffsetByScale(0.8), 0.8);
        Pane goldVisible2Pane = createCardPane(goldVisibleCard2ID, CardSideType.FRONT, 3 * defaultElementsOffset + 2 * cardWidth + xOffsetByScale(0.8), goldenCardsY + yOffsetByScale(0.8), 0.8);

        // Add draw buttons to cards
        addDrawButtonToCard(resDeckPane, resDeckCardID, "deck");
        addDrawButtonToCard(resVisible1Pane, resVisibleCard1ID, "visible");
        addDrawButtonToCard(resVisible2Pane, resVisibleCard2ID, "visible");

        addDrawButtonToCard(goldDeckPane, goldDeckCardID, "deck");
        addDrawButtonToCard(goldVisible1Pane, goldVisibleCard1ID, "visible");
        addDrawButtonToCard(goldVisible2Pane, goldVisibleCard2ID, "visible");

        // Add cards to the stage
        root.getChildren().add(resDeckPane);
        root.getChildren().add(resVisible1Pane);
        root.getChildren().add(resVisible2Pane);

        root.getChildren().add(goldDeckPane);
        root.getChildren().add(goldVisible1Pane);
        root.getChildren().add(goldVisible2Pane);
    }

    private void drawMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID,
                                  TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2,
                                  int secretObjectiveCardID1, int secretObjectiveCardID2){
        // Create player hand
        drawPlayerHand(resourceCardID1, resourceCardID2, goldenCardID, TokenColors.getColorFromToken(tokenColor));

        // Create common objectives
        renderCommonObjectives(commonObjectiveCardID1, commonObjectiveCardID2);

        // Render secret objective choice
        renderSecretObjectiveChoice(secretObjectiveCardID1, secretObjectiveCardID2);
    }

    private void renderCommonObjectives(int objective1ID, int objective2ID){
        Pane objective1Pane = createCardPane(objective1ID, CardSideType.FRONT, 200, 100, 1);
        Pane objective2Pane = createCardPane(objective2ID, CardSideType.FRONT, 200, 300, 1);

        root.getChildren().add(objective1Pane);
        root.getChildren().add(objective2Pane);
    }

    private void renderSecretObjectiveChoice(int secretObjective1ID, int secretObjective2ID){
        Rectangle paneRect = new Rectangle();
        paneRect.setWidth(stageWidth);
        paneRect.setHeight(stageHeight);
        paneRect.setFill(Color.rgb(0, 0, 0, 0.2));

        // Create white background anchor pane
        AnchorPane backgroundPane = new AnchorPane();
        backgroundPane.setPrefSize(stageWidth * 0.5, stageHeight * 0.5);
        backgroundPane.setLayoutX(stageWidth * 0.25);
        backgroundPane.setLayoutY(stageHeight * 0.25);
        backgroundPane.setStyle("-fx-background-color: white;");
        backgroundPane.setOpacity(0.9);

        // Create secret objective panes
        Pane secretObjective1Pane = createCardPane(secretObjective1ID, CardSideType.FRONT, stageWidth * 0.5 - cardWidth - 50, (stageHeight - cardHeight) * 0.5, 1);
        Pane secretObjective2Pane = createCardPane(secretObjective2ID, CardSideType.FRONT, stageWidth * 0.5 + 50, (stageHeight - cardHeight) * 0.5, 1);

        // Create buttons to select secret objectives
        Button secretObjective1Button = new Button();
        secretObjective1Button.setPrefSize(cardWidth, cardHeight);
        secretObjective1Button.setLayoutX(0);
        secretObjective1Button.setLayoutY(0);
        secretObjective1Button.setOnAction((_) -> {
            secretObjectiveCardID = secretObjective1ID;
            sender.chooseSecretObjective(secretObjectiveCardID);
        });
        secretObjective1Button.setOpacity(0.1);

        // Create button to confirm choice
        Button secretObjective2Button = new Button();
        secretObjective2Button.setPrefSize(cardWidth, cardHeight);
        secretObjective2Button.setLayoutX(0);
        secretObjective2Button.setLayoutY(0);
        secretObjective2Button.setOnAction((_) -> {
            secretObjectiveCardID = secretObjective2ID;
            sender.chooseSecretObjective(secretObjectiveCardID);
        });
        secretObjective2Button.setOpacity(0.1);

        // Add everything to the root
        secretObjective1Pane.getChildren().add(secretObjective1Button);
        secretObjective2Pane.getChildren().add(secretObjective2Button);

        tempChoicePane.getChildren().add(paneRect);
        tempChoicePane.getChildren().add(backgroundPane);
        tempChoicePane.getChildren().add(secretObjective1Pane);
        tempChoicePane.getChildren().add(secretObjective2Pane);
    }

    private void addDrawButtonToCard(Pane cardPane, int cardID, String commonCardType){
        Button drawButton = new Button();
        drawButton.setPrefSize(cardPane.getPrefWidth(), cardPane.getPrefHeight());
        drawButton.setLayoutX(0);
        drawButton.setLayoutY(0);
        drawButton.setOpacity(0.1);

        drawButton.setOnAction((_) -> {
            // Check if you can draw card
            if (playerHand.size() < 3){
                // Add card to hand and render it in the right spot
                playerHand.add(cardID);
                Pane drawnCard = createCardPane(cardID, CardSideType.FRONT, lastPlacedCardX, lastPlacedCardY, 1);
                addSelectButtonToCard(drawnCard, cardID);
                playerHandPane.getChildren().add(drawnCard);

                // Draw new card and render it on common cards' place
                double newCardScale = 0.8;
                CardSideType newCardSide = CardSideType.FRONT;
                if (commonCardType.equals("deck")){
                    newCardScale = 1;
                    newCardSide = CardSideType.BACK;
                }

                Random rng = new Random();
                int newCardID = rng.nextInt(80);

                Pane newCardPane = createCardPane(newCardID, newCardSide, cardPane.getLayoutX(), cardPane.getLayoutY(), newCardScale);
                addDrawButtonToCard(newCardPane, newCardID, commonCardType);
                root.getChildren().add(newCardPane);

                // Remove card from common cards' place
                root.getChildren().remove(cardPane);
            }
            else {
                showError("Cannot draw more cards!");
            }
        });

        cardPane.getChildren().add(drawButton);
    }

    private Pane createCardPane(int cardID, CardSideType side, double xPos, double yPos, double scale){
        // Create Pane for card
        Pane cardPane = new Pane();

        cardPane.setPrefSize(cardWidth * scale, cardHeight * scale);
        cardPane.setLayoutX(xPos);
        cardPane.setLayoutY(yPos);

        // Create Image
        String cardName = "/cards/" + side.toString().toLowerCase() + "/card" + cardID + ".png";

        Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(cardName)));
        ImageView cardImgV = new ImageView(cardImg);

        cardImgV.setFitHeight(cardHeight * scale);
        cardImgV.setFitWidth(cardWidth * scale);

        cardImgV.setX(0);
        cardImgV.setY(0);

        cardPane.getChildren().add(cardImgV);

        return cardPane;
    }

    private double xOffsetByScale(double scale){
        return (1 - scale) * cardWidth / 2;
    }

    private double yOffsetByScale(double scale){
        return (1 - scale) * cardHeight / 2;
    }

    private void addPlaceButtons(Pane cardPane, int ID, Position pos){
        PlaceableCard card = GameResources.getPlaceableCardByID(ID);

        Position blPos = new Position(pos.getX() - 1, pos.getY() - 1);
        Position brPos = new Position(pos.getX() + 1, pos.getY() - 1);
        Position tlPos = new Position(pos.getX() - 1, pos.getY() + 1);
        Position trPos = new Position(pos.getX() + 1, pos.getY() + 1);

        if (card.getFront().getBLCorner().isAvailable() && !illegalPosList.contains(blPos) && !validPosToButtonPane.containsKey(blPos)){
            Pane blPane = new Pane();
            blPane.setPrefSize(gridCellWidth, gridCellHeight);

            Button blButton = new Button();
            blButton.setPrefSize(cardWidth, cardHeight);
            blButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(-cardWidth * (1 - cardCornerWidthProportion));
            blButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(cardHeight - cardHeight * cardCornerHeightProportion);
            blButton.setOpacity(0.6);
            blButton.setOnAction((_) -> {
                if (playerHand.size() == 3 && selectedCardID >= 0){
                    // Place the card
                    placeCard(selectedCardID, CardSideType.FRONT, blPos);

                    // Remove the button
                    cardPane.getChildren().remove(blButton);

                    // Remove the card from the hand view
                    playerHandPane.getChildren().remove(selectedCardPane);
                }
                else {
                    showError("Cannot place a card!");
                }
            });

            blPane.getChildren().add(blButton);
            Position buttonGridPos = mapToGridPos(blPos);
            playerFieldGrid.add(blPane, buttonGridPos.getX(), buttonGridPos.getY());
            validPosToButtonPane.put(blPos, blPane);
        }
        if (card.getFront().getBRCorner().isAvailable() && !illegalPosList.contains(brPos) && !validPosToButtonPane.containsKey(brPos)){
            Pane brPane = new Pane();
            brPane.setPrefSize(gridCellWidth, gridCellHeight);

            Button brButton = new Button();
            brButton.setPrefSize(cardWidth, cardHeight);
            brButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(cardWidth * (1 - cardCornerWidthProportion));
            brButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(cardHeight - cardHeight * cardCornerHeightProportion);
            brButton.setOpacity(0.6);
            brButton.setOnAction((_) -> {
                if (playerHand.size() == 3 && selectedCardID >= 0){
                    // Place the card
                    placeCard(selectedCardID, CardSideType.FRONT, brPos);

                    // Remove the button
                    cardPane.getChildren().remove(brButton);

                    // Remove the card from the hand view
                    playerHandPane.getChildren().remove(selectedCardPane);
                }
                else {
                    showError("Cannot place a card!");
                }
            });

            brPane.getChildren().add(brButton);
            Position buttonGridPos = mapToGridPos(brPos);
            playerFieldGrid.add(brPane, buttonGridPos.getX(), buttonGridPos.getY());
            validPosToButtonPane.put(brPos, brPane);
        }
        if (card.getFront().getTLCorner().isAvailable() && !illegalPosList.contains(tlPos) && !validPosToButtonPane.containsKey(tlPos)){
            Pane tlPane = new Pane();
            tlPane.setPrefSize(gridCellWidth, gridCellHeight);

            Button tlButton = new Button();
            tlButton.setPrefSize(cardWidth, cardHeight);
            tlButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(-cardWidth * (1 - cardCornerWidthProportion));
            tlButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(-cardHeight * (1 - cardCornerHeightProportion));
            tlButton.setOpacity(0.6);
            tlButton.setOnAction((_) -> {
                if (playerHand.size() == 3 && selectedCardID >= 0){
                    // Place the card
                    placeCard(selectedCardID, CardSideType.FRONT, tlPos);

                    // Remove the button
                    cardPane.getChildren().remove(tlButton);

                    // Remove the card from the hand view
                    playerHandPane.getChildren().remove(selectedCardPane);
                }
                else {
                    showError("Cannot place a card!");
                }
            });

            tlPane.getChildren().add(tlButton);
            Position buttonGridPos = mapToGridPos(tlPos);
            playerFieldGrid.add(tlPane, buttonGridPos.getX(), buttonGridPos.getY());
            validPosToButtonPane.put(tlPos, tlPane);
        }
        if (card.getFront().getTRCorner().isAvailable() && !illegalPosList.contains(trPos) && !validPosToButtonPane.containsKey(trPos)){
            Pane trPane = new Pane();
            trPane.setPrefSize(gridCellWidth, gridCellHeight);

            Button trButton = new Button();
            trButton.setPrefSize(cardWidth, cardHeight);
            trButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(cardWidth - cardWidth * cardCornerWidthProportion);
            trButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(-cardHeight * (1 - cardCornerHeightProportion));
            trButton.setOpacity(0.6);
            trButton.setOnAction((_) -> {
                if (playerHand.size() == 3 && selectedCardID >= 0){
                    // Place the card
                    placeCard(selectedCardID, CardSideType.FRONT, trPos);

                    // Remove the button
                    cardPane.getChildren().remove(trButton);

                    // Remove the card from the hand view
                    playerHandPane.getChildren().remove(selectedCardPane);
                }
                else {
                    showError("Cannot place a card!");
                }
            });

            trPane.getChildren().add(trButton);
            Position buttonGridPos = mapToGridPos(trPos);
            playerFieldGrid.add(trPane, buttonGridPos.getX(), buttonGridPos.getY());
            validPosToButtonPane.put(trPos, trPane);
        }
    }

    // TODO: Fix currSide. Adapt to card placed side
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

            if (validPosToButtonPane.containsKey(p)){
                playerFieldGrid.getChildren().remove(validPosToButtonPane.get(p));
                validPosToButtonPane.remove(p);
            }
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

    private Position mapToGridPos(Position mapPos){
        return new Position(mapPos.getX() + gridRows / 2, gridColumns / 2 - mapPos.getY());
    }

    private void placeCard(int ID, CardSideType side, Position pos){
        if (pos.getX() <= -gridColumns / 2 || pos.getX() >= gridColumns / 2 ||
                pos.getY() <= -gridRows / 2 || pos.getY() >= gridRows / 2){
            updateGridDimension();
        }

        // Update last placed card position in hand
        lastPlacedCardX = selectedCardX;
        lastPlacedCardY = selectedCardY;

        // Remove card from hand
        playerHand.remove(Integer.valueOf(ID));

        // Create Image
        String cardName = "/cards/" + side.toString().toLowerCase() + "/card" + ID + ".png";

        if (!playArea.containsKey(pos)){
            playArea.put(pos, ID);
        }

        updateIlLegalPositions(pos, side);

        // Create Pane for card
        Pane cardPane = new Pane();
        cardPane.setPrefSize(gridCellWidth, gridCellHeight);

        Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(cardName)));
        ImageView cardImgV = new ImageView(cardImg);

        cardImgV.setFitHeight(cardHeight);
        cardImgV.setFitWidth(cardWidth);

        cardImgV.setX(-cardWidth * cardCornerWidthProportion);
        cardImgV.setY(-cardHeight * cardCornerHeightProportion);

        cardPane.getChildren().add(cardImgV);

        addPlaceButtons(cardPane, ID, pos);

        Position gridPos = mapToGridPos(pos);

        // Add card to the grid
        playerFieldGrid.add(cardPane, gridPos.getX(), gridPos.getY());
    }

    private void updateGridDimension(){
        gridRows += 2;
        gridColumns += 2;

        playerFieldGrid.getChildren().clear();

        gridWidth = gridCellWidth * gridColumns + 2 * cardCornerWidthProportion * cardWidth;
        gridHeight = gridCellHeight * gridRows + 2 * cardCornerHeightProportion * cardHeight;

        playerFieldGrid.setPrefSize(gridWidth, gridHeight);

        for (int i = 0; i < gridColumns - 1; i++){
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefSize(gridCellWidth, gridCellHeight);

            playerFieldGrid.add(anchorPane, i, i);
        }

        legalPosList.clear();
        illegalPosList.clear();
        validPosToButtonPane.clear();

        for (Position p : playArea.keySet()){
            placeCard(playArea.get(p), CardSideType.FRONT, p);
        }
    }

    private void drawPlayerHand(int card1ID, int card2ID, int card3ID, Color playerColor){
        playerHand.add(card1ID);
        playerHand.add(card2ID);
        playerHand.add(card3ID);

        double playerHandWidth = playerHandPane.getPrefWidth();
        double playerHandHeight = playerHandPane.getPrefHeight();

        // Create panes for each card
        double cardsYPos = (playerHandHeight - cardHeight) * 0.5;

        Pane card1Pane = createCardPane(card1ID, CardSideType.FRONT, 2 * defaultElementsOffset + cardWidth, cardsYPos, 1);
        Pane card2Pane = createCardPane(card2ID, CardSideType.FRONT, 3 * defaultElementsOffset + 2 * cardWidth, cardsYPos, 1);
        Pane card3Pane = createCardPane(card3ID, CardSideType.FRONT, 4 * defaultElementsOffset + 3 * cardWidth, cardsYPos, 1);

        // Add select buttons to cards
        addSelectButtonToCard(card1Pane, card1ID);
        addSelectButtonToCard(card2Pane, card2ID);
        addSelectButtonToCard(card3Pane, card3ID);

        // Add rectangle to highlight player color
        Rectangle plRect = new Rectangle();
        plRect.setStrokeWidth(5);
        plRect.setX(3);
        plRect.setY(3);
        plRect.setWidth(playerHandWidth - 5);
        plRect.setHeight(playerHandHeight - 2);
        plRect.setFill(Color.TRANSPARENT);
        plRect.setStroke(playerColor);

        playerHandPane.getChildren().add(plRect);

        // Add cards to stage
        playerHandPane.getChildren().add(card1Pane);
        playerHandPane.getChildren().add(card2Pane);
        playerHandPane.getChildren().add(card3Pane);
    }

    private void addSelectButtonToCard(Pane cardPane, int cardID){
        Button selectButton = new Button();
        selectButton.setPrefSize(cardPane.getPrefWidth(), cardPane.getPrefHeight());
        selectButton.setLayoutX(0);
        selectButton.setLayoutY(0);
        selectButton.setOpacity(0.1);

        selectButton.setOnAction((_) -> {
            if (selectedCardPane != cardPane){
                if (selectedCardPane != null){
                    selectedCardPane.setLayoutY(selectedCardPane.getLayoutY() + selectedCardOffset);
                }
                selectedCardID = cardID;
                selectedCardX = cardPane.getLayoutX();
                selectedCardY = cardPane.getLayoutY();
                selectedCardPane = cardPane;
                cardPane.setLayoutY(cardPane.getLayoutY() - selectedCardOffset);
            }
        });

        cardPane.getChildren().add(selectButton);
    }

    private void showError(String errorMessage){
        AnchorPane errorPane = new AnchorPane();
        errorPane.setPrefSize(stageWidth - 20, 50);
        errorPane.setLayoutX(10);
        errorPane.setLayoutY(stageHeight - 50);

        Rectangle errorRect = new Rectangle();
        errorRect.setX(0);
        errorRect.setY(0);
        errorRect.setWidth(stageWidth - 20);
        errorRect.setHeight(50);
        errorRect.setFill(Color.GRAY);
        errorRect.setStroke(Color.RED);

        Label errorLabel = new Label(errorMessage);
        errorLabel.setPrefSize(stageWidth - 20, 50);
        errorLabel.setLayoutX(0);
        errorLabel.setLayoutY(0);
        errorLabel.setFont(new Font(Font.getDefault().getName(), 24));
        errorLabel.setTextFill(Color.RED);

        errorPane.getChildren().add(errorRect);
        errorPane.getChildren().add(errorLabel);

        root.getChildren().add(errorPane);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> root.getChildren().remove(errorPane));
            }
        }, 4000);
    }

    private void drawSeparationLines(){
        Line lineV = new Line();
        lineV.setStartX(3 * (defaultElementsOffset + cardWidth) + 10);
        lineV.setStartY(0);
        lineV.setEndX(3 * (defaultElementsOffset + cardWidth) + 10);
        lineV.setEndY(stageHeight + 5);
        lineV.setStrokeWidth(3);

        Line lineH = new Line();
        lineH.setStartX(3 * (defaultElementsOffset + cardWidth) + 10);
        lineH.setStartY(playerFieldRectHeight);
        lineH.setEndX(stageWidth + 5);
        lineH.setEndY(playerFieldRectHeight);
        lineH.setStrokeWidth(3);

        root.getChildren().add(lineV);
        root.getChildren().add(lineH);
    }

    // TODO: Is zooming ok? (panel dimensions problem)
    void keyPressed(KeyEvent key){
        if (key.getCode() == KeyCode.CONTROL) {
            pressingCtrl = true;
        }

        if (!pressingCtrl){
            return;
        }

        switch (key.getCode()){
            case UP:
                if (zoomLevel < maxZoom){
                    playerField.setScaleX(playerField.getScaleX() + 0.05);
                    playerField.setScaleY(playerField.getScaleY() + 0.05);
                    zoomLevel += 0.05;
                }
                break;
            case DOWN:
                if (zoomLevel > minZoom){
                    playerField.setScaleX(playerField.getScaleX() - 0.05);
                    playerField.setScaleY(playerField.getScaleY() - 0.05);
                    zoomLevel -= 0.05;
                }
                break;
        }
    }

    void keyReleased(KeyEvent key){
        if (Objects.requireNonNull(key.getCode()) == KeyCode.CONTROL) {
            pressingCtrl = false;
        }
    }

    @Override
    public void showMatches(Map<Integer, List<String>> matches) {
        Platform.runLater(() -> drawMainMenuScene(matches));
    }

    @Override
    public void createMatch(int matchID, String hostNickname) {
        this.matchID = matchID;

        // Create window for waiting for other players
        Platform.runLater(() -> selectNickname(hostNickname));
    }

    @Override
    public void selectMatch(int matchID, List<String> nicknames) {
        this.matchID = matchID;

        // Create window for choosing nickname
        Platform.runLater(() -> openSelectNickWindow(nicknames));
    }

    @Override
    public void chooseNickname(String nickname) {
        // Create window for waiting for other players
        Platform.runLater(() -> selectNickname(nickname));
    }

    @Override
    public void startGame(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) {
        Platform.runLater(() -> drawGameStart(resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, starterCardID));

//        stage.widthProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
//            root.getChildren().clear();
//            updateWindow();
//        });
//        stage.heightProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
//            root.getChildren().clear();
//            updateWindow();
//        });

        // Add events for zooming
        stage.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
        stage.addEventHandler(KeyEvent.KEY_RELEASED, this::keyReleased);
    }

    @Override
    public void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                                int commonObjectiveCardID1, int commonObjectiveCardID2,
                                int secretObjectiveCardID1, int secretObjectiveCardID2) {
        Platform.runLater(() -> {
            // Clear temp choice pane and place starter card before proceeding
            tempChoicePane.getChildren().clear();

            playerHand.add(resourceCardID1);
            playerHand.add(resourceCardID2);
            playerHand.add(goldenCardID);

            placeCard(starterCardID, starterCardSide, new Position(0, 0));

            drawMissingSetUp(resourceCardID1, resourceCardID2, goldenCardID, tokenColor, commonObjectiveCardID1, commonObjectiveCardID2, secretObjectiveCardID1, secretObjectiveCardID2);
        });
    }

    @Override
    public void confirmSecretObjective() {
        Platform.runLater(() -> {
            tempChoicePane.getChildren().clear();

            // Place secret objective in player hand
            double cardsYPos = (playerHandPane.getPrefHeight() - cardHeight) * 0.5;
            Pane secretObjectivePane = createCardPane(secretObjectiveCardID, CardSideType.FRONT, defaultElementsOffset + xOffsetByScale(0.9), cardsYPos + yOffsetByScale(0.9), 0.9);

            playerHandPane.getChildren().add(secretObjectivePane);
        });
    }

    @Override
    public void errorMessage(String errorMessage) {
        Platform.runLater(() -> showError(errorMessage));
    }

    @Override
    public void showNewPlayer(String nicknames) {

    }

    @Override
    public void showFirstPlayerTurn(int playerID, String playerNickname) {

    }

    @Override
    public void placeCard() {

    }

    @Override
    public void showNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID, String playerNickname) {

    }

    @Override
    public void updateDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2, int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {

    }

    @Override
    public void setID(int playerID) {
        sender.setPlayerID(playerID);
    }
}
