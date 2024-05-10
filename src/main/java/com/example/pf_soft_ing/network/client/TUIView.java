package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.*;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.game.GameResources;
import com.example.pf_soft_ing.player.TokenColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TUIView implements View {

    private ClientSender sender;

    Map<Integer, List<String>> cardIDToCardFrontTUILines;
    Map<Integer, List<String>> cardIDToCardBackTUILines;

    public void setSender(ClientSender sender) {
        this.sender = sender;
    }

    @Override
    public void setID(int playerID) {
        sender.setPlayerID(playerID);
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
        System.out.println("Match created with ID: " + matchID + " and host nickname: " + hostNickname);
        System.out.println("Wait for players to join...");
    }

    @Override
    public void selectMatch(int matchID, List<String> nicknames) {
        System.out.println("Match selected with ID: " + matchID);
        System.out.println("To choose the nickname type: ChooseNickname <nickname>");
    }

    @Override
    public void chooseNickname(String nickname) {
        System.out.println("Joined match with nickname: " + nickname);
    }

    @Override
    public void startGame(int resDeckCardID, int visibleResCardID1, int visibleResCardID2,
                          int goldDeckCardID, int visibleGoldCardID1, int visibleGoldCardID2,
                          int starterCardID) {
        System.out.println("Game started.");

        // Print 4 visible cards, 2 deck top card and starter card
        printDrawArea(resDeckCardID, visibleResCardID1, visibleResCardID2,
                goldDeckCardID, visibleGoldCardID1, visibleGoldCardID2);

        // Create and print starter card
        createStarterCardLines(starterCardID, CardSideType.FRONT);
        createStarterCardLines(starterCardID, CardSideType.BACK);

        System.out.println("To view the other side of the starter card type: FlipStarterCard");
        System.out.println("To place the starter card on the current side type: PlaceStarterCard");
    }

    @Override
    public void setMissingSetUp(int resourceCardID1, int resourceCardID2, int goldenCardID, TokenColors tokenColor, int commonObjectiveCardID1, int commonObjectiveCardID2, int secretObjectiveCardID1, int secretObjectiveCardID2) {

    }

    @Override
    public void confirmSecretObjective(int secretObjectiveCardID) {

    }

    @Override
    public void errorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showNewPlayer(String nicknames) {
        System.out.println("New player joined: " + nicknames);
        System.out.println("Wait for players to join...");
    }

    @Override
    public void showPlayerTurn(int playerID) {

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
                D1 = cardSide.getResources().getFirst().toString();
                D2 = " ";
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
}
