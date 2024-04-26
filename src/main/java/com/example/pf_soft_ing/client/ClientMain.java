package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.Encoder;
import com.example.pf_soft_ing.ServerConnection.RMIReceiverInterface;
import com.example.pf_soft_ing.network.RMI.ClientRMI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Objects;

public class ClientMain {

//    private ClientController clientController;

    public static void main( String[] ip ) {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome.\nThis is the application to play \"Codex Naturalis\".\n\nBefore starting you have to choose view; digit:\n\t-\'1\' for TUI;\n\t-\'2\' for GUI;");
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
        System.out.println("\nInsert the port number:");
        String port;
        try {
            port = stdIn.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nNow you have to choose the connection protocol; digit:\n\t-\'1\' for Socket connection;\n\t-\'2\' for RMI connection;");
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
                clientController = startSocket(ip, port, view);
            } else {
                if (Objects.equals(input, "2")) {
                    startFlag = false;
                    clientController = startRMI(ip, port, view);
                }
            }
        }

        clientController.getMatches();

    }

    protected static ClientController startSocket(String[] ip, String port, View view){
        String hostName = ip[0];
        int portNumber = Integer.parseInt(port);

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
            System.out.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
        return null;
    }

    protected static ClientController startRMI(String[] ip, String port, View view){
        String hostName = ip[0];
        int portNumber = Integer.parseInt(port);

        try {
//            ClientRMI clientRMI = new ClientRMI(hostName, portNumber);
//            clientRMI.startClient();
            Registry registry = LocateRegistry.getRegistry(hostName, portNumber);
            RMIReceiverInterface server = (RMIReceiverInterface) registry.lookup("RMIReceiver");

            ClientEncoder clientEncoder = new ClientRMISender(server);
            ClientController clientController = new ClientController(clientEncoder, view);
            ClientRMIReceiver clientRMIReceiver = new ClientRMIReceiver(new ClientDecoder(clientController));
            clientEncoder.setClient(clientRMIReceiver.getClient());

            return clientController;
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
//        String hostName = args[0];
//        int portNumber = Integer.parseInt(args[1]);
//        try {
//            startRMIReceiver(portNumber);
//            ClientRMI clientRMI = new ClientRMI(hostName, portNumber);
//            clientRMI.startClient();
//            ClientEncoder clientEncoder = new ClientRMISender();
//            ClientController clientController = new ClientController(ClientEncoder, view);
//
//        } catch (NotBoundException e) {
//            throw new RuntimeException(e);
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
    }
}
