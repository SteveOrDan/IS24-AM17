package com.example.pf_soft_ing.app;

import com.example.pf_soft_ing.network.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TUIApp {
    public static void main(String[] ip) {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter port number:");

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
                sender = ClientMain.startSocket(ip[0], portNumber, new TUIView());

                connected = true;
                System.out.println("Socket connection established.");
            }
            catch (Exception e) {
                try {
                    sender = ClientMain.startRMI(ip[0], portNumber, new TUIView());

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
}
