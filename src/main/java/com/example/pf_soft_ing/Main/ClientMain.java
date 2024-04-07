package com.example.pf_soft_ing.Main;

import com.example.pf_soft_ing.ServerConnection.RMIConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {
    public static void main( String[] args ){
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {
            String s = "";
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
                out.println(input + "_");
                System.out.println("echo: " + input);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }


    }

}
