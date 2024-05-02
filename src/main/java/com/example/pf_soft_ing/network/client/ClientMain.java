package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.network.server.RMIReceiverInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {

    public static void main(String[] ip) {
        System.out.println("Welcome.\nThis is the application to play \"Codex Naturalis\".\n\nBefore starting you have to choose view:\n\t-'1' for TUI;\n\t-'2' for GUI;");

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String input;
        View view = null;
        boolean hasChosenUI = false;

        while (!hasChosenUI){
            try {
                input = stdIn.readLine();

                if (input.equals("1")) {
                    hasChosenUI = true;
                    view = new TUIView();
                }
                else if (input.equals("2")) {
                    System.out.println("Work in progress ¯\\_(ツ)_/¯");
                    hasChosenUI = true;
                    view = new GUIView();
                }
            }
            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("\nInsert the port number:");

        boolean connected = false;
        ClientSender sender = null;

        while (!connected){
            boolean havePort = false;
            int portNumber = 0;

            while (!havePort){
                try {
                    portNumber = Integer.parseInt(stdIn.readLine());
                    havePort = true;
                }
                catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.println("Please insert a valid number.");
                }
            }

            try {
                sender = startSocket(ip[0], portNumber, view);

                connected = true;
                System.out.println("Socket connection established.");
            }
            catch (Exception e) {
                try {
                    sender = startRMI(ip[0], portNumber, view);

                    connected = true;
                    System.out.println("RMI connection established.");
                }
                catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                    System.out.println("Please insert a valid port number.");
                }
            }
        }

        sender.getMatches();
    }

    public static ClientSender startSocket(String hostName, int portNumber, View view) throws IOException {
        Socket echoSocket = new Socket(hostName, portNumber);

        ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        ClientSender clientSender = new ClientSocketSender(out, stdin);
        clientSender.startInputReading();

        ClientSocketReceiver clientReceiver = new ClientSocketReceiver(view);
        clientReceiver.startReceivingThread(in);

        return clientSender;
    }

    public static ClientSender startRMI(String hostName, int portNumber, View view) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostName, portNumber);
        RMIReceiverInterface serverInterface = (RMIReceiverInterface) registry.lookup("RMIReceiver");

        ClientSender clientSender = new ClientRMISender(serverInterface);

        ClientRMIReceiver clientReceiver = new ClientRMIReceiver(view);
        clientSender.setClient(clientReceiver.getClient());

        return clientSender;
    }
}
