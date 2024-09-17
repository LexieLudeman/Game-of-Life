package com.talinapps.gameoflife

class SquareCell(val cellLocation: Location, alive: Int) {
    var cellState: State = if (alive == 1) State.ALIVE else State.DEAD
    var neighbors: List<SquareCell> = listOf()

    fun died() {
        cellState = State.DEAD
    }

    fun revived() {
        cellState = State.ALIVE
    }
}
