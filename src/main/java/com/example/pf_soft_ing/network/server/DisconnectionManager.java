package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.game.MatchController;

import java.util.Timer;
import java.util.TimerTask;

public class DisconnectionManager {

    private static final int MAX_PACKET_LOSS = 3;
    private static final int PING_INTERVAL = 1000;
    private static final int PING_TIMEOUT = 500;

    private final MatchController matchController;
    private final Sender sender;
    private final int playerID;

    private int packetLoss = 0;
    private final Timer timer = new Timer();
    private TimerTask packetLossTask;
    private Thread pingThread;

    public DisconnectionManager(MatchController matchController, Sender sender, int playerID) {
        this.matchController = matchController;
        this.sender = sender;
        this.playerID = playerID;
    }

    public void startPing() {
        pingThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(PING_INTERVAL);
                } catch (InterruptedException e) {
                    System.err.println("Ping thread interrupted");
                }
                if (packetLoss >= MAX_PACKET_LOSS) {
                    matchController.disconnectPlayer(playerID);
                    break;
                }
                else {
                    sender.sendPing();
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
            }
        };
        // Schedule the task to run after the timeout
        timer.schedule(packetLossTask, PING_TIMEOUT);
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
}
