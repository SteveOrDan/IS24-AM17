package com.example.pf_soft_ing.network.server.socket;

import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.MVC.controller.GameController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketReceiver implements Runnable {

    private final ObjectInputStream in;
    private final int playerID;

    public SocketReceiver(Socket socket, GameController gameController) {
        try {
            in = new ObjectInputStream(socket.getInputStream());

            playerID = gameController.createPlayer(new SocketSender(new ObjectOutputStream(socket.getOutputStream()))).getID();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = (Message) in.readObject();
                Decoder.decode(message, playerID);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
