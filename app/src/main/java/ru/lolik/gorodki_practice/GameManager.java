package ru.lolik.gorodki_practice;

import android.graphics.Canvas;

/**
 * Created by John on 06.07.2016.
 */
public class GameManager extends Thread{

    GameView gameView;
    boolean running;
    static final long FPS = 60;

    public GameManager(GameView gameView)
    {
        this.gameView = gameView;
    }

    public void setRunning(boolean run)
    {
        running = run;
    }

    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas canvas = null;
            startTime = System.currentTimeMillis();
            try {
                canvas = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder())
                {
                    gameView.nextGameLoop(canvas);
                }
            } finally {
                if (canvas != null) {
                    gameView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
            } catch (Exception e) {}
        }
    }
}
