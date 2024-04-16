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

        boolean ok = false;
        while (!ok) {
            try {
                serverSocket = new ServerSocket(portNumber);
                ok = true;
            } catch (IOException e) {
                System.out.println(e.toString());
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return connectionSocket;
    }

    public static PrintWriter CreateClientConnectionOUT(Socket connectionSocket) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(connectionSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    public static BufferedReader WaitClientConnectionIN(Socket connectionSocket){
        BufferedReader in =null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Client connected!");
        return in;
    }

    public static void InputReader(BufferedReader in, PrintWriter out, GameController gameController){
        BufferedReader stdIn =
                new BufferedReader(
                        new InputStreamReader(System.in));
        String s = "";
        out.println("Choose Nickname:");
        try {
            s = in.readLine();
            System.out.println(s);
            out.println("Your Id: " + gameController.addPlayer(s));
            while (true) {
                String sInput;
//                try {
//                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//                    input = bufferRead.readLine();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                out.println(input);
//                System.out.println("echo: " + input);
                sInput = in.readLine();
                System.out.println(sInput);
                String[] inputArray = sInput.split(" ");
                switch (inputArray[0]){
                    case "1":
                        System.out.println("comando 1" + sInput);
                        out.println("Bravo");
                        break;
                    case "2":
                        System.out.println("comando 2" + sInput);
                        out.println("Meno bravo");
                        break;
                    case "3":
                        System.out.println("comando 3" + sInput);
                        out.println("Cattivo");
                        break;
                    default:
                        System.out.println("extra");
                        out.println("Errore");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void StartClientConnection( String[] args ) {

    }
}