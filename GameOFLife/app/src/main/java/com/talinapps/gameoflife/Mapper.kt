package com.talinapps.gameoflife

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.WindowManager

class Mapper : SurfaceView, Runnable {

    private var grid: Grid? = null
    private val SIDE_LENGTH = 1
    private val CELL_SIZE = 30
    private var columnWidth = 1
    private var rowHeight = 1
    private var rows = 1
    private var cols = 1

    private val g = Rect()
    private val paint = Paint()

    private var evolver: Thread? = null
    private val PAUSE_TIME = 250
    @Volatile
    private var running = false

    constructor(context: Context) : super(context) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup()
    }

    fun start() {
        running = true
        evolver = Thread(this)
        evolver?.start()
    }

    fun stop() {
        running = false
        try {
            evolver?.join()
        } catch (exception: InterruptedException) {
            exception.printStackTrace()
        }
    }

    private fun setup() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val point = Point()
        display.getSize(point)
        cols = point.x / CELL_SIZE
        rows = point.y / CELL_SIZE
        columnWidth = point.x / cols
        rowHeight = point.y / rows
        grid = Grid(cols, rows)
    }

    private fun drawSquares(canvas: Canvas) {
        for (i in 0 until cols) {
            for (j in 0 until rows) {
                val currentLocal = Location(i, j)
                val cell = grid?.getCell(currentLocal)
                cell?.let {
                    g.set((cell.cellLocation.x * columnWidth) - SIDE_LENGTH,
                        (cell.cellLocation.y * rowHeight) - SIDE_LENGTH,
                        (cell.cellLocation.x * columnWidth + columnWidth) - SIDE_LENGTH,
                        (cell.cellLocation.y * rowHeight + rowHeight) - SIDE_LENGTH)

                    paint.color = if (cell.cellState == State.ALIVE)
                        resources.getColor(R.color.living_cells)
                    else
                        resources.getColor(R.color.dead_cells)

                    canvas.drawRect(g, paint)
                }
            }
        }
    }

    override fun run() {
        while (running) {
            if (!holder.surface.isValid) continue
            try {
                Thread.sleep(PAUSE_TIME.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val canvas = holder.lockCanvas()
            grid?.cycleCells()
            drawSquares(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }
}
