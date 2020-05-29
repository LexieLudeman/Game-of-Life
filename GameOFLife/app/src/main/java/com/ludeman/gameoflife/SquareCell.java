package com.ludeman.gameoflife;

import java.util.List;

public class SquareCell {
    final Location cellLocation;
    State cellState;
    List<SquareCell> neighbors;

    public SquareCell(Location location, Integer alive) {
        cellLocation = location;
        if (alive == 1) cellState = State.ALIVE;
        else cellState = State.DEAD;
    }

    public void died(){
        cellState = State.DEAD;
    }

    public void revived() {
        cellState = State.ALIVE;
    }
}
