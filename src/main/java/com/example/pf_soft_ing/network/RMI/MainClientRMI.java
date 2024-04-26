package com.example.pf_soft_ing.network.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MainClientRMI {

   public static void main(String[] args) {
       String hostName = args[0];
       int portNumber = Integer.parseInt(args[1]);

       try {
           ClientRMI clientRMI = new ClientRMI(hostName, portNumber);
           clientRMI.startClient();
       }
       catch (RemoteException | NotBoundException e) {
           throw new RuntimeException(e);
       }
   }
}
