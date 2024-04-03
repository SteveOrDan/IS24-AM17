package com.example.pf_soft_ing.ServerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static java.lang.System.exit;



public class RMIConnection {

    private static ServerSocket serverSocket;
    private static BufferedReader in;

    public static ServerSocket StartServerConnection(String[] args) {
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

    public static BufferedReader WaitClientConnection(){
        Socket clientSocket = null;
        try{
            clientSocket = serverSocket.accept();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Client connected!");

        return in;
    }

    public static void InputReader(BufferedReader bufferedReader){
        String s = "";
        try {
            while ((s = bufferedReader.readLine()) != null){
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void StartClientConnection( String[] args ) {
            System.out.println( "Hello World!");
    }
}
