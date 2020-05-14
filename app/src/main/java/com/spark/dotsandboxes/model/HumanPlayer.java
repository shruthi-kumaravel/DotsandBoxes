package com.spark.dotsandboxes.model;

public class HumanPlayer extends Player {
    //one value array
    private final Line[] inputBuffer = new Line[1];
    //default constructor of Human player
    public HumanPlayer(String name) {
        super(name);
    }
    //method to add line
    public void add(Line line) {
        synchronized (inputBuffer) {
            inputBuffer[0] = line;
            inputBuffer.notify();
        }
    }
    //getting line input
    private Line getInput() {
        synchronized (inputBuffer) {
            if (inputBuffer[0] != null) {
                Line temp = inputBuffer[0];
                inputBuffer[0] = null;
                return temp;
            }
            try {
                inputBuffer.wait();
            } catch (InterruptedException ignored) {
            }
            return this.getInput();
        }
    }
    //calling move
    @Override
    public Line move() {
        return getInput();
    }
}
