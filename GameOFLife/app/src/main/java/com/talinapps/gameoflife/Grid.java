package com.talinapps.gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Grid {
    private final SquareCell[][] grid;
    private final int gridWidth;
    private final int gridHeight;

    public Grid(int w, int h) {
        this.gridHeight = h;
        this.gridWidth = w;
        grid = new SquareCell[w][h];
        fillGrid();

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                makeNeighbors(grid[i][j]);
            }
        }
    }

    private void fillGrid() {
        Random rand = new Random();
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                Location location = new Location(i, j);
                int living = rand.nextInt(2);
                grid[i][j] = new SquareCell(location, living);
            }
        }
    }

    private int neighborCount(SquareCell cell) {
        int count = 0;
        List<SquareCell> neighbors = cell.neighbors;
        for (SquareCell c : neighbors) {
            if (c.cellState == State.ALIVE) count++;
        }
        return count;
    }

    private void makeNeighbors(SquareCell cell) {
        List<SquareCell> neighbors = new ArrayList<>();
        int x = cell.cellLocation.getX();
        int y = cell.cellLocation.getY();
        SquareCell currentCell;

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if ((i != x || j != y) && i >= 0
                        && i < gridWidth && j >= 0 && j < gridHeight) {
                    currentCell = grid[i][j];
                    neighbors.add(currentCell);
                }
            }
        }

        cell.neighbors = neighbors;
    }

    public SquareCell getCell(Location location) {
        return grid[location.getX()][ location.getY()];
    }

    public void cycleCells() {
        SquareCell currentCell;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                currentCell = grid[i][j];
                int neighborCount = neighborCount(currentCell);

                if (currentCell.cellState == State.ALIVE) {
                    //Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                    if (neighborCount < 2) {
                        currentCell.died();
                    }

                    //Any live cell with more than three live neighbours dies, as if by overpopulation.
                    else if (neighborCount > 3) {
                        currentCell.died();
                    }
                }

                else {
                    //Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                    if (neighborCount == 3) {
                        currentCell.revived();
                    }
                }

            }
        }
    }

}
