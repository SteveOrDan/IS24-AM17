package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.game.GameState;
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

    private final PlayerViewModel player;
    private final List<PlayerViewModel> opponents = new ArrayList<>();

    private final List<PlaceableCard> playerHand = new ArrayList<>();

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
    private Position cardPlacePosition;

    private int placedCardIndexInHand;

    private double selectedCardX;
    private double selectedCardY;
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
    private GridPane playerFieldGrid;
    private AnchorPane playerField;
    private AnchorPane playerHandPane;
    private AnchorPane tempChoicePane;
    private AnchorPane commonAreaPane;

    private final String ip;
    private ClientSender sender;

    private final Stage stage;

    public GUIView(Stage stage, String ip) {
        this.stage = stage;
        this.ip = ip;
        player = new PlayerViewModel();

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

        Label waitingLabel = new Label("Waiting for other players...\nEntering match " + player.getMatchID() + " with nick " + nick);
        waitingLabel.setPrefSize(stageWidth, stageHeight);
        waitingLabel.setLayoutX(50);
        waitingLabel.setLayoutY(50);
        waitingLabel.setFont(new Font(Font.getDefault().getName(), 32));

        anchorPane.getChildren().add(waitingLabel);

        root.getChildren().add(anchorPane);
    }

    private void drawGameStart(String nickname, Map<Integer, String> IDToNicknameMap,
                               int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                               int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                               int starterCardID){
        GameResources.initializeAllDecks();

        root.getChildren().clear();

        player.setNickname(nickname);

        for (Map.Entry<Integer, String> entry : IDToNicknameMap.entrySet()){
            if (entry.getKey() != player.getPlayerID()){
                PlayerViewModel opponent = new PlayerViewModel();
                opponent.setNickname(entry.getValue());
                opponent.setPlayerID(entry.getKey());
                opponent.setMatchID(player.getMatchID());

                opponents.add(opponent);
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
        ScrollPane playerFieldScroll = new ScrollPane();
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
        playerHandPane = new AnchorPane();

        double playerHandWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
        double playerHandHeight = (2 * cardHeight) + (3 * defaultElementsOffset);

        playerHandPane.setPrefSize(playerHandWidth, playerHandHeight);
        playerHandPane.setLayoutX(3 * (defaultElementsOffset + cardWidth) + 10);
        playerHandPane.setLayoutY(playerFieldRectHeight);

        root.getChildren().add(playerHandPane);
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

        createChatPane();

        // Create other player's section
        createOtherPlayerSection();

        root.getChildren().add(commonAreaPane);
        // endregion
    }

    private void createOtherPlayerSection() {
        AnchorPane playersSection = new AnchorPane();
        playersSection.setPrefSize(commonAreaWidth, stageHeight);
        playersSection.setLayoutX(50);
        playersSection.setLayoutY(0);

        Pane yourPlayerPane = createPlayerLabel(player.getNickname(), 1);
        playersSection.getChildren().add(yourPlayerPane);

        int i = 2;
        for (PlayerViewModel opponent : opponents) {
            Pane playerPane = createPlayerLabel(opponent.getNickname(), i);
            i++;

            playersSection.getChildren().add(playerPane);
        }

        commonAreaPane.getChildren().add(playersSection);
    }

    private Pane createPlayerLabel(String playerNick, int posMultiplier){
        Pane playerPane = new Pane();
        playerPane.setPrefSize(300, 50);
        playerPane.setLayoutX(50);
        playerPane.setLayoutY(50 * posMultiplier);

        Label playerLabel = new Label(playerNick + ": 0 points");
        playerLabel.setPrefSize(300, 50);
        playerLabel.setLayoutX(25);
        playerLabel.setLayoutY(0);

        // Player circle when not selected
        Circle playerCircle = new Circle(10, Color.BLACK); // TokenColors.getColorFromToken(otherPlayersColors.get(enemyNickname)));
        playerCircle.setLayoutX(5);
        playerCircle.setLayoutY(25);

        playersCircles.add(playerCircle);

        // Player rect when selected
        Rectangle playerRectangle = new Rectangle(292, 42, Color.BLACK); // TokenColors.getColorFromToken(otherPlayersColors.get(enemyNickname)));
        playerRectangle.setLayoutX(4);
        playerRectangle.setLayoutY(4);
        playerRectangle.setStrokeWidth(4);
        playerRectangle.setFill(Color.TRANSPARENT);

        playersRectangles.add(playerRectangle);

        Button playerAreaButton = new Button();
        playerAreaButton.setPrefSize(300, 50);
        playerAreaButton.setLayoutX(0);
        playerAreaButton.setLayoutY(0);
        playerAreaButton.setOpacity(0.1);
        playerAreaButton.setOnAction((_) -> switchToPlayerField(playerNick));

        playerPane.getChildren().add(playerRectangle);
        playerPane.getChildren().add(playerCircle);
        playerPane.getChildren().add(playerLabel);
        playerPane.getChildren().add(playerAreaButton);

        return playerPane;
    }

    private void switchToPlayerField(String playerNickname) {
        // TODO: implement

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
            if (chatInput.getText().isEmpty()) {
                return;
            }

            if (chatInput.getText().startsWith("/")) {
                String[] command = chatInput.getText().split(" ");
                if (command[0].equals("/whisper") && command.length >= 3) {
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
            int playerID = player.getPlayerID();

            if (isVisible){
                if (isGolden){
                    sender.drawVisibleGoldenCard(playerID, cardIndex);
                }
                else {
                    sender.drawVisibleResourceCard(playerID, cardIndex);
                }
            }
            else {
                if (isGolden){
                    sender.drawGoldenCard(playerID);
                }
                else {
                    sender.drawResourceCard(playerID);
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

        if (card.getFront().getBLCorner().isAvailable() && !illegalPosList.contains(blPos) && !validPosToButtonPane.containsKey(blPos)){
            addPlaceButtonAtPos(blPos);
        }
        if (card.getFront().getBRCorner().isAvailable() && !illegalPosList.contains(brPos) && !validPosToButtonPane.containsKey(brPos)){
            addPlaceButtonAtPos(brPos);
        }
        if (card.getFront().getTLCorner().isAvailable() && !illegalPosList.contains(tlPos) && !validPosToButtonPane.containsKey(tlPos)){
            addPlaceButtonAtPos(tlPos);
        }
        if (card.getFront().getTRCorner().isAvailable() && !illegalPosList.contains(trPos) && !validPosToButtonPane.containsKey(trPos)){
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
        placeButton.setOnAction((_) -> {
            sender.placeCard(selectedCard.getID(), selectedCard.getCurrSideType(), pos);
        });

        positionPane.getChildren().add(placeButton);
        Position buttonGridPos = mapToGridPos(pos);
        playerFieldGrid.add(positionPane, buttonGridPos.getX(), buttonGridPos.getY());
        validPosToButtonPane.put(pos, positionPane);
    }

    private void updateIlLegalPositions(Position pos){
        ArrayList<Position> newLegalPos = new ArrayList<>();
        ArrayList<Position> newIllegalPos = new ArrayList<>();

        PlaceableCard card = player.getPlayArea().get(pos);

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

    private void placeCard(PlaceableCard card, Position pos){
        if (pos.getX() <= -gridColumns / 2 || pos.getX() >= gridColumns / 2 ||
                pos.getY() <= -gridRows / 2 || pos.getY() >= gridRows / 2){
            updateGridDimension();
        }

        cardPlacePosition = pos;
        placedCardIndexInHand = selectedCardIndex;

        // Remove card from hand
        playerHand.remove(card);
        playerHandPane.getChildren().remove(selectedCardPane);

        // Create Image
        String cardName = "/cards/" + card.getCurrSideType().toString().toLowerCase() + "/card" + card.getID() + ".png";

        if (!player.getPlayArea().containsKey(pos)){
            player.getPlayArea().put(pos, card);
        }

        updateIlLegalPositions(pos);

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

        for (Position p : player.getPlayArea().keySet()){
            placeCard(player.getPlayArea().get(p), p);
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
        player.setMatchID(matchID);

        // Create window for waiting for other players
        Platform.runLater(() -> selectNickname(hostNickname));
    }

    @Override
    public void selectMatch(int matchID, List<String> nicknames) {
        player.setMatchID(matchID);

        // Create window for choosing nickname
        Platform.runLater(() -> openSelectNickWindow(nicknames));
    }

    @Override
    public void chooseNickname(String nickname) {
        // Create window for waiting for other players
        Platform.runLater(() -> selectNickname(nickname));
    }

    @Override
    public void startGame(String nickname, Map<Integer, String> IDToNicknameMap,
                          int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) {
        Platform.runLater(() -> {
            drawGameStart(nickname, IDToNicknameMap, resDeckCardID, visibleResCardID1, visibleResCardID2,
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

            placeCard(starterCard, new Position(0, 0));

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
    public void showFirstPlayerTurn(int lastPlayerID, int playerID, Map<Integer, Map<Position, Integer>> IDtoOpponentPlayArea) {
        Platform.runLater(() -> {
            if (player.getPlayerID() == lastPlayerID){
                confirmSecretObjective();
            }

            String nickname = getPlayerNickname(playerID);

            if (playerID == player.getPlayerID()){
                showSystemMessage("It's your turn!");
            }
            else {
                showSystemMessage("It's " + nickname + "'s turn!");
            }

            currPlayerTurnLabel.setText("Current player: " + nickname);
        });
    }

    @Override
    public void placeCard(int playerId, int cardID, Position pos, CardSideType side, int score) {
        if (playerId == player.getPlayerID()){
            Platform.runLater(() -> placeCard(selectedCard, cardPlacePosition));
        }
        else {
            //TODO: save opponent placed card
        }
    }

    @Override
    public void showNewPlayerTurn(int drawnCardID, int lastPlayerID, int newPlayerID) {
        Platform.runLater(() -> {
            String nickname = getPlayerNickname(newPlayerID);

            if (player.getPlayerID() == lastPlayerID){
                drawCard(drawnCardID);

                showSystemMessage("It's " + nickname + "'s turn!");
            }
            else {
                if (player.getPlayerID() == newPlayerID){
                    showSystemMessage("It's your turn!");
                }
                else {
                    showSystemMessage("It's " + nickname + "'s turn!");
                }
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
        sender.setPlayerID(playerID);
        player.setPlayerID(playerID);
    }

    @Override
    public void receiveChatMessage(String senderNickname, String recipientNickname, String message) {
        Platform.runLater(() -> {
            String fullMessage;
            String actualSender = senderNickname;

            if (actualSender.equals(player.getNickname())){
                actualSender = "You";
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
    public void showRanking(List<String> rankings) {

    }

    @Override
    public void showNewPlayerTurnNewState(int drawnCardID, int lastPlayerID, int newPlayerID, GameState gameState) {

    }

    @Override
    public void showNewPlayerExtraTurn(int cardID, int lastPlayerID, Position pos, CardSideType side, int newPlayerID, int score) {

    }

    private String getPlayerNickname(int playerID){
        if (player.getPlayerID() == playerID){
            return player.getNickname();
        }
        else {
            for (PlayerViewModel p : opponents){
                if (p.getPlayerID() == playerID){
                    return p.getNickname();
                }
            }

            return null;
        }
    }
}
