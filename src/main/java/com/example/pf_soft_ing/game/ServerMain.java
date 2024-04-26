package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.exceptions.GameIsFullException;
import com.example.pf_soft_ing.exceptions.NicknameAlreadyExistsException;
import com.example.pf_soft_ing.game.networkControllers.RMIController;
import com.example.pf_soft_ing.game.networkControllers.SocketController;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.pf_soft_ing.game.networkControllers.SocketController.*;
import static com.example.pf_soft_ing.game.networkControllers.SocketController.waitClientConnectionIN;

public class ServerMain {

    public static void main( String[] args ){

        GameController gameController = new GameController();
        ServerSocket serverSocket = startServerConnection(args);
        new RMIController(gameController, args);

        new Thread() {
            public void run() {
                while (true) {
                    Socket newClientSocket = connectClient(serverSocket);
                    new Thread() {
                        public void run() {
                            PrintWriter out = createClientConnectionOUT(newClientSocket);
                            BufferedReader in = waitClientConnectionIN(newClientSocket);
                            SocketController.startInteraction(in, out, gameController);
                        }
                    }.start();
                }
            }
        }.start();

    }
}
