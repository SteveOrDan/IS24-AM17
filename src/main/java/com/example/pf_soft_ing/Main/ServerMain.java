package com.example.pf_soft_ing.Main;

import com.example.pf_soft_ing.ServerConnection.RMIConnection;

import java.io.BufferedReader;
import java.net.ServerSocket;

import static com.example.pf_soft_ing.ServerConnection.RMIConnection.WaitClientConnection;

public class ServerMain {
    public static void main( String[] args ){
        ServerSocket serverSocket = RMIConnection.StartServerConnection(args);
        new Thread(){
            public void run(){

                BufferedReader in = WaitClientConnection();
                RMIConnection.InputReader(in);
            }
        }.start();
        new Thread(){
            public void run(){
                BufferedReader in = WaitClientConnection();
                RMIConnection.InputReader(in);
            }
        }.start();
        new Thread(){
            public void run(){
                BufferedReader in = WaitClientConnection();
                RMIConnection.InputReader(in);
            }
        }.start();
        new Thread(){
            public void run(){
                BufferedReader in = WaitClientConnection();
                RMIConnection.InputReader(in);
            }
        }.start();




    }

}
