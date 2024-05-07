package com.example.pf_soft_ing.card;

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

        tuiGraphics.printBLTRDiagObjCard(CC.BLUE_BG);
        tuiGraphics.printTLBRDiagObjCard(CC.RED_BG);

        tuiGraphics.printBRLShapeCard(CC.GREEN_BG, CC.YELLOW_BG);
        tuiGraphics.printBLLShapeCard(CC.PURPLE_BG, CC.CYAN_BG);
        tuiGraphics.printTRLShapeCard(CC.RED_BG, CC.GREEN_BG);
        tuiGraphics.printTLLShapeCard(CC.YELLOW_BG, CC.PURPLE_BG);

        tuiGraphics.printTriElementCard(CC.BLUE_BG);

        tuiGraphics.printPlaceableCard(30, "front");
        tuiGraphics.printPlaceableCard(53, "front");
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

    public void printPlaceableCard(int cardID, String side) {
        PlaceableCard card = GameResources.getPlaceableCardByID(cardID);

        if (card instanceof ResourceCard) {
            printResCard((ResourceCard) card, side);
        }
        else if (card instanceof GoldenCard) {
            printGoldCard((GoldenCard) card, side);
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

        D = side.equals("back") ? "T" : " ";

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

        D = side.equals("back") ? "T" : " ";

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

        HashMap<ResourceType, Integer> requiredResources = card.getRequiredResources();

        int size = 0;
        for (ResourceType res : requiredResources.keySet()) {
            size += requiredResources.get(res);
        }

        if (size == 3) {
            G = " ";

            for (ResourceType res : requiredResources.keySet()) {
                for (int i = 0; i < requiredResources.get(res); i++) {
                    G += res.getColor() + res + CC.RESET;
                }
            }

            G += " ";
        }
        else if (size == 4) {
            G = "";

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
        else {
            G = "";
            for (ResourceType res : requiredResources.keySet()) {
                G += res.getColor() + res + CC.RESET;
            }
        }

        System.out.println(A1 + "═══════════" + B1);
        System.out.println(A2 + "    " + C + "    " + B2);
        System.out.println(A3 + "           " + B3);
        System.out.println("║           " + D + "           ║");
        System.out.println(E1 + "           " + F1);
        System.out.println(E2 + "   " + G + "   " + F2);
        System.out.println(E3 + "═══════════" + F3);
    }

    public void printBLTRDiagObjCard(String color) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                       ┃");
        System.out.println("┃                 " + color + "   " + CC.RESET + "   ┃");
        System.out.println("┃              " + color + "   " + CC.RESET + "      ┃");
        System.out.println("┃           " + color + "   " + CC.RESET + "         ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTLBRDiagObjCard(String color) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                       ┃");
        System.out.println("┃           " + color + "   " + CC.RESET + "         ┃");
        System.out.println("┃              " + color + "   " + CC.RESET + "      ┃");
        System.out.println("┃                 " + color + "   " + CC.RESET + "   ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printBRLShapeCard(String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                       ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃                 " + mainColor + "   " + CC.RESET + "   ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printBLLShapeCard(String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                       ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃           " + mainColor + "   " + CC.RESET + "         ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTRLShapeCard(String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                       ┃");
        System.out.println("┃                 " + mainColor + "   " + CC.RESET + "   ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTLLShapeCard(String mainColor, String secColor) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                       ┃");
        System.out.println("┃           " + mainColor + "   " + CC.RESET + "         ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃              " + secColor + "   " + CC.RESET + "      ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    public void printTriElementCard(String color) {
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃                       ┃");
        System.out.println("┃                       ┃");
        System.out.println("┃              " + color + "   " + CC.RESET + "      ┃");
        System.out.println("┃           " + color + "   " + CC.RESET + "   " + color + "   " + CC.RESET + "   ┃");
        System.out.println("┃                       ┃");
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━┛");
    }
}
