package com.example.pf_soft_ing.SocketMain;

import com.example.pf_soft_ing.GameController;
import com.example.pf_soft_ing.ServerConnection.SocketController;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.pf_soft_ing.ServerConnection.SocketController.*;

public class ServerMain {
    public static void main( String[] args ){
        ServerSocket serverSocket = SocketController.StartServerConnection(args);
        GameController gameController = new GameController();
        while (true){
            Socket newClientSocket = ConnectClient(serverSocket);
            new Thread(){
                public void run(){
                    PrintWriter out = CreateClientConnectionOUT(newClientSocket);
                    BufferedReader in = WaitClientConnectionIN(newClientSocket);
                    SocketController.InputReader(in, out, gameController);
                }
            }.start();
        }
    }

}
