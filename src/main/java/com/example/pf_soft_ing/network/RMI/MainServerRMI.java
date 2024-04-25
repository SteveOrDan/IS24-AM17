package com.example.pf_soft_ing.network.RMI;


import com.example.pf_soft_ing.ServerConnection.RMIReceiver;

import java.rmi.RemoteException;

import static com.example.pf_soft_ing.network.ServerConnection.RMIController.startRMIReceiver;

public class MainServerRMI {
    public static void main(String[] args ) throws Exception {
        startRMIReceiver();
    }
}
