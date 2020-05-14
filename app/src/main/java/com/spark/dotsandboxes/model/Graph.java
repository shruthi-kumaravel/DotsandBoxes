package com.spark.dotsandboxes.model;
import java.util.Observable;

public class Graph extends Observable {
    private Player[] players;
    private int currentInd;
    private int width;
    private int height;
    //Box filled array
    private Player[][] occupied;
    private int[][] hLine;
    private int[][] vLines;
    private Line latestLine;

    public Graph(int width, int height, Player[] players) {
        this.width = width;
        this.height = height;
        this.players = players;

        //setting the player to the occupied list
        occupied = new Player[height][width];
        hLine = new int[height + 1][width];
        vLines = new int[height][width + 1];

        addPlayersToGame(players);
        currentInd = 0;
    }
    // getting players
    public Player[] getPlayers() {
        return players;
    }
    //getting width
    public int getWidth() {
        return width;
    }
    //getting height
    public int getHeight() {
        return height;
    }
    //getting recently filled line
    public Line getLatestLine() {
        return latestLine;
    }
    //adding players to the game
    private void addPlayersToGame(Player[] players) {
        for (Player player : players) {
            player.addToGame(this);
        }
    }
    //starting game
    public void start() {
        while (!isGameFinished()) {
            addMove(currentPlayer().move());
            setChanged();
            notifyObservers();
        }
    }
    //adding player movement
    public void addMove(Line move) {
        if (isLineOccupied(move)) {
            return;
        }
        boolean newBoxOccupied = tryToOccupyBox(move);
        setLineOccupied(move);
        latestLine = move;

        if (!newBoxOccupied)
            toNextPlayer();
    }
    //get current player index
    public Player currentPlayer() {
        return players[currentInd];
    }

    //getting the box is filled or not.
    public boolean isLineOccupied(Direction direction, int row, int column) {
        return isLineOccupied(new Line(direction, row, column));
    }

    public boolean isLineOccupied(Line line) {
        switch (line.direction()) {
            case HORIZONTAL:
                return (hLine[line.row()][line.column()] == 1
                        || hLine[line.row()][line.column()] == 2);
            case VERTICAL:
                return (vLines[line.row()][line.column()] == 1
                        || vLines[line.row()][line.column()] == 2);
        }
        throw new IllegalArgumentException(line.direction().toString());
    }
    //getting line occupier to fill the line
    public int getLineOccupier(Line line) {
        switch (line.direction()) {
            case HORIZONTAL:
                return hLine[line.row()][line.column()];
            case VERTICAL:
                return vLines[line.row()][line.column()];
        }
        throw new IllegalArgumentException(line.direction().toString());
    }
    //getting the occupied box
    public Player getBoxOccupier(int row, int column) {
        return occupied[row][column];
    }
    //getting player points
    public int getPlayerOccupyingBoxCount(Player player) {
        int count = 0;
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (getBoxOccupier(i, j) == player)
                    count++;
            }
        }
        return count;
    }
    //marking/occupying box to a user
    private boolean tryToOccupyBox(Line move) {
        boolean rightOccupied = tryToOccupyRightBox(move);
        boolean underOccupied = tryToOccupyUnderBox(move);
        boolean upperOccupied = tryToOccupyUpperBox(move);
        boolean leftOccupied = tryToOccupyLeftBox(move);
        return leftOccupied || rightOccupied || upperOccupied || underOccupied;
    }
    //Setting lines to occupy
    private void setLineOccupied(Line line) {
        switch (line.direction()) {
            case HORIZONTAL:
                hLine[line.row()][line.column()] = currentInd + 1;
                break;
            case VERTICAL:
                vLines[line.row()][line.column()] = currentInd + 1;
                break;
        }
    }
    //setting box to occupy
    private void setBoxOccupied(int row, int column, Player player) {
        occupied[row][column] = player;
    }
    //try to occupy the 4 side boxes
    private boolean tryToOccupyUpperBox(Line move) {
        if (move.direction() != Direction.HORIZONTAL || move.row() <= 0)
            return false;
        if (isLineOccupied(Direction.HORIZONTAL, move.row() - 1, move.column())
                && isLineOccupied(Direction.VERTICAL, move.row() - 1, move.column())
                && isLineOccupied(Direction.VERTICAL, move.row() - 1, move.column() + 1)) {
            setBoxOccupied(move.row() - 1, move.column(), currentPlayer());
            return true;
        } else {
            return false;
        }
    }
    //try to occupy the 4 side boxes
    private boolean tryToOccupyUnderBox(Line move) {
        if (move.direction() != Direction.HORIZONTAL || move.row() >= (height))
            return false;
        if (isLineOccupied(Direction.HORIZONTAL, move.row() + 1, move.column())
                && isLineOccupied(Direction.VERTICAL, move.row(), move.column())
                && isLineOccupied(Direction.VERTICAL, move.row(), move.column() + 1)) {
            setBoxOccupied(move.row(), move.column(), currentPlayer());
            return true;
        } else {
            return false;
        }
    }
    //try to occupy the 4 side boxes
    private boolean tryToOccupyLeftBox(Line move) {
        if (move.direction() != Direction.VERTICAL || move.column() <= 0)
            return false;
        if (isLineOccupied(Direction.VERTICAL, move.row(), move.column() - 1)
                && isLineOccupied(Direction.HORIZONTAL, move.row(), move.column() - 1)
                && isLineOccupied(Direction.HORIZONTAL, move.row() + 1, move.column() - 1)) {
            setBoxOccupied(move.row(), move.column() - 1, currentPlayer());
            return true;
        } else {
            return false;
        }
    }
    //try to occupy the 4 side boxes
    private boolean tryToOccupyRightBox(Line move) {
        if (move.direction() != Direction.VERTICAL || move.column() >= (width))
            return false;
        if (isLineOccupied(Direction.VERTICAL, move.row(), move.column() + 1)
                && isLineOccupied(Direction.HORIZONTAL, move.row(), move.column())
                && isLineOccupied(Direction.HORIZONTAL, move.row() + 1, move.column())) {
            setBoxOccupied(move.row(), move.column(), currentPlayer());
            return true;
        } else {
            return false;
        }
    }
    //giving the opportunity to the next player
    private void toNextPlayer() {
        currentInd = (currentInd + 1) % players.length;
    }
    //checking if the game is finished or not
    protected boolean isGameFinished() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (getBoxOccupier(i, j) == null)
                    return false;
            }
        }
        return true;
    }
    // getting the winner
    public Player getWinner() {
        if (!isGameFinished()) {
            return null;
        }

        int[] playersOccupyingBoxCount = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            playersOccupyingBoxCount[i] = getPlayerOccupyingBoxCount(players[i]);
        }

        if (playersOccupyingBoxCount[0] > playersOccupyingBoxCount[1])
            return players[0];
        else
            return players[1];
    }
}
