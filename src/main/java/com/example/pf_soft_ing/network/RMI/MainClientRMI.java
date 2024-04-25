package com.example.pf_soft_ing.network.RMI;




public class MainClientRMI {
   public static void main(String args[]) throws Exception {
       ClientRMI client = new ClientRMI();
       client.startClient("localhost");
    }
}
