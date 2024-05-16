package com.example.pf_soft_ing.app;

import com.example.pf_soft_ing.card.CC;
import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TUITest {

    private final Map<Position, PlaceableCard> playArea = new HashMap<>();

    private final Map<Integer, Position> posIDToValidPos = new HashMap<>();

    public static void main() {
        TUITest tui = new TUITest();

        GameResources.initializeAllDecks();

        tui.fillMap();

        tui.printPlayArea();
    }

    private void fillMap() {
        PlaceableCard starterCard = GameResources.getPlaceableCardByID(83);
        starterCard.setCurrSideType(CardSideType.BACK);
        starterCard.setPriority(0);
        playArea.put(new Position(0, 0), starterCard);

        PlaceableCard card1 = GameResources.getPlaceableCardByID(0);
        card1.setPriority(1);
        playArea.put(new Position(-1, 1), card1);

        PlaceableCard card2 = GameResources.getPlaceableCardByID(1);
        card2.setPriority(2);
        playArea.put(new Position(1, 1), card2);

        PlaceableCard card3 = GameResources.getPlaceableCardByID(12);
        card3.setPriority(3);
        playArea.put(new Position(2, 0), card3);

        PlaceableCard card4 = GameResources.getPlaceableCardByID(3);
        card4.setPriority(4);
        playArea.put(new Position(3, 1), card4);

        PlaceableCard card5 = GameResources.getPlaceableCardByID(24);
        card5.setPriority(5);
        playArea.put(new Position(4, 0), card5);

        PlaceableCard card6 = GameResources.getPlaceableCardByID(5);
        card6.setPriority(6);
        playArea.put(new Position(3, -1), card6);
    }

//    private char[][] createPlayAreaArr() {
//        if (playArea.isEmpty()) {
//            return null;
//        }
//
//        // Get the max and min values of x and y
//        int maxX = playArea.keySet().stream().map(Position::getX).max(Integer::compareTo).orElse(0);
//        int minX = playArea.keySet().stream().map(Position::getX).min(Integer::compareTo).orElse(0);
//        int maxY = playArea.keySet().stream().map(Position::getY).max(Integer::compareTo).orElse(0);
//        int minY = playArea.keySet().stream().map(Position::getY).min(Integer::compareTo).orElse(0);
//
//        // +3 because of the starter card and the 1 extra space on each border (for the validPosIDs)
//        int[][] cardIDArr = new int[maxY - minY + 3][maxX - minX + 3];
//        char[][] playAreaArr = new char[3 * (maxY - minY + 3) + 2][3 * (maxX - minX + 3) + 2];
//
//        // The position of the starter card is new Position(-minX + 1, maxY + 1);
//        // And the array's Y is inverted, so the starter card's position in the array is [xPos - minX + 1][-yPos + maxY + 1]
//
//        for (int[] IDs : cardIDArr) {
//            Arrays.fill(IDs, -1);
//        }
//
//        // Fill the array with elements from the playArea map
//        for (Position pos : playArea.keySet()) {
//            cardIDArr[-pos.getY() + maxY + 1][pos.getX() - minX + 1] = playArea.get(pos);
//        }
//
//        // Fill array of characters to print
//        for (int y = 0; y < cardIDArr.length; y++) {
//            for (int x = 0; x < cardIDArr[y].length; x++) {
//                if (x > 0 && x < cardIDArr[y].length - 1 &&
//                        y > 0 && y < cardIDArr.length - 1) {
//                    PlaceableCard card = GameResources.getPlaceableCardByID(cardIDArr[y][x]);
//
//                    if (card != null) {
//                        // region Set permanent resources and constant card areas
//                        char PR1, PR2, PR3;
//
//                        Side currSide = card.getCurrSide();
//
//                        if (card.getCurrSideType().equals(CardSideType.BACK)) {
//                            List<ResourceType> permanentRes = card.getCurrSide().getResources();
//
//                            List<ResourceType> cornerResources = new ArrayList<>(){{
//                                add(currSide.getTLCorner().getResource());
//                                add(currSide.getTRCorner().getResource());
//                                add(currSide.getBLCorner().getResource());
//                                add(currSide.getBRCorner().getResource());
//                            }};
//
//                            for (ResourceType cornerRes : cornerResources) {
//                                if (cornerRes != null){
//                                    permanentRes.remove(cornerRes);
//                                }
//                            }
//
//                            if (permanentRes.size() == 1) {
//                                PR1 = ' ';
//                                PR2 = currSide.getResources().getFirst().toString().charAt(0);
//                                PR3 = ' ';
//                            }
//                            else if (permanentRes.size() == 2) {
//                                PR1 = currSide.getResources().getFirst().toString().charAt(0);
//                                PR2 = ' ';
//                                PR3 = currSide.getResources().get(1).toString().charAt(0);
//                            }
//                            else { // size == 3
//                                PR1 = currSide.getResources().getFirst().toString().charAt(0);
//                                PR2 = currSide.getResources().get(1).toString().charAt(0);
//                                PR3 = currSide.getResources().get(2).toString().charAt(0);
//                            }
//                        }
//                        else {
//                            PR1 = ' ';
//                            PR2 = ' ';
//                            PR3 = ' ';
//                        }
//
//                        playAreaArr[3 * y][3 * x + 2] = '─';
//                        playAreaArr[3 * y + 1][3 * x + 2] = ' ';
//                        playAreaArr[3 * y + 2][3 * x + 2] = PR2;
//                        playAreaArr[3 * y + 3][3 * x + 2] = ' ';
//                        playAreaArr[3 * y + 4][3 * x + 2] = '─';
//
//                        playAreaArr[3 * y + 2][3 * x] = '│';
//                        playAreaArr[3 * y + 2][3 * x + 1] = PR1;
//                        playAreaArr[3 * y + 2][3 * x + 3] = PR3;
//                        playAreaArr[3 * y + 2][3 * x + 4] = '│';
//                        // endregion
//
//                        PlaceableCard TLCard = GameResources.getPlaceableCardByID(cardIDArr[y - 1][x - 1]);
//                        PlaceableCard TRCard = GameResources.getPlaceableCardByID(cardIDArr[y - 1][x + 1]);
//                        PlaceableCard BLCard = GameResources.getPlaceableCardByID(cardIDArr[y + 1][x - 1]);
//                        PlaceableCard BRCard = GameResources.getPlaceableCardByID(cardIDArr[y + 1][x + 1]);
//
//                        // Check if there is a card in the TL corner
//                        if (playAreaArr[3 * y][3 * x] == '\u0000') {
//                            if (TLCard != null) {
//                                if (TLCard.getPriority() > card.getPriority()){
//                                    playAreaArr[3 * y + 1][3 * x + 1] = '┘';
//                                    playAreaArr[3 * y + 1][3 * x] = '┬';
//                                    playAreaArr[3 * y][3 * x + 1] = '├';
//                                    ResourceType TLRes = TLCard.getCurrSide().getBRCorner().getResource();
//                                    playAreaArr[3 * y][3 * x] = TLRes == null ? ' ' : TLRes.toString().charAt(0);
//                                }
//                                else {
//                                    playAreaArr[3 * y][3 * x] = '┌';
//                                    playAreaArr[3 * y][3 * x + 1] = '┴';
//                                    playAreaArr[3 * y + 1][3 * x] = '┤';
//                                    ResourceType TLRes = card.getCurrSide().getTLCorner().getResource();
//                                    playAreaArr[3 * y + 1][3 * x + 1] = TLRes == null ? ' ' : TLRes.toString().charAt(0);
//                                }
//                            }
//                            else {
//                                playAreaArr[3 * y][3 * x] = '┌';
//                                playAreaArr[3 * y][3 * x + 1] = '─';
//                                playAreaArr[3 * y + 1][3 * x] = '│';
//                                ResourceType TLRes = card.getCurrSide().getTLCorner().getResource();
//                                playAreaArr[3 * y + 1][3 * x + 1] = TLRes == null ? ' ' : TLRes.toString().charAt(0);
//                            }
//                        }
//
//                        // Check if there is a card in the TR corner
//                        if (playAreaArr[3 * y][3 * x + 3] == '\u0000') {
//                            if (TRCard != null) {
//                                if (TRCard.getPriority() > card.getPriority()){
//                                    playAreaArr[3 * y + 1][3 * x + 4] = '┬';
//                                    playAreaArr[3 * y + 1][3 * x + 3] = '└';
//                                    playAreaArr[3 * y][3 * x + 3] = '┤';
//                                    ResourceType TRRes = TRCard.getCurrSide().getBLCorner().getResource();
//                                    playAreaArr[3 * y][3 * x + 4] = TRRes == null ? ' ' : TRRes.toString().charAt(0);
//                                }
//                                else {
//                                    playAreaArr[3 * y][3 * x + 3] = '┴';
//                                    playAreaArr[3 * y][3 * x + 4] = '┐';
//                                    playAreaArr[3 * y + 1][3 * x + 4] = '├';
//                                    ResourceType TRRes = card.getCurrSide().getTRCorner().getResource();
//                                    playAreaArr[3 * y + 1][3 * x + 3] = TRRes == null ? ' ' : TRRes.toString().charAt(0);
//                                }
//                            }
//                            else {
//                                playAreaArr[3 * y][3 * x + 4] = '┐';
//                                playAreaArr[3 * y][3 * x + 3] = '─';
//                                playAreaArr[3 * y + 1][3 * x + 4] = '│';
//                                ResourceType TRRes = card.getCurrSide().getTRCorner().getResource();
//                                playAreaArr[3 * y + 1][3 * x + 3] = TRRes == null ? ' ' : TRRes.toString().charAt(0);
//                            }
//                        }
//
//                        // Check if there is a card in the BL corner
//                        if (playAreaArr[3 * y + 3][3 * x] == '\u0000'){
//                            if (BLCard != null) {
//                                if (BLCard.getPriority() > card.getPriority()){
//                                    playAreaArr[3 * y + 3][3 * x] = '┴';
//                                    playAreaArr[3 * y + 4][3 * x + 1] = '├';
//                                    playAreaArr[3 * y + 3][3 * x + 1] = '┐';
//                                    ResourceType BLRes = BLCard.getCurrSide().getTRCorner().getResource();
//                                    playAreaArr[3 * y + 4][3 * x] = BLRes == null ? ' ' : BLRes.toString().charAt(0);
//                                }
//                                else {
//                                    playAreaArr[3 * y + 4][3 * x + 1] = '┬';
//                                    playAreaArr[3 * y + 3][3 * x] = '┤';
//                                    playAreaArr[3 * y + 4][3 * x] = '└';
//                                    ResourceType BLRes = card.getCurrSide().getBLCorner().getResource();
//                                    playAreaArr[3 * y + 3][3 * x + 1] = BLRes == null ? ' ' : BLRes.toString().charAt(0);
//                                }
//                            }
//                            else {
//                                playAreaArr[3 * y + 4][3 * x + 1] = '─';
//                                playAreaArr[3 * y + 3][3 * x] = '│';
//                                playAreaArr[3 * y + 4][3 * x] = '└';
//                                ResourceType BLRes = card.getCurrSide().getBLCorner().getResource();
//                                playAreaArr[3 * y + 3][3 * x + 1] = BLRes == null ? ' ' : BLRes.toString().charAt(0);
//                            }
//                        }
//
//                        // Check if there is a card in the BR corner
//                        if (playAreaArr[3 * y + 3][3 * x + 3] == '\u0000'){
//                            if (BRCard != null) {
//                                if (BRCard.getPriority() > card.getPriority()) {
//                                    playAreaArr[3 * y + 3][3 * x + 3] = '┌';
//                                    playAreaArr[3 * y + 3][3 * x + 4] = '┴';
//                                    playAreaArr[3 * y + 4][3 * x + 3] = '┤';
//                                    ResourceType BRRes = BRCard.getCurrSide().getTLCorner().getResource();
//                                    playAreaArr[3 * y + 4][3 * x + 4] = BRRes == null ? ' ' : BRRes.toString().charAt(0);
//                                }
//                                else {
//                                    playAreaArr[3 * y + 4][3 * x + 4] = '┘';
//                                    playAreaArr[3 * y + 3][3 * x + 4] = '├';
//                                    playAreaArr[3 * y + 4][3 * x + 3] = '┬';
//                                    ResourceType BRRes = card.getCurrSide().getBRCorner().getResource();
//                                    playAreaArr[3 * y + 3][3 * x + 3] = BRRes == null ? ' ' : BRRes.toString().charAt(0);
//                                }
//                            }
//                            else {
//                                playAreaArr[3 * y + 4][3 * x + 4] = '┘';
//                                playAreaArr[3 * y + 3][3 * x + 4] = '│';
//                                playAreaArr[3 * y + 4][3 * x + 3] = '─';
//                                ResourceType BRRes = card.getCurrSide().getBRCorner().getResource();
//                                playAreaArr[3 * y + 3][3 * x + 3] = BRRes == null ? ' ' : BRRes.toString().charAt(0);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return playAreaArr;
//    }
//
//    private void printMap() {
//        char[][] playAreaArr = createPlayAreaArr();
//
//        if (playAreaArr == null) {
//            System.out.println("The play area is empty!");
//            return;
//        }
//
//        int minX = playArea.keySet().stream().map(Position::getX).min(Integer::compareTo).orElse(0);
//        int maxY = playArea.keySet().stream().map(Position::getY).max(Integer::compareTo).orElse(0);
//
//        // Fill the validPosIDs
//        for (int i : posIDToValidPos.keySet()) {
//            Position pos = posIDToValidPos.get(i);
//            Position arrPos = new Position(3 * (pos.getX() - minX + 1), 3 * (-pos.getY() + maxY + 1));
//            if (i < 10) {
//                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 1] = (char) (i + 48);
//            }
//            else {
//                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 1] = (char) (i / 10 + 48);
//                playAreaArr[arrPos.getY() + 2][arrPos.getX() + 2] = (char) (i % 10 + 48);
//            }
//        }
//
//        // Print the play area
//        for (char[] chars : playAreaArr) {
//            StringBuilder line = new StringBuilder();
//
//            for (char c : chars) {
//                if (c == '\u0000') {
//                    line.append(' ');
//                } else {
//                    line.append(c);
//                }
//            }
//            System.out.println(line);
//        }
//    }

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

    private void printPlayArea() {
        String[][] playAreaArr = createPlayerArr(playArea);

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
                if (s != null) {
                    line.append(s);
                } else {
                    line.append(" ");
                }
            }
            System.out.println(line);
        }
    }
}
