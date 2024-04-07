package com.example.pf_soft_ing.Main;

import com.example.pf_soft_ing.ServerConnection.RMIConnection;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.pf_soft_ing.ServerConnection.RMIConnection.*;

public class ServerMain {
    public static void main( String[] args ){
        ServerSocket serverSocket = RMIConnection.StartServerConnection(args);
        new Thread(){
            public void run(){
                Socket newClientSocket = ConnectClient(serverSocket);
                PrintWriter out = CreateClientConnectionOUT(newClientSocket);
                BufferedReader in = WaitClientConnectionIN(newClientSocket);
                RMIConnection.InputReader(in, out);
            }
        }.start();
        new Thread(){
            public void run(){
                Socket newClientSocket = ConnectClient(serverSocket);
                PrintWriter out = CreateClientConnectionOUT(newClientSocket);
                BufferedReader in = WaitClientConnectionIN(newClientSocket);
                RMIConnection.InputReader(in, out);
            }
        }.start();
        new Thread(){
            public void run(){
                Socket newClientSocket = ConnectClient(serverSocket);
                PrintWriter out = CreateClientConnectionOUT(newClientSocket);
                BufferedReader in = WaitClientConnectionIN(newClientSocket);
                RMIConnection.InputReader(in, out);
            }
        }.start();
        new Thread(){
            public void run(){
                Socket newClientSocket = ConnectClient(serverSocket);
                PrintWriter out = CreateClientConnectionOUT(newClientSocket);
                BufferedReader in = WaitClientConnectionIN(newClientSocket);
                RMIConnection.InputReader(in, out);
            }
        }.start();




    }

}