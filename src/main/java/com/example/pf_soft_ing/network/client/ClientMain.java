package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.app.GUIApp;
import com.example.pf_soft_ing.app.TUIApp;
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
        boolean hasChosenUI = false;

        while (!hasChosenUI){
            try {
                input = stdIn.readLine();

                if (input.equals("1")) {
                    TUIApp.main(ip);
                    hasChosenUI = true;
                }
                else if (input.equals("2")) {
                    GUIApp.main(ip);
                    hasChosenUI = true;
                }
            }
            catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
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
        clientSender.startInputReading();

        ClientRMIReceiver clientReceiver = new ClientRMIReceiver(view);
        clientSender.setClient(clientReceiver.getClient());

        return clientSender;
    }
}
