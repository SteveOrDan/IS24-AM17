package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.RMIReceiverInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

public class ClientMain {

    public static void main(String[] ip) {
        System.out.println("Welcome.\nThis is the application to play \"Codex Naturalis\".\n\nBefore starting you have to choose view; digit:\n\t-'1' for TUI;\n\t-'2' for GUI;");

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input;
        boolean UIFlag = true;
        View view = null;

        while (UIFlag){
            try {
                input = stdIn.readLine();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (Objects.equals(input, "1")) {
                UIFlag = false;
                view = new TUIView();
            }
            else {
                if (Objects.equals(input, "2")) {
                    System.out.println("Work in progress ¯\\_(ツ)_/¯");
//                    UIFlag = false;
//                    view = new GUIView();
                }
            }
        }

        System.out.println("\nInsert the port number:");

        ClientController clientController = null;
        boolean connected = false;

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
                clientController = startSocket(ip[0], portNumber, view);

                connected = true;
                System.out.println("Socket connection established.");
            }
            catch (Exception e) {
                try {
                    clientController = startRMI(ip[0], portNumber, view);

                    connected = true;
                    System.out.println("RMI connection established.");
                }
                catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                    System.out.println("Please insert a valid port number.");
                }
            }
        }

        clientController.getMatches();
    }

    private static ClientController startSocket(String hostName, int portNumber, View view) throws IOException {
        Socket echoSocket = new Socket(hostName, portNumber);

        ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        ClientSender clientSender = new ClientSocketSender(out, stdin);
        clientSender.startInputReading();


        ClientController clientController = new ClientController(clientSender, view);

        ClientSocketReceiver clientReceiver = new ClientSocketReceiver(clientController);
        clientReceiver.startReceivingThread(in);

        return clientController;
    }

    private static ClientController startRMI(String hostName, int portNumber, View view) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostName, portNumber);
        RMIReceiverInterface server = (RMIReceiverInterface) registry.lookup("RMIReceiver");

        ClientSender clientSender = new ClientRMISender(server);
        ClientController clientController = new ClientController(clientSender, view);

        ClientRMIReceiver clientReceiver = new ClientRMIReceiver(clientController);
        clientSender.setClient(clientReceiver.getClient());

        return clientController;
    }
}
