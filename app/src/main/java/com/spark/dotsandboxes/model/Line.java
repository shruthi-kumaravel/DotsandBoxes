package com.spark.dotsandboxes.model;

public class Line {
    private final Direction direction;
    private final int row;
    private final int column;
    //default constructor of line
    public Line(Direction direction, int row, int column) {
        this.direction = direction;
        this.row = row;
        this.column = column;
    }
    //get methods of line
    public Direction direction() {
        return direction;
    }

    public int row() {
        return row;
    }

    public int column() {
        return column;
    }
    //return equal or not
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        return row == line.row && column == line.column && direction == line.direction;
    }

    @Override
    public String toString() {
        return "direction:" + direction().toString() + "row:" + row + "column" + column;
    }
}
