package com.spark.dotsandboxes.model;

public abstract class Player {
    protected final String name;
    protected Graph game;
    //default constructor of player
    public Player(String name) {
        this.name = name;
    }
    //getting player index
    public static int indexIn(Player player, Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (player == players[i])
                return i;
        }
        return -1;
    }
    //abstract method
    public abstract Line move();

    public Graph getGame() {
        return game;
    }
    //adds player to game
    public void addToGame(Graph game) {
        this.game = game;
    }
    //getting name
    public String getName() {
        return name;
    }
}
