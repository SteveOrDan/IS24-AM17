package com.example.pf_soft_ing.disconnetion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class DisconnectionTest {

    private int packetLoss = 0;
    private final Timer timer = new Timer();
    private TimerTask packetLossTask;
    private Thread pingThread;
    private static final int MAX_PACKET_LOSS = 3;

    public void startPing() {
        pingThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.err.println("Ping thread interrupted");
                }
                if (packetLoss >= MAX_PACKET_LOSS) {
                    System.out.println("Packet loss: " + packetLoss);
                    System.out.println("Player disconnected");
                    break;
                }
                else {
                    System.out.println("Sent ping");
                    schedulePacketLossTask();
                }
            }
        });

        pingThread.start();
    }

    private void schedulePacketLossTask() {
        packetLossTask = new TimerTask() {
            @Override
            public void run() {
                packetLoss++;
                System.out.println("Packet loss: " + packetLoss);
            }
        };
        // Schedule the task to run after 1 second
        timer.schedule(packetLossTask, 1000);
    }

    public void resetPacketLoss() {
        if (packetLossTask != null) {
            packetLossTask.cancel();
        }
        packetLoss = 0;
    }

    public void stopPing() {
        pingThread.interrupt();
    }

    public static void main(String[] args) {
        DisconnectionTest disconnectionTest = new DisconnectionTest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        disconnectionTest.startPing();

        new Thread(() -> {
            while (true) {
                try {
                    String input = reader.readLine();
                    if (input.equals("stop")) {
                        System.out.println("Stopping ping");
                        disconnectionTest.startPing();
                        break;
                    }
                    else {
                        disconnectionTest.resetPacketLoss();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }
}
