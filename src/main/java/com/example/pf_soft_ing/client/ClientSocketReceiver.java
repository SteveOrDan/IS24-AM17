package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.messages.*;
import com.example.pf_soft_ing.ServerConnection.messages.answers.ErrorMessage;
import com.example.pf_soft_ing.ServerConnection.messages.answers.MatchCreatedMsg;
import com.example.pf_soft_ing.ServerConnection.messages.answers.SelectMatchResultMsg;
import com.example.pf_soft_ing.ServerConnection.messages.answers.ReturnMatchesMsg;
import com.example.pf_soft_ing.ServerConnection.messages.requests.CloseConnectionMsg;
import com.example.pf_soft_ing.ServerConnection.messages.requests.CreateMatchMsg;

import java.io.IOException;
import java.io.ObjectInputStream;

import static java.lang.Integer.parseInt;

public class ClientSocketReceiver {

    private final ClientController clientController;

    public ClientSocketReceiver(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Starts a new thread that listens for incoming messages from the server
     * until it receives a CloseConnectionMsg
     * @param inputStream ObjectInputStream
     */
    public void startReceivingThread(ObjectInputStream inputStream){
        new Thread(){
            public void run(){
                boolean isRunning = true;

                while (isRunning) {
                    Message input;

                    try {
                        input = (Message) inputStream.readObject();

                        if (input instanceof CloseConnectionMsg) {
                            isRunning = false;
                        }

                        messageDecoder(input);
                    }
                    catch (IOException | ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                        isRunning = false;
                    }
                }
                interrupt();
            }
        }.start();
    }

    private void messageDecoder(Message message){
        switch (message) {
            case ReturnMatchesMsg castedMsg -> {
                clientController.printMatches(castedMsg.getMatches());
            }
            case SelectMatchResultMsg castedMsg -> {
                clientController.joinMatch(castedMsg.getMatchID(), castedMsg.getNicknames());
            }
            case MatchCreatedMsg castedMsg -> {
                clientController.matchCreated(castedMsg.getMatchID());
            }
            case ErrorMessage castedMsg -> {
                clientController.errorMessage(castedMsg.getMessage());
            }
            case null, default -> {
                System.out.println("Invalid message type");
            }
        }
    }
}
