package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.game.GameState;
import com.example.pf_soft_ing.player.TokenColors;
import javafx.application.Platform;
import javafx.geometry.Pos;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class GUIView implements View {

    private final List<Position> legalPosList = new ArrayList<>();
    private final List<Position> illegalPosList = new ArrayList<>();

    private final Map<Position, Pane> validPosToButtonPane = new HashMap<>();

    private final List<PlayerViewModel> opponents = new ArrayList<>();
    private int matchID;

    private int playerID;
    private String nickname;
    private TokenColors tokenColor;
    private int score = 0;

    private int priority = 0;

    private final List<PlaceableCard> playerHand = new ArrayList<>();

    private final Map<Position, PlaceableCard> playArea = new HashMap<>();

    private final List<Shape> playersCircles = new ArrayList<>();
    private final List<Shape> playersRectangles = new ArrayList<>();

    private final List<String> chatMessages = new ArrayList<>();
    private VBox chatBox;
    private double chatBoxHeight = 0;

    private PlaceableCard starterCard;

    private int secretObjectiveCardID;

    private String connectionType;

    private boolean chatOpen = false;
    private AnchorPane chatPane;

    private int selectedCardIndex;

     private PlaceableCard selectedCard;

    private int placedCardIndexInHand;

    private Pane selectedCardPane;

    private int commonResDeckCardID;
    private int commonVisibleResCardID1;
    private int commonVisibleResCardID2;
    private int commonGoldDeckCardID;
    private int commonVisibleGoldCardID1;
    private int commonVisibleGoldCardID2;

    private Pane commonResDeckCardPane;
    private Pane commonVisibleResCard1Pane;
    private Pane commonVisibleResCard2Pane;
    private Pane commonGoldDeckCardPane;
    private Pane commonVisibleGoldCard1Pane;
    private Pane commonVisibleGoldCard2Pane;

    private Label currPlayerTurnLabel;

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

    private double commonAreaWidth;

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
    private ScrollPane playerFieldScroll;
    private AnchorPane playerField;
    private GridPane playerFieldGrid;
    private AnchorPane playerHandParentPane;
    private AnchorPane playerHandPane;
    private AnchorPane tempChoicePane;
    private AnchorPane commonAreaPane;
    private AnchorPane helpPane;
    private AnchorPane endgameMapsPane;

    private final Map<String, Label> nicknameToScoreLabel = new HashMap<>();

    private final String ip;
    private ClientSender sender;

    private final Stage stage;

    public GUIView(Stage stage, String ip) {
        this.stage = stage;
        this.ip = ip;

        launchApp();
    }

    private void launchApp(){
        stage.setTitle("Codex Naturalis");
        root = new Group();
        stage.setScene(new Scene(root));
        stage.setWidth(1721);
        stage.setHeight(926);
        stage.show();

        stageWidth = stage.getWidth() - 15; // Don't ask me why 15
        stageHeight = stage.getHeight() - barOffset;

        stage.setMaximized(true);

//        testShowRanking();
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
        addMatchButton.setOnAction((_) -> openCreateMatchWindow());

        // Create button to refresh matches
        Button refreshButton = new Button("Refresh");
        refreshButton.setPrefSize(200, 50);
        refreshButton.setLayoutX(200);
        refreshButton.setLayoutY(stageHeight - 100);
        refreshButton.setOnAction((_) -> sender.getMatches());

        mainMenu.getChildren().add(refreshButton);
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

    private void drawGameStart(Map<Integer, String> IDToNicknameMap,
                               int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                               int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                               int starterCardID){
        GameResources.initializeAllDecks();

        root.getChildren().clear();

        for (Map.Entry<Integer, String> entry : IDToNicknameMap.entrySet()){
            if (entry.getKey() != playerID){
                PlayerViewModel opponent = new PlayerViewModel();
                opponent.setPlayerID(entry.getKey());
                opponent.setNickname(entry.getValue());

                opponents.add(opponent);
            }
            else {
                nickname = entry.getValue();
            }
        }

        // Calculate all dimensions and creates all panes needed for the game
        setPlayArea();

        // Add common cards to the stage
        renderCommonCards(resDeckCardID, visibleResCardID1, visibleResCardID2, goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);

        // Add vertical and horizontal lines to separate player field, hand and common view
        drawSeparationLines();

        // Draw starter card side choice
        renderStarterCardChoice(starterCardID);
    }

    private void setPlayArea(){
        cardWidth = (stageWidth - (9 * defaultElementsOffset)) / 7;
        cardHeight = cardWidth / 1.5;

        commonAreaWidth = 3 * (defaultElementsOffset + cardWidth) + 10;

        playerFieldRectWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
        playerFieldRectHeight = stageHeight - (2 * cardHeight) - (3 * defaultElementsOffset);

        // Calculate grid cell dimensions
        gridCellWidth = cardWidth - (cardCornerWidthProportion * cardWidth);
        gridCellHeight = cardHeight - (cardCornerHeightProportion * cardHeight);

        // Calculate number of rows and columns. I need odd numbers so that starter card is in the middle
        gridRows = (int) Math.floor(Math.ceil(playerFieldRectHeight / gridCellHeight) / 2) * 2 + 1;
        gridColumns = (int) Math.floor(Math.ceil(playerFieldRectWidth / gridCellWidth) / 2) * 2 + 1;

        // Calculate total grid dimensions
        gridWidth = gridCellWidth * gridColumns + cardCornerWidthProportion * cardWidth;
        gridHeight = gridCellHeight * gridRows + cardCornerHeightProportion * cardHeight;

        // region Player field
        // Create scroll pane for player field
        playerFieldScroll = new ScrollPane();
        playerFieldScroll.setPrefSize(playerFieldRectWidth, playerFieldRectHeight);
        playerFieldScroll.setPannable(true);
        playerFieldScroll.setLayoutX(commonAreaWidth);
        playerFieldScroll.setLayoutY(0);

        // Create player field anchor pane
        double playerFieldWidth = gridCellWidth * gridColumns + 3 * cardCornerWidthProportion * cardWidth; // gridCellWidth * gridColumns + 2 * cardCornerWidthProportion * cardWidth;
        double playerFieldHeight = gridCellHeight * gridRows + 3 * cardCornerHeightProportion * cardHeight; // gridCellHeight * gridRows + 2 * cardCornerHeightProportion * cardHeight;

        playerField = new AnchorPane();
        playerField.setPrefSize(playerFieldWidth, playerFieldHeight);
        playerField.setLayoutX(0);
        playerField.setLayoutY(0);

        // Create grid for player field
        playerFieldGrid = new GridPane();
        playerFieldGrid.setPrefSize(gridWidth, gridHeight);
        playerFieldGrid.setLayoutX(2 * cardCornerWidthProportion * cardWidth);
        playerFieldGrid.setLayoutY(2 * cardCornerHeightProportion * cardHeight);

        playerFieldGrid.getColumnConstraints().add(new ColumnConstraints(gridCellWidth));
        playerFieldGrid.getRowConstraints().add(new RowConstraints(gridCellHeight));

        // I have to add an element on each column and row to make the grid "visible"
        for (int i = 0; i < gridColumns - 1; i++){
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefSize(gridCellWidth, gridCellHeight);

            playerFieldGrid.add(anchorPane, i, i);
        }

        // Add player field to root
        playerField.getChildren().add(playerFieldGrid);

        playerFieldScroll.setContent(playerField);

        root.getChildren().add(playerFieldScroll);
        // endregion

        // region Player hand
        // Create player hand's pane
        playerHandParentPane = new AnchorPane();
        playerHandPane = new AnchorPane();

        double playerHandWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
        double playerHandHeight = (2 * cardHeight) + (3 * defaultElementsOffset);

        playerHandParentPane.setPrefSize(playerHandWidth, playerHandHeight);
        playerHandParentPane.setLayoutX(3 * (defaultElementsOffset + cardWidth) + 10);
        playerHandParentPane.setLayoutY(playerFieldRectHeight);

        playerHandPane.setPrefSize(playerHandWidth, playerHandHeight);
        playerHandPane.setLayoutX(0);
        playerHandPane.setLayoutY(0);

        playerHandParentPane.getChildren().add(playerHandPane);
        root.getChildren().add(playerHandParentPane);
        // endregion

        // region Temp choice pane
        // Create a temporary pane for starter card and secret objective choice
        tempChoicePane = new AnchorPane();
        tempChoicePane.setPrefSize(3 * (defaultElementsOffset + cardWidth) + 10, stageHeight);
        tempChoicePane.setLayoutX(0);
        tempChoicePane.setLayoutY(0);
        // endregion

        // region Common area
        // Create common area pane
        commonAreaPane = new AnchorPane();
        commonAreaPane.setPrefSize(commonAreaWidth, stageHeight);
        commonAreaPane.setLayoutX(0);
        commonAreaPane.setLayoutY(0);

        // Create label for current player's turn
        currPlayerTurnLabel = new Label("Current player: None");
        currPlayerTurnLabel.setPrefSize(commonAreaWidth, 50);
        currPlayerTurnLabel.setLayoutX(50);
        currPlayerTurnLabel.setLayoutY(0);

        commonAreaPane.getChildren().add(currPlayerTurnLabel);

        // Create chat area
        Button openChatButton = new Button(">");
        openChatButton.setPrefSize(50, 100);
        openChatButton.setLayoutX(0);
        openChatButton.setLayoutY(stageHeight / 2 - 50);
        openChatButton.setOnAction((_) -> openCloseChat());

        commonAreaPane.getChildren().add(openChatButton);

        // Create chat pane
        createChatPane();

        // Create other player's section
        // createOtherPlayerSection();

        // Create help button
        createHelpSection();

        root.getChildren().add(commonAreaPane);
        // endregion
    }

    private void createHelpSection() {
        helpPane = new AnchorPane();
        helpPane.setPrefSize(stageWidth, stageHeight);
        helpPane.setLayoutX(0);
        helpPane.setLayoutY(0);

        // Black background
        Rectangle backgroundRect = new Rectangle();
        backgroundRect.setWidth(stageWidth);
        backgroundRect.setHeight(stageHeight);
        backgroundRect.setFill(Color.rgb(0, 0, 0, 0.3));

        // White foreground
        AnchorPane helpBackground = new AnchorPane();
        helpBackground.setPrefSize(stageWidth * 0.5, stageHeight * 0.5);
        helpBackground.setLayoutX(stageWidth * 0.25);
        helpBackground.setLayoutY(stageHeight * 0.25);
        helpBackground.setOpacity(0.9);

        // Black background
        Rectangle foregroundRect = new Rectangle();
        foregroundRect.setWidth(stageWidth * 0.5);
        foregroundRect.setHeight(stageHeight * 0.5);
        foregroundRect.setFill(Color.rgb(255, 255, 255, 1));

        // Create title label
        Label helpLabel = new Label("Help");
        helpLabel.setPrefSize(stageWidth * 0.5, 50);
        helpLabel.setLayoutX(0);
        helpLabel.setLayoutY(0);
        helpLabel.setFont(new Font(Font.getDefault().getName(), 24));
        helpLabel.setAlignment(Pos.CENTER);

        // Create global chat help label
        Label globalChatHelpLabel = new Label("Global chat: Type a message and press enter to send it to all players.");
        globalChatHelpLabel.setPrefSize(stageWidth * 0.5, 50);
        globalChatHelpLabel.setLayoutX(0);
        globalChatHelpLabel.setLayoutY(100);
        globalChatHelpLabel.setFont(new Font(Font.getDefault().getName(), 18));
        globalChatHelpLabel.setWrapText(true);
        globalChatHelpLabel.setAlignment(Pos.CENTER);

        // Create private chat help label
        Label privateChatHelpLabel = new Label("Private chat: Type /whisper <player> <message> to send a private message to a player.");
        privateChatHelpLabel.setPrefSize(stageWidth * 0.5, 50);
        privateChatHelpLabel.setLayoutX(0);
        privateChatHelpLabel.setLayoutY(175);
        privateChatHelpLabel.setFont(new Font(Font.getDefault().getName(), 18));
        privateChatHelpLabel.setWrapText(true);
        privateChatHelpLabel.setAlignment(Pos.CENTER);

        // Create zoom help label
        Label zoomHelpLabel = new Label("Zoom: Hold the Ctrl key and press arrow key up or down to zoom in or out.");
        zoomHelpLabel.setPrefSize(stageWidth * 0.5, 50);
        zoomHelpLabel.setLayoutX(0);
        zoomHelpLabel.setLayoutY(250);
        zoomHelpLabel.setFont(new Font(Font.getDefault().getName(), 18));
        zoomHelpLabel.setWrapText(true);
        zoomHelpLabel.setAlignment(Pos.CENTER);

        // Create close button
        Button closeButton = new Button("X");
        closeButton.setPrefSize(50, 50);
        closeButton.setLayoutX(stageWidth - 100);
        closeButton.setLayoutY(50);
        closeButton.setOnAction((_) -> root.getChildren().remove(helpPane));

        // Add to hierarchy
        helpBackground.getChildren().add(foregroundRect);
        helpBackground.getChildren().add(helpLabel);
        helpBackground.getChildren().add(globalChatHelpLabel);
        helpBackground.getChildren().add(privateChatHelpLabel);
        helpBackground.getChildren().add(zoomHelpLabel);

        helpPane.getChildren().add(backgroundRect);
        helpPane.getChildren().add(helpBackground);
        helpPane.getChildren().add(closeButton);

        // Create help button
        Button helpButton = new Button("?");
        helpButton.setPrefSize(50, 50);
        helpButton.setLayoutX(commonAreaWidth - 50);
        helpButton.setLayoutY(0);
        helpButton.setOnAction((_) -> root.getChildren().add(helpPane));

        commonAreaPane.getChildren().add(helpButton);
    }

    private void switchToPlayerField(String playerNickname, Circle playerCircle, Rectangle playerRectangle) {
        for (Shape circle : playersCircles) {
            circle.setVisible(true);
        }
        for (Shape rectangle : playersRectangles) {
            rectangle.setVisible(false);
        }

        playerCircle.setVisible(false);
        playerRectangle.setVisible(true);

        if (nickname.equals(playerNickname)) {
            // Set play area to be the default one
            playerFieldScroll.setContent(playerField);

            playerHandParentPane.getChildren().clear();
            playerHandParentPane.getChildren().add(playerHandPane);
        }
        else {
            Map<Position, PlaceableCard> playArea = getOpponentByNickname(playerNickname).getPlayArea();
            List<PlaceableCard> playerHand = getOpponentByNickname(playerNickname).getPlayerHand();
            TokenColors color = getOpponentByNickname(playerNickname).getTokenColor();

            // Create the play area grid
            AnchorPane opponentFieldPane = createEnemyPlayField(playArea);

            // Create the hand pane
            AnchorPane opponentHandPane = createOpponentHandPane(playerHand, color);

            // Redraw the play area for that player
            playerFieldScroll.setContent(opponentFieldPane);

            playerHandParentPane.getChildren().clear();
            playerHandParentPane.getChildren().add(opponentHandPane);
        }
    }

    private AnchorPane createOpponentHandPane(List<PlaceableCard> opponentHand, TokenColors color) {
        double playerHandWidth = playerHandPane.getPrefWidth();
        double playerHandHeight = playerHandPane.getPrefHeight();

        AnchorPane opponentHandPane = new AnchorPane();
        opponentHandPane.setPrefSize(playerHandWidth, playerHandHeight);
        opponentHandPane.setLayoutX(0);
        opponentHandPane.setLayoutY(0);

        double cardsYPos = (opponentHandPane.getPrefHeight() - cardHeight) * 0.5;

        for (int i = 0; i < 3; i++) {
            if (i < opponentHand.size() && opponentHand.get(i) != null) {
                Pane cardPane = createCardPane(opponentHand.get(i).getID(), CardSideType.BACK, (i + 2) * defaultElementsOffset + (i + 1) * cardWidth, cardsYPos, 1);

                opponentHandPane.getChildren().add(cardPane);
            }
        }

        // Create panes for each card
//        Pane card1Pane = createCardPane(opponentHand.getFirst().getID(), CardSideType.BACK, 2 * defaultElementsOffset + cardWidth, cardsYPos, 1);
//        Pane card2Pane = createCardPane(opponentHand.get(1).getID(), CardSideType.BACK, 3 * defaultElementsOffset + 2 * cardWidth, cardsYPos, 1);
//        Pane card3Pane = createCardPane(opponentHand.get(2).getID(), CardSideType.BACK, 4 * defaultElementsOffset + 3 * cardWidth, cardsYPos, 1);

        // Uh... 90 just because it's an objective card
        Pane secretObjectivePane = createCardPane(90, CardSideType.BACK, defaultElementsOffset + xOffsetByScale(0.9), cardsYPos + yOffsetByScale(0.9), 0.9);

        // Add rectangle to highlight player color
        Rectangle plRect = new Rectangle();
        plRect.setStrokeWidth(6);
        plRect.setX(3);
        plRect.setY(3);
        plRect.setWidth(playerHandWidth - 5);
        plRect.setHeight(playerHandHeight - 2);
        plRect.setFill(Color.TRANSPARENT);
        plRect.setStroke(TokenColors.getColorFromToken(color));
        plRect.setMouseTransparent(true);

        opponentHandPane.getChildren().add(plRect);

        // Add cards to stage
        opponentHandPane.getChildren().add(secretObjectivePane);
//        opponentHandPane.getChildren().add(card1Pane);
//        opponentHandPane.getChildren().add(card2Pane);
//        opponentHandPane.getChildren().add(card3Pane);

        return opponentHandPane;
    }

    private AnchorPane createEnemyPlayField(Map<Position, PlaceableCard> playArea) {
        // Calculate number of rows and columns
        int maxX = playArea.keySet().stream().map(Position::getX).max(Integer::compareTo).orElse(0);
        int minX = playArea.keySet().stream().map(Position::getX).min(Integer::compareTo).orElse(0);
        int maxY = playArea.keySet().stream().map(Position::getY).max(Integer::compareTo).orElse(0);
        int minY = playArea.keySet().stream().map(Position::getY).min(Integer::compareTo).orElse(0);

        int columns = maxX - minX + 1;
        int rows = maxY - minY + 1;

        // Create anchor pane for play area
        AnchorPane scrollContent = new AnchorPane();
        scrollContent.setPrefSize(columns * gridCellWidth + cardWidth, rows * gridCellHeight + cardHeight);
        scrollContent.setLayoutX(0);
        scrollContent.setLayoutY(0);

        // Create grid pane for play area
        GridPane playAreaGrid = new GridPane();
        playAreaGrid.setPrefSize(columns * gridCellWidth, rows * gridCellHeight);
        playAreaGrid.setLayoutX(cardWidth / 2);
        playAreaGrid.setLayoutY(cardHeight / 2);

        playAreaGrid.getColumnConstraints().add(new ColumnConstraints(gridCellWidth));
        playAreaGrid.getRowConstraints().add(new RowConstraints(gridCellHeight));

        Map<Position, PlaceableCard> sortedPlayArea = sortByPriority(playArea);

        for (Map.Entry<Position, PlaceableCard> entry : sortedPlayArea.entrySet()) {
            PlaceableCard card = entry.getValue();
            Position pos = entry.getKey();

            // Create Image
            String cardName = "/cards/" + card.getCurrSideType().toString().toLowerCase() + "/card" + card.getID() + ".png";

            // Create Pane for card
            Pane cardPane = new Pane();
            cardPane.setPrefSize(gridCellWidth, gridCellHeight);

            Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(cardName)));
            ImageView cardImgV = new ImageView(cardImg);

            cardImgV.setFitHeight(cardHeight);
            cardImgV.setFitWidth(cardWidth);

            cardImgV.setX(-cardWidth * cardCornerWidthProportion / 2);
            cardImgV.setY(-cardHeight * cardCornerHeightProportion / 2);

            cardPane.getChildren().add(cardImgV);

            Position gridPos = new Position(pos.getX() - minX, -pos.getY() + maxY);

            // Add card to the grid
            playAreaGrid.add(cardPane, gridPos.getX(), gridPos.getY());
        }

        scrollContent.getChildren().add(playAreaGrid);

        return scrollContent;
    }

    private void createChatPane() {
        chatPane = new AnchorPane();
        chatPane.setPrefSize(commonAreaWidth - 100, stageHeight - 100);
        chatPane.setLayoutX(0);
        chatPane.setLayoutY(50);

        ScrollPane chatScroll = new ScrollPane();
        chatScroll.setPrefSize(commonAreaWidth - 100, stageHeight - 150);
        chatScroll.setPannable(true);
        chatScroll.setLayoutX(0);
        chatScroll.setLayoutY(0);
        chatScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        chatBox = new VBox();
        chatBox.setPrefSize(commonAreaWidth - 110, stageHeight - 150);
        chatBox.setLayoutX(0);
        chatBox.setLayoutY(0);

        AnchorPane chatInputPane = new AnchorPane();
        chatInputPane.setPrefSize(commonAreaWidth - 100, 50);
        chatInputPane.setLayoutX(0);
        chatInputPane.setLayoutY(stageHeight - 100);

        TextField chatInput = new TextField();
        chatInput.setPrefSize(commonAreaWidth - 200, 50);
        chatInput.setLayoutX(0);
        chatInput.setLayoutY(0);
        chatInput.setPromptText("Enter message...");

        Button sendButton = new Button("Send");
        sendButton.setPrefSize(50, 50);
        sendButton.setLayoutX(commonAreaWidth - 75);
        sendButton.setLayoutY(0);
        sendButton.setOnAction((_) -> {
            if (chatInput.getText().isEmpty() || chatInput.getText().isBlank()) {
                return;
            }

            if (chatInput.getText().startsWith("/")) {
                String[] command = chatInput.getText().split(" ");
                if (command[0].equals("/whisper") && command.length >= 3 && !command[1].equals(nickname)) {
                    sender.sendChatMessage(command[1], chatInput.getText().substring(command[0].length() + command[1].length() + 2));
                }
                else {
                    showError("Invalid chat format.");
                }
            }
            else {
                sender.sendChatMessage("all", chatInput.getText());
            }

            chatInput.clear();
        });

        // Create close chat button
        Button closeChatButton = new Button("X");
        closeChatButton.setPrefSize(50, 50);
        closeChatButton.setLayoutX(commonAreaWidth - 50);
        closeChatButton.setLayoutY(0);
        closeChatButton.setOnAction((_) -> openCloseChat());

        chatInputPane.getChildren().add(chatInput);
        chatInputPane.getChildren().add(sendButton);

        chatScroll.setContent(chatBox);
        chatPane.getChildren().add(chatScroll);
        chatPane.getChildren().add(chatInputPane);
        chatPane.getChildren().add(closeChatButton);

        chatOpen = false;
    }

    private void openCloseChat() {
        if (chatOpen) {
            commonAreaPane.getChildren().remove(chatPane);
        }
        else {
            commonAreaPane.getChildren().add(chatPane);
        }

        chatOpen = !chatOpen;
    }

    private void renderStarterCardChoice(int starterCardID){
        starterCard = GameResources.getPlaceableCardByID(starterCardID);

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
        starterCard.setCurrSideType(CardSideType.FRONT);

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
            sender.placeStarterCard(starterCard.getCurrSideType());
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
        starterCard.setCurrSideType(starterCard.getCurrSideType() == CardSideType.FRONT ? CardSideType.BACK : CardSideType.FRONT);

        Pane newPane = createCardPane(starterCard.getID(), starterCard.getCurrSideType(), starterCardPane.getLayoutX(), starterCardPane.getLayoutY(), 1);
        tempChoicePane.getChildren().add(newPane);

        tempChoicePane.getChildren().remove(starterCardPane);
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

        commonResDeckCardID = resDeckCardID;
        commonVisibleResCardID1 = resVisibleCard1ID;
        commonVisibleResCardID2 = resVisibleCard2ID;

        commonGoldDeckCardID = goldDeckCardID;
        commonVisibleGoldCardID1 = goldVisibleCard1ID;
        commonVisibleGoldCardID2 = goldVisibleCard2ID;

        commonResDeckCardPane = resDeckPane;
        commonVisibleResCard1Pane = resVisible1Pane;
        commonVisibleResCard2Pane = resVisible2Pane;

        commonGoldDeckCardPane = goldDeckPane;
        commonVisibleGoldCard1Pane = goldVisible1Pane;
        commonVisibleGoldCard2Pane = goldVisible2Pane;

        // Add draw buttons to cards
        addDrawButtonToCard(resDeckPane, -1, false, false);
        addDrawButtonToCard(resVisible1Pane, 0, false, true);
        addDrawButtonToCard(resVisible2Pane, 1, false, true);

        addDrawButtonToCard(goldDeckPane, -1, true, false);
        addDrawButtonToCard(goldVisible1Pane, 0, true, true);
        addDrawButtonToCard(goldVisible2Pane, 1, true, true);

        // Add cards to the stage
        commonAreaPane.getChildren().add(resDeckPane);
        commonAreaPane.getChildren().add(resVisible1Pane);
        commonAreaPane.getChildren().add(resVisible2Pane);

        commonAreaPane.getChildren().add(goldDeckPane);
        commonAreaPane.getChildren().add(goldVisible1Pane);
        commonAreaPane.getChildren().add(goldVisible2Pane);
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
        Pane objective1Pane = createCardPane(objective1ID, CardSideType.FRONT, commonAreaWidth / 2 - cardWidth - 30, 350, 1);
        Pane objective2Pane = createCardPane(objective2ID, CardSideType.FRONT, commonAreaWidth / 2 + 30, 350, 1);

        commonAreaPane.getChildren().add(objective1Pane);
        commonAreaPane.getChildren().add(objective2Pane);
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

    private void addDrawButtonToCard(Pane cardPane, int cardIndex, boolean isGolden, boolean isVisible){
        Button drawButton = new Button();
        drawButton.setPrefSize(cardPane.getPrefWidth(), cardPane.getPrefHeight());
        drawButton.setLayoutX(0);
        drawButton.setLayoutY(0);
        drawButton.setOpacity(0.1);

        drawButton.setOnAction((_) -> {
            if (isVisible){
                if (isGolden){
                    sender.drawVisibleGoldenCard(cardIndex);
                }
                else {
                    sender.drawVisibleResourceCard(cardIndex);
                }
            }
            else {
                if (isGolden){
                    sender.drawGoldenCard();
                }
                else {
                    sender.drawResourceCard();
                }
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

    private void addPlaceButtons(int ID, Position pos){
        PlaceableCard card = GameResources.getPlaceableCardByID(ID);

        Position blPos = new Position(pos.getX() - 1, pos.getY() - 1);
        Position brPos = new Position(pos.getX() + 1, pos.getY() - 1);
        Position tlPos = new Position(pos.getX() - 1, pos.getY() + 1);
        Position trPos = new Position(pos.getX() + 1, pos.getY() + 1);

        if (card.getCurrSide().getBLCorner().isAvailable() && !illegalPosList.contains(blPos) && !validPosToButtonPane.containsKey(blPos)){
            addPlaceButtonAtPos(blPos);
        }
        if (card.getCurrSide().getBRCorner().isAvailable() && !illegalPosList.contains(brPos) && !validPosToButtonPane.containsKey(brPos)){
            addPlaceButtonAtPos(brPos);
        }
        if (card.getCurrSide().getTLCorner().isAvailable() && !illegalPosList.contains(tlPos) && !validPosToButtonPane.containsKey(tlPos)){
            addPlaceButtonAtPos(tlPos);
        }
        if (card.getCurrSide().getTRCorner().isAvailable() && !illegalPosList.contains(trPos) && !validPosToButtonPane.containsKey(trPos)){
            addPlaceButtonAtPos(trPos);
        }
    }

    private void addPlaceButtonAtPos(Position pos) {
        Pane positionPane = new Pane();
        positionPane.setPrefSize(gridCellWidth, gridCellHeight);

        Button placeButton = new Button();
        placeButton.setPrefSize(cardWidth, cardHeight);
        placeButton.setLayoutX(-cardWidth * cardCornerWidthProportion);
        placeButton.setLayoutY(-cardHeight * cardCornerHeightProportion);
        placeButton.setOpacity(0.6);
        placeButton.setOnAction((_) -> sender.placeCard(selectedCard.getID(), selectedCard.getCurrSideType(), pos));

        positionPane.getChildren().add(placeButton);
        Position buttonGridPos = mapToGridPos(pos);
        playerFieldGrid.add(positionPane, buttonGridPos.getX(), buttonGridPos.getY());
        validPosToButtonPane.put(pos, positionPane);
    }

    private void updatePlacementPositions(Position pos){
        ArrayList<Position> newLegalPos = new ArrayList<>();
        ArrayList<Position> newIllegalPos = new ArrayList<>();

        PlaceableCard card = playArea.get(pos);

        Side currSide = card.getCurrSide();

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

    private void placeCardAction(PlaceableCard card, Position pos){
        if (pos.getX() <= -gridColumns / 2 || pos.getX() >= gridColumns / 2 ||
                pos.getY() <= -gridRows / 2 || pos.getY() >= gridRows / 2){
            updateGridDimension();
        }

        placedCardIndexInHand = selectedCardIndex;

        // Remove card from hand
        playerHand.remove(card);
        playerHandPane.getChildren().remove(selectedCardPane);

        // Create Image
        String cardName = "/cards/" + card.getCurrSideType().toString().toLowerCase() + "/card" + card.getID() + ".png";

        if (!playArea.containsKey(pos)){
            playArea.put(pos, card);
        }

        updatePlacementPositions(pos);

        // Clear Pane in the grid position
        playerFieldGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == mapToGridPos(pos).getY() && GridPane.getColumnIndex(node) == mapToGridPos(pos).getX());

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

        addPlaceButtons(card.getID(), pos);

        Position gridPos = mapToGridPos(pos);

        // Add card to the grid
        playerFieldGrid.add(cardPane, gridPos.getX(), gridPos.getY());

        // playerFieldRectWidth, playerFieldRectHeight
        // TODO: Check how it works
//        playerField.setLayoutX((playerFieldRectWidth - playerField.getPrefWidth()) / 2);
//        playerField.setLayoutY((playerFieldRectHeight - playerField.getPrefHeight()) / 2);
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

        Map<Position, PlaceableCard> sortedPlayArea = sortByPriority(playArea);

        for (Position p : sortedPlayArea.keySet()){
            placeCardAction(sortedPlayArea.get(p), p);
        }
    }

    private void drawCard(int drawnCardID) {
        PlaceableCard drawnCard = GameResources.getPlaceableCardByID(drawnCardID);

        playerHand.add(drawnCard);

        Pane cardPane = createCardInHandPane(drawnCard, placedCardIndexInHand);

        addSelectButtonToCard(cardPane, drawnCard, placedCardIndexInHand);

        playerHandPane.getChildren().add(cardPane);
    }

    private void drawPlayerHand(int card1ID, int card2ID, int card3ID, Color playerColor){
        PlaceableCard card1 = GameResources.getPlaceableCardByID(card1ID);
        PlaceableCard card2 = GameResources.getPlaceableCardByID(card2ID);
        PlaceableCard card3 = GameResources.getPlaceableCardByID(card3ID);

        double playerHandWidth = playerHandPane.getPrefWidth();
        double playerHandHeight = playerHandPane.getPrefHeight();

        // Create panes for each card
        Pane card1Pane = createCardInHandPane(card1, 0);
        Pane card2Pane = createCardInHandPane(card2, 1);
        Pane card3Pane = createCardInHandPane(card3, 2);

        playerHand.add(card1);
        playerHand.add(card2);
        playerHand.add(card3);

        // Add select buttons to cards
        addSelectButtonToCard(card1Pane, card1, 0);
        addSelectButtonToCard(card2Pane, card2, 1);
        addSelectButtonToCard(card3Pane, card3, 2);

        // Add rectangle to highlight player color
        Rectangle plRect = new Rectangle();
        plRect.setStrokeWidth(6);
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

    private Pane createCardInHandPane(PlaceableCard card, int index) {
        double cardsYPos = (playerHandPane.getPrefHeight() - cardHeight) * 0.5;

        // Create Pane for card
        Pane cardPane = new Pane();

        cardPane.setPrefSize(cardWidth, cardHeight);
        cardPane.setLayoutX((index + 2) * defaultElementsOffset + (index + 1) * cardWidth);
        cardPane.setLayoutY(cardsYPos);

        // Create Image
        String cardName = "/cards/" + card.getCurrSideType().toString().toLowerCase() + "/card" + card.getID() + ".png";

        Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(cardName)));
        ImageView cardImgV = new ImageView(cardImg);

        cardImgV.setFitHeight(cardHeight);
        cardImgV.setFitWidth(cardWidth);

        cardImgV.setX(0);
        cardImgV.setY(0);

        cardPane.getChildren().add(cardImgV);

        // Create buttons to flip card
        Button flipButton = new Button("Flip");
        flipButton.setPrefSize(100, 50);
        flipButton.setLayoutX((cardWidth - 100) / 2);
        flipButton.setLayoutY(cardHeight + 10);
        flipButton.setOnAction((_) -> flipCard(card, cardImgV));

        cardPane.getChildren().add(flipButton);

        return cardPane;
    }

    private void flipCard(PlaceableCard card, ImageView imageView) {
        // Flip placeable card
        card.setCurrSideType(card.getCurrSideType() == CardSideType.FRONT ? CardSideType.BACK : CardSideType.FRONT);

        // Change image view
        String cardName = "/cards/" + card.getCurrSideType().toString().toLowerCase() + "/card" + card.getID() + ".png";

        Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(cardName)));
        imageView.setImage(cardImg);
    }

    private void addSelectButtonToCard(Pane cardPane, PlaceableCard card, int index){
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
                selectedCardIndex = index;
                selectedCard = card;
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

    private void showSystemMessage(String systemMessage){
        AnchorPane messagePane = new AnchorPane();
        messagePane.setPrefSize(stageWidth - 20, 50);
        messagePane.setLayoutX(10);
        messagePane.setLayoutY(stageHeight - 50);

        Rectangle messageRect = new Rectangle();
        messageRect.setX(0);
        messageRect.setY(0);
        messageRect.setWidth(stageWidth - 20);
        messageRect.setHeight(50);
        messageRect.setFill(Color.LIGHTGRAY);
        messageRect.setStroke(Color.RED);

        Label messageLabel = new Label(systemMessage);
        messageLabel.setPrefSize(stageWidth - 20, 50);
        messageLabel.setLayoutX(0);
        messageLabel.setLayoutY(0);
        messageLabel.setFont(new Font(Font.getDefault().getName(), 24));
        messageLabel.setTextFill(Color.BLUE);

        messagePane.getChildren().add(messageRect);
        messagePane.getChildren().add(messageLabel);

        root.getChildren().add(messagePane);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> root.getChildren().remove(messagePane));
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
    public void startGame(Map<Integer, String> IDToNicknameMap,
                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) {
        Platform.runLater(() -> {
            drawGameStart(IDToNicknameMap, resDeckCardID, visibleResCardID1, visibleResCardID2,
                    goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, starterCardID);

            // Add events for zooming
            stage.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
            stage.addEventHandler(KeyEvent.KEY_RELEASED, this::keyReleased);
        });

//        stage.widthProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
//            root.getChildren().clear();
//            updateWindow();
//        });
//        stage.heightProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
//            root.getChildren().clear();
//            updateWindow();
//        });
    }

    @Override
    public void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor,
                                int commonObjectiveCardID1, int commonObjectiveCardID2,
                                int secretObjectiveCardID1, int secretObjectiveCardID2) {
        Platform.runLater(() -> {
            // Clear temp choice pane and place starter card before proceeding
            tempChoicePane.getChildren().clear();
            this.tokenColor = tokenColor;

            starterCard.setPriority(priority);
            priority++;

            placeCardAction(starterCard, new Position(0, 0));

            drawMissingSetUp(resourceCardID1, resourceCardID2, goldenCardID, tokenColor, commonObjectiveCardID1, commonObjectiveCardID2, secretObjectiveCardID1, secretObjectiveCardID2);
        });
    }

    @Override
    public void confirmSecretObjective() {
        Platform.runLater(() -> {
            tempChoicePane.getChildren().clear();

            root.getChildren().remove(tempChoicePane);

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
    public void showFirstPlayerTurn(int lastPlayerID, int playerID, int[] playerIDs, int[] starterCardIDs, CardSideType[] starterCardSideTypes, TokenColors[] tokenColors, int[][] playerHands) {
        Platform.runLater(() -> {
            if (this.playerID == lastPlayerID){
                confirmSecretObjective();
            }

            String nickname = getPlayerNickname(playerID);

            if (this.playerID == playerID){
                showSystemMessage("It's your turn!");
                nickname = "(You)";
            }
            else {
                showSystemMessage("It's " + nickname + "'s turn!");
            }

            for (int i = 0; i < playerIDs.length; i++){
                if (playerIDs[i] != this.playerID){
                    getOpponentByID(playerIDs[i]).setTokenColor(tokenColors[i]);
                    getOpponentByID(playerIDs[i]).placeCardInPlayArea(starterCardIDs[i], starterCardSideTypes[i], new Position(0, 0), 0);

                    for (int cardID : playerHands[i]) {
                        getOpponentByID(playerIDs[i]).drawCard(cardID);
                    }
                }
            }

            // Create player labels for each player in common area
            createOtherPlayerSection();

            currPlayerTurnLabel.setText("Current player: " + nickname);
        });
    }

    private void createOtherPlayerSection() {
        AnchorPane playersSection = new AnchorPane();
        playersSection.setPrefSize(commonAreaWidth - 100, 200);
        playersSection.setLayoutX(50);
        playersSection.setLayoutY(0);

        Pane yourPlayerPane = createPlayerLabel(nickname, tokenColor, 1);
        playersSection.getChildren().add(yourPlayerPane);

        int i = 2;
        for (PlayerViewModel opponent : opponents) {
            Pane playerPane = createPlayerLabel(opponent.getNickname(), opponent.getTokenColor(), i);
            i++;

            playersSection.getChildren().add(playerPane);
        }

        commonAreaPane.getChildren().add(playersSection);
    }

    private Pane createPlayerLabel(String playerNick, TokenColors color, int posMultiplier){
        Pane playerPane = new Pane();
        playerPane.setPrefSize(300, 50);
        playerPane.setLayoutX(50);
        playerPane.setLayoutY(50 * posMultiplier);

        String actualNick = playerNick.equals(nickname) ? nickname + " (You)" : playerNick;

        Label playerLabel = new Label(actualNick + ": 0 points");
        playerLabel.setPrefSize(300, 50);
        playerLabel.setLayoutX(30);
        playerLabel.setLayoutY(0);

        nicknameToScoreLabel.put(playerNick, playerLabel);

        // Player circle when not selected
        Circle playerCircle = new Circle(10, TokenColors.getColorFromToken(color));
        playerCircle.setLayoutX(15);
        playerCircle.setLayoutY(25);
        playerCircle.setMouseTransparent(true);
        playerCircle.setVisible(true);

        playersCircles.add(playerCircle);

        // Player rect when selected
        Rectangle playerRectangle = new Rectangle(292, 42);
        playerRectangle.setLayoutX(4);
        playerRectangle.setLayoutY(4);
        playerRectangle.setFill(Color.TRANSPARENT);
        playerRectangle.setStroke(TokenColors.getColorFromToken(color));
        playerRectangle.setStrokeWidth(4);
        playerRectangle.setMouseTransparent(true);
        playerRectangle.setVisible(false);

        playersRectangles.add(playerRectangle);

        if (playerNick.equals(nickname)){
            playerCircle.setVisible(false);
            playerRectangle.setVisible(true);
        }

        Button playerAreaButton = new Button();
        playerAreaButton.setPrefSize(300, 50);
        playerAreaButton.setLayoutX(0);
        playerAreaButton.setLayoutY(0);
        playerAreaButton.setOpacity(0.1);
        playerAreaButton.setOnAction((_) -> switchToPlayerField(playerNick, playerCircle, playerRectangle));

        playerPane.getChildren().add(playerCircle);
        playerPane.getChildren().add(playerLabel);
        playerPane.getChildren().add(playerAreaButton);
        playerPane.getChildren().add(playerRectangle);

        return playerPane;
    }

    @Override
    public void placeCard(int playerId, int cardID, Position pos, CardSideType side, int deltaScore) {
        Platform.runLater(() -> {
            if (playerId == this.playerID){
                selectedCard.setPriority(priority);
                priority++;

                // place card for current player
                placeCardAction(selectedCard, pos);
                score += deltaScore;

                if(deltaScore != 0) {
                    showSystemMessage("You scored " + deltaScore + " points!");
                }
                nicknameToScoreLabel.get(nickname).setText(nickname + " (You): " + score + " points");
            }
            else {
                // Place card in the opponent's play area
                getOpponentByID(playerId).placeCardInPlayArea(cardID, side, pos, deltaScore);

                String playerNick = getPlayerNickname(playerId);

                if(deltaScore != 0) {
                    showSystemMessage(playerNick + " scored " + deltaScore + " points!");
                }
                nicknameToScoreLabel.get(playerNick).setText(playerNick + ": " + getOpponentByID(playerId).getScore() + " points");
            }
        });
    }

    @Override
    public void showNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID) {
        Platform.runLater(() -> {
            String nickname = getPlayerNickname(newPlayerID);

            if (playerID == lastPlayerID){
                drawCard(drawnCardID);

                showSystemMessage("It's " + nickname + "'s turn!");
            }
            else {
                if (playerID == newPlayerID){
                    nickname = "(You)";

                    showSystemMessage("It's your turn!");
                }
                else {
                    showSystemMessage("It's " + nickname + "'s turn!");
                }

                getOpponentByID(lastPlayerID).drawCard(drawnCardID);
            }

            currPlayerTurnLabel.setText("Current player: " + nickname);
        });
    }

    @Override
    public void updateDrawArea(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                               int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2) {
        Platform.runLater(() -> {
            double resourceCardsY = stageHeight - (2 * cardHeight) - 2 * defaultElementsOffset;
            double goldenCardsY = stageHeight - cardHeight - defaultElementsOffset;

            if (commonResDeckCardID != resDeckCardID) {
                // Remove old card
                commonAreaPane.getChildren().remove(commonResDeckCardPane);

                // Add new card
                if (resDeckCardID >= 0) {
                    commonResDeckCardID = resDeckCardID;
                    Pane resDeckPane = createCardPane(resDeckCardID, CardSideType.BACK, defaultElementsOffset, resourceCardsY, 1);
                    addDrawButtonToCard(resDeckPane, -1, false, false);
                    commonAreaPane.getChildren().add(resDeckPane);
                }
            }
            if (commonVisibleResCardID1 != visibleResCardID1) {
                // Remove old card
                commonAreaPane.getChildren().remove(commonVisibleResCard1Pane);

                // Add new card
                if (visibleResCardID1 >= 0) {
                    commonVisibleResCardID1 = visibleResCardID1;
                    Pane resVisible1Pane = createCardPane(visibleResCardID1, CardSideType.FRONT, 2 * defaultElementsOffset + cardWidth + xOffsetByScale(0.8), resourceCardsY + yOffsetByScale(0.8), 0.8);
                    addDrawButtonToCard(resVisible1Pane, 0, false, true);
                    commonAreaPane.getChildren().add(resVisible1Pane);
                }
            }
            if (commonVisibleResCardID2 != visibleResCardID2) {
                // Remove old card
                commonAreaPane.getChildren().remove(commonVisibleResCard2Pane);

                // Add new card
                if (visibleResCardID2 >= 0) {
                    commonVisibleResCardID2 = visibleResCardID2;
                    Pane resVisible2Pane = createCardPane(visibleResCardID2, CardSideType.FRONT, 3 * defaultElementsOffset + 2 * cardWidth + xOffsetByScale(0.8), resourceCardsY + yOffsetByScale(0.8), 0.8);
                    addDrawButtonToCard(resVisible2Pane, 1, false, true);
                    commonAreaPane.getChildren().add(resVisible2Pane);
                }
            }

            if (commonGoldDeckCardID != goldDeckCardID) {
                // Remove old card
                commonAreaPane.getChildren().remove(commonGoldDeckCardPane);

                // Add new card
                if (goldDeckCardID >= 0) {
                    commonGoldDeckCardID = goldDeckCardID;
                    Pane goldDeckPane = createCardPane(goldDeckCardID, CardSideType.BACK, defaultElementsOffset, goldenCardsY, 1);
                    addDrawButtonToCard(goldDeckPane, -1, true, false);
                    commonAreaPane.getChildren().add(goldDeckPane);
                }
            }
            if (commonVisibleGoldCardID1 != visibleGoldCardID1) {
                // Remove old card
                commonAreaPane.getChildren().remove(commonVisibleGoldCard1Pane);

                // Add new card
                if (visibleGoldCardID1 >= 0) {
                    commonVisibleGoldCardID1 = visibleGoldCardID1;
                    Pane goldVisible1Pane = createCardPane(visibleGoldCardID1, CardSideType.FRONT, 2 * defaultElementsOffset + cardWidth + xOffsetByScale(0.8), goldenCardsY + yOffsetByScale(0.8), 0.8);
                    addDrawButtonToCard(goldVisible1Pane, 0, true, true);
                    commonAreaPane.getChildren().add(goldVisible1Pane);
                }
            }
            if (commonVisibleGoldCardID2 != visibleGoldCardID2) {
                // Remove old card
                commonAreaPane.getChildren().remove(commonVisibleGoldCard2Pane);

                // Add new card
                if (visibleGoldCardID2 >= 0) {
                    commonVisibleGoldCardID2 = visibleGoldCardID2;
                    Pane goldVisible2Pane = createCardPane(visibleGoldCardID2, CardSideType.FRONT, 3 * defaultElementsOffset + 2 * cardWidth + xOffsetByScale(0.8), goldenCardsY + yOffsetByScale(0.8), 0.8);
                    addDrawButtonToCard(goldVisible2Pane, 1, true, true);
                    commonAreaPane.getChildren().add(goldVisible2Pane);
                }
            }
        });
    }

    @Override
    public void setID(int playerID) {
        this.playerID = playerID;
        sender.setPlayerID(playerID);
    }

    @Override
    public void receiveChatMessage(String senderNickname, String recipientNickname, String message) {
        Platform.runLater(() -> {
            String fullMessage;
            String actualSender = senderNickname;

            if (actualSender.equals(nickname)){
                actualSender = "(You)";
            }

            if (recipientNickname.equals("all")){
                fullMessage = actualSender + ": " + message;
            }
            else {
                fullMessage = actualSender + " -> " + recipientNickname + ": " + message;
            }

            chatMessages.add(fullMessage);

            Label chatLabel = new Label(fullMessage);
            chatLabel.setPrefSize(commonAreaWidth - 25, 25);
            chatLabel.setLayoutX(0);
            chatLabel.setLayoutY(0);
            chatLabel.setFont(new Font(Font.getDefault().getName(), 16));
            chatLabel.setTextFill(Color.BLACK);

            chatBox.getChildren().add(chatLabel);

            chatBoxHeight += 25;
            chatBox.setPrefHeight(chatBoxHeight);
            chatBox.setLayoutY(250 - chatBoxHeight);
        });
    }

    @Override
    public void showRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfObjectives) {
        if (playerID == lastPlayerID){
            score += deltaScore;

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
        }
        else {
            // Place card for opponent
            PlayerViewModel opponent = getOpponentByID(lastPlayerID);
            opponent.placeCardInPlayArea(cardID, side, pos, deltaScore);
        }

        drawMatchRanking(nicknames, scores, numOfObjectives);
    }

    private void testShowRanking() {
        int playersNum = 4;

        GameResources.initializeAllDecks();

        PlaceableCard starter = GameResources.getPlaceableCardByID(81);
        starter.setCurrSideType(CardSideType.BACK);
        starter.setPriority(0);
        playArea.put(new Position(0, 0), starter);
        tokenColor = TokenColors.YELLOW;

        Random rng = new Random();

        for (int i = 1; i <= 10; i++) {
            int rand = rng.nextInt(80);
            PlaceableCard card = GameResources.getPlaceableCardByID(rand);
            card.setCurrSideType(rand % 2 == 0 ? CardSideType.FRONT : CardSideType.BACK);
            card.setPriority(i);
            playArea.put(new Position(i, i), card);
        }

        for (int i = 0; i < playersNum - 1; i++) {
            PlayerViewModel opp = new PlayerViewModel();
            opp.setTokenColor(TokenColors.values()[i]);

            opponents.add(opp);

            for (int j = 1; j <= 10; j++) {
                int rand = rng.nextInt(80);

                opp.placeCardInPlayArea(rand, rand % 2 == 0 ? CardSideType.FRONT : CardSideType.BACK, new Position(j, j), 0);
            }
        }

        String[] players = new String[playersNum];
        int[] scores = new int[playersNum];
        int[] numOfSecretObjectives = new int[playersNum];

        for (int i = 0; i < playersNum; i++) {
            players[i] = "Player " + i;
            scores[i] = i;
            numOfSecretObjectives[i] = i;
        }

        drawMatchRanking(players, scores, numOfSecretObjectives);
    }

    private void drawMatchRanking(String[] nicknames, int[] scores, int[] numOfSecretObjectives) {
        root.getChildren().clear();

        // Create ranking window
        AnchorPane rankingPane = new AnchorPane();
        rankingPane.setPrefSize(stageWidth, stageHeight);
        rankingPane.setLayoutX(0);
        rankingPane.setLayoutY(0);

        Rectangle backgroundRect = new Rectangle();
        backgroundRect.setX(0);
        backgroundRect.setY(0);
        backgroundRect.setWidth(stageWidth);
        backgroundRect.setHeight(stageHeight);
        backgroundRect.setFill(Color.LIGHTGRAY);
        backgroundRect.setStroke(Color.BLACK);

        Rectangle rankingRect = new Rectangle();
        rankingRect.setX(stageWidth * 0.1);
        rankingRect.setY(stageHeight * 0.1);
        rankingRect.setWidth(stageWidth * 0.8);
        rankingRect.setHeight(stageHeight * 0.8);
        rankingRect.setFill(Color.WHITE);
        rankingRect.setStroke(Color.WHITE);

        Label rankingLabel = new Label("Ranking");
        rankingLabel.setPrefSize(stageWidth, 100);
        rankingLabel.setLayoutX(0);
        rankingLabel.setLayoutY(100);
        rankingLabel.setFont(new Font(Font.getDefault().getName(), 32));
        rankingLabel.setTextFill(Color.BLACK);
        rankingLabel.setAlignment(Pos.CENTER);

        rankingPane.getChildren().add(backgroundRect);
        rankingPane.getChildren().add(rankingRect);
        rankingPane.getChildren().add(rankingLabel);

        for (int i = 0; i < nicknames.length; i++){
            Label playerLabel = new Label(nicknames[i] + ": " + scores[i] + " points, " + numOfSecretObjectives[i] + " secret objectives completed");
            playerLabel.setPrefSize(stageWidth, 50);
            playerLabel.setLayoutX(0);
            playerLabel.setLayoutY(200 + i * 50);
            playerLabel.setFont(new Font(Font.getDefault().getName(), 24));
            playerLabel.setTextFill(Color.BLACK);
            playerLabel.setAlignment(Pos.CENTER);

            rankingPane.getChildren().add(playerLabel);
        }

        // Create button to return to main menu
        Button returnButton = new Button("Return to main menu");
        returnButton.setPrefSize(200, 50);
        returnButton.setLayoutX(stageWidth / 2 - 250);
        returnButton.setLayoutY(stageHeight - 200);
        returnButton.setOnAction((_) -> sender.getMatches());

        rankingPane.getChildren().add(returnButton);

        // Create button to watch players' maps
        Button watchMapsButton = new Button("Watch players' maps");
        watchMapsButton.setPrefSize(200, 50);
        watchMapsButton.setLayoutX(stageWidth / 2 + 50);
        watchMapsButton.setLayoutY(stageHeight - 200);
        watchMapsButton.setOnAction((_) -> showEndgameMaps(opponents.size() + 1));

        rankingPane.getChildren().add(watchMapsButton);

        root.getChildren().add(rankingPane);
    }

    private void showEndgameMaps(int playersNum) {
        // Create window for watching players' maps
        endgameMapsPane = new AnchorPane();
        endgameMapsPane.setPrefSize(stageWidth, stageHeight);
        endgameMapsPane.setLayoutX(0);
        endgameMapsPane.setLayoutY(0);

        // Create background rectangle
        Rectangle backgroundRect = new Rectangle();
        backgroundRect.setX(0);
        backgroundRect.setY(0);
        backgroundRect.setWidth(stageWidth);
        backgroundRect.setHeight(stageHeight);
        backgroundRect.setFill(Color.LIGHTGRAY);
        backgroundRect.setStroke(Color.BLACK);

        endgameMapsPane.getChildren().add(backgroundRect);
        root.getChildren().add(endgameMapsPane);

        if (playersNum == 2) {
            renderPlayArea(playArea, tokenColor, 0, 0, stageWidth / 2, stageHeight);
            renderPlayArea(opponents.getFirst().getPlayArea(), opponents.getFirst().getTokenColor(), stageWidth / 2, 0, stageWidth / 2, stageHeight);
        }
        else if (playersNum == 3) {
            renderPlayArea(playArea, tokenColor, 0, 0, stageWidth / 2, stageHeight);
            renderPlayArea(opponents.getFirst().getPlayArea(), opponents.getFirst().getTokenColor(), stageWidth / 2, 0, stageWidth / 2, stageHeight / 2);
            renderPlayArea(opponents.get(1).getPlayArea(), opponents.get(1).getTokenColor(), stageWidth / 2, stageHeight / 2, stageWidth / 2, stageHeight / 2);
        }
        else if (playersNum == 4) {
            renderPlayArea(playArea, tokenColor, 0, 0, stageWidth / 2, stageHeight / 2);
            renderPlayArea(opponents.getFirst().getPlayArea(), opponents.getFirst().getTokenColor(), stageWidth / 2, 0, stageWidth / 2, stageHeight / 2);
            renderPlayArea(opponents.get(1).getPlayArea(), opponents.get(1).getTokenColor(), 0, stageHeight / 2, stageWidth / 2, stageHeight / 2);
            renderPlayArea(opponents.get(2).getPlayArea(), opponents.get(2).getTokenColor(), stageWidth / 2, stageHeight / 2, stageWidth / 2, stageHeight / 2);
        }

        // Create button to return to rankings
        Button returnButton = new Button("<-");
        returnButton.setPrefSize(50, 50);
        returnButton.setLayoutX(0);
        returnButton.setLayoutY(0);
        returnButton.setOnAction((_) -> root.getChildren().remove(endgameMapsPane));

        endgameMapsPane.getChildren().add(returnButton);
    }

    private void renderPlayArea(Map<Position, PlaceableCard> playArea, TokenColors colors,
                                double xPos, double yPos, double width, double height) {
        // Create anchor pane to store scroll
        AnchorPane playAreaPane = new AnchorPane();
        playAreaPane.setPrefSize(width, height);
        playAreaPane.setLayoutX(xPos);
        playAreaPane.setLayoutY(yPos);

        // Create play area scroll pane
        ScrollPane playAreaScrollPane = new ScrollPane();
        playAreaScrollPane.setPrefSize(width, height);
        playAreaScrollPane.setLayoutX(0);
        playAreaScrollPane.setLayoutY(0);
        playAreaScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playAreaScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playAreaScrollPane.setPannable(true);

        // Create border rectangle
        Rectangle borderRect = new Rectangle();
        borderRect.setX(4);
        borderRect.setY(4);
        borderRect.setWidth(width - 8);
        borderRect.setHeight(height - 8);
        borderRect.setFill(Color.TRANSPARENT);
        borderRect.setStroke(TokenColors.getColorFromToken(colors));
        borderRect.setStrokeWidth(8);
        borderRect.setMouseTransparent(true);

        // Calculate number of rows and columns
        int maxX = playArea.keySet().stream().map(Position::getX).max(Integer::compareTo).orElse(0);
        int minX = playArea.keySet().stream().map(Position::getX).min(Integer::compareTo).orElse(0);
        int maxY = playArea.keySet().stream().map(Position::getY).max(Integer::compareTo).orElse(0);
        int minY = playArea.keySet().stream().map(Position::getY).min(Integer::compareTo).orElse(0);

        int rows = maxX - minX;
        int columns = maxY - minY;

        cardWidth = (stageWidth - (9 * defaultElementsOffset)) / 7;
        cardHeight = cardWidth / 1.5;

        // Calculate grid cell dimensions
        gridCellWidth = cardWidth - (cardCornerWidthProportion * cardWidth);
        gridCellHeight = cardHeight - (cardCornerHeightProportion * cardHeight);

        // Create anchor pane for play area
        AnchorPane scrollContent = new AnchorPane();
        scrollContent.setPrefSize(columns * gridCellWidth + cardWidth, rows * gridCellHeight + cardHeight);
        scrollContent.setLayoutX(0);
        scrollContent.setLayoutY(0);

        // Create grid pane for play area
        GridPane playAreaGrid = new GridPane();
        playAreaGrid.setPrefSize(columns * gridCellWidth, rows * gridCellHeight);
        playAreaGrid.setLayoutX(cardWidth / 2);
        playAreaGrid.setLayoutY(cardHeight / 2);

        playAreaGrid.getColumnConstraints().add(new ColumnConstraints(gridCellWidth));
        playAreaGrid.getRowConstraints().add(new RowConstraints(gridCellHeight));

        Map<Position, PlaceableCard> sortedPlayArea = sortByPriority(playArea);

        for (Map.Entry<Position, PlaceableCard> entry : sortedPlayArea.entrySet()) {
            PlaceableCard card = entry.getValue();
            Position pos = entry.getKey();

            // Create Image
            String cardName = "/cards/" + card.getCurrSideType().toString().toLowerCase() + "/card" + card.getID() + ".png";

            // Create Pane for card
            Pane cardPane = new Pane();
            cardPane.setPrefSize(gridCellWidth, gridCellHeight);

            Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(cardName)));
            ImageView cardImgV = new ImageView(cardImg);

            cardImgV.setFitHeight(cardHeight);
            cardImgV.setFitWidth(cardWidth);

            cardImgV.setX(-cardWidth * cardCornerWidthProportion / 2);
            cardImgV.setY(-cardHeight * cardCornerHeightProportion / 2);

            cardPane.getChildren().add(cardImgV);

            Position gridPos = new Position(pos.getX() - minX, pos.getY() - minY);

            // Add card to the grid
            playAreaGrid.add(cardPane, gridPos.getX(), gridPos.getY());
        }

        scrollContent.getChildren().add(playAreaGrid);
        playAreaScrollPane.setContent(scrollContent);

        playAreaPane.getChildren().add(playAreaScrollPane);
        playAreaPane.getChildren().add(borderRect);

        endgameMapsPane.getChildren().add(playAreaPane);
    }

    @Override
    public void showNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID, GameState gameState) {
        Platform.runLater(() -> {
            if (gameState == GameState.FINAL_ROUND) {
                showSystemMessage("The game entered the final round state.");
            }
            else {
                showSystemMessage("The game entered the extra round state.");
            }

            showNewPlayerTurn(drawnCardID, lastPlayerID, newPlayerID);
        });
    }

    @Override
    public void showNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int deltaScore) {
        String nickname = getPlayerNickname(newPlayerID);

        if (playerID == lastPlayerID){
            score += deltaScore;

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

            if(deltaScore != 0) {
                showSystemMessage("You scored " + deltaScore + " points! Now it's " + nickname + " turn!");
            }
            else {
                showSystemMessage("It's " + nickname + " turn!");
            }

            nicknameToScoreLabel.get(this.nickname).setText(nickname + " (You): " + score + " points");
        }
        else {
            // Place card for opponent
            PlayerViewModel opponent = getOpponentByID(lastPlayerID);
            opponent.placeCardInPlayArea(cardID, side, pos, deltaScore);

            showSystemMessage("Opponent " + opponent.getNickname() + " scored " + deltaScore + " points");

            if (playerID == newPlayerID) {
                showSystemMessage("It's your turn!");
            }
            else {
                showSystemMessage("It's " + nickname + "'s turn!");
            }

            nicknameToScoreLabel.get(nickname).setText(nickname + ": " + getOpponentByID(lastPlayerID).getScore() + " points");
        }

        String actualNick = this.nickname.equals(nickname) ? "(You)" : nickname;

        currPlayerTurnLabel.setText("Current player: " + actualNick);
    }

    @Override
    public void receivePing() {
        sender.sendPong();
    }

    private String getPlayerNickname(int playerID){
        for (PlayerViewModel p : opponents){
            if (p.getPlayerID() == playerID){
                return p.getNickname();
            }
        }

        return nickname;
    }

    private PlayerViewModel getOpponentByID(int playerID){
        return opponents.stream()
                .filter(opponent -> opponent.getPlayerID() == playerID)
                .findFirst()
                .orElse(null);
    }

    private Map<Position, PlaceableCard> sortByPriority(Map<Position, PlaceableCard> map) {
        List<Map.Entry<Position, PlaceableCard>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> {
            if (o1.getValue().getPriority() == o2.getValue().getPriority()) {
                return 0;
            }
            return o1.getValue().getPriority() > o2.getValue().getPriority() ? 1 : -1;
        });

        Map<Position, PlaceableCard> result = new LinkedHashMap<>();
        for (Map.Entry<Position, PlaceableCard> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private PlayerViewModel getOpponentByNickname(String nickname) {
        return opponents.stream()
                .filter(opponent -> opponent.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
    }
}
