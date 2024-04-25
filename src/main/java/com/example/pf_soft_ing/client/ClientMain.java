package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class ClientMain {

//    private ClientController clientController;

    public static void main( String[] args ) {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome.\nThis is the application to play \"Codex Naturalis\".\n\nBefore starting you have to choose view; digit:\n\t-\'1\' for TUI;\n\t-\'2\' for GUI;\n");
        String input = null;
        boolean UIFlag = true;
        View view = null;
        while (UIFlag){
            try {
                input = stdIn.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (Objects.equals(input, "1")) {
                UIFlag = false;
                view = new TUIView();
            } else {
                if (Objects.equals(input, "2")) {
                    System.out.println("Work in progress ¯\\_(ツ)_/¯");
//                    UIFlag = false;
//                    view = new GUIView();
                }
            }
        }

        System.out.println("\n\nNow you have to choose the connection protocol; digit:\n\t-\'1\' for Socket connection;\n\t-\'2\' for RMI connection;\n");
        boolean startFlag = true;
        ClientController clientController = null;
        while (startFlag){
            try {
                input = stdIn.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (Objects.equals(input, "1")) {
                startFlag = false;
                clientController = startSocket(args, view);
            } else {
                if (Objects.equals(input, "2")) {
                    System.out.println("Work in progress ¯\\_(ツ)_/¯");
//                    startFlag = false;
//                    startRMI(view);
                }
            }
        }

        clientController.getMatches();

    }

    protected static ClientController startSocket(String[] args, View view){
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            ClientEncoder clientEncoder = new ClientSocketSender(out);
            ClientController clientController = new ClientController(clientEncoder, view);
            new ClientSocketReceiver(new ClientDecoder(clientController), in);

            return clientController;
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
        return null;
    }

    protected static void startRMI(){}
}
