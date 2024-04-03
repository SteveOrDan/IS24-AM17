package com.example.pf_soft_ing.RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI {
    static int PORT = 1234;
    public static void main( String[] args ){

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
            RMIController stub = (RMIController) registry.lookup("Loggable");
            //boolean logged = stub.login("Bob");
            //System.out.println("Remote method invoked " + logged);
            String result = stub.flipStarterCard(123);
            System.out.println(result);
        } catch (Exception e){
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
