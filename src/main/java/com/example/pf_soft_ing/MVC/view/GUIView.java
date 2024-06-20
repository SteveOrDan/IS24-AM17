package com.example.pf_soft_ing.MVC.view;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.network.client.ClientMain;
import com.example.pf_soft_ing.network.client.ClientSender;
import com.example.pf_soft_ing.MVC.model.player.PlayerViewModel;
import com.example.pf_soft_ing.utils.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.MVC.model.game.GameResources;
import com.example.pf_soft_ing.MVC.model.game.GameState;
import com.example.pf_soft_ing.MVC.model.player.TokenColors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.util.Duration;

import java.util.*;

public class GUIView implements View {

    private final static String ERAS_FONT = "Eras Bold ITC";

    private Timeline timeline;

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

    private VBox chatBox;
    private double chatBoxHeight = 0;

    private PlaceableCard starterCard;

    private int secretObjectiveCardID;

    private final String connectionType;

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
    private AnchorPane reconnectionPopup;

    private final Map<String, Label> nicknameToScoreLabel = new HashMap<>();

    private final String ip;
    private ClientSender sender;

    private final Stage stage;

    private static final int COUNT_DEC_PERIOD = 1000;
    private static final int MAX_PACKET_LOSS = 3;
    private int packetLoss = 0;
    private final Object packetLossLock = new Object();

    private final Timer timer = new Timer();

    private TimerTask packetLossTask = new TimerTask() {
        @Override
        public void run() {
            synchronized (packetLossLock) {
                if (packetLoss >= MAX_PACKET_LOSS) {
                    showConnectionLoss();
                    cancel();
                }
                packetLoss++;
            }
        }
    };

    public GUIView(Stage stage, String ip, String connectionType) {
        this.stage = stage;
        this.ip = ip;
        this.connectionType = connectionType.toLowerCase();

        launchApp();
    }

    /**
     * Launches the application
     */
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

    /**
     * Draws the connection choice scene
     */
    private void drawConnectionChoiceScene(){
        // Create connection choice scene
        AnchorPane connectionChoice = new AnchorPane();
        connectionChoice.setPrefSize(stageWidth, stageHeight);
        connectionChoice.setLayoutX(0);
        connectionChoice.setLayoutY(0);

        Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/FirstBG.png")));
        ImageView bgView = new ImageView(background);
        bgView.setFitWidth(stageWidth + 2);
        bgView.setFitHeight(stageHeight + 2);

        // Create label
        Label connectionLabel = new Label("Choose port for " + connectionType + " connection");
        connectionLabel.setPrefSize(stageWidth, 50);
        connectionLabel.setLayoutX(0);
        connectionLabel.setLayoutY((stageHeight - 150) * 0.5);
        connectionLabel.setFont(new Font(ERAS_FONT, 24));
        connectionLabel.setAlignment(Pos.CENTER);

        // Create text field for IP
        TextField IPField = new TextField();
        IPField.setPrefSize(200, 50);
        IPField.setLayoutX((stageWidth - 200) * 0.5);
        IPField.setLayoutY((stageHeight - 50) * 0.5);
        IPField.setPromptText("Enter port number...");
        IPField.setFont(new Font(ERAS_FONT, 24));

        Pane confirmIP = createTextButton("Confirm", 20, "/RectButton200x50.png",
                200, 50, (stageWidth - 200) * 0.5, (stageHeight + 150) * 0.5,
                () -> tryConnect(IPField.getText()));

        connectionChoice.getChildren().add(bgView);
        connectionChoice.getChildren().add(connectionLabel);
        connectionChoice.getChildren().add(IPField);
        connectionChoice.getChildren().add(confirmIP);

        root.getChildren().add(connectionChoice);
    }

    /**
     * Creates a button with a text a background image and an action
     * @param text Text of the button
     * @param fontSize Font size of the text
     * @param imagePath Path to the background image
     * @param width Width of the button
     * @param height Height of the button
     * @param x X position of the button
     * @param y Y position of the button
     * @param action Action to be executed when the button is clicked
     * @return The created button
     */
    public Pane createTextButton(String text, int fontSize, String imagePath, double width, double height, double x, double y, Runnable action) {
        // Create Pane
        Pane button = new Pane();
        button.setPrefSize(width, height);
        button.setLayoutX(x);
        button.setLayoutY(y);

        // Create image button
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        imageView.setOnMouseClicked((_) -> action.run());

        // Create label
        Label label = new Label(text);
        label.setPrefSize(width, height);
        label.setLayoutX(0);
        label.setLayoutY(0);
        label.setFont(new Font(ERAS_FONT, fontSize));
        label.setAlignment(Pos.CENTER);
        label.setMouseTransparent(true);

        button.getChildren().add(imageView);
        button.getChildren().add(label);

        return button;
    }

    /**
     * Tries to connect to the server
     * @param port Port to connect to
     */
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
            sender.connect();
        }
        else {
            showError(errorMessage);
        }
    }

    /**
     * Draws the main menu scene
     * @param matches List of available matches
     */
    private void drawMainMenuScene(Map<Integer, List<String>> matches){
        root.getChildren().clear();

        // Create main menu scene
        AnchorPane mainMenu = new AnchorPane();
        mainMenu.setPrefSize(stageWidth, stageHeight);
        mainMenu.setLayoutX(0);
        mainMenu.setLayoutY(0);

        // Create background
        Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SecondBG.png")));
        ImageView bgView = new ImageView(background);
        bgView.setFitWidth(stageWidth + 2);
        bgView.setFitHeight(stageHeight + 2);

        mainMenu.getChildren().add(bgView);

        // Create scroll rect
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(stageWidth - 220, stageHeight - 190);
        scrollPane.setLayoutX(110);
        scrollPane.setLayoutY(100);

        scrollPane.setPannable(true);

        int spacesNum = 0;

        for (Map.Entry<Integer, List<String>> match : matches.entrySet()){
            spacesNum += match.getValue().size() + 1;
        }

        VBox matchList = new VBox();
        matchList.setPrefSize(stageWidth - 200, spacesNum * 50);
        matchList.setLayoutX(0);
        matchList.setLayoutY(0);

        if (matches.isEmpty()) {
            Label noMatches = new Label("  No matches available");
            noMatches.setPrefSize(stageWidth - 50, 100);
            noMatches.setFont(new Font(ERAS_FONT, 24));
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
        Pane addMatchButton = createTextButton("Add Match", 20, "/RectButton200x50.png",
                200, 50, (stageWidth - 200) * 0.5, stageHeight - 75, this::openCreateMatchWindow);

        // Create button to refresh matches
        Pane refreshButton = createIconButton("/icons/Refresh.png",
                55, stageHeight - 90, () -> sender.getMatches());

        // Create reconnection popup
        createReconnectionPopup();

        // Create button for reconnection
        Pane reconnectButton = createTextButton("Reconnect", 20, "/RectButton200x50.png",
                200, 50, (stageWidth - 200) * 0.75, stageHeight - 75, () -> reconnectionPopup.setVisible(true));

        mainMenu.getChildren().add(refreshButton);
        mainMenu.getChildren().add(addMatchButton);
        mainMenu.getChildren().add(reconnectButton);

        root.getChildren().add(mainMenu);

        root.getChildren().add(reconnectionPopup);
    }

    /**
     * Creates the popup for reconnection button
     */
    private void createReconnectionPopup() {
        // Create Popup anchor pane
        reconnectionPopup = new AnchorPane();
        reconnectionPopup.setPrefSize(500, 300);
        reconnectionPopup.setLayoutX(stageWidth - 625);
        reconnectionPopup.setLayoutY(stageHeight - 400);

        // Create background
        Rectangle bgRectangle = new Rectangle();
        bgRectangle.setWidth(500);
        bgRectangle.setHeight(300);
        bgRectangle.setFill(Color.WHITESMOKE);
        bgRectangle.setStroke(Color.BLACK);
        bgRectangle.setStrokeWidth(4);
        bgRectangle.setArcWidth(20);
        bgRectangle.setArcHeight(20);

        // Create insert match ID label
        Label matchIDLabel = new Label("Match ID:");
        matchIDLabel.setPrefSize(300, 50);
        matchIDLabel.setLayoutX(25);
        matchIDLabel.setLayoutY(10);
        matchIDLabel.setFont(new Font(ERAS_FONT, 24));

        // Create match ID text field
        TextField matchIDField = new TextField();
        matchIDField.setPrefSize(400, 50);
        matchIDField.setLayoutX(25);
        matchIDField.setLayoutY(60);
        matchIDField.setPromptText("Enter match ID...");
        matchIDField.setFont(new Font(ERAS_FONT, 24));

        // Create insert nickname label
        Label nicknameLabel = new Label("Nickname:");
        nicknameLabel.setPrefSize(300, 50);
        nicknameLabel.setLayoutX(25);
        nicknameLabel.setLayoutY(160);
        nicknameLabel.setFont(new Font(ERAS_FONT, 24));

        // Create nickname text field
        TextField nicknameField = new TextField();
        nicknameField.setPrefSize(400, 50);
        nicknameField.setLayoutX(25);
        nicknameField.setLayoutY(210);
        nicknameField.setPromptText("Enter nickname...");
        nicknameField.setFont(new Font(ERAS_FONT, 24));

        // Create confirm button
        Pane confirmReconnect = createIconButton("/icons/Confirm.png",
                437.5, 210, () -> {
            try {
                int matchID = Integer.parseInt(matchIDField.getText());
                String nickname = nicknameField.getText();
                sender.reconnectToMatch(matchID, nickname);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });

        // Create close button
        Pane closeButton = createIconButton("/icons/Close.png",
                440, 10, () -> reconnectionPopup.setVisible(false));

        // Fill hierarchy
        reconnectionPopup.getChildren().add(bgRectangle);
        reconnectionPopup.getChildren().add(matchIDLabel);
        reconnectionPopup.getChildren().add(matchIDField);
        reconnectionPopup.getChildren().add(nicknameLabel);
        reconnectionPopup.getChildren().add(nicknameField);
        reconnectionPopup.getChildren().add(confirmReconnect);
        reconnectionPopup.getChildren().add(closeButton);

        reconnectionPopup.setVisible(false);
    }

    /**
     * Creates a 50 x 50 button with an icon and an action
     * @param iconImagePath Path to the icon image
     * @param x X position of the button
     * @param y Y position of the button
     * @param action Action to be executed when the button is clicked
     * @return The created button
     */
    private Pane createIconButton(String iconImagePath, double x, double y, Runnable action) {
        // Create Pane
        Pane button = new Pane();
        button.setPrefSize(50, 50);
        button.setLayoutX(x);
        button.setLayoutY(y);

        // Create image button
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SquareButton.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);
        imageView.setOnMouseClicked((_) -> action.run());

        // Create icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconImagePath)));
        ImageView iconView = new ImageView(icon);
        iconView.setFitWidth((double) 50 * 0.8);
        iconView.setFitHeight((double) 50 * 0.8);
        iconView.setLayoutX((double) 50 * 0.1);
        iconView.setLayoutY((double) 50 * 0.1);
        iconView.setMouseTransparent(true);

        button.getChildren().add(imageView);
        button.getChildren().add(iconView);

        return button;
    }

    /**
     * Creates a pane with the information of a match
     * @param matchID ID of the match
     * @param players List of players in the match
     * @return The created pane
     */
    private Pane createMatchPane(int matchID, List<String> players){
        Pane matchPane = new Pane();
        matchPane.setPrefSize(stageWidth - 50, (players.size() + 1) * 50);

        VBox matchInfo = new VBox();
        matchInfo.setPrefSize(stageWidth - 100, (players.size() + 1) * 50);
        matchInfo.setLayoutX(50);

        Label matchName = new Label("Match " + matchID);
        matchName.setPrefSize(stageWidth - 100, 50);
        matchName.setFont(new Font(ERAS_FONT, 24));

        matchInfo.getChildren().add(matchName);

        for (String player : players){
            Label playerLabel = new Label(player);
            playerLabel.setPrefSize(stageWidth - 100, 50);
            playerLabel.setFont(new Font(ERAS_FONT, 18));

            matchInfo.getChildren().add(playerLabel);
        }

        Button selectButton = new Button();
        selectButton.setPrefSize(stageWidth - 50, (players.size() + 1) * 50);
        selectButton.setOpacity(0.1);

        selectButton.setOnAction((_) -> { // event
            sender.selectMatch(matchID);
            timer.scheduleAtFixedRate(packetLossTask, 0, COUNT_DEC_PERIOD);
        });

        matchPane.getChildren().add(matchInfo);
        matchPane.getChildren().add(selectButton);

        return matchPane;
    }

    /**
     * Opens the window that allows the user to create a match
     */
    private void openCreateMatchWindow(){
        root.getChildren().clear();

        AnchorPane createPane = new AnchorPane();
        createPane.setPrefSize(stageWidth, stageHeight);
        createPane.setLayoutX(0);
        createPane.setLayoutY(0);

        // Create background
        Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SecondBG.png")));
        ImageView bgView = new ImageView(background);
        bgView.setFitWidth(stageWidth + 2);
        bgView.setFitHeight(stageHeight + 2);

        // Create labels
        Label numOfPlayersLabel = new Label("Enter number of players");
        numOfPlayersLabel.setPrefSize(stageWidth, 50);
        numOfPlayersLabel.setLayoutX(0);
        numOfPlayersLabel.setLayoutY(100);
        numOfPlayersLabel.setFont(new Font(ERAS_FONT, 24));
        numOfPlayersLabel.setAlignment(Pos.CENTER);

        Label chooseNickLabel = new Label("Enter nickname");
        chooseNickLabel.setPrefSize(stageWidth, 50);
        chooseNickLabel.setLayoutX(0);
        chooseNickLabel.setLayoutY(350);
        chooseNickLabel.setFont(new Font(ERAS_FONT, 24));
        chooseNickLabel.setAlignment(Pos.CENTER);

        // Create text field for number of players
        TextField playersField = new TextField();
        playersField.setPrefSize(300, 50);
        playersField.setLayoutX((stageWidth - 300) * 0.5);
        playersField.setLayoutY(200);
        playersField.setPromptText("Enter number of players...");
        playersField.setFont(new Font(ERAS_FONT, 24));

        // Create text field for nickname
        TextField nameField = new TextField();
        nameField.setPrefSize(300, 50);
        nameField.setLayoutX((stageWidth - 300) * 0.5);
        nameField.setLayoutY(450);
        nameField.setPromptText("Enter nickname...");
        nameField.setFont(new Font(ERAS_FONT, 24));

        // Create confirm button
        Pane confirmMatch = createTextButton("Create", 20, "/RectButton200x50.png",
                200, 50, (stageWidth - 200) * 0.5, 600, () -> {
            int numOfPlayers = playersField.getText().isEmpty() ? 0 : Integer.parseInt(playersField.getText());

            sender.createMatch(numOfPlayers, nameField.getText());
            timer.scheduleAtFixedRate(packetLossTask, 0, COUNT_DEC_PERIOD);
        });

        createPane.getChildren().add(bgView);
        createPane.getChildren().add(playersField);
        createPane.getChildren().add(nameField);
        createPane.getChildren().add(confirmMatch);
        createPane.getChildren().add(numOfPlayersLabel);
        createPane.getChildren().add(chooseNickLabel);

        root.getChildren().add(createPane);
    }

    /**
     * Opens the window that allows the user to select a nickname when entering a match
     * @param players List of players in the match
     */
    private void openSelectNickWindow(List<String> players){
        root.getChildren().clear();

        AnchorPane joinPane = new AnchorPane();
        joinPane.setPrefSize(stageWidth, stageHeight);
        joinPane.setLayoutX(0);
        joinPane.setLayoutY(0);

        // Create background
        Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SecondBG.png")));
        ImageView bgView = new ImageView(background);
        bgView.setFitWidth(stageWidth + 2);
        bgView.setFitHeight(stageHeight + 2);

        // Create label
        Label joinLabel = new Label("Choose a nickname");
        joinLabel.setPrefSize(stageWidth, 50);
        joinLabel.setLayoutX(0);
        joinLabel.setLayoutY(100);
        joinLabel.setFont(new Font(ERAS_FONT, 24));
        joinLabel.setAlignment(Pos.CENTER);

        // Create text field for nickname
        TextField nameField = new TextField();
        nameField.setPrefSize(200, 50);
        nameField.setLayoutX((stageWidth - 200) * 0.5);
        nameField.setLayoutY(150);
        nameField.setPromptText("Enter nickname...");
        nameField.setFont(new Font(ERAS_FONT, 24));

        // Create confirm button
        Pane confirmNick = createTextButton("Join", 20, "/RectButton200x50.png",
                200, 50, (stageWidth - 200) * 0.5, 200, () -> sender.chooseNickname(nameField.getText()));

        // Create IP label
        Label IPLabel = new Label("IP: " + ip);
        IPLabel.setPrefSize(200, 50);
        IPLabel.setLayoutX(10);
        IPLabel.setLayoutY(stageHeight - 60);
        IPLabel.setFont(new Font(ERAS_FONT, 18));

        // Create curr players label
        Label currPlayersLabel = new Label("Current players:");
        currPlayersLabel.setPrefSize(stageWidth, 50);
        currPlayersLabel.setLayoutX(0);
        currPlayersLabel.setLayoutY(300);
        currPlayersLabel.setFont(new Font(ERAS_FONT, 24));
        currPlayersLabel.setAlignment(Pos.CENTER);

        // Create table of current players
        VBox playerList = new VBox();
        playerList.setPrefSize(stageWidth, stageHeight - 400);
        playerList.setLayoutX(0);
        playerList.setLayoutY(350);

        for (String player : players){
            if (!player.isBlank()) {
                Label playerLabel = new Label("- " + player);
                playerLabel.setPrefSize(stageWidth - 100, 50);
                playerLabel.setFont(new Font(ERAS_FONT, 24));
                playerLabel.setAlignment(Pos.CENTER);

                playerList.getChildren().add(playerLabel);
            }
        }

        joinPane.getChildren().add(bgView);
        joinPane.getChildren().add(nameField);
        joinPane.getChildren().add(confirmNick);
        joinPane.getChildren().add(IPLabel);
        joinPane.getChildren().add(currPlayersLabel);
        joinPane.getChildren().add(playerList);
        joinPane.getChildren().add(joinLabel);

        root.getChildren().add(joinPane);
    }

    /**
     * Updates the window: if the match is full it will start the game, else it will show a loading screen
     * @param nick Nickname chosen by the player
     */
    private void selectNickname(String nick){
        root.getChildren().clear();

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(stageWidth, stageHeight);
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);

        // Create background
        Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SecondBG.png")));
        ImageView bgView = new ImageView(background);
        bgView.setFitWidth(stageWidth + 2);
        bgView.setFitHeight(stageHeight + 2);

        // Create label
        Label waitingLabel = new Label("Waiting for other players...\nEntering match " + matchID + " with nick " + nick);
        waitingLabel.setPrefSize(stageWidth, stageHeight);
        waitingLabel.setLayoutX(0);
        waitingLabel.setLayoutY(0);
        waitingLabel.setFont(new Font(ERAS_FONT, 32));
        waitingLabel.setAlignment(Pos.CENTER);

        // Create animation to update the label
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), _ -> waitingLabel.setText("Waiting for other players...\nEntering match " + matchID + " with nick " + nick + ".")),
                new KeyFrame(Duration.seconds(2), _ -> waitingLabel.setText("Waiting for other players...\nEntering match " + matchID + " with nick " + nick + "..")),
                new KeyFrame(Duration.seconds(3), _ -> waitingLabel.setText("Waiting for other players...\nEntering match " + matchID + " with nick " + nick + "...")));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        anchorPane.getChildren().add(bgView);
        anchorPane.getChildren().add(waitingLabel);

        root.getChildren().add(anchorPane);
    }

    /**
     * Shows the game start screen
     * @param IDToNicknameMap Map of player IDs to nicknames
     * @param resDeckCardID ID of the resource deck card
     * @param visibleResCardID1 ID of the first visible resource card
     * @param visibleResCardID2 ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param visibleGoldCardID1 ID of the first visible gold card
     * @param visibleGoldCardID2 ID of the second visible gold card
     * @param starterCardID ID of the starter card
     */
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

    /**
     * Calculates all the dimensions and creates all the panes needed for the game
     */
    private void setPlayArea(){
        // Calculate card dimensions
        cardWidth = (stageWidth - (9 * defaultElementsOffset)) / 7;
        cardHeight = cardWidth / 1.5;

        commonAreaWidth = 3 * (defaultElementsOffset + cardWidth) + 10;

        double playerFieldRectWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
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
        double playerFieldWidth = gridCellWidth * gridColumns + 3 * cardCornerWidthProportion * cardWidth;
        double playerFieldHeight = gridCellHeight * gridRows + 3 * cardCornerHeightProportion * cardHeight;

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
        playerFieldScroll.setStyle("-fx-background: #f5f3e5; -fx-background-color: #f5f3e5;");

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

        // Create background for hand Area
        Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BackgroundHandArea.png")));
        ImageView bgView = new ImageView(background);
        bgView.setLayoutY(0);
        bgView.setLayoutX(0);
        bgView.setFitHeight(playerHandHeight);
        bgView.setFitWidth(playerHandWidth);

        playerHandPane.getChildren().add(bgView);

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

        // Create background
        Image backgroundCommonArea = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BackgroundCommonArea.png")));
        ImageView bgViewCommonArea = new ImageView(backgroundCommonArea);
        bgViewCommonArea.setLayoutY(0);
        bgViewCommonArea.setLayoutX(0);
        bgViewCommonArea.setFitHeight(stageHeight);
        bgViewCommonArea.setFitWidth(commonAreaWidth);

        commonAreaPane.getChildren().add(bgViewCommonArea);

        // Create label for current player's turn
        currPlayerTurnLabel = new Label("Current player: None");
        currPlayerTurnLabel.setPrefSize(commonAreaWidth, 50);
        currPlayerTurnLabel.setLayoutX(50);
        currPlayerTurnLabel.setLayoutY(0);
        currPlayerTurnLabel.setFont(new Font(ERAS_FONT, 24));

        commonAreaPane.getChildren().add(currPlayerTurnLabel);

        // Create chat button
        Pane openChatButton = createIconButton("/icons/Chat.png",
                0, stageHeight / 2 - 25, this::openCloseChat);

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

    /**
     * Creates the help pane
     */
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
        helpBackground.setOpacity(1);

        // Create background for help Area
        Image backgroundHelp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BackgroundHandArea.png")));
        ImageView bgHelpView = new ImageView(backgroundHelp);
        bgHelpView.setLayoutY(0);
        bgHelpView.setLayoutX(0);
        bgHelpView.setFitHeight(stageHeight * 0.5);
        bgHelpView.setFitWidth(stageWidth * 0.5);

        // Black edge background
        Rectangle edgeRect = new Rectangle();
        edgeRect.setWidth(stageWidth * 0.5);
        edgeRect.setHeight(stageHeight * 0.5);
        edgeRect.setArcWidth(10);
        edgeRect.setArcHeight(10);
        edgeRect.setFill(Color.TRANSPARENT);
        edgeRect.setStrokeWidth(4);
        edgeRect.setStroke(Color.BLACK);

        // Create title label
        Label helpLabel = new Label("Help");
        helpLabel.setPrefSize(stageWidth * 0.5, 50);
        helpLabel.setLayoutX(0);
        helpLabel.setLayoutY(10);
        helpLabel.setFont(new Font(ERAS_FONT, 24));
        helpLabel.setAlignment(Pos.CENTER);

        // Create global chat help label
        Label globalChatHelpLabel = new Label("Global chat: Type a message and press enter to send it to all players.");
        globalChatHelpLabel.setPrefSize(stageWidth * 0.5, 50);
        globalChatHelpLabel.setLayoutX(0);
        globalChatHelpLabel.setLayoutY(100);
        globalChatHelpLabel.setFont(new Font(ERAS_FONT, 18));
        globalChatHelpLabel.setWrapText(true);
        globalChatHelpLabel.setAlignment(Pos.CENTER);

        // Create private chat help label
        Label privateChatHelpLabel = new Label("Private chat: Type /whisper <player> <message> to send a private message to a player.");
        privateChatHelpLabel.setPrefSize(stageWidth * 0.5, 50);
        privateChatHelpLabel.setLayoutX(0);
        privateChatHelpLabel.setLayoutY(175);
        privateChatHelpLabel.setFont(new Font(ERAS_FONT, 18));
        privateChatHelpLabel.setWrapText(true);
        privateChatHelpLabel.setAlignment(Pos.CENTER);

        // Create zoom help label
        Label zoomHelpLabel = new Label("Zoom: Hold the Ctrl key and press arrow key up or down to zoom in or out.");
        zoomHelpLabel.setPrefSize(stageWidth * 0.5, 50);
        zoomHelpLabel.setLayoutX(0);
        zoomHelpLabel.setLayoutY(250);
        zoomHelpLabel.setFont(new Font(ERAS_FONT, 18));
        zoomHelpLabel.setWrapText(true);
        zoomHelpLabel.setAlignment(Pos.CENTER);

        // Create close button
        Pane closeButton = createIconButton("/icons/Close.png",
                stageWidth - 100, 0, () -> root.getChildren().remove(helpPane));

        // Add to hierarchy
        helpBackground.getChildren().add(bgHelpView);
        helpBackground.getChildren().add(helpLabel);
        helpBackground.getChildren().add(globalChatHelpLabel);
        helpBackground.getChildren().add(privateChatHelpLabel);
        helpBackground.getChildren().add(zoomHelpLabel);
        helpBackground.getChildren().add(edgeRect);

        helpPane.getChildren().add(backgroundRect);
        helpPane.getChildren().add(helpBackground);
        helpPane.getChildren().add(closeButton);

        // Create help button
        Pane helpButton = createIconButton("/icons/Help.png",
                commonAreaWidth - 50, 0, () -> root.getChildren().add(helpPane));

        commonAreaPane.getChildren().add(helpButton);
    }

    /**
     * Updates the window to show the field of the other player
     * @param playerNickname Nickname of the player
     * @param playerCircle Circle representing the player
     * @param playerRectangle Rectangle representing the player
     */
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

    /**
     * Create the hand pane of the other player
     * @param opponentHand List of cards in the hand of the opponent
     * @param color Color of the opponent
     * @return The created pane
     */
    private AnchorPane createOpponentHandPane(List<PlaceableCard> opponentHand, TokenColors color) {
        double playerHandWidth = playerHandPane.getPrefWidth();
        double playerHandHeight = playerHandPane.getPrefHeight();

        AnchorPane opponentHandPane = new AnchorPane();
        opponentHandPane.setPrefSize(playerHandWidth, playerHandHeight);
        opponentHandPane.setLayoutX(0);
        opponentHandPane.setLayoutY(0);

        // Create background for opponent hand Area
        Image background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BackgroundHandArea.png")));
        ImageView bgView = new ImageView(background);
        bgView.setLayoutY(0);
        bgView.setLayoutX(0);

        bgView.setFitHeight(playerHandHeight);
        bgView.setFitWidth(playerHandWidth);
        opponentHandPane.getChildren().add(bgView);

        double cardsYPos = (opponentHandPane.getPrefHeight() - cardHeight) * 0.5;

        for (int i = 0; i < 3; i++) {
            if (i < opponentHand.size() && opponentHand.get(i) != null) {
                Pane cardPane = createCardPane(opponentHand.get(i).getID(), CardSideType.BACK, (i + 2) * defaultElementsOffset + (i + 1) * cardWidth, cardsYPos, 1);

                opponentHandPane.getChildren().add(cardPane);
            }
        }

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

        return opponentHandPane;
    }

    /**
     * Creates the play area of the other player
     * @param playArea Map of the play area of the opponent
     * @return The created pane
     */
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

    /**
     * Creates the chat pane
     */
    private void createChatPane() {
        chatPane = new AnchorPane();
        chatPane.setPrefSize(commonAreaWidth - 100, stageHeight - 100);
        chatPane.setLayoutX(1);
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

        // Background
        Rectangle chatBG = new Rectangle();
        chatBG.setWidth(commonAreaWidth - 110);
        chatBG.setHeight(stageHeight - 150);
        chatBG.setFill(Color.web("#f5f3e5"));

        chatBox.getChildren().add(chatBG);

        // Black edge
        Rectangle chatEdge = new Rectangle();
        chatEdge.setWidth(commonAreaWidth - 100);
        chatEdge.setHeight(stageHeight - 150);
        chatEdge.setArcWidth(10);
        chatEdge.setArcHeight(10);
        chatEdge.setFill(Color.TRANSPARENT);
        chatEdge.setStrokeWidth(2);
        chatEdge.setStroke(Color.BLACK);

        AnchorPane chatInputPane = new AnchorPane();
        chatInputPane.setPrefSize(commonAreaWidth - 100, 50);
        chatInputPane.setLayoutX(0);
        chatInputPane.setLayoutY(stageHeight - 100);

        // Create text input field
        TextField chatInput = new TextField();
        chatInput.setPrefSize(commonAreaWidth - 200, 50);
        chatInput.setLayoutX(0);
        chatInput.setLayoutY(0);
        chatInput.setPromptText("Enter message...");
        chatInput.setFont(new Font(ERAS_FONT, 18));

        // Create send button
        Pane sendButton = createIconButton("/icons/Send.png",
                commonAreaWidth - 75, 0, () -> {
            if (chatInput.getText().isEmpty() || chatInput.getText().isBlank()) {
                return;
            }

            if (chatInput.getText().startsWith("/")) {
                String[] command = chatInput.getText().split(" ");
                if (command[0].equals("/whisper") && command.length >= 3 && !command[1].equals(nickname)) {
                    sender.sendChatMessage(playerID, command[1], chatInput.getText().substring(command[0].length() + command[1].length() + 2));
                }
                else {
                    showError("Invalid chat format.");
                }
            }
            else {
                sender.sendChatMessage(playerID, "all", chatInput.getText());
            }

            chatInput.clear();
        });

        // Create close chat button
        Pane closeChatButton = createIconButton("/icons/Close.png",
                commonAreaWidth - 50, 0, this::openCloseChat);

        chatInputPane.getChildren().add(chatInput);
        chatInputPane.getChildren().add(sendButton);

        chatScroll.setContent(chatBox);
        chatPane.getChildren().add(chatScroll);
        chatPane.getChildren().add(chatInputPane);
        chatPane.getChildren().add(closeChatButton);
        chatPane.getChildren().add(chatEdge);

        chatOpen = false;
    }

    /**
     * Opens or closes the chat
     */
    private void openCloseChat() {
        if (chatOpen) {
            commonAreaPane.getChildren().remove(chatPane);
        }
        else {
            commonAreaPane.getChildren().add(chatPane);
        }

        chatOpen = !chatOpen;
    }

    /**
     * Shows the choice of the starter card side
     * @param starterCardID ID of the starter card
     */
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
        backgroundPane.setOpacity(1);

        // Create background for starter card area
        Image starterCardBG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BackgroundHandArea.png")));
        ImageView bgView = new ImageView(starterCardBG);
        bgView.setLayoutX(2);
        bgView.setLayoutY(2);
        bgView.setFitHeight(stageHeight * 0.5 - 3);
        bgView.setFitWidth(stageWidth * 0.5 - 3);

        // Black edge
        Rectangle edgeRect = new Rectangle();
        edgeRect.setWidth(stageWidth * 0.5);
        edgeRect.setHeight(stageHeight * 0.5);
        edgeRect.setArcWidth(10);
        edgeRect.setArcHeight(10);
        edgeRect.setFill(Color.TRANSPARENT);
        edgeRect.setStrokeWidth(4);
        edgeRect.setStroke(Color.BLACK);

        // Create label
        Label choiceLabel = new Label("Choose the side of the starter card");
        choiceLabel.setPrefSize(stageWidth * 0.5, 50);
        choiceLabel.setLayoutX(0);
        choiceLabel.setLayoutY(30);
        choiceLabel.setFont(new Font(ERAS_FONT, 24));
        choiceLabel.setAlignment(Pos.CENTER);

        // Render starter card
        Pane starterCardPane = createCardPane(starterCardID, CardSideType.FRONT, (stageWidth - cardWidth) * 0.5, (stageHeight - cardHeight) * 0.5 - 25, 1);
        starterCard.setCurrSideType(CardSideType.FRONT);

        // Render a button to flip the card
        Pane flipButton = createTextButton("Flip", 20, "/RectButton200x50.png",
                100, 50, ((stageWidth - 100) * 0.5 - 120), ((stageHeight + cardHeight) * 0.5 + 30),
                () -> {
                    // Flip the card
                    flipStarterCard(starterCardPane);
                });

        // Render a button to place the starter card
        Pane placeButton = createTextButton("Place", 20, "/RectButton200x50.png",
                100, 50, ((stageWidth - 100) * 0.5 + 120), ((stageHeight + cardHeight) * 0.5 + 30),
                () -> {
                    // Flip the card
                    sender.placeStarterCard(playerID, starterCard.getCurrSideType());
                });

        backgroundPane.getChildren().add(bgView);
        backgroundPane.getChildren().add(edgeRect);
        backgroundPane.getChildren().add(choiceLabel);

        // Add all elements to the root
        tempChoicePane.getChildren().add(paneRect);
        tempChoicePane.getChildren().add(backgroundPane);
        tempChoicePane.getChildren().add(flipButton);
        tempChoicePane.getChildren().add(placeButton);
        tempChoicePane.getChildren().add(starterCardPane);

        root.getChildren().add(tempChoicePane);
    }

    /**
     * Flips the starter card
     * @param starterCardPane Pane of the starter card
     */
    private void flipStarterCard(Pane starterCardPane){
        starterCard.setCurrSideType(starterCard.getCurrSideType() == CardSideType.FRONT ? CardSideType.BACK : CardSideType.FRONT);

        Pane newPane = createCardPane(starterCard.getID(), starterCard.getCurrSideType(), starterCardPane.getLayoutX(), starterCardPane.getLayoutY(), 1);
        tempChoicePane.getChildren().add(newPane);

        tempChoicePane.getChildren().remove(starterCardPane);
    }

    /**
     * Shows the cards in the common area (or draw area)
     * @param resDeckCardID ID of the resource deck card
     * @param resVisibleCard1ID ID of the first visible resource card
     * @param resVisibleCard2ID ID of the second visible resource card
     * @param goldDeckCardID ID of the gold deck card
     * @param goldVisibleCard1ID ID of the first visible gold card
     * @param goldVisibleCard2ID ID of the second visible gold card
     */
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

    /**
     * Draws the player's hand, the common objectives and the secret objective choice
     * @param resourceCardID1 ID of the first resource card
     * @param resourceCardID2 ID of the second resource card
     * @param goldenCardID ID of the golden card
     * @param tokenColor Color of the player
     * @param commonObjectiveCardID1 ID of the first common objective card
     * @param commonObjectiveCardID2 ID of the second common objective card
     * @param secretObjectiveCardID1 ID of the first secret objective card
     * @param secretObjectiveCardID2 ID of the second secret objective card
     */
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

    /**
     * Draws the common objectives
     * @param objective1ID ID of the first objective
     * @param objective2ID ID of the second objective
     */
    private void renderCommonObjectives(int objective1ID, int objective2ID){
        Pane objective1Pane = createCardPane(objective1ID, CardSideType.FRONT, commonAreaWidth / 2 - cardWidth - 30, 350, 1);
        Pane objective2Pane = createCardPane(objective2ID, CardSideType.FRONT, commonAreaWidth / 2 + 30, 350, 1);

        commonAreaPane.getChildren().add(objective1Pane);
        commonAreaPane.getChildren().add(objective2Pane);
    }

    /**
     * Draws the secret objective choice
     * @param secretObjective1ID ID of the first secret objective
     * @param secretObjective2ID ID of the second secret objective
     */
    private void renderSecretObjectiveChoice(int secretObjective1ID, int secretObjective2ID){
        Rectangle paneRect = new Rectangle();
        paneRect.setWidth(stageWidth);
        paneRect.setHeight(stageHeight);
        paneRect.setFill(Color.rgb(0, 0, 0, 0.3));

        // Create white background anchor pane
        AnchorPane backgroundPane = new AnchorPane();
        backgroundPane.setPrefSize(stageWidth * 0.5, stageHeight * 0.5);
        backgroundPane.setLayoutX(stageWidth * 0.25);
        backgroundPane.setLayoutY(stageHeight * 0.25);
        backgroundPane.setOpacity(1);

        // Create background for starter card area
        Image starterCardBG = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BackgroundHandArea.png")));
        ImageView bgView = new ImageView(starterCardBG);
        bgView.setLayoutX(2);
        bgView.setLayoutY(2);
        bgView.setFitHeight(stageHeight * 0.5 - 3);
        bgView.setFitWidth(stageWidth * 0.5 - 3);

        // Black edge
        Rectangle edgeRect = new Rectangle();
        edgeRect.setWidth(stageWidth * 0.5);
        edgeRect.setHeight(stageHeight * 0.5);
        edgeRect.setArcWidth(10);
        edgeRect.setArcHeight(10);
        edgeRect.setFill(Color.TRANSPARENT);
        edgeRect.setStrokeWidth(4);
        edgeRect.setStroke(Color.BLACK);

        // Create label
        Label choiceLabel = new Label("Choose your secret objective");
        choiceLabel.setPrefSize(stageWidth * 0.5, 50);
        choiceLabel.setLayoutX(0);
        choiceLabel.setLayoutY(30);
        choiceLabel.setFont(new Font(ERAS_FONT, 24));
        choiceLabel.setAlignment(Pos.CENTER);

        // Create secret objective panes
        Pane secretObjective1Pane = createCardPane(secretObjective1ID, CardSideType.FRONT, stageWidth * 0.5 - cardWidth - 50, (stageHeight - cardHeight) * 0.5, 1);
        Pane secretObjective2Pane = createCardPane(secretObjective2ID, CardSideType.FRONT, stageWidth * 0.5 + 50, (stageHeight - cardHeight) * 0.5, 1);

        // Create button to select 1st secret objective
        Pane secretObjective1Button = createTextButton("1st objective", 20, "/RectButton200x50.png",
                cardWidth - 26, 50, (stageWidth * 0.5 - ((cardWidth - 26) * 0.5)) - 50 - (cardWidth *0.5), ((stageHeight - cardHeight) * 0.5) + 200,
                () -> {
                    secretObjectiveCardID = secretObjective1ID;
                    sender.chooseSecretObjective(playerID, secretObjectiveCardID);
                });

        // Create button to select 2nd secret objective
        Pane secretObjective2Button = createTextButton("2nd objective", 20, "/RectButton200x50.png",
                cardWidth - 26, 50, (stageWidth * 0.5 - ((cardWidth - 26) * 0.5)) + 50 + (cardWidth *0.5), ((stageHeight - cardHeight) * 0.5) + 200,
                () -> {
                    secretObjectiveCardID = secretObjective2ID;
                    sender.chooseSecretObjective(playerID, secretObjectiveCardID);
                });

        backgroundPane.getChildren().add(bgView);
        backgroundPane.getChildren().add(edgeRect);
        backgroundPane.getChildren().add(choiceLabel);

        tempChoicePane.getChildren().add(paneRect);
        tempChoicePane.getChildren().add(backgroundPane);
        tempChoicePane.getChildren().add(secretObjective1Pane);
        tempChoicePane.getChildren().add(secretObjective2Pane);
        tempChoicePane.getChildren().add(secretObjective1Button);
        tempChoicePane.getChildren().add(secretObjective2Button);
    }

    /**
     * Adds a button the card to draw it
     * @param cardPane Pane of the card
     * @param cardIndex Index of the card
     * @param isGolden True if the card is golden, false otherwise
     * @param isVisible True if the card is visible, false otherwise
     */
    private void addDrawButtonToCard(Pane cardPane, int cardIndex, boolean isGolden, boolean isVisible){
        Button drawButton = new Button();
        drawButton.setPrefSize(cardPane.getPrefWidth(), cardPane.getPrefHeight());
        drawButton.setLayoutX(0);
        drawButton.setLayoutY(0);
        drawButton.setOpacity(0.1);

        drawButton.setOnAction((_) -> {
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

    /**
     * Creates a card's pane
     * @param cardID ID of the card
     * @param side Side of the card
     * @param xPos X position of the card
     * @param yPos Y position of the card
     * @param scale Scale of the card
     * @return The created pane
     */
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

    /**
     * Calculates the X offset of the card by the scale
     * @param scale Scale of the card
     * @return The offset
     */
    private double xOffsetByScale(double scale){
        return (1 - scale) * cardWidth / 2;
    }

    /**
     * Calculates the Y offset of the card by the scale
     * @param scale Scale of the card
     * @return The offset
     */
    private double yOffsetByScale(double scale){
        return (1 - scale) * cardHeight / 2;
    }

    /**
     * Draws the buttons that allow the player to place a card in the play area
     * @param ID ID of the placed card
     * @param pos Position of the card
     */
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

    /**
     * Adds a place button at a given position
     * @param pos Position of the button
     */
    private void addPlaceButtonAtPos(Position pos) {
        Pane positionPane = new Pane();
        positionPane.setPrefSize(gridCellWidth, gridCellHeight);

        Button placeButton = new Button();
        placeButton.setPrefSize(cardWidth, cardHeight);
        placeButton.setLayoutX(-cardWidth * cardCornerWidthProportion);
        placeButton.setLayoutY(-cardHeight * cardCornerHeightProportion);
        placeButton.setOpacity(0.6);
        placeButton.setOnAction((_) -> sender.placeCard(playerID, selectedCard.getID(), selectedCard.getCurrSideType(), pos));

        positionPane.getChildren().add(placeButton);
        Position buttonGridPos = mapToGridPos(pos);
        playerFieldGrid.add(positionPane, buttonGridPos.getX(), buttonGridPos.getY());
        validPosToButtonPane.put(pos, positionPane);
    }

    /**
     * Updates the list of legal and illegal positions after a card has been placed
     * @param pos Position of the placed card
     */
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

    /**
     * Calculates the grid position from the map position
     * @param mapPos Position on the map
     * @return Position on the grid
     */
    private Position mapToGridPos(Position mapPos){
        return new Position(mapPos.getX() + gridRows / 2, gridColumns / 2 - mapPos.getY());
    }

    /**
     * Places a card in the play area
     * @param card Card to place
     * @param pos Position to place the card
     */
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
    }

    /**
     * Updates the dimensions of the grid
     */
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

    /**
     * The player draws a card
     * @param drawnCardID ID of the drawn card
     */
    private void drawCard(int drawnCardID) {
        PlaceableCard drawnCard = GameResources.getPlaceableCardByID(drawnCardID);

        playerHand.add(drawnCard);

        Pane cardPane = createCardInHandPane(drawnCard, placedCardIndexInHand);

        addSelectButtonToCard(cardPane, drawnCard, placedCardIndexInHand);

        playerHandPane.getChildren().add(cardPane);
    }

    /**
     * Draws the player's hand
     * @param card1ID ID of the first card
     * @param card2ID ID of the second card
     * @param card3ID ID of the third card
     * @param playerColor Color of the player
     */
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

    /**
     * Creates a pane for a card in the player's hand
     * @param card Card to create the pane for
     * @param index Index of the card
     * @return The created pane
     */
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
        Pane flipButton = createIconButton("/icons/Flip.png",
                (cardWidth - 50) / 2, cardHeight + 10, () -> flipCard(card, cardImgV));

        cardPane.getChildren().add(flipButton);

        return cardPane;
    }

    /**
     * Flips a card
     * @param card Card to flip
     * @param imageView Image view of the card
     */
    private void flipCard(PlaceableCard card, ImageView imageView) {
        // Flip placeable card
        card.setCurrSideType(card.getCurrSideType() == CardSideType.FRONT ? CardSideType.BACK : CardSideType.FRONT);

        // Change image view
        String cardName = "/cards/" + card.getCurrSideType().toString().toLowerCase() + "/card" + card.getID() + ".png";

        Image cardImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(cardName)));
        imageView.setImage(cardImg);
    }

    /**
     * Creates a button that allows the player to select a card
     * @param cardPane Pane of the card
     * @param card Card to select
     * @param index Index of the card
     */
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

    /**
     * Shows an error message
     * @param errorMessage Error message to show
     */
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
        errorLabel.setFont(new Font(ERAS_FONT, 24));
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

    /**
     * Shows a system message
     * @param systemMessage System message to show
     */
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
        messageLabel.setFont(new Font(ERAS_FONT, 24));
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

    /**
     * Draws some lines in the stage to separate the different areas
     */
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

    /**
     * Detects if the player is pressing some keys
     * @param key Key event
     */
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

    /**
     * Detects if the player has released some keys
     * @param key Key event
     */
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
            if (timeline != null){
                timeline.stop();
            }

            drawGameStart(IDToNicknameMap, resDeckCardID, visibleResCardID1, visibleResCardID2,
                    goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2, starterCardID);

            // Add events for zooming
            stage.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
            stage.addEventHandler(KeyEvent.KEY_RELEASED, this::keyReleased);
        });
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

    @Override
    public void showFirstPlayerTurnAfterRec(int playerID, int[] playerIDs, int[] starterCardIDs, CardSideType[] starterCardSides, TokenColors[] tokenColors, int[][] playerHands) {
        // TODO: Implement
    }

    /**
     * Creates the section of the common area that shows the other players information
     */
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

    /**
     * Creates the label for a player in the common area
     * @param playerNick Nickname of the player
     * @param color Color of the player
     * @param posMultiplier Multiplier for the position
     * @return The created pane
     */
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
        playerLabel.setFont(new Font(ERAS_FONT, 24));

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

            Label chatLabel = new Label(fullMessage);
            chatLabel.setPrefSize(commonAreaWidth - 25, 25);
            chatLabel.setLayoutX(0);
            chatLabel.setLayoutY(0);
            chatLabel.setFont(new Font(ERAS_FONT, 16));
            chatLabel.setTextFill(Color.BLACK);

            chatBox.getChildren().add(chatLabel);

            chatBoxHeight += 25;
            chatBox.setPrefHeight(chatBoxHeight);
            chatBox.setLayoutY(250 - chatBoxHeight);
        });
    }

    @Override
    public void showRanking(int lastPlayerID, int cardID, Position pos, CardSideType side, int deltaScore, String[] nicknames, int[] scores, int[] numOfObjectives) {
        Platform.runLater(() -> {
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
        });
    }

    /**
     * Draws the match ranking
     * @param nicknames Nicknames of the players
     * @param scores Scores of the players
     * @param numOfSecretObjectives Number of secret objectives completed by the players
     */
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
        rankingLabel.setFont(new Font(ERAS_FONT, 32));
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
            playerLabel.setFont(new Font(ERAS_FONT, 24));
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

    /**
     * Shows the maps of the players at the end of the game
     * @param playersNum Number of players
     */
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
        Pane returnButton = createIconButton("/icons/Back.png",
                0, 0, () -> root.getChildren().remove(endgameMapsPane));

        endgameMapsPane.getChildren().add(returnButton);
    }

    /**
     * Renders the play area of a player
     * @param playArea Play area of the player
     * @param colors Colors of the player
     * @param xPos X position of the play area
     * @param yPos Y position of the play area
     * @param width Width of the play area
     * @param height Height of the play area
     */
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

        int columns = maxX - minX + 1;
        int rows = maxY - minY + 1;

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
        Platform.runLater(() -> {
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
        });
    }

    @Override
    public void showPlayerDisconnection(int playerID) {
        Platform.runLater(() -> showSystemMessage(getPlayerNickname(playerID) + " has disconnected."));
    }

    @Override
    public void showPlayerDisconnectionWithOnePlayerLeft(int playerID) {
        showSystemMessage(getPlayerNickname(playerID) + " has disconnected. You are the only player left in the game.");
    }

    @Override
    public void reconnectOnStarterPlacement(int playerID, Map<Integer, String> IDToOpponentNickname, int[] gameSetupCards) {
        Platform.runLater(() -> {
            startConnectionCheck();

            drawGameStart(IDToOpponentNickname, gameSetupCards[0], gameSetupCards[1], gameSetupCards[2],
                    gameSetupCards[3], gameSetupCards[4], gameSetupCards[5], gameSetupCards[6]);
        });
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
                        showConnectionLoss();
                        cancel();
                    }
                    packetLoss++;
                }
            }
        };
        timer.scheduleAtFixedRate(packetLossTask, 0, COUNT_DEC_PERIOD);
    }

    @Override
    public void reconnectOnObjectiveChoice(int playerID, Map<Integer, String> IDToOpponentNickname, int[] gameSetupCards, CardSideType starterSide, TokenColors tokenColor) {
        Platform.runLater(() -> {
            startConnectionCheck();

            setID(playerID);

            // Draw game start
            GameResources.initializeAllDecks();

            root.getChildren().clear();

            for (Map.Entry<Integer, String> entry : IDToOpponentNickname.entrySet()){
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
            renderCommonCards(gameSetupCards[0], gameSetupCards[1], gameSetupCards[2], gameSetupCards[3], gameSetupCards[4], gameSetupCards[5]);

            // Add vertical and horizontal lines to separate player field, hand and common view
            drawSeparationLines();


            // Set missing set up
            tempChoicePane.getChildren().clear();
            this.tokenColor = tokenColor;

            starterCard = GameResources.getPlaceableCardByID(gameSetupCards[13]);
            starterCard.setCurrSideType(starterSide);

            starterCard.setPriority(priority);
            priority++;

            placeCardAction(starterCard, new Position(0, 0));

            drawMissingSetUp(gameSetupCards[6], gameSetupCards[7], gameSetupCards[8], tokenColor, gameSetupCards[9], gameSetupCards[10], gameSetupCards[11], gameSetupCards[12]);
        });
    }

    @Override
    public void reconnectAfterSetup(int playerID, Map<Integer, String> idToNicknameMap, int[] gameSetupCards,
                                    CardSideType starterSide, TokenColors tokenColor) {
        Platform.runLater(() -> {
            startConnectionCheck();

            GameResources.initializeAllDecks();

            setID(playerID);
            this.tokenColor = tokenColor;

            for (Map.Entry<Integer, String> entry : idToNicknameMap.entrySet()) {
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

            // Create the player's view
            root.getChildren().clear();
            setPlayArea();

            // Create common cards section (and common objectives)
            renderCommonCards(gameSetupCards[0], gameSetupCards[1], gameSetupCards[2],
                    gameSetupCards[3], gameSetupCards[4], gameSetupCards[5]);
            renderCommonObjectives(gameSetupCards[9], gameSetupCards[10]);

            // Place starter card
            PlaceableCard starterCard = GameResources.getPlaceableCardByID(gameSetupCards[12]);

            starterCard.setCurrSideType(starterSide);
            starterCard.setPriority(priority);
            priority++;

            placeCardAction(starterCard, new Position(0, 0));

            // Create player hand's section
            drawPlayerHand(gameSetupCards[6], gameSetupCards[7], gameSetupCards[8], TokenColors.getColorFromToken(tokenColor));

            // Place secret objective in player hand
            secretObjectiveCardID = gameSetupCards[11];
            double cardsYPos = (playerHandPane.getPrefHeight() - cardHeight) * 0.5;
            Pane secretObjectivePane = createCardPane(secretObjectiveCardID, CardSideType.FRONT, defaultElementsOffset + xOffsetByScale(0.9), cardsYPos + yOffsetByScale(0.9), 0.9);

            playerHandPane.getChildren().add(secretObjectivePane);

            // Create separator lines
            drawSeparationLines();

            showSystemMessage("You have successfully reconnected to the match.");
        });
    }

    @Override
    public void reconnect(int playerID, int[] playersIDs, String[] playersNicknames, TokenColors[] playersTokenColors,
                          int[][] playersHands, List<Position[]> playersPlacedCardsPos, List<int[]> playersPlacedCardsIDs,
                          List<CardSideType[]> playersPlacedCardsSides, List<int[]> playersPlacedCardsPriorities,
                          int[] playersScores, int[] gameSetupCards, int currPlayerID) {
        Platform.runLater(() -> {
            startConnectionCheck();

            GameResources.initializeAllDecks();

            // Set the player ID
            setID(playerID);
            int numOfPlayers = playersIDs.length;

            // Create the player's view
            root.getChildren().clear();
            setPlayArea();

            // For all players in the match
            for (int i = 0; i < numOfPlayers; i++) {
                // If the ID is the same as the player's ID
                if (playersIDs[i] == playerID) {
                    // Set the player's nickname
                    nickname = playersNicknames[i];
                    // Set the player's token color
                    tokenColor = playersTokenColors[i];

                    // Set the player's hand
                    for (int cardID : playersHands[i]) {
                        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);
                        this.playerHand.add(card);
                    }

                    priority = 0;
                    // Set the player's play area
                    for (int j = 0; j < playersPlacedCardsIDs.get(i).length; j++) {
                        PlaceableCard card = GameResources.getPlaceableCardByID(playersPlacedCardsIDs.get(i)[j]);

                        card.setCurrSideType(playersPlacedCardsSides.get(i)[j]);
                        card.setPriority(playersPlacedCardsPriorities.get(i)[j]);

                        placeCardAction(card, playersPlacedCardsPos.get(i)[j]);

                        priority++;
                    }
                    // Set the player's score
                    score = playersScores[i];
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

            // Create other players' section
            createOtherPlayerSection();

            // Update players scores
            for (PlayerViewModel opponent : opponents) {
                nicknameToScoreLabel.get(opponent.getNickname()).setText(opponent.getNickname() + ": " + opponent.getScore() + " points");
            }

            // Create common cards' section
            renderCommonCards(gameSetupCards[3], gameSetupCards[4], gameSetupCards[5], gameSetupCards[6], gameSetupCards[7], gameSetupCards[8]);
            renderCommonObjectives(gameSetupCards[1], gameSetupCards[2]);

            // Create player's hand section
            drawPlayerHand(playerHand.getFirst().getID(), playerHand.get(1).getID(), playerHand.get(2).getID(), TokenColors.getColorFromToken(tokenColor));

            // Place secret objective in player hand
            secretObjectiveCardID = gameSetupCards[0];
            double cardsYPos = (playerHandPane.getPrefHeight() - cardHeight) * 0.5;
            Pane secretObjectivePane = createCardPane(secretObjectiveCardID, CardSideType.FRONT, defaultElementsOffset + xOffsetByScale(0.9), cardsYPos + yOffsetByScale(0.9), 0.9);

            playerHandPane.getChildren().add(secretObjectivePane);

            // Create separator lines
            drawSeparationLines();

            showSystemMessage("You have successfully reconnected to the match.");
        });
    }

    @Override
    public void showPlayerReconnection(int playerID) {
        Platform.runLater(() -> showSystemMessage(getPlayerNickname(playerID) + " has reconnected."));
    }

    @Override
    public void undoCardPlacement(int playerID, Position pos, int score, int nextPlayerID) {
        Platform.runLater(() -> {
            showSystemMessage(getPlayerNickname(playerID) + " has disconnected.");

            if (this.playerID == nextPlayerID) {
                showSystemMessage("It's your turn.");
            }
            else if (nextPlayerID == -1) {
                showSystemMessage("You are the only player left in the game. Please wait...");
            }
            else {
                showSystemMessage("It's " + getPlayerNickname(nextPlayerID) + "'s turn.");
            }

            getOpponentByID(playerID).removeCard(pos, score);
        });
    }

    @Override
    public void showSoleWinnerMessage() {
        Platform.runLater(() -> {
            packetLossTask.cancel();
            root.getChildren().clear();

            // Create winner message window
            AnchorPane winnerPane = new AnchorPane();
            winnerPane.setPrefSize(stageWidth, stageHeight);
            winnerPane.setLayoutX(0);
            winnerPane.setLayoutY(0);

            // Create BG image
            ImageView bgImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SecondBG.png"))));
            bgImage.setFitWidth(stageWidth);
            bgImage.setFitHeight(stageHeight);

            // Create winner message
            Label winnerLabel = new Label("Congratulations! You are the winner!");
            winnerLabel.setPrefSize(stageWidth, 100);
            winnerLabel.setLayoutX(0);
            winnerLabel.setLayoutY(100);
            winnerLabel.setFont(new Font(ERAS_FONT, 32));
            winnerLabel.setTextFill(Color.BLACK);
            winnerLabel.setAlignment(Pos.CENTER);

            // Create winner message
            Label subtitle = new Label("No, you are not that good... you were just alone for too long.");
            subtitle.setPrefSize(stageWidth, 100);
            subtitle.setLayoutX(0);
            subtitle.setLayoutY(200);
            subtitle.setFont(new Font(ERAS_FONT, 20));
            subtitle.setTextFill(Color.BLACK);
            subtitle.setAlignment(Pos.CENTER);

            // Create button to return to main menu
            Button returnButton = new Button("Return to main menu");
            returnButton.setPrefSize(200, 50);
            returnButton.setLayoutX(stageWidth / 2 - 100);
            returnButton.setLayoutY(stageHeight - 200);
            returnButton.setOnAction((_) -> sender.getMatches());

            // Create hierarchy
            winnerPane.getChildren().add(bgImage);
            winnerPane.getChildren().add(winnerLabel);
            winnerPane.getChildren().add(subtitle);
            winnerPane.getChildren().add(returnButton);
            root.getChildren().add(winnerPane);
        });
    }

    @Override
    public void receivePing() {
        Platform.runLater(() -> {
            synchronized (packetLossLock) {
                packetLoss = 0;
            }
            sender.sendPong(playerID);
        });
    }

    /**
     * Gets the nickname of the player by the player ID
     * @param playerID ID of the player
     * @return Nickname of the player
     */
    private String getPlayerNickname(int playerID){
        for (PlayerViewModel p : opponents){
            if (p.getPlayerID() == playerID){
                return p.getNickname();
            }
        }

        return nickname;
    }

    /**
     * Gets the opponent by the player ID
     * @param playerID ID of the player
     * @return Opponent object with the given ID
     */
    private PlayerViewModel getOpponentByID(int playerID){
        return opponents.stream()
                .filter(opponent -> opponent.getPlayerID() == playerID)
                .findFirst()
                .orElse(null);
    }

    /**
     * Sorts the map by the priority of the cards
     * @param map Map to be sorted
     * @return Sorted map
     */
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

    /**
     * Returns the opponent by the nickname
     * @param nickname Nickname of the opponent
     * @return Opponent object with the given nickname
     */
    private PlayerViewModel getOpponentByNickname(String nickname) {
        return opponents.stream()
                .filter(opponent -> opponent.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
    }

    /**
     * Shows a message to the user that the connection has been lost.
     */
    private void showConnectionLoss() {
        Platform.runLater(() -> {
            AnchorPane connectionLostPane = new AnchorPane();
            connectionLostPane.setPrefSize(stageWidth, stageHeight);
            connectionLostPane.setLayoutX(0);
            connectionLostPane.setLayoutY(0);

            // Create background rectangle
            Rectangle backgroundRect = new Rectangle();
            backgroundRect.setX((stageWidth - 500) / 2);
            backgroundRect.setY((stageHeight - 300) / 2);
            backgroundRect.setWidth(500);
            backgroundRect.setHeight(300);
            backgroundRect.setFill(Color.WHITESMOKE);
            backgroundRect.setStroke(Color.BLACK);
            backgroundRect.setStrokeWidth(6);

            // Add connection lost icon
            ImageView connectionLostIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/ConnectionLost.png"))));
            connectionLostIcon.setFitWidth(200);
            connectionLostIcon.setFitHeight(200);
            connectionLostIcon.setLayoutX(150);
            connectionLostIcon.setLayoutY(50);

            // Create disconnection text
            Label disconnectionLabel = new Label("Connection lost! Restart the game.");
            disconnectionLabel.setPrefSize(500, 50);
            disconnectionLabel.setLayoutX(0);
            disconnectionLabel.setLayoutY(250);
            disconnectionLabel.setFont(new Font(ERAS_FONT, 24));
            disconnectionLabel.setTextFill(Color.RED);
            disconnectionLabel.setAlignment(Pos.CENTER);

            // Create hierarchy
            connectionLostPane.getChildren().add(backgroundRect);
            connectionLostPane.getChildren().add(connectionLostIcon);
            connectionLostPane.getChildren().add(disconnectionLabel);
            root.getChildren().add(connectionLostPane);
        });
    }
}
