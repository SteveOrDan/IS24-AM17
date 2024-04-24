package com.example.pf_soft_ing.ServerConnection;

import com.example.pf_soft_ing.game.MatchController;
import com.example.pf_soft_ing.player.PlayerModel;

public class RMIReceiver extends Decoder {

    public RMIReceiver(MatchController matchController, PlayerModel playerModel) {
        super(matchController, playerModel);
    }
}
