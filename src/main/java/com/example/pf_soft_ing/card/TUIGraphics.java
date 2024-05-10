package com.example.pf_soft_ing.card;

import com.example.pf_soft_ing.card.objectiveCards.*;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;

import java.util.HashMap;

public class TUIGraphics {

//    Unused emojis
//    private final static String ANIMAL = "\uD83D\uDC3A";
//    private final static String PLANT = "\uD83C\uDF3F";
//    private final static String FUNGI = "\uD83C\uDF44";
//    private final static String INSECT = "\uD83D\uDC1C";
//
//    private final static String MANUSCRIPT = "\uD83D\uDCDC";
//    private final static String INKWELL = "\uD83E\uDED9";
//    private final static String QUILL = "\uD83E\uDEB6";

    public static void main(String[] args) {
        TUIGraphics tuiGraphics = new TUIGraphics();

        GameResources.initializeAllDecks();

        for (int i = 0; i<=101 ; i++){

            System.out.println("CardID:" + i);
            tuiGraphics.printCard(i, "front");
            tuiGraphics.printCard(i, "back");
        }
    }

    public void printResCardRef() {
        System.out.println("┏━━━━━┳━━━━━━━━━━━┳━━━━━┓");
        System.out.println("┃     ┃           ┃     ┃");
        System.out.println("┣━━━━━┛           ┗━━━━━┫");
        System.out.println("┃                       ┃");
        System.out.println("┣━━━━━┓           ┏━━━━━┫");
        System.out.println("┃     ┃           ┃     ┃");
        System.out.println("┗━━━━━┻━━━━━━━━━━━┻━━━━━┛");
    }

    public void printGoldCardRef() {
        System.out.println("╔═════╦═══════════╦═════╗");
        System.out.println("║     ║           ║     ║");
        System.out.println("╠═════╝           ╚═════╣");
        System.out.println("║                       ║");
        System.out.println("╠═════╗           ╔═════╣");
        System.out.println("║     ║           ║     ║");
        System.out.println("╚═════╩═══════════╩═════╝");
    }
    public void printStarCardRef() {
        System.out.println("┌─────┬───────────┬─────┐");
        System.out.println("│     │           │     │");
        System.out.println("├─────┘           └─────┤");
        System.out.println("│                       │");
        System.out.println("├─────┐           ┌─────┤");
        System.out.println("│     │           │     │");
        System.out.println("└─────┴───────────┴─────┘");
    }
    public void printStarterCard(StarterCard card, String side){
        String A1, A2, A3, B1, B2, B3, D1, D2, D3, E1, E2, E3, F1, F2, F3;
        int resourcesCorners;

        Side cardSide;

        if (side.equals("front")){
            cardSide = card.getFront();
        }
        else {
            cardSide = card.getBack();
        }

        resourcesCorners = 0;

        if (cardSide.getTLCorner().isAvailable()) {
            A1 = "┌─────┬";
//            String r = cardSide.getTLCorner().getResource() != null ?
//                    cardSide.getTLCorner().getResource().getColor() + cardSide.getTLCorner().getResource().toString() + CC.RESET: " ";
            String r = " ";
            if(cardSide.getTLCorner().getResource() != null){
                r = cardSide.getTLCorner().getResource().getColor() + cardSide.getTLCorner().getResource().toString() + CC.RESET;
                resourcesCorners = resourcesCorners + 1;
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
//            String r = cardSide.getTRCorner().getResource() != null ?
//                    cardSide.getTRCorner().getResource().getColor() + cardSide.getTRCorner().getResource().toString() + CC.RESET : " ";

            String r = " ";
            if(cardSide.getTRCorner().getResource() != null){
                r = cardSide.getTRCorner().getResource().getColor() + cardSide.getTRCorner().getResource().toString() + CC.RESET ;
                resourcesCorners = resourcesCorners + 1;
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
            if(cardSide.getBLCorner().getResource() != null){
                r = cardSide.getBLCorner().getResource().getColor() + cardSide.getBLCorner().getResource().toString() + CC.RESET;
                resourcesCorners = resourcesCorners + 1;
            }
//            String r = cardSide.getBLCorner().getResource() != null ?
//                    cardSide.getBLCorner().getResource().getColor() + cardSide.getBLCorner().getResource().toString() + CC.RESET : " ";
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
//            String r = cardSide.getBRCorner().getResource() != null ?
//                    cardSide.getBRCorner().getResource().getColor() + cardSide.getBRCorner().getResource().toString() + CC.RESET : " ";
            String r = " ";

            if(cardSide.getBRCorner().getResource() != null){
                r = cardSide.getBRCorner().getResource().getColor() + cardSide.getBRCorner().getResource().toString() + CC.RESET;
                resourcesCorners = resourcesCorners + 1;
            }
            F2 = "│  " + r + "  │";
            F3 = "┴─────┘";
        }
        else {
            F1 = "      │";
            F2 = "      │";
            F3 = "──────┘";
        }

        D1 = " ";
        D2 = " ";
        D3 = " ";


        if (side.equals("back")) {
            int resourcesToPrint = cardSide.getResources().size() - resourcesCorners;
            int indexList;
            if (resourcesCorners == 0) {
                indexList = 0;
            } else {
                indexList = resourcesToPrint - 1;
            }

            if (resourcesToPrint == 1) {
                D1 = cardSide.getResources().get(indexList).toString();
                D2 = " ";
                D3 = " ";
            }
            if (resourcesToPrint == 2) {
                D1 = cardSide.getResources().get(indexList).toString();
                D2 = cardSide.getResources().get(indexList + 1).toString();
                D3 = " ";
            }
            if (resourcesToPrint == 3) {
                D1 = cardSide.getResources().get(indexList).toString();
                D2 = cardSide.getResources().get(indexList + 1).toString();
                D3 = cardSide.getResources().get(indexList + 2).toString();
            }
        }

        System.out.println(A1 + "───────────" + B1);
        System.out.println(A2 + "           " + B2);
        System.out.println(A3 + "     " + D1 + "     " + B3);
        System.out.println("│           " + D2 + "           │");
        System.out.println(E1 + "     " + D3 +"     " + F1);
        System.out.println(E2 + "           " + F2);
        System.out.println(E3 + "───────────" + F3);
    }


    public void printCard(int cardID, String side){
        if (cardID < 86) {
            PlaceableCard card = GameResources.getPlaceableCardByID(cardID);
            printPlaceableCard(card, side);
        } else {
            ObjectiveCard card = GameResources.getObjectiveCardByID(cardID);
            printObjectiveCard(card);
        }
    }
    public void printPlaceableCard(PlaceableCard card, String side) {
//        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);

        if (card instanceof ResourceCard) {
            printResCard((ResourceCard) card, side);
        }
        if (card instanceof GoldenCard) {
            printGoldCard((GoldenCard) card, side);
        }
        if (card instanceof StarterCard) {
            printStarterCard((StarterCard) card, side);
        }
    }

    public void printObjectiveCard(ObjectiveCard card) {
//        ObjectiveCard card = GameResources.getObjectiveCardByID(cardID);
        if(card instanceof TLBRDiagonalObjectiveCard){
            if (((TLBRDiagonalObjectiveCard) card).elementType.equals(CardElementType.INSECT)) {
                printTLBRDiagObjCard(card, CC.PURPLE_BG);
            }
            else
                printTLBRDiagObjCard(card, CC.GREEN_BG);
        }
        if (card instanceof TRBLDiagonalObjectiveCard){
            if (((TRBLDiagonalObjectiveCard) card).elementType.equals(CardElementType.FUNGI)) {
                printBLTRDiagObjCard(card, CC.RED_BG);
            }
            else
                printBLTRDiagObjCard(card, CC.CYAN_BG);
        }
        if (card instanceof TLLShapeObjectiveCard){
            printTLLShapeCard(card, CC.CYAN_BG, CC.PURPLE_BG);
        }
        if (card instanceof  TRLShapeObjectiveCard){
            printTRLShapeCard(card, CC.RED_BG, CC.CYAN_BG);
        }
        if (card instanceof BLLShapeObjectiveCard){
            printBLLShapeCard(card, CC.PURPLE_BG, CC.GREEN_BG);
        }
        if (card instanceof BRLShapeObjectiveCard) {
            printBRLShapeCard(card, CC.GREEN_BG, CC.RED_BG);
        }
        if (card instanceof  ResourcesCountObjectiveCard) {
            String Resources = ((ResourcesCountObjectiveCard) card).resourceType.toString();
            switch (Resources) {
                case "A":{
                    printTriElementCard((ResourcesCountObjectiveCard) card, CC.CYAN_BG);

                    break;
                }
                case "P":{
                    printTriElementCard((ResourcesCountObjectiveCard)card, CC.GREEN_BG);

                    break;
                }
                case "F":{
                    printTriElementCard((ResourcesCountObjectiveCard)card, CC.RED_BG);

                    break;
                }
                case "I":{
                    printTriElementCard((ResourcesCountObjectiveCard)card, CC.PURPLE_BG);

                    break;
                }
                default: {
                    printTriElementCard((ResourcesCountObjectiveCard)card, CC.RESET);

                    break;
                }
            }
        }
        if (card instanceof  TrinityObjectiveCard) {
            printTrinityCard((TrinityObjectiveCard) card);
        }
    }

    private void printResCard(ResourceCard card, String side) {
        String A1, A2, A3, B1, B2, B3, C, D, E1, E2, E3, F1, F2, F3;

        Side cardSide;

        if (side.equals("front")){
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

        C = (side.equals("front") && card.getPoints() != 0) ? String.valueOf(card.getPoints()) : " ";

        D = " ";
        if (side.equals("back")){
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

        System.out.println(A1 + "━━━━━━━━━━━" + B1);
        System.out.println(A2 + "     " + C + "     " + B2);
        System.out.println(A3 + "           " + B3);
        System.out.println("┃           " + D + "           ┃");
        System.out.println(E1 + "           " + F1);
        System.out.println(E2 + "           " + F2);
        System.out.println(E3 + "━━━━━━━━━━━" + F3);
    }

    private void printGoldCard(GoldenCard card, String side) {
        String A1, A2, A3, B1, B2, B3, C, D, E1, E2, E3, F1, F2, F3, G;

        Side cardSide;

        if (side.equals("front")){
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

        if (side.equals("front")) {
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
        if (side.equals("back")){
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

        G = "";
        HashMap<ResourceType, Integer> requiredResources = card.getRequiredResources();

        int size = 0;
        for (ResourceType res : requiredResources.keySet()) {
            size += requiredResources.get(res);
        }

        if(side.equals("front")){

            if (size == 3) {
                for (ResourceType res : requiredResources.keySet()) {
                    for (int i = 0; i < requiredResources.get(res); i++) {
                        G += res.getColor() + res + CC.RESET;
                    }
                }

                G += "  ";
            }
            if (size == 4) {

                int checkedNum = 0;
                for (ResourceType res : requiredResources.keySet()) {
                    for (int i = 0; i < requiredResources.get(res); i++) {
                        if (checkedNum == 2) {
                            G += " ";
                        }
                        G += res.getColor() + res + CC.RESET;
                        checkedNum++;
                    }
                }
            }
            if (size == 5) {
                for (ResourceType res : requiredResources.keySet()) {
                    for (int i = 0; i < requiredResources.get(res); i++) {
                        G += res.getColor() + res + CC.RESET;
                    }
                }
            }
        }else {
            if (size == 3) {
                G += "     ";
            }
            if (size == 4) {
                G += "     ";
            }
            if (size == 5) {
                G += "     ";
            }
        }

        System.out.println(A1 + "═══════════" + B1);
        System.out.println(A2 + "    " + C + "    " + B2);
        System.out.println(A3 + "           " + B3);
        System.out.println("║           " + D + "           ║");
        System.out.println(E1 + "           " + F1);
        System.out.println(E2 + "    " + G + "  " + F2);
        System.out.println(E3 + "═══════════" + F3);
    }

    public void printBLTRDiagObjCard(ObjectiveCard card, String color) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃               "+ card.getPoints()+ "       ┃");
        System.out.println("┃                 " + color + "   " + CC.RESET + "   ┃");
        System.out.println("┃              " + color + "   " + CC.RESET + "      ┃");
        System.out.println("┃           " + color + "   " + CC.RESET + "         ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTLBRDiagObjCard(ObjectiveCard card, String color) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃               "+ card.getPoints()+ "       ┃");
        System.out.println("┃           " + color + "   " + CC.RESET + "         ┃");
        System.out.println("┃              " + color + "   " + CC.RESET + "      ┃");
        System.out.println("┃                 " + color + "   " + CC.RESET + "   ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printBRLShapeCard(ObjectiveCard card, String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃               "+ card.getPoints()+ "       ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃                 " + mainColor + "   " + CC.RESET + "   ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printBLLShapeCard(ObjectiveCard card, String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃               "+ card.getPoints()+ "       ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃           " + mainColor + "   " + CC.RESET + "         ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTRLShapeCard(ObjectiveCard card, String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃               "+ card.getPoints()+ "       ┃");
        System.out.println("┃                 " + mainColor + "   " + CC.RESET + "   ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTLLShapeCard(ObjectiveCard card, String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃               "+ card.getPoints()+ "       ┃");
        System.out.println("┃           " + mainColor + "   " + CC.RESET + "         ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTriElementCard(ResourcesCountObjectiveCard card, String color) {
        if (94 <= card.getID() && card.getID() <= 97) {

            System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("┃               "+ card.getPoints()+ "       ┃");
            System.out.println("┃                       ┃");
            System.out.println("┃              " + color + "   " + CC.RESET + "      ┃");
            System.out.println("┃           " + color + "   " + CC.RESET + "   " + color + "   " + CC.RESET + "   ┃");
            System.out.println("┃                       ┃");
            System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
        } else {
            String C = card.resourceType.toString();

            System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("┃               "+ card.getPoints()+ "       ┃");
            System.out.println("┃                       ┃");
            System.out.println("┃              " + C + "  "  + C + "     ┃");
            System.out.println("┃                       ┃");
            System.out.println("┃                       ┃");
            System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
        }
    }
    public void printTrinityCard(TrinityObjectiveCard card) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃               "+ card.getPoints()+ "       ┃");
        System.out.println("┃                       ┃");
        System.out.println("┃            " + "Q  K  M" + "    ┃");
        System.out.println("┃                       ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }
}

