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
    public static ServerSocket StartServerConnection(String[] args) {
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

    public static Socket ConnectClient(ServerSocket serverSocket) {
        Socket connectionSocket = null;

        try {
            connectionSocket = serverSocket.accept();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return connectionSocket;
    }

    public static PrintWriter CreateClientConnectionOUT(Socket connectionSocket) {
        PrintWriter out = null;

        try {
            out = new PrintWriter(connectionSocket.getOutputStream(), true);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out;
    }

    public static BufferedReader WaitClientConnectionIN(Socket connectionSocket){
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

    public static void CreateSocketThreads(BufferedReader in, PrintWriter out, GameController gameController){
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        new Thread(){
            public void run(){
                boolean isRunning = true;

                while (isRunning) {
                    String input = "";

                    try {
                        input = in.readLine();

                        if (input != null) {
                            if (input.equals("end")){
                                isRunning = false;
                            }

                            String finalInput = input;

                            new Thread() {
                                public void run() {
                                    MessageDecoder(finalInput, gameController, out);
                                }
                            }.start();
                        }
                    }
                    catch (IOException e) {
                        System.err.println(STR."Exception \{e}");
                        isRunning = false;
                    }
                }
            }
        }.start();

        String s = "";
        out.println("Choose Nickname:");
    }

    public static void SendMessage(String output, PrintWriter out){
        new Thread(){
            public void run(){
                out.println(output);
            }
        }.start();
    }

    public static void MessageDecoder(String input, GameController gameController,PrintWriter out) {
        System.out.println(input);
        String[] inputArray = input.split(" ");

        switch (inputArray[0]) {
            case "1":
                System.out.println("comando 1" + input);
                SendMessage("Bravo", out);
                break;

            case "2":
                System.out.println("comando 2" + input);
                SendMessage("Meno bravo", out);
                break;

            case "3":
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                SendMessage("Cattivo", out);
                break;

            case "4":
                int id = gameController.addPlayer(inputArray[1]);
                SendMessage("Your Id: " + id, out);
                break;

            case "end":
                SendMessage("end", out);
                break;

            default:
                System.out.println("extra");
                out.println("Errore");
        }
    }

}