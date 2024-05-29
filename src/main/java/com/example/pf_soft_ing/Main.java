package com.example.pf_soft_ing;

import com.example.pf_soft_ing.network.client.ClientMain;
import com.example.pf_soft_ing.network.server.ServerMain;

public class Main {
    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("server")) {
            String[] newArgs = new String[1];
            newArgs[0] = args[1];

            ServerMain.main(newArgs);
        }
        else if (args[0].equalsIgnoreCase("client")) {
            String[] newArgs = new String[3];
            newArgs[0] = args[1]; // IP
            newArgs[1] = args[2]; // connection type
            newArgs[2] = args[3]; // view type

            ClientMain.main(newArgs);
        }
        else {
            System.out.println("Error: Specify server or client.");
        }
    }
}
