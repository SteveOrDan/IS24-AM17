package com.example.pf_soft_ing.network.SocketMain;

import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.network.ServerConnection.SocketController;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.pf_soft_ing.network.ServerConnection.SocketController.*;

public class ServerMain {
    public static void main( String[] args ){
        ServerSocket serverSocket = startServerConnection(args);
        GameController gameController = new GameController();

        while (true) {
            Socket newClientSocket = connectClient(serverSocket);
            new Thread() {
                public void run() {
                    PrintWriter out = createClientConnectionOUT(newClientSocket);
                    BufferedReader in = waitClientConnectionIN(newClientSocket);
                    SocketController.createSocketThreads(in, out, gameController);
                }
            }.start();
        }
    }

}
