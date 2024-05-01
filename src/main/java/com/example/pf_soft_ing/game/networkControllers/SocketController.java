package com.example.pf_soft_ing.game.networkControllers;

import com.example.pf_soft_ing.ServerConnection.SocketReceiver;
import com.example.pf_soft_ing.game.GameController;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketController {

    private final GameController gameController;
    private int port;
    private static ServerSocket serverSocket;

    public SocketController(int port, GameController gameController) {
        this.port = port;
        this.gameController = gameController;
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();

        boolean foundPort = false;
        while(!foundPort) {
            try {
                serverSocket = new ServerSocket(port);
                foundPort = true;
            }
            catch (Exception e) {
                port++;
            }
        }

        System.out.println("Socket port: " + port);

        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.submit(new SocketReceiver(socket, gameController));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }).start();
    }
}
