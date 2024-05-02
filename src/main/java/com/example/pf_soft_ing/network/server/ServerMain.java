package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.game.GameController;

public class ServerMain {

    private static final GameController gameController = new GameController();

    public static void main(String[] args) {
        startRMIServer(args);
        startSocketServer(args);
    }

    public static void startRMIServer(String[] args){
        RMIServer rmiServer = new RMIServer(gameController);
        rmiServer.startRMIReceiver(Integer.parseInt(args[0]));
    }

    public static void startSocketServer(String[] args){
        SocketServer server = new SocketServer(Integer.parseInt(args[0]), gameController);
        Decoder.setGameController(gameController);
        server.startServer();
    }
}
