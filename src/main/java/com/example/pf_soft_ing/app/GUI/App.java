package com.example.pf_soft_ing.app.GUI;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.exceptions.CardNotPlacedException;
import com.example.pf_soft_ing.game.GameResources;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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

import java.io.IOException;
import java.util.*;

/**
 * JavaFX App
 */
public class App extends Application {

    String boardName = "Board.png";

    List<Position> legalPosList = new ArrayList<>();
    List<Position> illegalPosList = new ArrayList<>();
    Map<Position, PlaceableCard> playArea = new HashMap<>();

    Map<Position, Pane> validPosToButtonPane = new HashMap<>();

    List<Integer> playerHand = new ArrayList<>();

    boolean pressingCtrl = false;

    Group root;
    // Timer timer;

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

    double gridWidth;
    double gridHeight;

    int gridRows;
    int gridColumns;

    // Card corner 267 x 326 out of 1232 x 815
    // --> 0.215 x 0.4
    double cardCornerWidthProportion = 0.215;
    double cardCornerHeightProportion = 0.4;

    GridPane playerFieldGrid;
    AnchorPane playerField;

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

//        stage.widthProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
//            root.getChildren().clear();
//            drawGameStage(stage);
//        });
//        stage.heightProperty().addListener((_, _, _) -> { // obs, oldVal, newVal
//            root.getChildren().clear();
//            drawGameStage(stage);
//        });

        drawGameStage(stage);

        stage.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
        stage.addEventHandler(KeyEvent.KEY_RELEASED, this::keyReleased);
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

    private Pane createCardPane(ObjectiveCard card, String side, double xPos, double yPos, double scale){
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
        gridRows = (int) Math.floor(Math.ceil(playerFieldRectHeight / gridCellHeight) / 2) * 2 + 1;
        gridColumns = (int) Math.floor(Math.ceil(playerFieldRectWidth / gridCellWidth) / 2) * 2 + 1;

        double playerFieldWidth = gridCellWidth * gridColumns + 2 * cardCornerWidthProportion * cardWidth;
        double playerFieldHeight = gridCellHeight * gridRows + 2 * cardCornerHeightProportion * cardHeight;

        // Calculate total grid dimensions
        gridWidth = gridCellWidth * gridColumns + 2 * cardCornerWidthProportion * cardWidth;
        gridHeight = gridCellHeight * gridRows + 2 * cardCornerHeightProportion * cardHeight;

        // Create player field anchor pane
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

        // Add starter card to the middle of the grid
        placeCard(80, "front", new Position(0, 0));

        // Fill hierarchy of the scene
        playerField.getChildren().add(playerFieldGrid);

        playerFieldScroll.setContent(playerField);

        root.getChildren().add(playerFieldScroll);
    }

    private void addCornerButtons(Pane cardPane, int ID, Position pos){
        PlaceableCard card = GameResources.getPlaceableCardByID(ID);

        Random rand = new Random();
        int ID1 = rand.nextInt(80);
        int ID2 = rand.nextInt(80);
        int ID3 = rand.nextInt(80);
        int ID4 = rand.nextInt(80);

        Position blPos = new Position(pos.getX() - 1, pos.getY() - 1);
        Position brPos = new Position(pos.getX() + 1, pos.getY() - 1);
        Position tlPos = new Position(pos.getX() - 1, pos.getY() + 1);
        Position trPos = new Position(pos.getX() + 1, pos.getY() + 1);

//        if (card.getCurrSide().getBLCorner().isAvailable() && !illegalPosList.contains(blPos) && !validPosToButtonPane.containsKey(blPos)){
//            Pane blPane = new Pane();
//            blPane.setPrefSize(gridCellWidth, gridCellHeight);
//
//            Button blButton = new Button();
//            blButton.setPrefSize(cardWidth, cardHeight);
//            blButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(-cardWidth * (1 - cardCornerWidthProportion));
//            blButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(cardHeight - cardHeight * cardCornerHeightProportion);
//            blButton.setOpacity(0.6);
//            blButton.setOnAction((_) -> {
//                placeCard(ID1, "front", blPos);
//                cardPane.getChildren().remove(blButton);
//            });
//
//            blPane.getChildren().add(blButton);
//            Position buttonGridPos = mapToGridPos(blPos);
//            playerFieldGrid.add(blPane, buttonGridPos.getX(), buttonGridPos.getY());
//            validPosToButtonPane.put(blPos, blPane);
//        }
//        if (card.getCurrSide().getBRCorner().isAvailable() && !illegalPosList.contains(brPos) && !validPosToButtonPane.containsKey(brPos)){
//            Pane brPane = new Pane();
//            brPane.setPrefSize(gridCellWidth, gridCellHeight);
//
//            Button brButton = new Button();
//            brButton.setPrefSize(cardWidth, cardHeight);
//            brButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(cardWidth * (1 - cardCornerWidthProportion));
//            brButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(cardHeight - cardHeight * cardCornerHeightProportion);
//            brButton.setOpacity(0.6);
//            brButton.setOnAction((_) -> {
//                placeCard(ID2, "front", brPos);
//                cardPane.getChildren().remove(brButton);
//            });
//
//            brPane.getChildren().add(brButton);
//            Position buttonGridPos = mapToGridPos(brPos);
//            playerFieldGrid.add(brPane, buttonGridPos.getX(), buttonGridPos.getY());
//            validPosToButtonPane.put(brPos, brPane);
//        }
//        if (card.getCurrSide().getTLCorner().isAvailable() && !illegalPosList.contains(tlPos) && !validPosToButtonPane.containsKey(tlPos)){
//            Pane tlPane = new Pane();
//            tlPane.setPrefSize(gridCellWidth, gridCellHeight);
//
//            Button tlButton = new Button();
//            tlButton.setPrefSize(cardWidth, cardHeight);
//            tlButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(-cardWidth * (1 - cardCornerWidthProportion));
//            tlButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(-cardHeight * (1 - cardCornerHeightProportion));
//            tlButton.setOpacity(0.6);
//            tlButton.setOnAction((_) -> {
//                placeCard(ID3, "front", tlPos);
//                cardPane.getChildren().remove(tlButton);
//            });
//
//            tlPane.getChildren().add(tlButton);
//            Position buttonGridPos = mapToGridPos(tlPos);
//            playerFieldGrid.add(tlPane, buttonGridPos.getX(), buttonGridPos.getY());
//            validPosToButtonPane.put(tlPos, tlPane);
//        }
//        if (card.getCurrSide().getTRCorner().isAvailable() && !illegalPosList.contains(trPos) && !validPosToButtonPane.containsKey(trPos)){
//            Pane trPane = new Pane();
//            trPane.setPrefSize(gridCellWidth, gridCellHeight);
//
//            Button trButton = new Button();
//            trButton.setPrefSize(cardWidth, cardHeight);
//            trButton.setLayoutX(-cardWidth * cardCornerWidthProportion);  //(cardWidth - cardWidth * cardCornerWidthProportion);
//            trButton.setLayoutY(-cardHeight * cardCornerHeightProportion);//(-cardHeight * (1 - cardCornerHeightProportion));
//            trButton.setOpacity(0.6);
//            trButton.setOnAction((_) -> {
//                placeCard(ID4, "front", trPos);
//                cardPane.getChildren().remove(trButton);
//            });
//
//            trPane.getChildren().add(trButton);
//            Position buttonGridPos = mapToGridPos(trPos);
//            playerFieldGrid.add(trPane, buttonGridPos.getX(), buttonGridPos.getY());
//            validPosToButtonPane.put(trPos, trPane);
//        }
    }

    private void updateIlLegalPositions(Position pos){
//        ArrayList<Position> newLegalPos = new ArrayList<>();
//        ArrayList<Position> newIllegalPos = new ArrayList<>();
//
//        Side currSide = playArea.get(pos).getCurrSide();
//
//        if (currSide.getBLCorner().isAvailable()){
//            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
//        }
//        else{
//            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() - 1));
//        }
//
//        if (currSide.getBRCorner().isAvailable()){
//            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
//        }
//        else{
//            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() - 1));
//        }
//
//        if (currSide.getTLCorner().isAvailable()){
//            newLegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
//        }
//        else{
//            newIllegalPos.add(new Position(pos.getX() - 1, pos.getY() + 1));
//        }
//
//        if (currSide.getTRCorner().isAvailable()){
//            newLegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
//        }
//        else{
//            newIllegalPos.add(new Position(pos.getX() + 1, pos.getY() + 1));
//        }
//
//        for (Position p : newIllegalPos){
//            if (!illegalPosList.contains(p)){
//                illegalPosList.add(p);
//            }
//
//            legalPosList.remove(p);
//
//            if (validPosToButtonPane.containsKey(p)){
//                playerFieldGrid.getChildren().remove(validPosToButtonPane.get(p));
//                validPosToButtonPane.remove(p);
//            }
//        }
//
//        for (Position p : newLegalPos){
//            if (!legalPosList.contains(p) && !illegalPosList.contains(p)){
//                legalPosList.add(p);
//            }
//        }
//
//        // Set the new card's position as illegal (a card just got placed)
//        legalPosList.remove(pos);
//
//        if (!illegalPosList.contains(pos)){
//            illegalPosList.add(pos);
//        }
    }

    private Position mapToGridPos(Position mapPos){
        return new Position(mapPos.getX() + gridRows / 2, gridColumns / 2 - mapPos.getY());
    }

    private void placeCard(int ID, String side, Position pos){
        if (pos.getX() <= -gridColumns / 2 || pos.getX() >= gridColumns / 2 ||
                pos.getY() <= -gridRows / 2 || pos.getY() >= gridRows / 2){
            updateGridDimension();
        }

        // Create Image
        String cardName = side + "Card" + ID + ".png";

        if (!playArea.containsKey(pos)){
            playArea.put(pos, GameResources.getPlaceableCardByID(ID));
        }

        updateIlLegalPositions(pos);

        // Create Pane for card
        Pane cardPane = new Pane();
        cardPane.setPrefSize(gridCellWidth, gridCellHeight);

        Image cardImg = new Image(cardName);
        ImageView cardImgV = new ImageView(cardImg);

        cardImgV.setFitHeight(cardHeight);
        cardImgV.setFitWidth(cardWidth);

        cardImgV.setX(-cardWidth * cardCornerWidthProportion);
        cardImgV.setY(-cardHeight * cardCornerHeightProportion);

        cardPane.getChildren().add(cardImgV);

        addCornerButtons(cardPane, ID, pos);

        Position gridPos = mapToGridPos(pos);

        // Add card to the grid
        playerFieldGrid.add(cardPane, gridPos.getX(), gridPos.getY());

        System.out.println("Grid dimensions: " + gridRows + ", " + gridColumns);
        System.out.println("Placed card " + ID + " at grid position " + gridPos.getX() + ", " + gridPos.getY());
    }

    private void updateGridDimension(){
        System.out.println("Updating grid dimensions");
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
            placeCard(playArea.get(p).getId(), "front", p);
        }
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

        Pane secretObjectivePane = createCardPane(secretObjective, "front", defaultElementsOffset, cardsYPos, 0.9);
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
                playerField.setScaleX(playerField.getScaleX() + 0.05);
                playerField.setScaleY(playerField.getScaleY() + 0.05);
                break;
            case DOWN:
                playerField.setScaleX(playerField.getScaleX() - 0.05);
                playerField.setScaleY(playerField.getScaleY() - 0.05);
                break;
        }
    }

    void keyReleased(KeyEvent key){
        if (Objects.requireNonNull(key.getCode()) == KeyCode.CONTROL) {
            pressingCtrl = false;
        }
    }
}
