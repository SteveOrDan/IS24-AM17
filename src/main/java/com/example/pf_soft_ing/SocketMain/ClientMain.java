package com.example.pf_soft_ing.SocketMain;

import com.example.pf_soft_ing.ServerConnection.SocketController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

import static com.example.pf_soft_ing.ServerConnection.SocketController.CreateClientConnectionOUT;
import static com.example.pf_soft_ing.ServerConnection.SocketController.WaitClientConnectionIN;

public class ClientMain {
    public static void main( String[] args ){
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        try {
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
        new Thread(){
            public void run(){
                boolean runFlag = true;
                String s = "";
                while (runFlag) {
                    try {
                        s = in.readLine();
                        if (s != null){
                        if (Objects.equals(s, "end")){
                            runFlag = false;
                        }
                        System.out.println(s);
                        }
                    } catch (IOException e) {
                        runFlag = false;
                        System.err.println(STR."Exception 1 \{e}");
                    }
                }
            }
        }.start();

        new Thread(){
            public void run(){
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in));
                boolean runFlag = true;
                while (runFlag) {
                    try {
                        String input = stdIn.readLine();
                        if (input != null) {
                            if (input.equals("end")) {
                                runFlag = false;
                            }
                            out.println(input);
                            System.out.println(STR."echo: \{input}");
                        }
                    } catch (IOException e) {
                        runFlag = false;
                        System.err.println(STR."Exception 2 \{e}");
                    }
                }
            }
        }.start();
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