package com.spark.dotsandboxes.view;

import com.spark.dotsandboxes.model.Player;

import java.util.Map;

public interface PlayersStateView {
    void setCurrentPlayer(Player player);

    void setPlayerBoxesCount(Map<Player, Integer> player_BoxesCount_map);

    void setWinner(Player winner);
}
