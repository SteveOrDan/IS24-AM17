package com.example.pf_soft_ing.client;

import com.example.pf_soft_ing.ServerConnection.Decoder;
import com.example.pf_soft_ing.card.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

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
        System.out.println(input);
        String[] inputArray = input.split(" ");

        Map<Integer, List<String>> matches= new HashMap<Integer, List<String>>();

        switch (inputArray[0]) {
            case "0":
                for(int i = 1; i < inputArray.length; i++){
                    int key;
                    if (Objects.equals(inputArray[i], "M")){
                        key = parseInt(inputArray[i+1]);
                        i = i + 2;
                        if (Objects.equals(inputArray[i], "P")){
                            i++;
                            List<String> nicknames = new ArrayList<String>(Arrays.asList(inputArray).subList(i + 1, i + 1 + parseInt(inputArray[i])));
                            matches.put(key, nicknames);
                        }
                    }
                }
                clientDecoder.printMatches(matches);
                break;

            case "1":
                for(int i = 1; i < inputArray.length; i++){
                    int key;
                    if (Objects.equals(inputArray[i], "M")){
                        key = parseInt(inputArray[i+1]);
                        i += 2;
                        if (Objects.equals(inputArray[i], "P")){
                            i++;
                            List<String> nicknames = new ArrayList<String>(Arrays.asList(inputArray).subList(i + 1, i + 1 + parseInt(inputArray[i])));
                            matches.put(key, nicknames);
                        }
                    }
                }
                clientDecoder.failedMatch(matches);
                break;

            case "2":
                List<String> nicknames2 = new ArrayList<String>();
                if (inputArray.length > 2) {
                    nicknames2.addAll(Arrays.asList(inputArray).subList(2, inputArray.length));
                }
                clientDecoder.joinMatch(Integer.parseInt(inputArray[1]), nicknames2);
                break;

            case "3":
                List<String> nicknames3 = new ArrayList<String>();
                if (inputArray.length > 1) {
                    nicknames3.addAll(Arrays.asList(inputArray).subList(1, inputArray.length));
                }
                clientDecoder.failedNickname(nicknames3);
                break;

            case "4":
                Map<Integer, String> opponents= new HashMap<>();
                if (inputArray.length > 3) {
                    for (int i = 3; i < inputArray.length; i++) {
                        opponents.put(Integer.parseInt(inputArray[i]), inputArray[i+1]);
                        i++;
                    }
                }
                clientDecoder.addNickname(Integer.parseInt(inputArray[1]), inputArray[2], opponents);
                break;

            case "5":

                break;

            case "end":
                //TODO cosa fare se ricevo end??
                break;
        }
    }
}
