package com.example.pf_soft_ing.network.client;

import com.example.pf_soft_ing.card.Position;
import com.example.pf_soft_ing.card.side.CardSideType;
import com.example.pf_soft_ing.network.messages.Message;
import com.example.pf_soft_ing.network.messages.answers.ErrorMessage;
import com.example.pf_soft_ing.network.messages.requests.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSocketSender implements ClientSender {

    private final ObjectOutputStream out;
    private final BufferedReader stdIn;

    public ClientSocketSender(ObjectOutputStream out, BufferedReader stdIn){
        this.out = out;
        this.stdIn = stdIn;
    }

    public void sendMessage(Message output){
        try {
            out.writeObject(output);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Message encodeMessage(String userInput) {
        String[] parts = userInput.split(" ");
        String command = parts[0];

        return switch (command) {
            case "GetMatches" -> {
                if (parts.length != 1) {
                    yield new ErrorMessage("Error: GetMatches does not take any arguments");
                }
                yield new GetMatchesMsg();
            }
            case "CreateMatch" -> {
                if (parts.length != 3) {
                    yield new ErrorMessage("Error: CreateMatch takes exactly 2 arguments (num of players, nickname)");
                }
                yield new CreateMatchMsg(Integer.parseInt(parts[1]), parts[2]);
            }
            case "SelectMatch" -> {
                if (parts.length != 2) {
                    yield new ErrorMessage("Error: SelectMatch takes exactly 1 argument (match ID)");
                }
                yield new SelectMatchMsg(Integer.parseInt(parts[1]));
            }
            case "ChooseNickname" -> {
                if (parts.length != 2) {
                    yield new ErrorMessage("Error: ChooseNickname takes exactly 1 argument (nickname)");
                }
                yield new ChooseNicknameMsg(parts[1]);
            }
            case "exit", "quit" -> new CloseConnectionMsg();
            default -> new ErrorMessage("Error: " + command + " is not a valid command");
        };
    }

    @Override
    public void startInputReading() {
        new Thread(() -> {
            while (true) {
                try {
                    String userInput = stdIn.readLine();

                    if (userInput != null) {
                        Message message = encodeMessage(userInput);

                        if (message instanceof ErrorMessage) {
                            System.out.println(message);
                        }
                        else if (message instanceof CloseConnectionMsg) {
                            System.out.println("Closing connection");
                            break;
                        }
                        else {
                            sendMessage(message);
                        }
                    }
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void getMatches() {
        sendMessage(new GetMatchesMsg());
    }

    @Override
    public void createMatch(int numberOfPlayers, String nickname) {
        sendMessage(new CreateMatchMsg(numberOfPlayers, nickname));
    }

    @Override
    public void selectMatch(int matchID) {
        sendMessage(new SelectMatchMsg(matchID));
    }

    @Override
    public void chooseNickname(String nickname) {
        sendMessage(new ChooseNicknameMsg(nickname));
    }

    @Override
    public void placeStarterCard(int cardID, CardSideType side) {
        sendMessage(new PlaceStarterCardMsg(cardID, side));
    }

    @Override
    public void chooseSecretObjective(int cardID) {
        sendMessage(new ChooseSecretObjMsg(cardID));
    }

    @Override
    public void placeCard(int cardID, CardSideType side, Position pos) {
        sendMessage(new PlaceCardMsg(cardID, side, pos));
    }

    @Override
    public void drawResourceCard(int playerID) {

    }

    @Override
    public void drawVisibleResourceCard(int playerID, int index) {

    }

    @Override
    public void drawGoldenCard(int playerID) {

    }

    @Override
    public void drawVisibleGoldenCard(int playerID, int index) {

    }
}
