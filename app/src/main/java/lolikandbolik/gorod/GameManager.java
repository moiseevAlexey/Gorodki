package lolikandbolik.gorod;

import android.graphics.Canvas;


public class GameManager extends Thread{

    GameView gameView;  //GameView
    boolean running;    //Флаг работы гиры
    static final long FPS = 60; //Количество кадров в секунду

    public GameManager(GameView gameView)   //Конструктор
    {
        this.gameView = gameView;
    }

    public void setRunning(boolean run)
    {
        running = run;
    }   //Запуск игры

    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {   //Основной цикл игры
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
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);   //Расчет раскадровки
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
            } catch (Exception e) {}
        }
    }
}
