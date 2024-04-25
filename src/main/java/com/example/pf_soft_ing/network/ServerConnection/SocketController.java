package com.example.pf_soft_ing.network.ServerConnection;

import com.example.pf_soft_ing.card.PlaceableCard;
import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.game.MatchModel;
import com.example.pf_soft_ing.player.PlayerModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.System.exit;

public class SocketController {
    public static ServerSocket startServerConnection(String[] args) {
        ServerSocket serverSocket = null;

        if (args.length != 1) {
            System.out.println("Cannot start port:" + Arrays.toString(args));
            exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        boolean foundPort = false;
        while (!foundPort) {
            try {
                serverSocket = new ServerSocket(portNumber);
                foundPort = true;
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
                portNumber++;
            }
        }

        System.out.println(STR."Server started! Port:\{portNumber}");

        return serverSocket;
    }

    public static Socket connectClient(ServerSocket serverSocket) {
        Socket connectionSocket;

        try {
            connectionSocket = serverSocket.accept();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return connectionSocket;
    }

    public static PrintWriter createClientConnectionOUT(Socket connectionSocket) {
        PrintWriter out;

        try {
            out = new PrintWriter(connectionSocket.getOutputStream(), true);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out;
    }

    public static BufferedReader waitClientConnectionIN(Socket connectionSocket){
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Client connected!");
        return in;
    }

    public static void startInteraction(BufferedReader in, PrintWriter out, GameController gameController){
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));



//        boolean isValid = false;
//        String input;
//        while(!isValid) {
//            out.println("Choose Nickname:");
//
//            try {
//                input = in.readLine();
//                if (input.equals("end")){
//                    isValid = false;
//                }
//                new Thread() {
//                    public void run() {
//                        messageDecoder(input, gameController, out);
//                    }
//                }.start();
//            } catch (IOException e) {
//                System.err.println(STR."Exception \{e.getMessage()}");
//                isValid = false;
//            }
//        }

        new Thread(){
            public void run(){
                boolean isRunning = true;

                while (isRunning) {
                    String input;

                    try {
                        input = in.readLine();
                        if (input.equals("end")){
                            isRunning = false;
                        }
                        messageDecoder(input, gameController, out, in);
                    }
                    catch (IOException e) {
                        System.err.println(STR."Exception \{e.getMessage()}");
                        isRunning = false;
                    }
                }
                interrupt();
            }
        }.start();
    }

    private static void sendMessage(String output, PrintWriter out){
        new Thread(){
            public void run(){
                out.println(output);
            }
        }.start();
    }

    private static void messageDecoder(String input, GameController gameController,PrintWriter out, BufferedReader in) {
        System.out.println(input);
        String[] inputArray = input.split(" ");
        StringBuilder output = new StringBuilder("");
        Map<Integer, List<String>> matchesNicknames = gameController.getGameModel().getMatches();
        matchesNicknames.clear();
        MatchController matchController;

        switch (inputArray[0]) {
            case "0":
                output.append("0");
                for (Integer matchID : matchesNicknames.keySet()){
                    output.append(" ");
                    output.append(STR."M \{matchID}");
                    for (String s : matchesNicknames.get(matchID)){
                        output.append(" P ");
                        output.append(s);
                    }
                }
                sendMessage(output.toString(), out);
                break;


            case "1":
                matchController = gameController.getGameModel().getMatch(Integer.parseInt(inputArray[1]));
                if (matchController == null ){
                    output.append("1");
                    for (Integer matchID : matchesNicknames.keySet()){
                        output.append(" ");
                        output.append(STR."M \{matchID}");
                        for (String s : matchesNicknames.get(matchID)){
                            output.append(" P ");
                            output.append(s);
                        }
                    }
                } else {
                    output.append("2");
                    for (String s : matchController.getMatchModel().getNicknames()){
                        output.append(" ");
                        output.append(s);
                    }
                }

                //TODO synchronize on matchController

                sendMessage(output.toString(), out);
                getNickname(input, matchController, out, in);
                break;

            case "2":
                matchController = gameController.createGame(Integer.parseInt(inputArray[1]));
                //TODO synchronize on matchController

                output.append(STR."2 \{matchController.getMatchModel().getMatchID()}");
                getNickname( input, matchController, out, in);
                break;

            default:
                System.out.println("Extra");
                out.println("Error");
        }
    }

    private static void getNickname(String input, MatchController matchController, PrintWriter out, BufferedReader in){
        boolean isRunning = true;

        while (isRunning) {
            String input1;

            try {
                input1 = in.readLine();
                messageDecoder(input1, matchController, out, in);
            }
            catch (IOException e) {
                System.err.println(STR."Exception \{e.getMessage()}");
                isRunning = false;
            }
        }
    }

    private static void messageDecoder(String input, MatchController matchController,PrintWriter out, BufferedReader in) {
        System.out.println(input);
        String[] inputArray = input.split(" ");
        StringBuilder output = new StringBuilder("");

        switch (inputArray[0]) {

            case "3":
                try {
                    Integer playerId = matchController.addPlayer(inputArray[1], out);
                    output.append(STR."4 \{playerId} \{matchController.getMatchModel().getIDToPlayerMap().get(playerId).getNickname()}");
                    for (String s : matchController.getMatchModel().getNicknames()){
                        if (s != matchController.getMatchModel().getIDToPlayerMap().get(playerId).getNickname()){
                            output.append(STR." \{s}");
                        }
                    }
                    sendMessage(output.toString(), out);
                } catch (GameIsFullException e) {
                    output.append(STR."3 ");
                    for (String s : matchController.getMatchModel().getNicknames()){
                        output.append(STR." \{s}");
                    }
                    sendMessage(output.toString(), out);
                } catch (NicknameAlreadyExistsException e) {
                    output.append(STR."3 ");
                    for (String s : matchController.getMatchModel().getNicknames()){
                        output.append(STR." \{s}");
                    }
                    sendMessage(output.toString(), out);
                    throw new RuntimeException(e);
                }
                break;

            default:
                System.out.println("Extra");
                out.println("Error");
        }
    }

}