package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.GameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

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
        PrintWriter out = null;

        try {
            out = new PrintWriter(connectionSocket.getOutputStream(), true);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out;
    }

    public static BufferedReader waitClientConnectionIN(Socket connectionSocket){
        BufferedReader in =null;
        try {
            in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Client connected!");
        return in;
    }

    public static void createSocketThreads(BufferedReader in, PrintWriter out, GameController gameController){
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

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

                        new Thread() {
                            public void run() {
                                messageDecoder(input, gameController, out);
                            }
                        }.start();
                    }
                    catch (IOException e) {
                        System.err.println(STR."Exception \{e.getMessage()}");
                        isRunning = false;
                    }
                }
                interrupt();
            }
        }.start();

        out.println("Choose Nickname:");
    }

    public static void sendMessage(String output, PrintWriter out){
        new Thread(){
            public void run(){
                out.println(output);
            }
        }.start();
    }

    public static void messageDecoder(String input, GameController gameController,PrintWriter out) {
        System.out.println(input);
        String[] inputArray = input.split(" ");

        switch (inputArray[0]) {
            case "1":
                System.out.println("Command 1" + input);
                sendMessage("Good", out);
                break;

            case "2":
                System.out.println("Command 2" + input);
                sendMessage("Meh", out);
                break;

            case "3":
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sendMessage("Idiot", out);
                break;

            case "4":
                int id = gameController.addPlayer(inputArray[1]);
                sendMessage("Your Id: " + id, out);
                break;

            case "end":
                sendMessage("End", out);
                break;

            default:
                System.out.println("Extra");
                out.println("Error");
        }
    }

}