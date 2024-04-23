package com.example.pf_soft_ing.network.RMI;

import com.example.pf_soft_ing.card.Position;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class MainClientRMI {
    public void main(String[] args) throws RemoteException {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        ClientRMI client1 = new ClientRMI();
        try {
            client1.doJob(hostName,portNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
