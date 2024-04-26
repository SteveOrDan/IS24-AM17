package com.example.pf_soft_ing.game.networkControllers;

import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
//                System.out.println(e.getMessage());
                portNumber++;
            }
        }

        System.out.println(STR."Soket port:\{portNumber}");

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
        MatchController matchController = null;

        boolean isRunning = true;

        while (isRunning) {
            String input;

            try {
                input = in.readLine();
                if (input.equals("end")){
                    isRunning = false;
                }
                matchController = messageDecoder(input, gameController, matchController, out, in);
            }
            catch (IOException e) {
                System.err.println(STR."Exception \{e.getMessage()}");
                isRunning = false;
            }
        }
    }

    private static MatchController messageDecoder(String input, GameController gameController, MatchController matchController, PrintWriter out, BufferedReader in) {
        String[] inputArray = input.split(" ");
        StringBuilder output = new StringBuilder("");
        Map<Integer, List<String>> matchesNicknames = gameController.getGameModel().getMatches();

        switch (inputArray[0]) {
            case "0":
                gameController.getMatches(out);
                return null;

            case "1":
                return gameController.sendMatch(Integer.parseInt(inputArray[1]), out);

            case "2":
                return gameController.createMatch(Integer.parseInt(inputArray[1]), out);

            case "3":
                gameController.sendNickname(matchController, inputArray[1], out, in);
                return matchController;

            default:
                System.out.println("Extra");
                out.println("Error");
        }
        return null;
    }

}