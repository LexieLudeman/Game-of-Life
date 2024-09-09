package com.talinapps.gameoflife;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;

public class Mapper extends SurfaceView implements Runnable {

    private Grid grid;
    private static final int SIDE_LENGTH = 1;
    private static final int CELL_SIZE = 30;
    private int columnWidth = 1;
    private int rowHeight = 1;
    private int rows = 1;
    private int cols = 1;

    private final Rect g = new Rect();
    private final Paint paint = new Paint();

    private Thread evolver;
    private static final int PAUSE_TIME = 250;
    private Boolean running = false;

    public Mapper(Context context) {
        super(context);
        setup();
    }

    public Mapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public void start() {
        running = true;
        evolver = new Thread(this);
        evolver.start();
    }

    public void stop() {
        running = false;
        try {
            evolver.join();
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private void setup() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        cols = point.x / CELL_SIZE;
        rows = point.y / CELL_SIZE;
        columnWidth = point.x / cols;
        rowHeight = point.y / rows;
        grid = new Grid(cols, rows);
    }

    private void drawSquares(Canvas canvas) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Location currentLocal = new Location(i, j);
                SquareCell cell = grid.getCell(currentLocal);
                g.set((cell.cellLocation.getX() * columnWidth) - SIDE_LENGTH,
                        (cell.cellLocation.getY() * rowHeight) - SIDE_LENGTH,
                        (cell.cellLocation.getX() * columnWidth + columnWidth) - SIDE_LENGTH,
                        (cell.cellLocation.getY() * rowHeight + rowHeight) - SIDE_LENGTH);

                paint.setColor(cell.cellState == State.ALIVE ? getResources().getColor(R.color.living_cells) :
                        getResources().getColor(R.color.dead_cells));
                canvas.drawRect(g, paint);
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            if (!getHolder().getSurface().isValid()) continue;
            try {
                Thread.sleep(PAUSE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Canvas canvas = getHolder().lockCanvas();
            grid.cycleCells();
            drawSquares(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

}
