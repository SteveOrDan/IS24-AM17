package com.example.pf_soft_ing.app;

import com.example.pf_soft_ing.network.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TUIApp {
    public static void main(String[] ip) {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        boolean connected = false;
        ClientSender sender = null;
        int portNumber = 0;

        while (!connected){
            System.out.println("Choose connection type:\n\t-'1' for Socket;\n\t-'2' for RMI;");

            try {
                String connectionType = stdIn.readLine();
                while (!connectionType.equals("1") && !connectionType.equals("2")) {
                    System.out.println("Please insert a valid choice (either 1 or 2).");
                    connectionType = stdIn.readLine();
                }
                System.out.println("Enter the port number:");
                boolean hasPort = false;

                while (!hasPort) {
                    try {
                        portNumber = Integer.parseInt(stdIn.readLine());
                        hasPort = true;
                    }
                    catch (Exception e) {
                        System.out.println("Please insert a valid number.");
                    }
                }

                if (connectionType.equals("1")) {
                    View view = new TUIView();
                    sender = ClientMain.startSocket(ip[0], portNumber, view);
                    view.setSender(sender);
                    connected = true;
                }
                else {
                    View view = new TUIView();
                    sender = ClientMain.startRMI(ip[0], portNumber, view);
                    view.setSender(sender);
                    connected = true;
                }
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sender.getMatches();
    }
}
