package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.Decoder;
import com.example.pf_soft_ing.card.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ClientSocketReceiver {
    private final ClientDecoder clientDecoder;

    private final BufferedReader in;

    public ClientSocketReceiver(ClientDecoder clientDecoder, BufferedReader in) {
        this.clientDecoder = clientDecoder;
        this.in = in;

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
        List<String> nicknames = new ArrayList<String>();
        Map<Integer, List<String>> matches= new HashMap<Integer, List<String>>();

        switch (inputArray[0]) {
            case "0":
                for(int i = 1; i < inputArray.length; i++){
                    int key;
                    if (inputArray[i] == "M"){
                        key = parseInt(inputArray[i+1]);
                        i += 2;
                        if (inputArray[i] == "P"){
                            for(int j = i+1; j < i+1+ Integer.parseInt(inputArray[i+1]); j++) {
                                nicknames.add(inputArray[i + 1]);
                            }
                        }
                        matches.put(key, nicknames);
                        nicknames.clear();
                    }
                }
                clientDecoder.printMatches(matches);
                matches.clear();
                break;

            case "1":
                for(int i = 1; i < inputArray.length; i++){
                    int key;
                    if (inputArray[i] == "M"){
                        key = parseInt(inputArray[i+1]);
                        i += 2;
                        if (inputArray[i] == "P"){
                            for(int j = i+1; j < i+1+ Integer.parseInt(inputArray[i+1]); j++) {
                                nicknames.add(inputArray[i + 1]);
                            }
                        }
                        matches.put(key, nicknames);
                        nicknames.clear();
                    }
                }
                clientDecoder.failedMatch(matches);
                matches.clear();
                break;

            case "2":
                if (inputArray.length > 2) {
                    for (int i = 2; i < inputArray.length; i++) {
                        nicknames.add(inputArray[i]);
                    }
                }
                clientDecoder.joinMatch(Integer.parseInt(inputArray[1]), nicknames);
                nicknames.clear();
                break;

            case "3":
                if (inputArray.length > 1) {
                    for (int i = 1; i < inputArray.length; i++) {
                        nicknames.add(inputArray[i]);
                    }
                }
                clientDecoder.failedNickname(nicknames);
                nicknames.clear();
                break;

            case "4":
                Map<Integer, String> opponents= new HashMap<Integer, String>();
                if (inputArray.length > 3) {
                    for (int i = 3; i < inputArray.length; i++) {
                        opponents.put(Integer.parseInt(inputArray[i]), inputArray[i+1]);
                        i++;
                    }
                }
                clientDecoder.addNickname(Integer.parseInt(inputArray[1]), inputArray[2], opponents);
                opponents.clear();
                break;

            case "5":

                break;

            case "end":
                //TODO cosa fare se ricevo end??
                break;

            default:

        }
    }
}
