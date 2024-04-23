package com.example.pf_soft_ing.app.GUI;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.game.GameResources;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * JavaFX App
 */
public class App extends Application {

    String boardName = "Board.png";

    Group root;
    Timer timer;

    double barOffset = 40;
    double defaultElementsOffset = 10;

    double stageWidth;
    double stageHeight;

    double cardWidth;
    double cardHeight;

    double playerFieldRectWidth;
    double playerFieldRectHeight;

    double gridCellWidth;
    double gridCellHeight;

    // Card corner 267 x 326 out of 1232 x 815
    // --> 0.215 x 0.4
    double cardCornerWidthProportion = 0.215;
    double cardCornerHeightProportion = 0.4;

    GridPane playerFieldGrid;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Hello!");
//        Timer timer =  startTimer();
        root = new Group();
        stage.setScene(new Scene(root));
        stage.setWidth(1721);
        stage.setHeight(926);
        stage.show();

        stageWidth = stage.getWidth() - 15; // Don't ask me why 15
        stageHeight = stage.getHeight() - barOffset;

        stage.setMaximized(true);

//        drawMainMenuScene();

        stage.widthProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
            root.getChildren().clear();
            drawGameStage(stage);
        });
        stage.heightProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
            root.getChildren().clear();
            drawGameStage(stage);
        });

        drawGameStage(stage);
    }

    // Game flow:
    // 1. Place shuffled decks and 4 visible cards
    // 2. Place starter card
    // 3. Choose token
    // 4. Fill hand
    // 5. Place common objectives
    // 6. Choose secret objective

    private void drawMainMenuScene(){
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

        VBox matchList = new VBox();
        matchList.setPrefSize(stageWidth - 50, 6 * 200);
        matchList.setLayoutX(0);
        matchList.setLayoutY(0);

        Pane match1 = createMatchPane("Match 1");
        Pane match2 = createMatchPane("Match 2");
        Pane match3 = createMatchPane("Match 3");
        Pane match4 = createMatchPane("Match 4");
        Pane match5 = createMatchPane("Match 5");
        Pane match6 = createMatchPane("Match 6");


        matchList.getChildren().add(match1);
        matchList.getChildren().add(match2);
        matchList.getChildren().add(match3);
        matchList.getChildren().add(match4);
        matchList.getChildren().add(match5);
        matchList.getChildren().add(match6);

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
            System.out.println("Adding match!");
            openCreateMatchWindow();
        });

        mainMenu.getChildren().add(addMatchButton);

        root.getChildren().add(mainMenu);
    }

    private Pane createMatchPane(String title){
        Pane matchPane = new Pane();
        matchPane.setPrefSize(stageWidth - 50, 200);

        VBox matchInfo = new VBox();
        matchInfo.setPrefSize(stageWidth - 100, 200);
        matchInfo.setLayoutX(50);

        Label matchName = new Label(title + " (IP: 127.0.0.1)");
        matchName.setPrefSize(stageWidth - 100, 50);
        matchName.setFont(new Font(Font.getDefault().getName(), 24));

        Label player1 = new Label("Player 1");
        player1.setPrefSize(stageWidth - 100, 50);
        player1.setFont(new Font(Font.getDefault().getName(), 18));

        Label player2 = new Label("Player 2");
        player2.setPrefSize(stageWidth - 100, 50);
        player2.setFont(new Font(Font.getDefault().getName(), 18));

        Label player3 = new Label("Player 3");
        player3.setPrefSize(stageWidth - 100, 50);
        player3.setFont(new Font(Font.getDefault().getName(), 18));

        matchInfo.getChildren().add(matchName);
        matchInfo.getChildren().add(player1);
        matchInfo.getChildren().add(player2);
        matchInfo.getChildren().add(player3);

        Button joinButton = new Button();
        joinButton.setPrefSize(stageWidth - 50, 200);
        joinButton.setOpacity(0.1);

        joinButton.setOnAction((_) -> { // event
            System.out.println("Joining match " + title);
            openJoinMatchWindow(title);
        });

        matchPane.getChildren().add(matchInfo);
        matchPane.getChildren().add(joinButton);

        return matchPane;
    }

    private void openCreateMatchWindow(){
        root.getChildren().clear();

        AnchorPane createMatch = new AnchorPane();
        createMatch.setPrefSize(stageWidth, stageHeight);
        createMatch.setLayoutX(0);
        createMatch.setLayoutY(0);

        root.getChildren().add(createMatch);
    }

    private void openJoinMatchWindow(String match){
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
        Button confirmNick = new Button("+");
        confirmNick.setPrefSize(50, 50);
        confirmNick.setLayoutX((stageWidth + 200) * 0.5 + 200);
        confirmNick.setLayoutY(100);
        confirmNick.setOnAction((_) -> { // event
            System.out.println("Joining match " + match + " with nick " + nameField.getText());
            joinGame(match, nameField.getText());
        });

        // Create IP label
        Label IPLabel = new Label("IP: 127.0.0.1");
        IPLabel.setPrefSize(200, 50);
        IPLabel.setLayoutX(5);
        IPLabel.setLayoutY(stageHeight - 70);

        // Create table of current players
        VBox playerList = new VBox();
        playerList.setPrefSize(stageWidth - 100, stageHeight - 300);
        playerList.setLayoutX(50);
        playerList.setLayoutY(150);

        Label player1 = new Label("Player 1");
        player1.setPrefSize(stageWidth - 100, 100);
        player1.setFont(new Font(Font.getDefault().getName(), 24));

        Label player2 = new Label("Player 2");
        player2.setPrefSize(stageWidth - 100, 100);
        player2.setFont(new Font(Font.getDefault().getName(), 24));

        Label player3 = new Label("Player 3");
        player3.setPrefSize(stageWidth - 100, 100);
        player3.setFont(new Font(Font.getDefault().getName(), 24));

        playerList.getChildren().add(player1);
        playerList.getChildren().add(player2);
        playerList.getChildren().add(player3);

        joinPane.getChildren().add(nameField);
        joinPane.getChildren().add(confirmNick);
        joinPane.getChildren().add(IPLabel);
        joinPane.getChildren().add(playerList);

        root.getChildren().add(joinPane);
    }

    private void joinGame(String match, String nick){
        if (nick.isEmpty()){
            System.out.println("Please enter a nickname!");
            return;
        }

        root.getChildren().clear();

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(stageWidth, stageHeight);
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);

        Label waitingLabel = new Label("Waiting for other players...\nEntering match " + match + " with nick " + nick);
        waitingLabel.setPrefSize(stageWidth, stageHeight);
        waitingLabel.setLayoutX(50);
        waitingLabel.setLayoutY(50);
        waitingLabel.setFont(new Font(Font.getDefault().getName(), 32));

        anchorPane.getChildren().add(waitingLabel);

        root.getChildren().add(anchorPane);
    }

    private void drawGameStage(Stage stage){
        // Set basic stage dimensions
        stageWidth = stage.getWidth() - 15; // Don't ask me why 15
        stageHeight = stage.getHeight() - barOffset;

        // Calculate card dimensions
        cardWidth = (stageWidth - (9 * defaultElementsOffset)) / 7;
        cardHeight = cardWidth / 1.5;

        playerFieldRectWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
        playerFieldRectHeight = stageHeight - (2 * cardHeight) - (3 * defaultElementsOffset);

        GameResources.initializeAllDecks();

        // Add common cards to the stage
        renderCommonCards(GameResources.getPlaceableCardByID(1), GameResources.getPlaceableCardByID(22), GameResources.getPlaceableCardByID(13),
                GameResources.getPlaceableCardByID(54), GameResources.getPlaceableCardByID(45), GameResources.getPlaceableCardByID(66));

        // Add board to the stage
        drawBoard();

        // Add common objectives to the stage
        renderCommonObjectives(GameResources.getObjectiveCardByID(97), GameResources.getObjectiveCardByID(99));

        // Create player field
        drawPlayArea();

        // Create player hand
        drawPlayerHand(GameResources.getObjectiveCardByID(96), GameResources.getPlaceableCardByID(16),
                GameResources.getPlaceableCardByID(3), GameResources.getPlaceableCardByID(69));

        // Add vertical and horizontal lines to separate player field, hand and common view
        drawSeparationLines();
    }

    private void drawBoard(){
        Image boardImg = new Image(boardName);
        ImageView boardImgV = new ImageView(boardImg);

        boardImgV.setFitHeight(stageHeight - (2 * cardHeight) - (4 * defaultElementsOffset));
        boardImgV.setFitWidth(boardImgV.getFitHeight() * 0.5);

        boardImgV.setX(defaultElementsOffset);
        boardImgV.setY(defaultElementsOffset);

        root.getChildren().add(boardImgV);
    }

    private void renderCommonCards(PlaceableCard resDeckCard, PlaceableCard resVisibleCard1, PlaceableCard resVisibleCard2,
                                   PlaceableCard goldDeckCard, PlaceableCard goldVisibleCard1, PlaceableCard goldVisibleCard2){

        double resourceCardsY = stageHeight - (2 * cardHeight) - 2 * defaultElementsOffset;
        double goldenCardsY = stageHeight - cardHeight - defaultElementsOffset;

        Pane resDeckPane = createCardPane(resDeckCard, "back", defaultElementsOffset, resourceCardsY, 1);
        Pane resVisible1Pane = createCardPane(resVisibleCard1, "front", 2 * defaultElementsOffset + cardWidth, resourceCardsY, 0.8);
        Pane resVisible2Pane = createCardPane(resVisibleCard2, "front", 3 * defaultElementsOffset + 2 * cardWidth, resourceCardsY, 0.8);

        Pane goldDeckPane = createCardPane(goldDeckCard, "back", defaultElementsOffset, goldenCardsY, 1);
        Pane goldVisible1Pane = createCardPane(goldVisibleCard1, "front", 2 * defaultElementsOffset + cardWidth, goldenCardsY, 0.8);
        Pane goldVisible2Pane = createCardPane(goldVisibleCard2, "front", 3 * defaultElementsOffset + 2 * cardWidth, goldenCardsY, 0.8);

        // Add draw buttons to cards
        addDrawButtonToCard(resDeckPane, resDeckCard);
        addDrawButtonToCard(resVisible1Pane, resVisibleCard1);
        addDrawButtonToCard(resVisible2Pane, resVisibleCard2);

        addDrawButtonToCard(goldDeckPane, goldDeckCard);
        addDrawButtonToCard(goldVisible1Pane, goldVisibleCard1);
        addDrawButtonToCard(goldVisible2Pane, goldVisibleCard2);

        // Add cards to the stage
        root.getChildren().add(resDeckPane);
        root.getChildren().add(resVisible1Pane);
        root.getChildren().add(resVisible2Pane);

        root.getChildren().add(goldDeckPane);
        root.getChildren().add(goldVisible1Pane);
        root.getChildren().add(goldVisible2Pane);
    }

    private void renderCommonObjectives(ObjectiveCard objective1, ObjectiveCard objective2){

    }

    private void addDrawButtonToCard(Pane cardPane, PlaceableCard card){
        Button drawButton = new Button();
        drawButton.setPrefSize(cardPane.getPrefWidth(), cardPane.getPrefHeight());
        drawButton.setLayoutX(0);
        drawButton.setLayoutY(0);
        drawButton.setOpacity(0.1);

        drawButton.setOnAction((_) -> {
            System.out.println("Drawing card " + card.getId());
        });

        cardPane.getChildren().add(drawButton);
    }

    private Pane createCardPane(PlaceableCard card, String side, double xPos, double yPos, double scale){
        // Create Pane for card
        Pane cardPane = new Pane();

        double xOffset = (1 - scale) * cardWidth / 2;
        double yOffset = (1 - scale) * cardHeight / 2;

        cardPane.setPrefSize(cardWidth * scale, cardHeight * scale);
        cardPane.setLayoutX(xPos + xOffset);
        cardPane.setLayoutY(yPos + yOffset);
        
        // Create Image
        String cardName = side + "Card" + card.getId() + ".png";

        Image cardImg = new Image(cardName);
        ImageView cardImgV = new ImageView(cardImg);

        cardImgV.setFitHeight(cardHeight * scale);
        cardImgV.setFitWidth(cardWidth * scale);

        cardImgV.setX(0);
        cardImgV.setY(0);

        cardPane.getChildren().add(cardImgV);

        return cardPane;
    }

    private Pane createObjectiveCardPane(ObjectiveCard card, String side, double xPos, double yPos, double scale){
        // Create Pane for card
        Pane cardPane = new Pane();

        double xOffset = (1 - scale) * cardWidth / 2;
        double yOffset = (1 - scale) * cardHeight / 2;

        cardPane.setPrefSize(cardWidth * scale, cardHeight * scale);
        cardPane.setLayoutX(xPos + xOffset);
        cardPane.setLayoutY(yPos + yOffset);

        // Create Image
        String cardName = side + "Card" + card.getId() + ".png";

        Image cardImg = new Image(cardName);
        ImageView cardImgV = new ImageView(cardImg);

        cardImgV.setFitHeight(cardHeight * scale);
        cardImgV.setFitWidth(cardWidth * scale);

        cardImgV.setX(0);
        cardImgV.setY(0);

        cardPane.getChildren().add(cardImgV);

        return cardPane;
    }

    private void drawPlayArea(){
        // - Scroll
        //   - AnchorPane
        //     - GridPane
        //       - Pane
        //         - ImageView
        //         - Button
        //         - Button
        //       - Pane
        //         - ...

        // Create scroll pane for player field
        ScrollPane playerFieldScroll = new ScrollPane();
        playerFieldScroll.setPrefSize(playerFieldRectWidth, playerFieldRectHeight);
        playerFieldScroll.setPannable(true);
        playerFieldScroll.setLayoutX(3 * (defaultElementsOffset + cardWidth) + 10);
        playerFieldScroll.setLayoutY(0);

        // Calculate grid cell dimensions
        gridCellWidth = cardWidth - (cardCornerWidthProportion * cardWidth);
        gridCellHeight = cardHeight - (cardCornerHeightProportion * cardHeight);

        // Calculate number of rows and columns. I need odd numbers so that starter card is in the middle
        int totalRows = (int) Math.floor(Math.ceil(playerFieldRectHeight / gridCellHeight) / 2) * 2 + 1;
        int totalColumns = (int) Math.floor(Math.ceil(playerFieldRectWidth / gridCellWidth) / 2) * 2 + 1;

        double playerFieldWidth = gridCellWidth * totalColumns + 2 * cardCornerWidthProportion * cardWidth;
        double playerFieldHeight = gridCellHeight * totalRows + 2 * cardCornerHeightProportion * cardHeight;

        // Calculate total grid dimensions
        double gridWidth = gridCellWidth * totalColumns;
        double gridHeight = gridCellHeight * totalRows;

        // Create player field anchor pane
        AnchorPane playerField = new AnchorPane();
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
        for (int i = 0; i < totalColumns - 1; i++){
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefSize(gridCellWidth, gridCellHeight);

            playerFieldGrid.add(anchorPane, i, i);
        }

        // Add starter card to the middle of the grid
        Pane sterterCardPane = new Pane();
        sterterCardPane.setPrefSize(gridCellWidth, gridCellHeight);

        Image starterImg = new Image("frontCard80.png");
        ImageView starterImgV = new ImageView(starterImg);

        starterImgV.setFitHeight(cardHeight);
        starterImgV.setFitWidth(cardWidth);

        PlaceableCard sCard = GameResources.getPlaceableCardByID(80);

        sterterCardPane.getChildren().add(starterImgV);

        addCornerButtons(sterterCardPane, sCard);

        playerFieldGrid.add(sterterCardPane, totalColumns / 2, totalRows / 2);

        // Fill hierarchy of the scene
        playerField.getChildren().add(playerFieldGrid);

        playerFieldScroll.setContent(playerField);

        root.getChildren().add(playerFieldScroll);
    }

    private void addCornerButtons(Pane cardPane, PlaceableCard card){
        if (card.getCurrSide().getBLCorner().isAvailable()){
            Button blButton = new Button();
            blButton.setPrefSize(cardWidth * cardCornerWidthProportion, cardHeight * cardCornerHeightProportion);
            blButton.setLayoutX(0);
            blButton.setLayoutY(cardHeight - cardHeight * cardCornerHeightProportion);
            blButton.setOpacity(0.5);
            blButton.setOnAction((_) -> {
                placeCard(2, 4);
            });

            cardPane.getChildren().add(blButton);
        }
        if (card.getCurrSide().getBRCorner().isAvailable()){
            Button brButton = new Button();
            brButton.setPrefSize(cardWidth * cardCornerWidthProportion, cardHeight * cardCornerHeightProportion);
            brButton.setLayoutX(cardWidth - cardWidth * cardCornerWidthProportion);
            brButton.setLayoutY(cardHeight - cardHeight * cardCornerHeightProportion);
            brButton.setOpacity(0.5);
            brButton.setOnAction((_) -> {
                placeCard(4, 4);
            });

            cardPane.getChildren().add(brButton);
        }
        if (card.getCurrSide().getTLCorner().isAvailable()){
            Button tlButton = new Button();
            tlButton.setPrefSize(cardWidth * cardCornerWidthProportion, cardHeight * cardCornerHeightProportion);
            tlButton.setLayoutX(0);
            tlButton.setLayoutY(0);
            tlButton.setOpacity(0.5);
            tlButton.setOnAction((_) -> {
                placeCard(2, 2);
            });

            cardPane.getChildren().add(tlButton);
        }
        if (card.getCurrSide().getTRCorner().isAvailable()){
            Button trButton = new Button();
            trButton.setPrefSize(cardWidth * cardCornerWidthProportion, cardHeight * cardCornerHeightProportion);
            trButton.setLayoutX(cardWidth - cardWidth * cardCornerWidthProportion);
            trButton.setLayoutY(0);
            trButton.setOpacity(0.5);
            trButton.setOnAction((_) -> {
                placeCard(4, 2);
            });

            cardPane.getChildren().add(trButton);
        }
    }

    private void placeCard(int x, int y){
        PlaceableCard card = GameResources.getPlaceableCardByID(20);
        Pane cardPane = new Pane();
        cardPane.setPrefSize(gridCellWidth, gridCellHeight);

        Image img = new Image("frontCard20.png");
        ImageView imgV = new ImageView(img);

        imgV.setFitHeight(cardHeight);
        imgV.setFitWidth(cardWidth);

        cardPane.getChildren().add(imgV);

        addCornerButtons(cardPane, card);

        playerFieldGrid.add(cardPane, x, y);

        System.out.println("Placing card on " + x + ", " + y);
    }

    private void drawPlayerHand(ObjectiveCard secretObjective, PlaceableCard card1, PlaceableCard card2, PlaceableCard card3){
        AnchorPane playerHand = new AnchorPane();
        double playerHandWidth = stageWidth - (3 * (defaultElementsOffset + cardWidth) + 10);
        double playerHandHeight = (2 * cardHeight) + (3 * defaultElementsOffset);

        playerHand.setPrefSize(playerHandWidth, playerHandHeight);
        playerHand.setLayoutX(3 * (defaultElementsOffset + cardWidth) + 10);
        playerHand.setLayoutY(playerFieldRectHeight);

        // Create panes for each card
        double cardsYPos = (playerHandHeight - cardHeight) * 0.5;

        Pane secretObjectivePane = createObjectiveCardPane(secretObjective, "front", defaultElementsOffset, cardsYPos, 0.9);
        Pane card1Pane = createCardPane(card1, "front", 2 * defaultElementsOffset + cardWidth, cardsYPos, 1);
        Pane card2Pane = createCardPane(card2, "front", 3 * defaultElementsOffset + 2 * cardWidth, cardsYPos, 1);
        Pane card3Pane = createCardPane(card3, "front", 4 * defaultElementsOffset + 3 * cardWidth, cardsYPos, 1);

        // Add select buttons to cards
        addSelectButtonToCard(card1Pane, card1);
        addSelectButtonToCard(card2Pane, card2);
        addSelectButtonToCard(card3Pane, card3);

        // Add rectangle to highlight player color
        Rectangle plRect = new Rectangle();
        plRect.setStrokeWidth(5);
        plRect.setX(3);
        plRect.setY(3);
        plRect.setWidth(playerHandWidth - 5);
        plRect.setHeight(playerHandHeight - 2);
        plRect.setFill(Color.TRANSPARENT);
        plRect.setStroke(Color.LIME);

        playerHand.getChildren().add(plRect);

        // Add cards to stage
        playerHand.getChildren().add(secretObjectivePane);
        playerHand.getChildren().add(card1Pane);
        playerHand.getChildren().add(card2Pane);
        playerHand.getChildren().add(card3Pane);

        root.getChildren().add(playerHand);
    }

    private void addSelectButtonToCard(Pane cardPane, PlaceableCard card){
        Button selectButton = new Button();
        selectButton.setPrefSize(cardPane.getPrefWidth(), cardPane.getPrefHeight());
        selectButton.setLayoutX(0);
        selectButton.setLayoutY(0);
        selectButton.setOpacity(0.1);

        selectButton.setOnAction((_) -> {
            System.out.println("Selected card " + card.getId());
            selectCard(card);
        });

        cardPane.getChildren().add(selectButton);
    }

    private void selectCard(PlaceableCard card){
        // TODO
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

    private Timer startTimer(){
        timer = new Timer("Timer");
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    // method()
                });
            }
        };
        long delay = 0;
        long period = 100L;
        timer.schedule(task, delay, period);
        return timer;
    }
}
