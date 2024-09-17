package com.talinapps.gameoflife

import java.util.Random

class Grid(private val gridWidth: Int, private val gridHeight: Int) {
    private val grid: Array<Array<SquareCell>> = Array(gridWidth) { Array(gridHeight) { SquareCell(Location(0, 0), 0) } }

    init {
        fillGrid()

        for (i in 0 until gridWidth) {
            for (j in 0 until gridHeight) {
                makeNeighbors(grid[i][j])
            }
        }
    }

    private fun fillGrid() {
        val rand = Random()
        for (i in 0 until gridWidth) {
            for (j in 0 until gridHeight) {
                val location = Location(i, j)
                val living = rand.nextInt(2)
                grid[i][j] = SquareCell(location, living)
            }
        }
    }

    private fun neighborCount(cell: SquareCell): Int {
        return cell.neighbors.count { it.cellState == State.ALIVE }
    }

    private fun makeNeighbors(cell: SquareCell) {
        val neighbors = mutableListOf<SquareCell>()
        val x = cell.cellLocation.x
        val y = cell.cellLocation.y

        for (i in (x - 1)..(x + 1)) {
            for (j in (y - 1)..(y + 1)) {
                if ((i != x || j != y) && i in 0 until gridWidth && j in 0 until gridHeight) {
                    neighbors.add(grid[i][j])
                }
            }
        }

        cell.neighbors = neighbors
    }

    fun getCell(location: Location): SquareCell {
        return grid[location.x][location.y]
    }

    fun cycleCells() {
        for (i in 0 until gridWidth) {
            for (j in 0 until gridHeight) {
                val currentCell = grid[i][j]
                val neighborCount = neighborCount(currentCell)

                if (currentCell.cellState == State.ALIVE) {
                    when {
                        neighborCount < 2 -> currentCell.died() // Underpopulation
                        neighborCount > 3 -> currentCell.died() // Overpopulation
                    }
                } else {
                    if (neighborCount == 3) {
                        currentCell.revived() // Reproduction
                    }
                }
            }
        }
    }
}
