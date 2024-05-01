package com.example.pf_soft_ing.game;

import com.example.pf_soft_ing.ServerConnection.Decoder;
import com.example.pf_soft_ing.game.networkControllers.RMIController;
import com.example.pf_soft_ing.game.networkControllers.SocketController;

public class ServerMain {

    private static final GameController gameController = new GameController();

    public static void main(String[] args) {
        startRMIServer(args);
        startSocketServer(args);
    }

    public static void startRMIServer(String[] args){
        RMIController rmiController = new RMIController(gameController);
        rmiController.startRMIReceiver(Integer.parseInt(args[0]));
    }

    public static void startSocketServer(String[] args){
        SocketController server = new SocketController(Integer.parseInt(args[0]), gameController);
        Decoder.setGameController(gameController);
        server.startServer();
    }
}
