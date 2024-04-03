package com.example.pf_soft_ing.Main;

import com.example.pf_soft_ing.ServerConnection.RMIConnection;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.nio.Buffer;
import java.rmi.Remote;

import static com.example.pf_soft_ing.ServerConnection.RMIConnection.WaitClientConnection;

public class ServerMain implements Remote {
    public static void main( String[] args ){
        ServerSocket serverSocket = RMIConnection.StartServerConnection(args);
        BufferedReader in = WaitClientConnection();
        RMIConnection.InputReader(in);


    }

}
