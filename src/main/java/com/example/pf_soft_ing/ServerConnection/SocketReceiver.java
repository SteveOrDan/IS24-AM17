package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.card.Position;

import java.io.BufferedReader;
import java.io.IOException;

import static java.lang.Integer.parseInt;

public class SocketReceiver {

    private final Decoder decoder;

    public SocketReceiver(Decoder decoder, BufferedReader in) {
        this.decoder = decoder;

        new Thread(){
            public void run(){
                boolean isRunning = true;

                while (isRunning) {
                    String input;

                    try {
                        input = in.readLine();
                        if (input.equals("end")){
                            isRunning = false;
                        }

                        new Thread() {
                            public void run() {
                                messageDecoder(input);
                            }
                        }.start();
                    }
                    catch (IOException e) {
                        System.err.println(STR."Exception \{e.getMessage()}");
                        isRunning = false;
                    }
                }
                interrupt();
            }
        }.start();
    }

    protected void messageDecoder(String input){
        String[] inputArray = input.split(" ");
        int playerId = 0; // Temporary value

        switch (inputArray[0]) {
            case "1":
                Position pos = new Position(parseInt(inputArray[2]), parseInt(inputArray[3]));
                decoder.placeCard(parseInt(inputArray[1]), pos);
                break;

            case "2":
                decoder.drawResourceCard();
                break;

            case "3":
                //Change playerId witch playerModel
                decoder.drawVisibleResourceCard(playerId, parseInt(inputArray[1]));
                break;

            case "4":
                //Change playerId witch playerModel
                decoder.drawGoldenCard(playerId);
                break;

            case "5":
                //Change playerId witch playerModel
                decoder.drawVisibleGoldenCard(playerId, Integer.parseInt(inputArray[1]));
                break;

            case "end":
                //TODO What to do when I receive end?
                break;

            default:
                decoder.requestError();
        }
    }
}
