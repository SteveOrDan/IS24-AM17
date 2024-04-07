package com.example.pf_soft_ing.ServerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import static java.lang.System.exit;



public class RMIConnection {
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
            } catch (IOException e) {
                System.out.println(e.toString());
                ok = false;
            }
            ok = true;
            if (!ok) {
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

    public static void InputReader(BufferedReader in, PrintWriter out){
        BufferedReader stdIn =
                new BufferedReader(
                        new InputStreamReader(System.in));
        String s = "";
        out.println("userInput_");
        try {
            while (true) {
                s = in.readLine();
                System.out.println(s);
                System.out.println("Enter new command here : ");
                String input = null;
                try {
                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                    input = bufferRead.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println(input);
                System.out.println("echo: " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void StartClientConnection( String[] args ) {

    }
}