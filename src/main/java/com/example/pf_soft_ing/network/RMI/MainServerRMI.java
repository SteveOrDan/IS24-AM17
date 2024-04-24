package com.example.pf_soft_ing.network.RMI;

public class MainServerRMI {
    public static void main(String[] args ){
        System.out.println("Hello from Server!");

        if (args.length != 1){
            System.err.println("Cannot start no port.");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);

        //try {
            //bind(portNumber);
        //} //catch (RemoteException e) {
            //throw new RuntimeException(e);
        //}
    }
}
