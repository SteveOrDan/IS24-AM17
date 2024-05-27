package com.example.pf_soft_ing.network.server;

import com.example.pf_soft_ing.game.GameController;
import com.example.pf_soft_ing.game.MatchController;

import java.util.Timer;
import java.util.TimerTask;

public class DisconnectionManager {

    private static final int MAX_PACKET_LOSS = 3;
    private static final int COUNT_DEC_PERIOD = 1000;

    private final GameController gameController;
    private final MatchController matchController;
    private final Sender sender;
    private final int playerID;

    private int packetLoss = 0;
    private final Timer timer = new Timer();
    private final TimerTask packetLossTask = new TimerTask() {
        @Override
        public void run() {
            if (packetLoss >= MAX_PACKET_LOSS) {
                System.out.println("Player " + playerID + " disconnected");
                matchController.disconnectPlayer(playerID);
                gameController.checkMatchState(matchController);
                cancel();
            }
            packetLoss++;
        }
    };

    public DisconnectionManager(GameController gameController, MatchController matchController, Sender sender, int playerID) {
        this.gameController = gameController;
        this.matchController = matchController;
        this.sender = sender;
        this.playerID = playerID;
    }

    public void startConnectionCheck() {
        // Let the client start sending pings
        sender.startHeartbeat();
        // Start checking for packet loss
        timer.scheduleAtFixedRate(packetLossTask, 0, COUNT_DEC_PERIOD);
    }

    public void resetPacketLoss() {
        packetLoss = 0;
    }
}
