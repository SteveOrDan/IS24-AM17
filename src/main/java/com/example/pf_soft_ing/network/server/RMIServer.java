package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.game.GameController;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    private final GameController gameController;

    public RMIServer(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Starts the RMI receiver
     * @param portNumber Port number to start the RMI receiver on
     */
    public void startRMIReceiver(int portNumber){
        Registry registry;
        RMIReceiverInterface rmiReceiver;

        boolean foundPort = false;
        while (!foundPort) {
            try {

                rmiReceiver = new RMIReceiver(gameController);
                registry = LocateRegistry.createRegistry(portNumber);

                try {
                    registry.bind("RMIReceiver", rmiReceiver);
                }
                catch (AlreadyBoundException e) {
                    throw new RuntimeException(e);
                }
                foundPort = true;
            }
            catch (IOException e) {
                portNumber++;
            }
        }
        System.out.println("RMI port: " + portNumber);
    }
}
