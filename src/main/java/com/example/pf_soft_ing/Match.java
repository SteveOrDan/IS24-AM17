package com.example.pf_soft_ing;

import java.util.ArrayList;
import java.util.List;

public class Match {
    final int id;

    final List<PlayerModel> players;

    public Match(int id) {
        this.id = id;
        this.players = new ArrayList<>();
    }
}
