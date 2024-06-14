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

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Error: You have to insert the IP address and the view type (TUI or GUI).");
            return;
        }

        if (args[2].equals("TUI")) {
            TUIApp.main(args);
        }
        else {
            GUIApp.main(args);
        }
    }

    /**
     * Starts a socket connection with the server
     * @param hostName IP address of the server
     * @param portNumber Port number of the server
     * @param view View of the client
     * @return ClientSender object
     * @throws IOException If the connection fails
     */
    public static ClientSender startSocket(String hostName, int portNumber, View view) throws IOException {
        Socket echoSocket = new Socket(hostName, portNumber);

        ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());

        ClientSocketReceiver clientReceiver = new ClientSocketReceiver(view);
        clientReceiver.startReceivingThread(in);

        return new ClientSocketSender(out);
    }

    /**
     * Starts an RMI connection with the server
     * @param hostName IP address of the server
     * @param portNumber Port number of the server
     * @param view View of the client
     * @return ClientSender object
     * @throws RemoteException If the connection fails
     * @throws NotBoundException If the connection fails
     */
    public static ClientSender startRMI(String hostName, int portNumber, View view) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostName, portNumber);
        RMIReceiverInterface serverInterface = (RMIReceiverInterface) registry.lookup("RMIReceiver");

        ClientRMIReceiver clientReceiver = new ClientRMIReceiver(view);

        return new ClientRMISender(serverInterface, clientReceiver.getClient());
    }
}
